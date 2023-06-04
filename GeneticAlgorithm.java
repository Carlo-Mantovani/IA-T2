import java.util.Random;

public class GeneticAlgorithm {

    private boolean ELITISM = true;
    private double mutationRate = 0.05;
    private double crossOverRate = 0.95;
    private Random random = new Random();

    public GeneticAlgorithm(boolean elite, double mutation, double crossOver) {
        this.ELITISM = elite;
        this.mutationRate = mutation;
        this.crossOverRate = crossOver;
    }

    public double[][] defineNewPopulation(double[][] population) {

        double[][] newPopulation = new double[population.length][population[0].length];

        if (ELITISM) {
            newPopulation[0] = setElite(population);
        }
        newPopulation = crossOver(population);

        newPopulation = mutation(newPopulation);

        System.out.println("New population: ");
        for (int i = 0; i < newPopulation.length; i++) {
            for (int j = 0; j < newPopulation[0].length; j++) {
                System.out.print(newPopulation[i][j] + " ");
            }
            System.out.println();
        }
        return newPopulation;
    }

    private double[][] crossOver(double[][] population) {
        double[][] newPopulation = new double[population.length][population[0].length];
        int index = 1;
        int startIndex = 1;
        if (!ELITISM) {
            startIndex = 0;
            index = 0;
        }
        for (int i = startIndex; i < population.length; i++) {
            if (random.nextDouble() <= crossOverRate) {
                double[] parent1 = getParents(population);
                double[] parent2 = getParents(population);
                double[] child = getChild(parent1, parent2);
                newPopulation[index] = child;
                index++;
            } else {
                newPopulation[index] = population[i];
                index++;
            }
        }
        return newPopulation;
    }

    private double[] getParents(double[][] population) {

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

    private double[] getChild(double[] parent1, double[] parent2) {
        double[] child = new double[parent1.length];

        for (int i = 0; i < parent1.length; i++) {
            child[i] = (parent1[1] + parent2[i]) / 2;
        }
        return child;
    }

    private double[][] mutation(double[][] population) {

        if (random.nextDouble() <= mutationRate) {
            int randomLine = random.nextInt(population.length);
            int randomColumn = random.nextInt(population[0].length);
            population[randomLine][randomColumn] = random.nextDouble(-1, 1);
        }
        return population;
    }

    private double[] setElite(double[][] population) {

        
        int bestIndex = 0;
        for (int i = 0; i < population.length; i++) {
            if (population[i][population[i].length - 1] > population[bestIndex][population[bestIndex].length - 1]) {
                bestIndex = i;
            }
        }
        
        return population[bestIndex];
    }

}
