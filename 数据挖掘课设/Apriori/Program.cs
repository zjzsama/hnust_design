namespace 数据挖掘课设.Apriori;
using System;
using System.Collections.Generic;
using System.Linq;

class Apriori
{
    static void Main(string[] args)
    {
        // 交易数据
        var transactions = new List<HashSet<string>>
        {
            new HashSet<string> { "A", "B", "C", "D", "E", "F", "G" },
            new HashSet<string> { "A", "B", "C", "D", "E", "H" },
            new HashSet<string> { "A", "B", "C", "D", "E", "F", "G", "H" },
            new HashSet<string> { "A", "B", "C", "G", "H" },
            new HashSet<string> { "A", "B", "C", "D", "G", "H" },
            new HashSet<string> { "A", "B", "C", "D", "E", "F", "G", "H" },
            new HashSet<string> { "A", "B", "C", "D", "E", "F", "G" },
            new HashSet<string> { "A", "B", "C", "E", "G", "H" },
            new HashSet<string> { "A", "B", "C", "D", "E", "F", "H" },
            new HashSet<string> { "C", "D", "E", "F", "G", "H" },
            new HashSet<string> { "A", "B", "C", "D", "G", "H" },
            new HashSet<string> { "A", "C", "D", "E", "F", "G", "H" },
            new HashSet<string> { "A", "B", "C", "E", "F", "G", "H" },
            new HashSet<string> { "B", "C", "E", "F", "G", "H" }
        };

        // 参数设置
        double minSupport = 0.5; // 最小支持度
        double minConfidence = 0.5; // 最小置信度

        // 调用 Apriori 算法
        var (freqItemsets, supportData) = AprioriAlgorithm(transactions, minSupport);

        // 生成关联规则
        var rules = GenerateRulesManually(freqItemsets, supportData, minConfidence);

        // 输出频繁项集及支持度
        Console.WriteLine("频繁项集及其支持度:");
        foreach (var kvp in supportData)
        {
            Console.WriteLine($"项集: {{{string.Join(", ", kvp.Key)}}}, 支持度: {kvp.Value:F2}");
        }

        // 输出关联规则
        Console.WriteLine("\n强关联规则:");
        foreach (var (antecedent, consequence, confidence) in rules)
        {
            Console.WriteLine($"规则: {{{string.Join(", ", antecedent)}}} -> {{{string.Join(", ", consequence)}}}, 置信度: {confidence:F2}");
        }
    }

    // Apriori 算法核心实现
    static (HashSet<HashSet<string>>, Dictionary<HashSet<string>, double>) AprioriAlgorithm(List<HashSet<string>> transactions, double minSupport)
    {
        int numTransactions = transactions.Count;
        var supportData = new Dictionary<HashSet<string>, double>(new HashSetComparer());
        var freqItemsets = new HashSet<HashSet<string>>(new HashSetComparer());

        // 生成频繁 1 项集
        var itemCounts = new Dictionary<string, int>();
        foreach (var transaction in transactions)
        {
            foreach (var item in transaction)
            {
                if (!itemCounts.ContainsKey(item))
                    itemCounts[item] = 0;
                itemCounts[item]++;
            }
        }

        // 过滤满足最小支持度的项
        var currentItemsets = new HashSet<HashSet<string>>(new HashSetComparer());
        foreach (var item in itemCounts.Keys)
        {
            double support = (double)itemCounts[item] / numTransactions;
            if (support >= minSupport)
            {
                var itemset = new HashSet<string> { item };
                currentItemsets.Add(itemset);
                supportData[itemset] = support;
                freqItemsets.Add(itemset);
            }
        }

        // 生成更大的频繁项集
        int k = 2;
        while (currentItemsets.Count > 0)
        {
            var candidates = GenerateCandidates(currentItemsets, k);
            var candidateCounts = new Dictionary<HashSet<string>, int>(new HashSetComparer());

            foreach (var transaction in transactions)
            {
                foreach (var candidate in candidates)
                {
                    if (candidate.IsSubsetOf(transaction))
                    {
                        if (!candidateCounts.ContainsKey(candidate))
                            candidateCounts[candidate] = 0;
                        candidateCounts[candidate]++;
                    }
                }
            }

            currentItemsets.Clear();
            foreach (var candidate in candidateCounts.Keys)
            {
                double support = (double)candidateCounts[candidate] / numTransactions;
                if (support >= minSupport)
                {
                    currentItemsets.Add(candidate);
                    supportData[candidate] = support;
                    freqItemsets.Add(candidate);
                }
            }

            k++;
        }

        return (freqItemsets, supportData);
    }

    // 生成候选集
    static HashSet<HashSet<string>> GenerateCandidates(HashSet<HashSet<string>> itemsets, int k)
    {
        var candidates = new HashSet<HashSet<string>>(new HashSetComparer());

        foreach (var set1 in itemsets)
        {
            foreach (var set2 in itemsets)
            {
                var union = new HashSet<string>(set1);
                union.UnionWith(set2);

                if (union.Count == k)
                    candidates.Add(union);
            }
        }

        return candidates;
    }

    // 手动生成关联规则
    static List<(HashSet<string> Antecedent, HashSet<string> Consequence, double Confidence)> GenerateRulesManually(
        HashSet<HashSet<string>> freqItemsets,
        Dictionary<HashSet<string>, double> supportData,
        double minConfidence)
    {
        var rules = new List<(HashSet<string>, HashSet<string>, double)>();

        foreach (var itemset in freqItemsets)
        {
            // 仅处理长度大于1的频繁项集
            if (itemset.Count > 1)
            {
                // 枚举 itemset 的所有非空子集作为前件
                var subsets = GetSubsets(itemset);

                foreach (var subset in subsets)
                {
                    // 后件 = 项集 - 前件
                    var consequence = new HashSet<string>(itemset);
                    consequence.ExceptWith(subset);

                    // 确保后件非空且支持度数据存在
                    if (consequence.Count > 0 && supportData.ContainsKey(subset))
                    {
                        // 计算置信度
                        double confidence = supportData[itemset] / supportData[subset];
                        if (confidence >= minConfidence)
                        {
                            // 添加规则
                            rules.Add((subset, consequence, confidence));
                        }
                    }
                }
            }
        }

        return rules;
    }

    // 生成所有非空子集
    static List<HashSet<string>> GetSubsets(HashSet<string> itemset)
    {
        var subsets = new List<HashSet<string>>();
        var items = itemset.ToList();

        // 使用位操作生成所有可能的子集
        int subsetCount = 1 << items.Count; // 2^n 个子集
        for (int i = 1; i < subsetCount; i++) // 从1开始，跳过空集
        {
            var subset = new HashSet<string>();
            for (int j = 0; j < items.Count; j++)
            {
                if ((i & 1 << j) > 0) // 检查第 j 位是否为 1
                {
                    subset.Add(items[j]);
                }
            }
            subsets.Add(subset);
        }

        return subsets;
    }

    // 自定义 HashSet 比较器
    class HashSetComparer : IEqualityComparer<HashSet<string>>
    {
        public bool Equals(HashSet<string> x, HashSet<string> y)
        {
            return x.SetEquals(y);
        }

        public int GetHashCode(HashSet<string> obj)
        {
            int hash = 0;
            foreach (var item in obj)
                hash ^= item.GetHashCode();
            return hash;
        }
    }
}
