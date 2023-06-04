#Classe Board que representa o tabuleiro do jogo
class Board:
    def __init__(
        self, p1="b", p2="b", p3="b", p4="b", p5="b", p6="b", p7="b", p8="b", p9="b"
    ):
        self.p1 = p1
        self.p2 = p2
        self.p3 = p3
        self.p4 = p4
        self.p5 = p5
        self.p6 = p6
        self.p7 = p7
        self.p8 = p8
        self.p9 = p9

    #retorna o valor da posicao do tabuleiro
    def getPosition(self, index):
        if index == 0:
            return self.p1
        elif index == 1:
            return self.p2
        elif index == 2:
            return self.p3
        elif index == 3:
            return self.p4
        elif index == 4:
            return self.p5
        elif index == 5:
            return self.p6
        elif index == 6:
            return self.p7
        elif index == 7:
            return self.p8
        elif index == 8:
            return self.p9
        else:
            return None
    #seta o valor da posicao do tabuleiro
    def setPosition(self, index, player):
        if index == 0:
            self.p1 = player
        elif index == 1:
            self.p2 = player
        elif index == 2:
            self.p3 = player
        elif index == 3:
            self.p4 = player
        elif index == 4:
            self.p5 = player
        elif index == 5:
            self.p6 = player
        elif index == 6:
            self.p7 = player
        elif index == 7:
            self.p8 = player
        elif index == 8:
            self.p9 = player
        return self
    #retorna o tabuleiro em formato de string para ser usado no classificador
    def getFormattedBoard(self):
        result = ""
        for i in range(9):
            if self.getPosition(i) == "x":
                result += "2"
            elif self.getPosition(i) == "o":
                result += "1"
            else:
                result += "0"
            if i != 8:
                result += ","
        return result
    
    def checkBoardFull(self):
        for i in range(9):
            if self.getPosition(i) == "b":
                return False
        return True
    #imprime o tabuleiro
    def __str__(self):
        result = ""
        for i in range(9):
            if i % 3 == 0:
                result += "\n"
            if self.getPosition(i) == "x" or self.getPosition(i) == "o":
                result += "|" + self.getPosition(i) + "|"
            else:
                result += "|" + "-" + "|"
        result += "\n"
        return result
