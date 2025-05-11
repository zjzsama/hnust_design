using System;
using System.Collections.Generic;
using System.Linq;

class Program
{
    static void Main()
    {
        // 交易记录
        List<List<string>> transactions = new List<List<string>>
        {
            new List<string>{"A", "B", "C", "D", "E", "F", "H"},
            new List<string>{"C", "D", "E", "F", "H"},
            new List<string>{"A", "D", "E", "F", "H"},
            new List<string>{"A", "B", "C", "D", "F", "H"},
            new List<string>{"A", "B", "C", "F", "H"},
            new List<string>{"A", "B", "C", "D", "E"},
            new List<string>{"A", "B", "C", "D", "E", "F"},
            new List<string>{"A", "B", "C", "D", "E", "F", "H"},
            new List<string>{"A", "B", "C", "D", "E"},
            new List<string>{"A", "C", "D", "E", "F", "H"},
            new List<string>{"A", "B", "C", "D"},
            new List<string>{"A", "B", "C", "D", "E", "F", "H"},
            new List<string>{"A", "D", "E", "F", "H"},
            new List<string>{"A", "B", "C", "D", "E", "F", "H"},
            new List<string>{"A", "B", "C", "D", "F", "H"},
            new List<string>{"A", "B", "C", "D", "E", "F", "H"},
            new List<string>{"A", "B", "D", "E", "F", "H"},
            new List<string>{"A", "B", "C", "D", "E", "F", "H"},
            new List<string>{"A", "B", "E", "F", "H"},
            new List<string>{"A", "B", "C", "D", "E", "F", "H"}
        };
        
        double minSupport = 0.6; // 最小支持度
        double minConfidence = 0.6; // 最小置信度

        // 第一步：统计每个项的频率
        var itemFrequency = transactions
            .SelectMany(t => t)
            .GroupBy(item => item)
            .ToDictionary(g => g.Key, g => g.Count());

        // 过滤掉不满足最小支持度的项
        int minSupportCount = (int)Math.Ceiling(minSupport * transactions.Count);
        var filteredItems = itemFrequency
            .Where(kv => kv.Value >= minSupportCount)
            .OrderByDescending(kv => kv.Value)
            .Select(kv => kv.Key)
            .ToList();

        // 第二步：构建FP-tree
        FPTree fpTree = new FPTree();
        foreach (var transaction in transactions)
        {
            var sortedTransaction = transaction
                .Where(item => filteredItems.Contains(item))
                .OrderBy(item => filteredItems.IndexOf(item))
                .ToList();
            fpTree.AddTransaction(sortedTransaction);
        }

        // 第三步：挖掘频繁项集
        List<FrequentPattern> patterns = fpTree.MinePatterns(minSupportCount);

        // 第四步：生成强关联规则
        List<AssociationRule> rules = GenerateAssociationRules(patterns, minConfidence);

        // 输出结果
        Console.WriteLine("频繁项集:");
        foreach (var pattern in patterns)
            Console.WriteLine($"{string.Join(",", pattern.Items)}: {pattern.Support}");

        Console.WriteLine("\n强关联规则:");
        foreach (var rule in rules)
            Console.WriteLine($"{string.Join(",", rule.Antecedent)} => {string.Join(",", rule.Consequent)} (置信度: {rule.Confidence:P})");
    }

    static List<AssociationRule> GenerateAssociationRules(List<FrequentPattern> patterns, double minConfidence)
    {
        var rules = new List<AssociationRule>();
        foreach (var pattern in patterns)
        {
            var subsets = GetSubsets(pattern.Items);
            foreach (var subset in subsets)
            {
                var remaining = pattern.Items.Except(subset).ToList();
                if (remaining.Any())
                {
                    double denominator = GetSupport(subset, patterns);
                    if (denominator == 0) continue; // 跳过置信度为 ∞ 的情况

                    double confidence = (double)pattern.Support / denominator;
                    if (confidence >= minConfidence)
                        rules.Add(new AssociationRule(subset, remaining, confidence));
                }
            }
        }
        return rules;
    }


    static int GetSupport(List<string> items, List<FrequentPattern> patterns)
    {
        return patterns.FirstOrDefault(p => p.Items.SequenceEqual(items))?.Support ?? 0;
    }

    static List<List<string>> GetSubsets(List<string> items)
    {
        var subsets = new List<List<string>>();
        int subsetCount = (int)Math.Pow(2, items.Count);
        for (int i = 1; i < subsetCount - 1; i++) // Exclude empty set and full set
        {
            var subset = new List<string>();
            for (int j = 0; j < items.Count; j++)
                if ((i & (1 << j)) != 0)
                    subset.Add(items[j]);
            subsets.Add(subset);
        }
        return subsets;
    }
}

// 定义FP-tree的节点
class FPTreeNode
{
    public string Item { get; set; }
    public int Count { get; set; }
    public FPTreeNode Parent { get; set; }
    public Dictionary<string, FPTreeNode> Children { get; set; } = new Dictionary<string, FPTreeNode>();

    public FPTreeNode(string item, FPTreeNode parent)
    {
        Item = item;
        Parent = parent;
        Count = 1;
    }
}

// 定义FP-tree
class FPTree
{
    private FPTreeNode root = new FPTreeNode(null, null);
    private Dictionary<string, List<FPTreeNode>> headerTable = new Dictionary<string, List<FPTreeNode>>();

    public void AddTransaction(List<string> transaction)
    {
        FPTreeNode currentNode = root;
        foreach (var item in transaction)
        {
            if (!currentNode.Children.ContainsKey(item))
            {
                var newNode = new FPTreeNode(item, currentNode);
                currentNode.Children[item] = newNode;

                if (!headerTable.ContainsKey(item))
                    headerTable[item] = new List<FPTreeNode>();
                headerTable[item].Add(newNode);
            }
            else
            {
                currentNode.Children[item].Count++;
            }
            currentNode = currentNode.Children[item];
        }
    }

    public List<FrequentPattern> MinePatterns(int minSupport)
    {
        var patterns = new List<FrequentPattern>();
        foreach (var item in headerTable.Keys)
        {
            var patternBase = new List<List<string>>();
            foreach (var node in headerTable[item])
            {
                var path = new List<string>();
                var current = node.Parent;
                while (current != null && current.Item != null)
                {
                    path.Add(current.Item);
                    current = current.Parent;
                }
                for (int i = 0; i < node.Count; i++)
                    patternBase.Add(path);
            }

            FPTree conditionalTree = new FPTree();
            foreach (var transaction in patternBase)
                conditionalTree.AddTransaction(transaction);

            var conditionalPatterns = conditionalTree.MinePatterns(minSupport);
            foreach (var pattern in conditionalPatterns)
            {
                pattern.Items.Add(item);
                patterns.Add(pattern);
            }

            patterns.Add(new FrequentPattern(new List<string> { item }, headerTable[item].Sum(n => n.Count)));
        }
        return patterns;
    }
}

// 定义频繁项集
class FrequentPattern
{
    public List<string> Items { get; set; }
    public int Support { get; set; }

    public FrequentPattern(List<string> items, int support)
    {
        Items = items;
        Support = support;
    }
}

// 定义关联规则
class AssociationRule
{
    public List<string> Antecedent { get; set; }
    public List<string> Consequent { get; set; }
    public double Confidence { get; set; }

    public AssociationRule(List<string> antecedent, List<string> consequent, double confidence)
    {
        Antecedent = antecedent;
        Consequent = consequent;
        Confidence = confidence;
    }
}
