using System;
using System.Collections.Generic;
using System.Linq;

class KMeans
{
    static void Main(string[] args)
    {
        // 数据点 (P1, P2, ..., P13)
        double[,] data = new double[2, 13]
        {
            {1, 2, 2, 4, 5, 6, 6, 7, 9, 1, 3, 5, 3}, // 第一维为 x 坐标
            {2, 1, 4, 3, 8, 7, 9, 9, 5, 12, 12, 12, 3} // 第二维为 y 坐标
        };

        // 聚类数目
        int k = 3;
        int maxIterations = 100; // 最大迭代次数

        // 初始化聚类中心，随机选择 3 个数据点作为初始中心
        Random rand = new Random();
        double[,] centroids = new double[k, 2];
        for (int i = 0; i < k; i++)
        {
            int randomIndex = rand.Next(13);
            centroids[i, 0] = data[0, randomIndex];
            centroids[i, 1] = data[1, randomIndex];
        }

        int[] labels = new int[13];
        double[,] newCentroids = new double[k, 2];
        bool centroidsChanged;

        for (int iteration = 0; iteration < maxIterations; iteration++)
        {
            centroidsChanged = false;

            // 1. 分配数据点到最近的聚类中心
            for (int i = 0; i < 13; i++)
            {
                double minDistance = double.MaxValue;
                int closestCentroid = 0;

                for (int j = 0; j < k; j++)
                {
                    double distance = EuclideanDistance(data[0, i], data[1, i], centroids[j, 0], centroids[j, 1]);
                    if (distance < minDistance)
                    {
                        minDistance = distance;
                        closestCentroid = j;
                    }
                }
                labels[i] = closestCentroid;
            }

            // 2. 重新计算聚类中心
            for (int j = 0; j < k; j++)
            {
                double sumX = 0, sumY = 0;
                int count = 0;

                for (int i = 0; i < 13; i++)
                {
                    if (labels[i] == j)
                    {
                        sumX += data[0, i];
                        sumY += data[1, i];
                        count++;
                    }
                }

                if (count > 0)
                {
                    newCentroids[j, 0] = sumX / count;
                    newCentroids[j, 1] = sumY / count;
                }
                else
                {
                    newCentroids[j, 0] = centroids[j, 0];
                    newCentroids[j, 1] = centroids[j, 1];
                }

                if (newCentroids[j, 0] != centroids[j, 0] || newCentroids[j, 1] != centroids[j, 1])
                {
                    centroidsChanged = true;
                }
            }

            // 更新聚类中心
            for (int j = 0; j < k; j++)
            {
                centroids[j, 0] = newCentroids[j, 0];
                centroids[j, 1] = newCentroids[j, 1];
            }

            if (!centroidsChanged)
            {
                break;
            }
        }

        // 显示每个聚类的点
        Console.WriteLine("聚类结果:");
        for (int j = 0; j < k; j++)
        {
            Console.Write($"Cluster {j + 1}: ");
            List<string> clusterPoints = new List<string>();

            for (int i = 0; i < 13; i++)
            {
                if (labels[i] == j)
                {
                    clusterPoints.Add($"P{i + 1}");
                }
            }

            Console.WriteLine(string.Join(", ", clusterPoints));
        }

        // 输出最终聚类中心
        Console.WriteLine("\n最终聚类中心:");
        for (int i = 0; i < k; i++)
        {
            Console.WriteLine($"聚类 {i + 1} 的中心: ({centroids[i, 0]}, {centroids[i, 1]})");
        }
    }

    // 计算欧氏距离
    static double EuclideanDistance(double x1, double y1, double x2, double y2)
    {
        return Math.Sqrt(Math.Pow(x2 - x1, 2) + Math.Pow(y2 - y1, 2));
    }
}
