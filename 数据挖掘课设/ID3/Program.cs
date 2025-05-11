using System;
using System.Collections.Generic;
using System.Linq;

public class ID3
{
    public class DataSet
    {
        public string Age { get; set; }
        public string Income { get; set; }
        public string Student { get; set; }
        public string Credit { get; set; }
        public string BuyComputer { get; set; }
    }

    public class TreeNode
    {
        public string Attribute { get; set; }
        public Dictionary<string, TreeNode> Children { get; set; } = new Dictionary<string, TreeNode>();
        public string Label { get; set; }
    }

    // 计算数据集的熵
    public static double CalculateEntropy(List<DataSet> data)
    {
        var countYes = data.Count(d => d.BuyComputer == "买");
        var countNo = data.Count(d => d.BuyComputer == "不买");

        double probYes = countYes / (double)data.Count;
        double probNo = countNo / (double)data.Count;

        if (probYes == 0 || probNo == 0)
            return 0;

        return -probYes * Math.Log(probYes, 2) - probNo * Math.Log(probNo, 2);
    }

    // 根据某一属性分割数据集
    public static Dictionary<string, List<DataSet>> SplitDataSet(List<DataSet> data, string attribute)
    {
        var split = new Dictionary<string, List<DataSet>>();

        foreach (var record in data)
        {
            string key = attribute switch
            {
                "Age" => record.Age,
                "Income" => record.Income,
                "Student" => record.Student,
                "Credit" => record.Credit,
                _ => throw new Exception("Invalid attribute")
            };

            if (!split.ContainsKey(key))
                split[key] = new List<DataSet>();

            split[key].Add(record);
        }

        return split;
    }

    // 计算信息增益
    public static double CalculateInformationGain(List<DataSet> data, string attribute)
    {
        double originalEntropy = CalculateEntropy(data);
        var subsets = SplitDataSet(data, attribute);

        double newEntropy = 0;
        foreach (var subset in subsets)
        {
            newEntropy += (subset.Value.Count / (double)data.Count) * CalculateEntropy(subset.Value);
        }

        return originalEntropy - newEntropy;
    }

    // 选择最佳的属性进行划分
    public static string ChooseBestAttribute(List<DataSet> data, List<string> attributes)
    {
        double maxGain = double.MinValue;
        string bestAttribute = null;

        foreach (var attribute in attributes)
        {
            double gain = CalculateInformationGain(data, attribute);
            if (gain > maxGain)
            {
                maxGain = gain;
                bestAttribute = attribute;
            }
        }

        return bestAttribute;
    }

    // 构建决策树
    public static TreeNode BuildDecisionTree(List<DataSet> data, List<string> attributes)
    {
        // 如果数据集为空或所有实例的标签相同，返回叶节点
        if (data.Count == 0)
            return null;

        var firstLabel = data[0].BuyComputer;
        if (data.All(d => d.BuyComputer == firstLabel))
        {
            return new TreeNode { Label = firstLabel };
        }

        // 如果没有特征可供选择，返回叶节点
        if (attributes.Count == 0)
        {
            return new TreeNode { Label = firstLabel };
        }

        // 选择最佳属性
        string bestAttribute = ChooseBestAttribute(data, attributes);
        var node = new TreeNode { Attribute = bestAttribute };

        // 递归创建子树
        var subsets = SplitDataSet(data, bestAttribute);
        foreach (var subset in subsets)
        {
            var remainingAttributes = new List<string>(attributes);
            remainingAttributes.Remove(bestAttribute);
            node.Children[subset.Key] = BuildDecisionTree(subset.Value, remainingAttributes);
        }

        return node;
    }

    // 打印决策树
    public static void PrintTree(TreeNode node, string indent = "")
    {
        if (node.Label != null)
        {
            Console.WriteLine(indent + "Label: " + node.Label);
        }
        else
        {
            Console.WriteLine(indent + "Attribute: " + node.Attribute);
            foreach (var child in node.Children)
            {
                Console.WriteLine(indent + "  " + child.Key + " ->");
                PrintTree(child.Value, indent + "    ");
            }
        }
    }

    public static void Main(string[] args)
    {
        // 输入数据集
        var dataSet = new List<DataSet>
        {
            new DataSet { Age = "青", Income = "高", Student = "否", Credit = "良", BuyComputer = "不买" },
            new DataSet { Age = "青", Income = "高", Student = "否", Credit = "优", BuyComputer = "不买" },
            new DataSet { Age = "中", Income = "高", Student = "否", Credit = "良", BuyComputer = "买" },
            new DataSet { Age = "老", Income = "中", Student = "否", Credit = "良", BuyComputer = "买" },
            new DataSet { Age = "老", Income = "低", Student = "是", Credit = "良", BuyComputer = "买" },
            new DataSet { Age = "老", Income = "低", Student = "是", Credit = "优", BuyComputer = "不买" },
            new DataSet { Age = "中", Income = "低", Student = "是", Credit = "优", BuyComputer = "买" },
            new DataSet { Age = "青", Income = "中", Student = "否", Credit = "良", BuyComputer = "不买" },
            new DataSet { Age = "青", Income = "低", Student = "是", Credit = "良", BuyComputer = "买" },
            new DataSet { Age = "老", Income = "中", Student = "是", Credit = "良", BuyComputer = "买" },
            new DataSet { Age = "青", Income = "中", Student = "是", Credit = "优", BuyComputer = "买" },
            new DataSet { Age = "中", Income = "中", Student = "否", Credit = "优", BuyComputer = "买" },
            new DataSet { Age = "中", Income = "高", Student = "是", Credit = "良", BuyComputer = "买" },
            new DataSet { Age = "老", Income = "中", Student = "否", Credit = "优", BuyComputer = "不买" },
            new DataSet { Age = "老", Income = "中", Student = "否", Credit = "优", BuyComputer = "买" }
        };

        // 属性集合
        var attributes = new List<string> { "Age", "Income", "Student", "Credit" };

        // 构建决策树
        var decisionTree = BuildDecisionTree(dataSet, attributes);

        // 打印决策树
        PrintTree(decisionTree);
    }
}
