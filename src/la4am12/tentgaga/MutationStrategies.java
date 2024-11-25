package la4am12.tentgaga;

/**
 * Copyright (C), 2024-11-21
 * FileName: MutationStrategies
 * Author:   LYL
 * Date:     2024/11/21 上午10:52
 * Description:
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.distribution.CauchyDistribution;
import org.apache.commons.math3.distribution.TDistribution;

public class MutationStrategies {
    private static final Random random = new Random();

    /**
     * 均匀随机变异
     *
     * @param genes        基因数组
     * @param mutationRate 变异率
     * @param boundary     边界值
     */
    public static void uniformMutation(int[] genes, double mutationRate, int boundary) {
        for (int i = 0; i < genes.length; i++) {
            if (random.nextDouble() < mutationRate) {
                genes[i] = random.nextInt(boundary);
            }
        }
    }

    /**
     * 柯西变异
     *
     * @param genes        基因数组
     * @param mutationRate 变异率
     * @param boundary     边界值
     */
    public static void cauchyMutation(int[] genes, double mutationRate, int boundary) {
        CauchyDistribution cauchy = new CauchyDistribution(0, 1);
        for (int i = 0; i < genes.length; i++) {
            if (random.nextDouble() < mutationRate) {
                double mutationStep = cauchy.sample();
                genes[i] += (int) mutationStep;
                if (genes[i] < 0) genes[i] = 0;
                if (genes[i] >= boundary) genes[i] = boundary - 1;
            }
        }
    }

    /**
     * 随机差分变异
     *
     * @param genes        目标基因数组
     * @param mutationRate 变异率
     * @param boundary     边界值
     * @param population   当前种群
     */
    public static void differentialMutation(int[] genes, double mutationRate, int boundary, List<int[]> population) {
        int[] diffVector = new int[genes.length];
        double F = 0.5; // 缩放因子

        // 随机选择三个不同的个体
        List<int[]> selected = new ArrayList<>(population);
        Collections.shuffle(selected);
        int[] r1 = selected.get(0);
        int[] r2 = selected.get(1);
        int[] r3 = selected.get(2);

        // 计算差分向量
        for (int i = 0; i < genes.length; i++) {
            diffVector[i] = r1[i] - r2[i] + r3[i];
        }

        // 应用差分向量
        for (int i = 0; i < genes.length; i++) {
            if (random.nextDouble() < mutationRate) {
                genes[i] += (int) (F * diffVector[i]);
                if (genes[i] < 0) genes[i] = 0;
                if (genes[i] >= boundary) genes[i] = boundary - 1;
            }
        }
    }

    /**
     * 自适应 t 分布扰动变异
     *
     * @param genes            基因数组
     * @param mutationRate     变异率
     * @param boundary         边界值
     * @param degreesOfFreedom 自由度
     */
    public static void adaptiveTMutation(int[] genes, double mutationRate, int boundary, double degreesOfFreedom) {
        TDistribution tDist = new TDistribution(degreesOfFreedom);
        for (int i = 0; i < genes.length; i++) {
            if (random.nextDouble() < mutationRate) {
                double mutationStep = tDist.sample();
                genes[i] += (int) mutationStep;
                if (genes[i] < 0) genes[i] = 0;
                if (genes[i] >= boundary) genes[i] = boundary - 1;
            }
        }
    }
}