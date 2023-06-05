
/**
 * Escreva a descrição da classe TestaRede aqui.
 * 
 * @author Silvia
 * @version 12/11/2020
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.text.TableView;

public class TestaRede {
    private double[] tabuleiro;
    private int[][] tabuleiroVelha;
    private Rede rn;
    private double[][] populacao;
    private int populacaoSize = 10;
    private GeneticAlgorithm ga = new GeneticAlgorithm(true, 0.05, 0.95);

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
            cromossomo = new double[totalPesos + 1];
            for (int i = 0; i < cromossomo.length; i++) {
                cromossomo[i] = gera.nextDouble(-1,1);
                if (gera.nextBoolean())
                    cromossomo[i] = cromossomo[i] * -1;
                // System.out.print(cromossomo[i] + " ");
            }
            cromossomo[cromossomo.length - 1] = 0;
            populacao[j] = cromossomo;
        }
        //printPopulation(populacao);

        populacao = ga.defineNewPopulation(populacao);
        //printPopulation(populacao);

        // Setando os pesos na rede
        rn.setPesosNaRede(tabuleiro.length, cromossomo); //

       // System.out.println();

        // Exibe rede neural
        //System.out.println("Rede Neural - Pesos: ");

        int state = checkBoardState(tabuleiroVelha);
        System.out.println("Estado: " + state);

        gameLoop();
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
        for (int i = 0; i < population.length; i++) {
        if (i == 1) {
        System.out.println("Cromossomo " + i + ": ");
        for (int j = 0; j < population[i].length; j++) {
        System.out.print(population[i][j] + " ");

        }
    }
        System.out.println();
        
    }

        // System.out.println(population[0][population[0].length - 1]);

        // System.out.println(population.length);
        // System.out.println(population[0].length);

    }

    private static int checkBoardState(int[][] board) {
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

    private void setTabuleiro(int[][] tabuleiroVelha) {
        int k = 0;
        for (int i = 0; i < tabuleiroVelha.length; i++) {
            for (int j = 0; j < tabuleiroVelha.length; j++) {
                tabuleiro[k] = tabuleiroVelha[i][j];
                k++;
            }
        }
    }

    private int getMaior(double[] saidaRede) {
        int indexMaior = 0;
        for (int i = 0; i < saidaRede.length; i++) {
            if (saidaRede[i] > saidaRede[indexMaior]) {
                indexMaior = i;
            }
        }
        return indexMaior;
    }

    private boolean checkOccupied(int linha, int coluna, int[][] tabuleiroVelha) {
        if (tabuleiroVelha[linha][coluna] != -1) {
            return true;
        }
        return false;
    }

    private boolean checkGameOver(int[][] board) {
        if (checkBoardState(board) == -1) {
            return false;
        }
        return true;
    }

    private double getAptitude(int[][] board, double aptidao, boolean flagPlayer) {
        int state = checkBoardState(board);
        if (flagPlayer){
        if (state == 0) {
            return -10;
        } else if (state == 1) {
            return 10;
        } else if (state == 2) {
            return 5;
        } else if (state == -1) {
            return 1;
        }
    }
    else{
        if (state == 0) {
            return -10;
      
        } else if (state == 2) {
            return 5;
        } 
    }
        return 0;
    }
     
    

    private int[][] resetBoard(){
        int [][] newBoard = new int[3][3];
        for (int i = 0; i < newBoard.length; i++) {
            for (int j = 0; j < newBoard.length; j++) {
                newBoard[i][j] = -1;
            }
        }
        return newBoard;
    }

    private int[][] randomPlay(int[][] board){
        int linha = 0;
        int coluna = 0;
        Random random = new Random();
        while (true) {
            linha = random.nextInt(3);
            coluna = random.nextInt(3);
            if (!checkOccupied(linha, coluna, board)) {
                break;
            }
        }
        board[linha][coluna] = 0;
        return board;

    }

    private void gameLoop() {
        TestaMinimax mini = new TestaMinimax(tabuleiroVelha);
        Sucessor melhor;

        int[][] board = resetBoard();
        double aptidao = 0;
        int index = 0;
        int indexAptidao = populacao[0].length - 1;

        for (int i = 0; i < 1000000; i++) {
            System.out.println("Iteracao: " + i);
            // for (int j = 0; j < populacao.length; j++) {
            //     System.out.println("Cromossomo: " + j);
            //     System.out.println(populacao[j][populacao[0].length - 1]);
            // }

            int lowCount = 0;
            int highCount = 0;
            index = 0;
            for (int j = 0; j < populacao.length; j++) {
                //System.out.println("Cromosome: " + j);
                index++;
                rn.setPesosNaRede(tabuleiro.length, populacao[j]);
                board = resetBoard();
                aptidao = 0;

                while (true) {
                    setTabuleiro(board);

                    double[] saidaRede = rn.propagacao(tabuleiro);
                    int indexMaior = getMaior(saidaRede);

                    //System.out.println("Jogada da Rede: ");
                    //System.out.println("Linha: " + indexMaior / 3 + " Coluna: " + indexMaior % 3);
                    
                    //System.out.println("Value in line and column" + board[indexMaior / 3][indexMaior % 3] );
                    if (checkOccupied(indexMaior / 3, indexMaior % 3, board)) {
                        aptidao -= 1;

                        //System.out.println("Jogada invalida");
                        break;
                    }

                    board[indexMaior / 3][indexMaior % 3] = 1;
                    aptidao += getAptitude(board, aptidao, true);

                    //System.out.println(toString(board));

                    if (checkGameOver(board)) {

                        break;
                    }
                    //System.out.println("Jogada do Minimax: ");
                   // mini.setMinMax(board);
                    //melhor = mini.joga();
                    //board[melhor.getLinha()][melhor.getColuna()] = 0;
                    randomPlay(board);
                    aptidao += getAptitude(board, aptidao, false);
                     //System.out.println(toString(board));
                    if (checkGameOver(board)) {

                        break;
                    }

                }
                populacao[j][indexAptidao] = aptidao;
                
                System.out.println("Aptidao: " + aptidao);
               
                if (aptidao >= 5 ) {
                  
                    //System.out.println("Cromossomo: " + j);
                    //System.out.println("State: " + checkBoardState(board));
                    highCount++;
                    //System.out.println(toString(board));
                }
                if (aptidao < 5 ){
                    lowCount++;
                    }
                  
                

                
               

            }

            System.out.println("Low Count" + lowCount);
            System.out.println("High Count" + highCount);
             //System.out.println("Antes: ");
            //printPopulation(populacao);
             populacao = ga.defineNewPopulation(populacao);
            //  for (int j = 0; j < populacao.length; j++) {
            //      System.out.println("AfterCromossomo: " + j);
            //      System.out.println(populacao[j][populacao[0].length - 1]);
            //  }
            //System.out.println("Depois: ");
            //printPopulation(populacao);

        }
    }

    public static void main(String args[]) {
        TestaRede teste = new TestaRede();

    }

}
