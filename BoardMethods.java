import java.util.Random;

public class BoardMethods {
    // calcula a aptidão do tabuleiro
    public double getAptitude(int[][] board, boolean flagPlayer, int turn) {
        int state = checkBoardState(board);
        if (flagPlayer) {// se for a rede
            if (state == 1) {// se a rede ganhou
                return 1200 / turn;
            } else if (state == -1) {// se a rede jogou em posicao livre
                return 15 * turn;
            }
        } else {
            if (state == 0) {// se a rede perdeu
                return -200;

            } else if (state == 2) {// se a rede empatou
                return 300;
            }
        }
        return 0;
    }

    // verifica posicao livre
    public static boolean checkOccupied(int linha, int coluna, int[][] tabuleiroVelha) {
        if (tabuleiroVelha[linha][coluna] != -1) {
            return true;
        }
        return false;
    }

    // verifica final de jogo
    public static boolean checkGameOver(int[][] board) {
        if (checkBoardState(board) == -1) {
            return false;
        }
        return true;
    }

    public boolean blocksOpponentVictory(int[][] board, int line, int column) {
        int jogadorOp = 0; // jogador 2 (O)
        int numLinhas = board.length;// numero de linhas
        int numColunas = board[0].length;// numero de colunas

        // verifica bloqueio vertical
        int contadorOponente = 0;
        for (int row = 0; row < numLinhas; row++) {
            if (row == line) {
                continue; // pula a linha atual
            }
            if (board[row][column] == jogadorOp) {
                contadorOponente++;
            }
        }
        if (contadorOponente == numLinhas - 1) {
            return true;
        }

        // verifica bloqueio horizontal
        contadorOponente = 0;
        for (int col = 0; col < numColunas; col++) {
            if (col == column) {
                continue; // pula a coluna atual
            }
            if (board[line][col] == jogadorOp) {
                contadorOponente++;
            }
        }
        if (contadorOponente == numColunas - 1) {
            return true;
        }

        // verifica bloqueio diagonal
        if (line == column || line + column == numLinhas - 1) {
            contadorOponente = 0;
            // verifica diagonal principal
            for (int i = 0; i < numLinhas; i++) {
                if (i == line) {
                    continue; // pula a linha atual
                }
                if (board[i][i] == jogadorOp) {
                    contadorOponente++;
                }
            }
            if (contadorOponente == numLinhas - 1) {
                return true;
            }

            contadorOponente = 0;
            // verifica diagonal secundaria
            for (int i = 0; i < numLinhas; i++) {
                if (i == line) {
                    continue; // verifica a linha atual
                }
                if (board[i][numLinhas - 1 - i] == jogadorOp) {
                    contadorOponente++;
                }
            }
            if (contadorOponente == numLinhas - 1) {
                return true;
            }
        }

        // se nao achar nenhum bloqueio
        return false;
    }

    public boolean allowsPotentialVictory(int[][] board, int line, int column) {
        int jogadorRN = 1; // valor da rede neural
        int numLinhas = board.length;
        int numColunas = board[0].length;

        // verifica vitoria vertical
        int contadorRN = 0;
        int posicoesLivres = 0;
        for (int row = 0; row < numLinhas; row++) {
            if (row == line) {
                continue; // pula a linha atual
            }
            if (board[row][column] == jogadorRN) {
                contadorRN++;
            } else if (board[row][column] == -1) { // verifica posicoes livres
                posicoesLivres++;
            }
        }
        if (contadorRN == numLinhas - 1 && posicoesLivres > 0) {// se achar uma potencial vitoria vertical
            return true;
        }

        // verifica vitoria horizontal
        contadorRN = 0;
        posicoesLivres = 0;
        for (int col = 0; col < numColunas; col++) {
            if (col == column) {
                continue; // pula a coluna atual
            }
            if (board[line][col] == jogadorRN) {
                contadorRN++;
            } else if (board[line][col] == -1) { // verifica posicoes livres
                posicoesLivres++;
            }
        }
        if (contadorRN == numColunas - 1 && posicoesLivres > 0) {// se achar uma potencial vitoria horizontal
            return true;
        }

        // verifica vitoria diagonal
        if (line == column || line + column == numLinhas - 1) {
            contadorRN = 0;
            posicoesLivres = 0;
            // verifica diagonal principal
            for (int i = 0; i < numLinhas; i++) {
                if (i == line) {
                    continue; // pula a linha atual
                }
                if (board[i][i] == jogadorRN) {
                    contadorRN++;
                } else if (board[i][i] == -1) { // ver
                    posicoesLivres++;
                }
            }
            if (contadorRN == numLinhas - 1 && posicoesLivres > 0) {
                return true;
            }

            contadorRN = 0;
            posicoesLivres = 0;
            // Check the anti-diagonal
            for (int i = 0; i < numLinhas; i++) {
                if (i == line) {
                    continue; // pula a linha atual
                }
                if (board[i][numLinhas - 1 - i] == jogadorRN) {
                    contadorRN++;
                } else if (board[i][numLinhas - 1 - i] == -1) { // verifica posicoes livres
                    posicoesLivres++;
                }
            }
            if (contadorRN == numLinhas - 1 && posicoesLivres > 0) {
                return true;
            }
        }

        // vitória potencial não encontrada
        return false;
    }

    // reseta o tabuleiro
    public static int[][] resetBoard() {
        int[][] newBoard = new int[3][3];
        for (int i = 0; i < newBoard.length; i++) {
            for (int j = 0; j < newBoard.length; j++) {
                newBoard[i][j] = -1;
            }
        }
        return newBoard;
    }

    // joga aleatoriamente em uma posicao vazia
    public int[][] randomPlay(int[][] board) {
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

    // verifica se o jogo acabou
    public static int checkBoardState(int[][] board) {
        // verifica linhas
        for (int line = 0; line < 3; line++) {
            if (board[line][0] == board[line][1] && board[line][1] == board[line][2]) {
                if (board[line][0] == 0) {
                    return 0; // "O ganha"
                } else if (board[line][0] == 1) {
                    return 1; // "X ganha"
                }
            }
        }

        // verifica colunas
        for (int col = 0; col < 3; col++) {
            if (board[0][col] == board[1][col] && board[1][col] == board[2][col]) {
                if (board[0][col] == 0) {
                    return 0; // "O ganha"
                } else if (board[0][col] == 1) {
                    return 1; // "X ganha"
                }
            }
        }

        // verifica diagonais
        if ((board[0][0] == board[1][1] && board[1][1] == board[2][2]) ||
                (board[0][2] == board[1][1] && board[1][1] == board[2][0])) {
            if (board[1][1] == 0) {
                return 0; // "O ganha"
            } else if (board[1][1] == 1) {
                return 1; // "X ganha"
            }
        }

        // verifica se deu velha
        boolean empate = true;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row][col] == -1) {
                    empate = false;
                    break;
                }
            }
        }
        if (empate) {
            return 2; // empate
        }

        // jogo nao acabou
        return -1;
    }
}
