
/**
 * Escreva a descrição da classe TestaRede aqui.
 * 
 * @author Silvia
 * @adaptado por Carlo Mantovani
 * @version 12/11/2020
 */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class TestaRede {
    private static double[] tabuleiro;
    private static int[][] tabuleiroVelha;
    private static Rede rn;
    private double[][] populacao;
    private int populacaoSize = 30;
    private static int totalIterations;
    private static GeneticAlgorithm ga = new GeneticAlgorithm(true, 0.05, 0.9);// elitismo, taxa de mutacao, taxa de
                                                                               // crossover
    private BoardMethods bm = new BoardMethods();// metodos para manipular o tabuleiro e obter aptidao
    private static double[] melhorPesos;
    private static int min_max_tie_count = 0;

    public TestaRede() {
        // ------------------------ EXEMPLO DE TABULEIRO
        // ------------------------------------------
        // tabuleiro do jogo da velha - Exemplo de teste
        tabuleiroVelha = new int[][] { { -1, -1, -1 }, // -1: celula livre 1: X 0: O
                { -1, -1, -1 },
                { -1, -1, -1 } };

        // Exibe tabuleiro
        // System.out.println("\f\nTabuleiro inicial: ");
        // for (int i = 0; i < tabuleiroVelha.length; i++) {
        // for (int j = 0; j < tabuleiroVelha.length; j++) {
        // System.out.print(tabuleiroVelha[i][j] + " \t");
        // }
        // System.out.println();
        // }
        // System.out.println(toString(tabuleiroVelha));

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
        int pesosOculta1 = oculta + 1; // numero de pesos por neuronio da camada oculta 1
        int pesosOculta2 = oculta + 1; // numero de pesos por neuronio da camada oculta 2
        int pesosOculta3 = oculta + 1; // numero de pesos por neuronio da camada oculta 3
        int pesosSaida = saida + 1; // numero de pesos por neuronio da camada de saida
        int totalPesos = pesosOculta1 * oculta + pesosOculta2 * oculta + pesosOculta3 * oculta + pesosSaida * saida;
        double[] cromossomo = new double[totalPesos + 1];
        populacao = new double[populacaoSize][totalPesos + 1];
        // gerando pesos aleatorios
        for (int j = 0; j < populacaoSize; j++) {
            cromossomo = new double[totalPesos + 1];
            for (int i = 0; i < cromossomo.length - 1; i++) {
                cromossomo[i] = gera.nextDouble();
                if (gera.nextBoolean())
                    cromossomo[i] = cromossomo[i] * -1;
            }
            cromossomo[cromossomo.length - 1] = 0;
            populacao[j] = cromossomo;
        }

        gameLoop();// loop do jogo

    }

    // conversor de simbolos
    private static String symbolConverter(int symbol) {
        if (symbol == 1) {
            return "X";
        } else if (symbol == 0) {
            return "O";
        } else {
            return "-";
        }
    }

    // decodificador de tabuleiro
    private static List<Integer> decodeBoard(int[][] tabuleiroVelha) {

        List<Integer> posicoes = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                posicoes.add(tabuleiroVelha[i][j]);
            }
        }

        return posicoes;
    }

    // imprime tabuleiro
    private static String toString(int[][] tabuleiroVelha) {
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

    private static void setTabuleiro(int[][] tabuleiroVelha) {
        int k = 0;
        for (int i = 0; i < tabuleiroVelha.length; i++) {
            for (int j = 0; j < tabuleiroVelha.length; j++) {
                tabuleiro[k] = tabuleiroVelha[i][j];
                k++;
            }
        }
    }

    private static int getMaior(double[] saidaRede) {
        int indexMaior = 0;
        for (int i = 0; i < saidaRede.length; i++) {

            if (saidaRede[i] > saidaRede[indexMaior]) {

                indexMaior = i;
            }
        }
        return indexMaior;
    }

    // obtem a melhor aptidao da populacao
    private double getBestAptitude(double[][] populacao) {
        double melhorAptidao = 0;
        for (int i = 0; i < populacao.length; i++) {
            if (populacao[i][populacao[i].length - 1] > melhorAptidao) {
                melhorAptidao = populacao[i][populacao[i].length - 1];
            }
        }
        return melhorAptidao;
    }

    // carrega pesos da rede a partir de um arquivo
    private static void startWithFile(double[] pesos) {
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
        int oculta = 9; // numero de neuronios da camada oculta
        int saida = 9; // numero de neuronios da camada de saida
        rn = new Rede(oculta, saida); // topologia da rede: 9 neurônios na camada oculta e 9 na de saída
        rn.setPesosNaRede(tabuleiro.length, pesos); // seta os pesos da rede
    }

    private static void printBoardPositions(int[][] board) {
        for (int i = 0; i < board.length; i++) {
            System.out.print("|");
            for (int j = 0; j < board.length; j++) {
                System.out.print(" " + (i * 3 + j) + " |");
            }
            System.out.print("\n");
        }
    }

    // treinamento da rede
    private void gameLoop() {
        TestaMinimax mini = new TestaMinimax(tabuleiroVelha);// instancia o minimax
        Sucessor melhor;// instancia o sucessor

        int[][] board = BoardMethods.resetBoard();// reseta o tabuleiro
        double aptidao = 0;// aptidao do cromossomo
        int indexAptidao = populacao[0].length - 1;// indice da aptidao
        Random random = new Random();// instancia um random
        int turn = 0;// turno
        int printRate = 1000;// taxa de print
        int medium = (totalIterations / 100) * 50;// após 50% das iterações
        int hard = (totalIterations / 100) * 75;// após 75% das iterações
        int veryHard = (totalIterations / 1000) * 900;// após 90% das iterações
        double minMaxRate = 0;// taxa de minimax
        int free_Position_Bonus = 300;// bonus de posição livre
        int win_Bonus = 10;// bonus de vitória
        int draw_Bonus = 10;// bonus de empate

        for (int i = 0; i < totalIterations; i++) {// para cada iteração

            // if (i % printRate == 0) {
            // System.out.println();
            // System.out.println("Iteracao: " + i);
            // System.out
            // .println("Melhor Aptidao da iteracao anterior(" + (i - 1) + "): " +
            // getBestAptitude(populacao));
            // }
            // altera a taxa de minimax e as taxas de crossover e mutação após 50%, 75% e
            // 90% das iterações
            if (i == medium) {// após 50% das iterações
                // System.out.println("Medium");
                minMaxRate = 0.3;
                printRate = 500;
                ga.setCrossOverRate(ga.getCrossOverRate() * 0.9);
                ga.setMutationRate(ga.getMutationRate() / 2);
                free_Position_Bonus /= 2;
                win_Bonus *= 5;
                draw_Bonus *= 5;
            } else if (i == hard) {// após 75% das iterações
                // System.out.println("Hard");
                minMaxRate = 0.5;
                printRate = 100;
                ga.setCrossOverRate(ga.getCrossOverRate() * 0.9);
                ga.setMutationRate(ga.getMutationRate() / 2);
                free_Position_Bonus /= 2;
                win_Bonus *= 5;
                draw_Bonus *= 5;

            } else if (i == veryHard) {// após 90% das iterações
                // System.out.println("Very Hard");
                minMaxRate = 1;
                printRate = 10;
                //ga.setCrossOverRate(ga.getCrossOverRate() * 0.9);
                ga.setMutationRate(ga.getMutationRate() / 2);
                free_Position_Bonus /= 2;
                win_Bonus *= 5;
                draw_Bonus *= 5;

            }

            boolean flagMiniMax = false;// flag para minimax

            for (int j = 0; j < populacao.length; j++) {// para cada cromossomo da população

                rn.setPesosNaRede(tabuleiro.length, populacao[j]);// seta os pesos da rede
                board = BoardMethods.resetBoard();// reseta o tabuleiro
                aptidao = 0;// reseta a aptidao
                turn = 0;// reseta o turno
                if (random.nextDouble(0.001, 1) < minMaxRate) {// se a taxa de minimax for atingida
                    flagMiniMax = true;
                } else {
                    flagMiniMax = false;
                }
                while (true) {// enquanto o jogo não acabar
                    setTabuleiro(board);// seta o tabuleiro

                    if (flagMiniMax) {// se a taxa de minimax for atingida, joga com minimax
                        if (turn == 0) {// se for o primeiro turno, joga aleatoriamente
                            bm.randomPlay(board);
                        } else {
                            mini.setMinMax(board);
                            melhor = mini.joga();
                            board[melhor.getLinha()][melhor.getColuna()] = 0;
                            if ((BoardMethods.checkBoardState(board)) == 2) {
                                // System.out.println("Minimax Tie");
                                min_max_tie_count++;
                                // bestPopulation = populacao[j];
                            }
                        }
                    } else {// senao, joga com a rede neural
                        bm.randomPlay(board);
                    }

                    turn++;// incrementa o turno
                    aptidao += bm.getAptitude(board, false, turn, free_Position_Bonus, win_Bonus, draw_Bonus);// calcula
                                                                                                              // a
                    // aptidao

                    if (BoardMethods.checkGameOver(board)) {// se o jogo acabou, acaba o jogo
                        break;
                    }

                    setTabuleiro(board);// seta o tabuleiro

                    double[] saidaRede = rn.propagacao(tabuleiro);// calcula a saida da rede
                    int indexMaior = getMaior(saidaRede);
                    int line = indexMaior / 3;
                    int column = indexMaior % 3;

                    if (BoardMethods.checkOccupied(line, column, board)) {// se a posição estiver ocupada, acaba o jogo
                        break;
                    }

                    if (bm.blocksOpponentVictory(board, line, column)) {// se bloquear a vitoria do oponente, ganha
                                                                        // pontos
                        aptidao += 350;
                    }
                    if (bm.allowsPotentialVictory(board, line, column)) {// se permitir uma vitoria, perde pontos
                        aptidao += 200;
                    }
                    turn++;// incrementa o turno
                    board[line][column] = 1;// joga na posicao
                    aptidao += bm.getAptitude(board, true, turn, free_Position_Bonus, win_Bonus, draw_Bonus);// calcula
                                                                                                             // a
                    // aptidao

                    if (BoardMethods.checkGameOver(board)) {// verifica se o jogo acabou
                        break;
                    }

                }
                populacao[j][indexAptidao] = aptidao;// seta a aptidao do cromossomo

            }

            if (i == totalIterations - 1) {// se for a ultima iteracao, acaba o jogo
                break;
            }
            populacao = ga.defineNewPopulation(populacao);// define a nova população

        }
        int bestIndex = 0;// indice do melhor cromossomo
        for (int i = 0; i < populacao.length; i++) {// verifica qual o melhor cromossomo
            if (populacao[i][populacao[0].length - 1] > populacao[bestIndex][populacao[0].length - 1]) {
                bestIndex = i;
            }
        }
        melhorPesos = populacao[bestIndex];// salva os pesos do melhor cromossomo
        // System.out.println("Melhor Aptidao: " +
        // populacao[bestIndex][populacao[0].length - 1]);
        // System.out.println("Melhor Pesos: ");
        for (int i = 0; i < melhorPesos.length; i++) {
            // System.out.print("Peso " + i + ":");
            // System.out.println(melhorPesos[i]);
        }

    }

    // menu de opcoes
    public static void main(String args[]) {

        Scanner kb = new Scanner(System.in);// leitor de teclado
        System.out.print("\nBem-vindo!\n");

        // menu de opcoes
        while (true) {
            System.out.print("\n");
            System.out.print("|-----------------------------------------------|\n");
            System.out.print("| Opcao 1 - Treinar Rede Neural                 |\n");
            System.out.print("| Opcao 2 - Jogar                               |\n");
            System.out.print("| Opcao 3 - Carregar Pesos Pre-Calculados       |\n");
            System.out.print("| Opcao 4 - Sair                                |\n");
            System.out.print("|-5 Testes--------------------------------------|\n");
            System.out.print("|-----------------------------------------------|\n");
            System.out.print("Selecione uma opcao: ");

            String opt = kb.nextLine();

            switch (opt) {

                case "1":// treina a rede neural

                    System.out.print("\n");
                    System.out.print("Quantidade de iteracoes: ");
                    totalIterations = kb.nextInt();
                    kb.nextLine();
                    long startTime = System.currentTimeMillis();// calcula o tempo de execucao
                    TestaRede teste = new TestaRede();

                    // salva os pesos em um arquivo
                    try {
                        FileWriter fileWriter = new FileWriter("pesos.txt");
                        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

                        for (double value : melhorPesos) {
                            bufferedWriter.write(Double.toString(value));
                            bufferedWriter.newLine();
                        }

                        bufferedWriter.close();
                        System.out.println("Array has been written to the file.");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    long endTime = System.currentTimeMillis();// calcula o tempo de execucao
                    long elapsedTime = endTime - startTime;
                    double elapsedTimeInSeconds = (double) elapsedTime / 1000.0;

                    System.out.println("Method execution time: " + elapsedTimeInSeconds + " seconds");// imprime o tempo
                                                                                                      // de execucao
                    break;

                case "2":// joga com a rede neural
                    rn.setPesosNaRede(tabuleiro.length, melhorPesos);// seta os pesos na rede
                    boolean jogar = true;// variavel que controla o loop do jogo
                    int[][] board = BoardMethods.resetBoard();// reseta o tabuleiro
                    while (jogar) {

                        System.out.print("\n");
                        printBoardPositions(board);// imprime as posicoes do tabuleiro
                        System.out.print("\n");

                        System.out.print("\n");
                        System.out.print(toString(board));// imprime o tabuleiro

                        System.out.print("\n");
                        System.out.print("Jogador 1, escolha uma posicao: ");// jogador 1 (humano) joga
                        int position = kb.nextInt();
                        kb.nextLine();
                        if (BoardMethods.checkOccupied(position / 3, position % 3, board)) {// verifica se a posicao
                                                                                            // esta ocupada
                            System.out.print("\n");
                            System.out.print("Posicao ocupada!\n");
                            break;
                        }
                        board[position / 3][position % 3] = 0;// joga na posicao escolhida
                        System.out.print(toString(board));
                        if (BoardMethods.checkGameOver(board)) {// verifica se o jogo acabou
                            System.out.print("\n");
                            // System.out.print(toString(board));
                            System.out.print("\n");
                            if (BoardMethods.checkBoardState(board) == 2)
                                System.out.print("Empate!");
                            else if (BoardMethods.checkBoardState(board) == 0)
                                System.out.print("Humano venceu!");
                            break;
                        }

                        System.out.print("\n");
                        System.out.print("Jogada da Rede: ");// jogada da rede neural

                        setTabuleiro(board);// seta o tabuleiro do jogo
                        double[] saidaRede = rn.propagacao(tabuleiro);// calcula a saida da rede neural
                        int indexMaior = getMaior(saidaRede);
                        System.out.print("\n");
                        System.out.println("Linha escolhida pela rede: " + indexMaior / 3);
                        System.out.println("Coluna escolhida pela rede: " + indexMaior % 3);

                        if (BoardMethods.checkOccupied(indexMaior / 3, indexMaior % 3, board)) {// verifica se a posicao
                                                                                                // esta ocupada
                            System.out.println("Jogada da rede em posicao ocupada");
                            break;
                        }

                        board[indexMaior / 3][indexMaior % 3] = 1;// joga na posicao escolhida
                        if (BoardMethods.checkGameOver(board)) {// verifica se o jogo acabou
                            System.out.print("\n");
                            System.out.print(toString(board));
                            System.out.print("\n");
                            if (BoardMethods.checkBoardState(board) == 1)
                                System.out.print("Rede venceu!");

                            break;
                        }

                        // System.out.print(toString(board));

                    }

                    break;

                case "3":// carrega os pesos pre-calculados
                    System.out.print("\n");
                    System.out.print("Carregando pesos...\n");
                    melhorPesos = new double[361];
                    try {
                        File file = new File("bestweights.txt");
                        Scanner scanner = new Scanner(file);
                        int i = 0;
                        while (scanner.hasNextLine()) {
                            melhorPesos[i] = Double.parseDouble(scanner.nextLine());
                            i++;
                        }
                        scanner.close();
                    } catch (FileNotFoundException e) {
                        System.out.println("An error occurred.");
                        e.printStackTrace();
                    }
                    startWithFile(melhorPesos);
                    break;

                case "4":// sai do programa
                    System.out.print("\n");
                    System.out.print("Obrigado por jogar!\n");
                    System.exit(0);
                    kb.close();
                    break;
                case "5":
                    double mutation_rate = 0.05;
                    double crossover_rate = 1;
                    totalIterations = 10000;

                    for (int i = 0; i < 10; i++) {
                        System.out.println("Teste " + i);
                        //mutation_rate += 0.01;
                        crossover_rate -= 0.02;
                        min_max_tie_count = 0;
                        ga.setMutationRate(mutation_rate);
                        ga.setCrossOverRate(crossover_rate);
                        System.out.println("Taxa de Crossover " + crossover_rate);
                        System.out.println("Taxa de Mutacao " + mutation_rate);
                        TestaRede testing = new TestaRede();
                        System.out.println("Frequencia de Empate Minimax " + min_max_tie_count);
                        System.out.println("");
                    }
                    break;

                default:// opcao invalida
                    System.out.print("\n");
                    System.out.print("Opcao invalida!\n");
                    break;
            }
        }
    }
}
