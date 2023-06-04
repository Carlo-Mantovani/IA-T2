import Board as b
import Knn as k
import MLP as m
import Bayesian as by
import copy


class App:

    #verifica se o jogo acabou
    def checkEndGame(category):
        if category == "6":
            return False
        else:
            if category == "5":
                print("Jogador X venceu!")
            elif category == "4":
                print("Jogador O venceu!")
            elif category == "3":
                print("Empate!")
            else:
                print("Erro: Algoritmo não encerrou quando deveria")
            return True

    #utiliza o algoritmo selecionado para prever a categoria
    def predict(board, flagKnn, flagMlp, flagBay, knn, mlp, bay):
        if flagKnn:
            return knn.executaKnnBoard(board)
        elif flagMlp:
            return mlp.predict(board.getFormattedBoard())
        elif flagBay:
            return bay.predict(board.getFormattedBoard())
        else:
            return "Erro: Nenhum algoritmo selecionado"

    #verifica se a posição escolhida está ocupada
    def checkIfOccupied(board, position):
        if board.getPosition(position) == "x" or board.getPosition(position) == "o":
            return True
        else:
            return False

    #atualiza o tabuleiro com a jogada do jogador
    def updateBoard(board, position, player):
        board.setPosition(position, player)

        return board

    #imprime os indices do tabuleiro
    def printBoardPositions():
        return "Board Indexes" + "\n0 | 1 | 2" + "\n" + "3 | 4 | 5" + "\n" + "6 | 7 | 8"

    #verifica se a configuração do tabuleiro já está no arquivo de fim (jogo acabou)
    def checkCategoryInFile(board, predictedCategory,flagCheckAll):
        categories = []
        sentences = []
        if flagCheckAll:
            categories = [5, 4, 3]
        else:
            categories = [int(predictedCategory[0])]
        for category in categories:
            sentence = board.getFormattedBoard()
            sentence += "," + str(category)
            sentences.append(sentence)
        

        filename = "tic-tac-toe-endgame.data"

        with open(filename) as f:
            for line in f:
                for sentence in sentences:
                    if sentence in line:
                        # print("Found in file")
                        return True
        return False


    # traduz a categoria predita para uma mensagem
    def translateCategory(category):
        if category == "5":
            return " 5- Vitoria X"
        elif category == "4":
            return "4 - Vitoria O"
        elif category == "3":
            return "3 - Empate"
        elif category == "6":
            return "6 - Continuar"
        else:
            return "Erro: Categoria nao encontrada"

    #menu principal
    def main():
        print("Bem-vindo!\n")

        # menu de opcoes
        while True:
            print("\n")
            print("|-----------------------------------------------|")
            print("| Opcao 1 - Jogar Tic-Tac-Toe                   |")
            print("| Opcao 2 - Sair                                |")
            print("|-----------------------------------------------|")

            opt = input("Selecione uma opcao: ")

            #se a opcao for 1, inicia o jogo
            if opt == "1":
                
                #inicia o tabuleiro e as variaveis de controle
                board = b.Board()
                acertos = 0
                erros = 0

                #menu de opcoes de algoritmos
                print("\n")
                print("|-----------------------------------------------|")
                print("| Opcao 1 - Knn                                 |")
                print("| Opcao 2 - MLP                                 |")
                print("| Opcao 3 - Bayesiano                           |")
                print("|-----------------------------------------------|")

                algo = input("Selecione o algoritmo: ")

                #declara as flags de controle de algoritmo
                game = True #flag de controle do jogo
                knn = None #armazena o objeto do knn
                mlp = None #armaena o objeto do mlp
                bay = None #armazena o objeto do bayesiano
                flagKnn = False #flag de controle do knn
                flagMlp = False #flag de controle do mlp
                flagBay = False #flag de controle do bayesiano
                flagMistake = False #flag de controle de erro, ativa quando o algoritmo erra e nao acaba o jogo

                #instancia o algoritmo selecionado
                if algo == "1":
                    knn = k.Knn()
                    flagKnn = True
                    flagMlp = False
                    flagBay = False
                elif algo == "2":
                    mlp = m.MLP()
                    flagMlp = True
                    flagKnn = False
                    flagBay = False
                elif algo == "3":
                    bay = by.Bayesian()
                    flagBay = True
                    flagKnn = False
                    flagMlp = False
                else:
                    print("Opcao invalida!")
                    continue

                #loop do jogo
                while game:
                    print("\n")
                    
                    print(App.printBoardPositions())#imprime os indices do tabuleiro
                    print(board)#imprime o tabuleiro
                    auxBoard = copy.deepcopy(board) #copia o tabuleiro para que se possa resetar ele caso o jogador erre
                    position = input("Jogador X, escolha uma posicao: ")#jogada do jogador X
                    if App.checkIfOccupied(board, int(position)) == True:#verifica se a posicao escolhida esta ocupada
                        print("Posicao invalida ou Ocupada")
                        continue
                    board = App.updateBoard(board, int(position), "x")#atualiza o tabuleiro com a jogada do jogador
                    print(board)

                    prediction = App.predict(#faz a predicao do algoritmo
                        board, flagKnn, flagMlp, flagBay, knn, mlp, bay
                    )
                    print("Prediction: ", App.translateCategory(prediction))#traduz a categoria predita para uma mensagem
                    if prediction == "6":#se a categoria for 6, o jogo continua
                        if flagMistake == True:#se o algoritmo ja errou, incrementa o numero de erros
                            erros += 1
                        else:
                            if App.checkCategoryInFile(board, prediction, True) == True:#verifica se a configuracao do tabuleiro esta no arquivo de fim
                                erros += 1
                                flagMistake = True
                            else:
                                acertos += 1#se nao estiver, incrementa o numero de acertos
                    else:#se a categoria nao for 6, o jogo acaba
                        if App.checkCategoryInFile(board, prediction,False) == False:#se a configuracao do tabuleiro nao estiver no arquivo de fim, incrementa o numero de erros
                            erros += 1
                        else:
                            acertos += 1#se estiver, incrementa o numero de acertos

                    if App.checkEndGame(prediction):#verifica se o jogo acabou
                        game = False
                        break
                    print("\n")
                    print(App.printBoardPositions())
                    print(board)

                    if (board.checkBoardFull() == True):#verifica se o tabuleiro esta cheio
                        print("Tabuleiro cheio sem resposta")
                        game = False
                        break

                    position = input("Jogador O, escolha uma posicao: ")#jogada do jogador O
                    if App.checkIfOccupied(board, int(position)) == True:
                        print("Posicao invalida ou Ocupada")
                        board = copy.deepcopy(auxBoard)
                        continue
                    board = App.updateBoard(board, int(position), "o")
                    print(board)
                    prediction = App.predict(
                        board, flagKnn, flagMlp, flagBay, knn, mlp, bay
                    )
                    print("Prediction: ", App.translateCategory(prediction))

                    if prediction == "6":
                        if flagMistake == True:
                            erros += 1
                        else:
                            if App.checkCategoryInFile(board, prediction, True) == True:
                               
                                erros += 1
                                flagMistake = True
                            else:
                                acertos += 1
                    else:
                        if App.checkCategoryInFile(board, prediction,False) == False:
                            erros += 1
                        else:
                            acertos += 1

                    if App.checkEndGame(prediction):
                        game = False
                        break
                print("Acertos: ", acertos)#imprime o numero de acertos
                print("Erros: ", erros)#imprime o numero de erros
                print("Taxa de acerto: ", (acertos / (acertos + erros)) * 100, "%")#imprime a taxa de acerto

            elif opt == "2":#se a opcao for 2, sai do jogo
                print("\n")
                print("Obrigado por jogar!\n")
                exit()

            else:
                print("\n")
                print("Opcao invalida!\n")


# chamada do menu principal
if __name__ == "__main__":
    App.main()
