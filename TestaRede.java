
/**
 * Escreva a descrição da classe TestaRede aqui.
 * 
 * @author Silvia
 * @version 12/11/2020
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestaRede {
    private double[] tabuleiro;
    private int[][] tabuleiroVelha;
    private Rede rn;
    private double[][] populacao;
    private int populacaoSize = 10;
    private GeneticAlgorithm ga = new GeneticAlgorithm(false, 0.05, 0.95);

    public TestaRede() {
        // ------------------------ EXEMPLO DE TABULEIRO
        // ------------------------------------------
        // tabuleiro do jogo da velha - Exemplo de teste
        tabuleiroVelha = new int[][] { { -1, -1, -1 }, // -1: celula livre 1: X 0: O
                { -1, -1, -1 },
                { -1, -1, -1 } };

        // Exibe tabuleiro
        System.out.println("\f\nTabuleiro inicial: ");
        for (int i = 0; i < tabuleiroVelha.length; i++) {
            for (int j = 0; j < tabuleiroVelha.length; j++) {
                System.out.print(tabuleiroVelha[i][j] + " \t");
            }
            System.out.println();
        }
        System.out.println(toString(tabuleiroVelha));

        // tabuleiro de teste - conversao de matriz para vetor
        tabuleiro = new double[tabuleiroVelha.length * tabuleiroVelha.length];
        int k = 0;
        for (int i = 0; i < tabuleiroVelha.length; i++) {
            for (int j = 0; j < tabuleiroVelha.length; j++) {
                tabuleiro[k] = tabuleiroVelha[i][j];
                k++;
            }
        }

        // ------------------------ EXEMPLO DE REDE
        // ------------------------------------------
        // Cria e configura a rede
        // Criando a rede
        int oculta = 9; // numero de neuronios da camada oculta
        int saida = 9; // numero de neuronios da camada de saida
        rn = new Rede(oculta, saida); // topologia da rede: 9 neurônios na camada oculta e 9 na de saída

        // Simulando um cromossomo da populacao do AG
        Random gera = new Random();
        int pesosOculta = oculta + 1; // numero de pesos por neuronio da camada oculta
        int pesosSaida = saida + 1; // numero de pesos por neuronio da camada de saida
        int totalPesos = pesosOculta * oculta + pesosSaida * saida;
        double[] cromossomo = new double[totalPesos + 1];
        populacao = new double[populacaoSize][totalPesos + 1];

        for (int j = 0; j < populacaoSize; j++) {
            for (int i = 0; i < cromossomo.length; i++) {
                cromossomo[i] = gera.nextDouble();
                if (gera.nextBoolean())
                    cromossomo[i] = cromossomo[i] * -1;
                // System.out.print(cromossomo[i] + " ");
            }
            cromossomo[cromossomo.length - 1] = 0;
            populacao[j] = cromossomo;
        }
        printPopulation(populacao);

        populacao = ga.defineNewPopulation(populacao);
        printPopulation(populacao);

        // Setando os pesos na rede
        rn.setPesosNaRede(tabuleiro.length, cromossomo); //

        System.out.println();

        // Exibe rede neural
        System.out.println("Rede Neural - Pesos: ");

        int state = checkGameOver(tabuleiroVelha);
        System.out.println("Estado: " + state);

        // System.out.println(rn);

        // --------------EXEMPLO DE EXECUCAO ----------------------------------------

        // for (int n = 1; n <= 3; n++) {
        // System.out.println("\n\n>>>RODADA: " + n);
        // // Exibe um exemplo de propagação : saida dos neurônios da camada de saída
        // double[] saidaRede = rn.propagacao(tabuleiro);
        // System.out.println("Rede Neural - Camada de Saida: Valor de Y");
        // for (int i = 0; i < saidaRede.length; i++) {
        // System.out.println("Neuronio " + i + " : " + saidaRede[i]);
        // }

        // // Define posicao a jogar de acordo com rede
        // int indMaior = 0;
        // double saidaMaior = saidaRede[0];
        // for (int i = 1; i < saidaRede.length; i++) {
        // if (saidaRede[i] > saidaMaior) {
        // saidaMaior = saidaRede[i];
        // indMaior = i;
        // }
        // }
        // int linha = indMaior / 3;
        // int coluna = indMaior % 3;
        // System.out.println("Neuronio de maior valor: " + indMaior + " - " +
        // saidaRede[indMaior]);
        // System.out.println(">>> Rede escolheu - Linha: " + linha + " Coluna: " +
        // coluna);

        // if (tabuleiroVelha[linha][coluna] != -1)
        // System.out.println("Posicao ocupada");
        // else {
        // tabuleiroVelha[linha][coluna] = 1;

        // System.out.println("\nTabuleiro apos jogada: ");
        // for (int i = 0; i < tabuleiroVelha.length; i++) {
        // for (int j = 0; j < tabuleiroVelha.length; j++) {
        // System.out.print(tabuleiroVelha[i][j] + "\t");
        // }
        // System.out.println();
        // }
        // }

        // // -----------------------------------------JOGA MINIMAX
        // TestaMinimax mini = new TestaMinimax(tabuleiroVelha);
        // Sucessor melhor = mini.joga();

        // System.out.println(">>> MINIMAX escolheu - Linha: " + melhor.getLinha() + "
        // Coluna: " + melhor.getColuna());

        // if (tabuleiroVelha[melhor.getLinha()][melhor.getColuna()] != -1)
        // System.out.println("Posicao ocupada");
        // else {
        // tabuleiroVelha[melhor.getLinha()][melhor.getColuna()] = 0;

        // System.out.println("\nTabuleiro apos jogada: ");
        // for (int i = 0; i < tabuleiroVelha.length; i++) {
        // for (int j = 0; j < tabuleiroVelha.length; j++) {
        // System.out.print(tabuleiroVelha[i][j] + "\t");
        // }
        // System.out.println();
        // }
        // }

        // // tabuleiro de teste - conversao de matriz para vetor
        // k = 0;
        // for (int i = 0; i < tabuleiroVelha.length; i++) {
        // for (int j = 0; j < tabuleiroVelha.length; j++) {
        // tabuleiro[k] = tabuleiroVelha[i][j];
        // k++;
        // }
        // }
        // }
    }

    private String symbolConverter(int symbol) {
        if (symbol == 1) {
            return "X";
        } else if (symbol == 0) {
            return "O";
        } else {
            return "-";
        }
    }

    public List<Integer> decodeBoard(int[][] tabuleiroVelha) {

        List<Integer> posicoes = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                posicoes.add(tabuleiroVelha[i][j]);
            }
        }

        return posicoes;
    }

    public String toString(int[][] tabuleiroVelha) {
        String result = "";
        List<Integer> posicoes = decodeBoard(tabuleiroVelha);
        for (int i = 0; i < posicoes.size(); i++) {
            if (i % 3 == 0) {
                result += "\n";
            }

            result += "|" + symbolConverter(posicoes.get(i)) + "|";

        }
        result += "\n";
        return result;
    }

    private void printPopulation(double[][] population) {
        // for (int i = 0; i < population.length; i++) {
        // System.out.println("Cromossomo " + i + ": ");
        // for (int j = 0; j < population[i].length; j++) {
        // System.out.print(population[i][j] + " ");

        // }
        // System.out.println();
        // }

        System.out.println(population[0][population[0].length - 1]);

        System.out.println(population.length);
        System.out.println(population[0].length);

    }

    private static int checkGameOver(int[][] board) {
        // Check rows
        for (int line = 0; line < 3; line++) {
            if (board[line][0] == board[line][1] && board[line][1] == board[line][2]) {
                if (board[line][0] == 0) {
                    return 0; // "O victory"
                } else if (board[line][0] == 1) {
                    return 1; // "X victory"
                }
            }
        }

        // Check columns
        for (int col = 0; col < 3; col++) {
            if (board[0][col] == board[1][col] && board[1][col] == board[2][col]) {
                if (board[0][col] == 0) {
                    return 0; // "O victory"
                } else if (board[0][col] == 1) {
                    return 1; // "X victory"
                }
            }
        }

        // Check diagonals
        if ((board[0][0] == board[1][1] && board[1][1] == board[2][2]) ||
                (board[0][2] == board[1][1] && board[1][1] == board[2][0])) {
            if (board[1][1] == 0) {
                return 0; // "O victory"
            } else if (board[1][1] == 1) {
                return 1; // "X victory"
            }
        }

        // Check if the game is a draw
        boolean draw = true;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row][col] == -1) {
                    draw = false;
                    break;
                }
            }
        }
        if (draw) {
            return 2; // "Not over, but it's a draw"
        }

        // Game is not over
        return -1;
    }

    public static void main(String args[]) {
        TestaRede teste = new TestaRede();

    }

}
