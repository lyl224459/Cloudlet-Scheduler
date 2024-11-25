package la4am12.ssa;

import la4am12.datacenter.OptFunction;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SparrowSearchAlgorithm {
    private int pop; // 种群大小
    private int M; // 最大迭代次数
    private double c; // 搜索空间的下界
    private double d; // 搜索空间的上界
    private int dim; // 问题的维度
    private OptFunction optFunction; // 目标函数的函数指针
    private double[] bestX; // 最佳解向量
    private double fMin; // 最小值
    private double[] Convergence_curve; // 收敛曲线

    public SparrowSearchAlgorithm(int pop, int M, double c, double d, int dim, OptFunction optFunction) {
        this.pop = pop;
        this.M = M;
        this.c = c;
        this.d = d;
        this.dim = dim;
        this.optFunction = optFunction;
    }
}
