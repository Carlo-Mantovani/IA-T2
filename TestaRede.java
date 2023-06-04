
/**
 * Escreva a descrição da classe TestaRede aqui.
 * 
 * @author Silvia
 * @version 12/11/2020
 */
import java.util.Random;
public class TestaRede
{
    private double[]tabuleiro;
    private int[][]tabuleiroVelha;
    private Rede rn;
    
    public TestaRede(){
        //------------------------ EXEMPLO DE TABULEIRO ------------------------------------------
        //tabuleiro do jogo da velha - Exemplo de teste
        tabuleiroVelha = new int[][]{{0,-1,-1},            //-1: celula livre  1: X   0: O
                                     {-1,-1,-1},
                                     {-1,-1,-1}};
         
        System.out.println("\f\nTabuleiro inicial: ");                             
        for(int i=0; i<tabuleiroVelha.length; i++){
            for(int j=0; j<tabuleiroVelha.length; j++){
                System.out.print(tabuleiroVelha[i][j] + " \t");
            }
            System.out.println();
        }
        
        //tabuleiro de teste - conversao de matriz para vetor      
        tabuleiro = new double[tabuleiroVelha.length*tabuleiroVelha.length];
        int k=0;
        for(int i=0; i<tabuleiroVelha.length; i++){
            for(int j=0; j<tabuleiroVelha.length; j++){
                tabuleiro[k] = tabuleiroVelha[i][j];
                k++;
            }
        }
        
        //------------------------ EXEMPLO DE REDE ------------------------------------------
        //Cria e configura a rede
        //Criando a rede
         int oculta = 9; //numero de neuronios da camada oculta
         int saida = 9;  //numero de neuronios da camada de saida
         rn = new Rede(oculta,saida);  //topologia da rede: 9 neurônios na camada oculta e 9 na de saída
        
        //Simulando um cromossomo da populacao do AG
        Random gera = new Random();
        int pesosOculta = oculta + 1; //numero de pesos por neuronio da camada oculta
        int pesosSaida = saida + 1;  //numero de pesos por neuronio da camada de saida
        int totalPesos = pesosOculta * oculta + pesosSaida * saida;  
        double []cromossomo = new double[totalPesos];
        
        for(int i=0; i<cromossomo.length; i++){
            cromossomo[i]=gera.nextDouble();
            if(gera.nextBoolean()) cromossomo[i] = cromossomo[i] * -1;
           // System.out.print(cromossomo[i] + " ");
        }
        
        //Setando os pesos na rede
        rn.setPesosNaRede(tabuleiro.length, cromossomo);  //
 
        System.out.println();
        
        //Exibe rede neural
        System.out.println("Rede Neural - Pesos: ");
        System.out.println(rn);
        
        //--------------EXEMPLO DE EXECUCAO ----------------------------------------
        
        for(int n=1; n<=3; n++){
            System.out.println("\n\n>>>RODADA: " + n);
            //Exibe um exemplo de propagação : saida dos neurônios da camada de saída
            double[]saidaRede = rn.propagacao(tabuleiro);
            System.out.println("Rede Neural - Camada de Saida: Valor de Y");
            for(int i=0; i<saidaRede.length; i++){
                System.out.println("Neuronio " + i + " : " + saidaRede[i]);
            }
        
            //Define posicao a jogar de acordo com rede
            int indMaior = 0;
            double saidaMaior = saidaRede[0];
            for(int i=1; i<saidaRede.length; i++){
                if(saidaRede[i]> saidaMaior){
                    saidaMaior = saidaRede[i];
                    indMaior = i;
                }
            }
            int linha = indMaior/3;
            int coluna= indMaior%3;
            System.out.println("Neuronio de maior valor: " + indMaior + " - " + saidaRede[indMaior]);
            System.out.println(">>> Rede escolheu - Linha: " + linha + " Coluna: " + coluna);
        
            if(tabuleiroVelha[linha][coluna]!=-1) System.out.println("Posicao ocupada");
            else{
                tabuleiroVelha[linha][coluna] = 1;
            
                System.out.println("\nTabuleiro apos jogada: ");                             
                for(int i=0; i<tabuleiroVelha.length; i++){
                    for(int j=0; j<tabuleiroVelha.length; j++){
                        System.out.print(tabuleiroVelha[i][j] + "\t");
                    }
                    System.out.println();
                }
            }
            
                        
            //-----------------------------------------JOGA MINIMAX
            TestaMinimax mini = new TestaMinimax(tabuleiroVelha);
            Sucessor melhor = mini.joga();
            
            System.out.println(">>> MINIMAX escolheu - Linha: " + melhor.getLinha() + " Coluna: " + melhor.getColuna());
        
            if(tabuleiroVelha[melhor.getLinha()][melhor.getColuna()]!=-1) System.out.println("Posicao ocupada");
            else{
                tabuleiroVelha[melhor.getLinha()][melhor.getColuna()] = 0;
            
                System.out.println("\nTabuleiro apos jogada: ");                             
                for(int i=0; i<tabuleiroVelha.length; i++){
                    for(int j=0; j<tabuleiroVelha.length; j++){
                        System.out.print(tabuleiroVelha[i][j] + "\t");
                    }
                    System.out.println();
                }
            }
            
            //tabuleiro de teste - conversao de matriz para vetor      
            k=0;
            for(int i=0; i<tabuleiroVelha.length; i++){
                for(int j=0; j<tabuleiroVelha.length; j++){
                    tabuleiro[k] = tabuleiroVelha[i][j];
                    k++;
                }
            }
        }
    }
    
    public static void main(String args[]){
        TestaRede teste = new TestaRede();
            
    }
    
    
}
