import java.util.Random;

// Classe que implementa o algoritmo genético
public class GeneticAlgorithm {

    private boolean ELITISM = true;// flag elitismo
    private double mutationRate = 0.05;// taxa de mutação
    private double crossOverRate = 0.95;// taxa de crossover
    private Random random = new Random();// gerador de números aleatórios

    // construtor
    public GeneticAlgorithm(boolean elite, double mutation, double crossOver) {
        this.ELITISM = elite;
        this.mutationRate = mutation;
        this.crossOverRate = crossOver;
    }

    // define a nova população
    public double[][] defineNewPopulation(double[][] population) {

        double[][] newPopulation = new double[population.length][population[0].length];// nova população

        if (ELITISM) {// se for elitismo, mantém o melhor indivíduo
            newPopulation[0] = setElite(population);
        }
        newPopulation = crossOver(population);// crossover

        newPopulation = mutation(newPopulation);// mutação

        return newPopulation;// retorna a nova população
    }

    // realiza o crossover
    private double[][] crossOver(double[][] population) {
        // se for elitismo, começa a partir do segundo indivíduo
        int index = 1;
        int startIndex = 1;
        if (!ELITISM) {
            startIndex = 0;
            index = 0;
        }
        // preenche a nova população com os filhos
        for (int i = startIndex; i < population.length - 1; i++) {
            // seleciona os pais
            double[] parent1 = getParents(population);
            double[] parent2 = getParents(population);
            if (random.nextDouble() <= crossOverRate) {// se for para realizar o crossover
                double[] child = getChild(parent1, parent2);// gera o filho

                population[index] = child;

            } else {// se não for para realizar o crossover, mantém o melhor indivíduo
                if (parent1[parent1.length - 1] > parent2[parent2.length - 1]) {
                    population[index] = parent1;
                } else {
                    population[index] = parent2;
                }

            }
            index++;
        }
        return population;
    }

    private double[] getParents(double[][] population) {// seleciona os pais

        double[] parent = new double[population[0].length];

        double[] parent1 = population[random.nextInt(population.length)];
        double[] parent2 = population[random.nextInt(population.length)];

        if (parent1[parent1.length - 1] > parent2[parent2.length - 1]) {
            parent = parent1;
        } else {
            parent = parent2;
        }

        return parent;
    }

    private double[] getChild(double[] parent1, double[] parent2) {// gera o filho
        double[] child = new double[parent1.length];

        for (int i = 0; i < parent1.length - 1; i++) {
            child[i] = (parent1[i] + parent2[i]) / 2;
        }
        child[parent1.length - 1] = 0;
        return child;
    }

    private double[][] mutation(double[][] population) {// mutação
        // se for elitismo, começa a partir do segundo indivíduo
        int lowerBound = 0;
        if (ELITISM) {
            lowerBound = 1;
        }
        // para cada gene, verifica se vai ocorrer a mutação
        for (int i = lowerBound; i < population.length; i++) {
            for (int j = 0; j < population[0].length - 1; j++) {
                if (random.nextDouble() <= mutationRate) {
                    population[i][j] = random.nextDouble() * 2 - 1;
                }
            }
        }

        return population;
    }

    private double[] setElite(double[][] population) {

        // encontra o melhor indivíduo da população
        int bestIndex = 0;
        for (int i = 0; i < population.length; i++) {

            if (population[i][population[i].length - 1] > population[bestIndex][population[bestIndex].length - 1]) {
                bestIndex = i;

            }
        }

        return population[bestIndex];
    }

    // getters e setters
    public double getMutationRate() {
        return mutationRate;
    }

    public void setMutationRate(double mutationRate) {
        this.mutationRate = mutationRate;
    }

    public double getCrossOverRate() {
        return crossOverRate;
    }

    public void setCrossOverRate(double crossOverRate) {
        this.crossOverRate = crossOverRate;
    }

}
