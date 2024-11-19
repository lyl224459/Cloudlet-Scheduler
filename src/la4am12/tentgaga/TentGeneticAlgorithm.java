package la4am12.tentgaga;

/**
 * @author : LA4AM12
 * @create : 2023-03-12 18:11:52
 * @description : genetic algorithm
 */

import la4am12.datacenter.OptFunction;

import java.util.*;

import static la4am12.datacenter.chaosMap.*;

public class TentGeneticAlgorithm {
    /**
     * optFunction: 优化函数，用于评估染色体的适应度。
     * boundary: 边界值，可能用于限制某些参数的范围。
     * population: 种群大小，即每一代中染色体的数量。
     * genesN: 基因数量，每个染色体包含的基因数。
     * maxGenerations: 最大代数，算法运行的最大迭代次数。
     * crossoverRate: 交叉率，决定两个染色体进行交叉的概率。
     * mutationRate: 变异率，决定染色体发生变异的概率。
     * tournamentSize: 锦标赛选择的规模，用于选择下一代的染色体。
     * random: 随机数生成器，用于生成随机数。
     * Chromosomes: 染色体列表，存储当前种群中的所有染色体。
     * bestChromosome: 最佳染色体，存储当前找到的最佳解。
     * */
    private OptFunction optFunction;
    private int boundary;
    private int population;
    private int genesN, maxGenerations;
    private double crossoverRate;
    private double mutationRate;
    private int tournamentSize;
    private static final Random random = new Random();
    private List<Chromosome> Chromosomes;
    Chromosome bestChromosome = null;

    public TentGeneticAlgorithm(OptFunction optFunction, int population, double crossoverRate, double mutationRate, int boundary, int genesN, int tournamentSize, int maxGenerations) {
        this.optFunction = optFunction;
        this.population = population;
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
        this.boundary = boundary;
        this.genesN = genesN;
        this.maxGenerations = maxGenerations;
        this.tournamentSize = tournamentSize;
    }

    public void initializePopulation() {
        // 检查Chromosomes是否为null，如果是，则进行初始化
        if (Chromosomes == null) {
            // 创建一个ArrayList来存储染色体
            Chromosomes = new ArrayList<>();
            // 遍历种群大小，创建每个个体
            for (int i = 0; i < population; i++) {
                // 创建一个基因数组，长度为genesN
                int[] genes = new int[genesN];
                // 生成初始混沌值
                double x = random.nextDouble();
                // 遍历基因数组，为每个基因赋予混沌值
                for (int j = 0; j < genesN; j++) {
                    // 使用Tent混沌映射生成下一个混沌值
                    x = circleMap(x);
                    // 将混沌值转换为基因值，范围为0到boundary-1
                    genes[j] = (int)(random.nextInt(boundary) * x);
                }
                // 将基因数组作为参数创建一个新的染色体对象，并添加到Chromosomes列表中
                Chromosomes.add(new Chromosome(genes));
            }
        }
    }

    public void evaluatePopulation() {
        for (Chromosome chromosome : Chromosomes) {
            double fitness = optFunction.calc(chromosome.getGenes());
            chromosome.setFitness(fitness);
        }
        bestChromosome = Collections.min(Chromosomes);
        // System.out.println(bestChromosome);
    }

    public void evolvePopulation() {
        List<Chromosome> newPopulation = new ArrayList<>();
        for (int i = 0; i < population; i++) {
            Chromosome parent1 = tournamentSelection();
            Chromosome parent2 = tournamentSelection();
            Chromosome offspring = crossover(parent1, parent2);
            mutate(offspring);
            newPopulation.add(offspring);
        }
        Chromosomes = newPopulation;
    }

    // 从种群中选择一个染色体进行锦标赛选择
    public Chromosome tournamentSelection() {
        List<Chromosome> tournament = new ArrayList<>();
        for (int i = 0; i < tournamentSize; i++) {
            int randomIndex = random.nextInt(population);
            tournament.add(Chromosomes.get(randomIndex));
        }
        return Collections.min(tournament);
    }

    // 对两个染色体进行交叉，生成一个新的染色体
    public Chromosome crossover(Chromosome parent1, Chromosome parent2) {
        int[] genes1 = parent1.getGenes();
        int[] genes2 = parent2.getGenes();
        int[] offspringGenes = new int[genesN];
        for (int i = 0; i < genesN; i++) {
            if (random.nextDouble() < crossoverRate) {
                offspringGenes[i] = genes1[i];
            } else {
                offspringGenes[i] = genes2[i];
            }
        }
        return new Chromosome(offspringGenes);
    }

    // 对染色体进行变异
    public void mutate(Chromosome chromosome) {
        int[] genes = chromosome.getGenes();
        for (int i = 0; i < genesN; i++) {
            if (random.nextDouble() < mutationRate) {
                genes[i] = random.nextInt(boundary);
            }
        }
        chromosome.setGenes(genes);
    }

    // 执行遗传算法
    public int[] run() {
        initializePopulation();
        evaluatePopulation();
        for (int i = 0; i < maxGenerations; i++) {
            evolvePopulation();
            evaluatePopulation();
        }
        return bestChromosome.getGenes();
    }

    // 染色体类
    private static class Chromosome implements Comparable<Chromosome> {
        private int[] genes;
        private double fitness;

        public Chromosome(int[] genes) {
            this.genes = genes;
        }

        public int[] getGenes() {
            return genes;
        }

        public void setGenes(int[] genes) {
            this.genes = genes;
        }

        public double getFitness() {
            return fitness;
        }

        public void setFitness(double fitness) {
            this.fitness = fitness;
        }

        @Override
        public int compareTo(Chromosome other) {
            return Double.compare(fitness, other.fitness);
        }

        @Override
        public String toString() {
            return "fitness:" + fitness + "genes:" + Arrays.toString(genes);
        }
    }

    public void setChromosomes(int[][] chromosomes) {
        Chromosomes = new ArrayList<>();
        for (int[] chromosome : chromosomes) {
            Chromosomes.add(new Chromosome(chromosome));
        }
    }
}

