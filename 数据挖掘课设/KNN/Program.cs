using System;
using System.Collections.Generic;
using System.Linq;

class KNN
{
    public static string PredictLevel(double height, List<Tuple<double, string>> data, int k)
    {
        // 1. 计算每个训练数据点与目标点之间的距离
        var distances = new List<Tuple<double, string>>();
        foreach (var record in data)
        {
            double distance = Math.Abs(record.Item1 - height); // 计算欧几里得距离
            distances.Add(Tuple.Create(distance, record.Item2));
        }

        // 2. 按距离升序排序
        distances.Sort((x, y) => x.Item1.CompareTo(y.Item1));

        // 3. 选择最近的k个邻居
        var nearestNeighbors = distances.Take(k).ToList();

        // 4. 投票选择最多的类别
        var voteCounts = nearestNeighbors.GroupBy(x => x.Item2)
                                          .ToDictionary(g => g.Key, g => g.Count());

        // 5. 返回出现最多的类别
        var predictedClass = voteCounts.Aggregate((x, y) => x.Value > y.Value ? x : y).Key;

        return predictedClass;
    }

    public static void Main()
    {
        // 训练数据：身高，等级
        List<Tuple<double, string>> data = new List<Tuple<double, string>>()
        {
            Tuple.Create(1.5, "矮"),
            Tuple.Create(1.92, "高"),
            Tuple.Create(1.7, "中等"),
            Tuple.Create(1.73, "中等"),
            Tuple.Create(1.6, "矮"),
            Tuple.Create(1.75, "中等"),
            Tuple.Create(1.6, "矮"),
            Tuple.Create(1.9, "高"),
            Tuple.Create(1.68, "中等"),
            Tuple.Create(1.78, "中等"),
            Tuple.Create(1.7, "中等"),
            Tuple.Create(1.68, "中等"),
            Tuple.Create(1.65, "矮"),
            Tuple.Create(1.78, "中等")
        };

        // 易昌的身高
        double newStudentHeight = 1.74;
        int k = 5;

        // 预测等级
        string predictedLevel = PredictLevel(newStudentHeight, data, k);

        // 输出结果
        Console.WriteLine($"易昌的身高为 {newStudentHeight} cm，根据kNN算法预测其等级为：{predictedLevel}");
    }
}
