
/**
 * Escreva a descrição da classe Rede aqui.
 * 
 * @author Silvia
 * @adaptado Carlo Mantovani
 * @version 25/06/2023
 */
public class Rede {
    private Neuronio[] camadaOculta1; // rede neural 9x9x9x9x9
    private Neuronio[] camadaOculta2;
    private Neuronio[] camadaOculta3;
    private Neuronio[] camadaSaida;
    private double[] saida; // valores de saída da propagacao

    // Construtor da rede neural
    public Rede(int numNeuroniosOculta, int numNeuroniosSaida) {
        if (numNeuroniosOculta <= 0 || numNeuroniosSaida <= 0) {
            numNeuroniosOculta = 9;
            numNeuroniosSaida = 9;
        }
        camadaOculta1 = new Neuronio[numNeuroniosOculta];
        camadaOculta2 = new Neuronio[numNeuroniosOculta];
        camadaOculta3 = new Neuronio[numNeuroniosOculta];
        camadaSaida = new Neuronio[numNeuroniosSaida];
    }

    public void setPesosNaRede(int numEntradas, double[] pesos) {
        int k = 0;
        double[] tmp;
        // Setando os pesos da camada oculta
        for (int i = 0; i < camadaOculta1.length; i++) {
            tmp = new double[numEntradas + 1]; // quantidade de pesos dos neurônios da camada oculta
            for (int j = 0; j < numEntradas + 1; j++) {
                tmp[j] = pesos[k];
                k++;

            }
            camadaOculta1[i] = new Neuronio(tmp);
        }
        // Setando os pesos da camada oculta2
        for (int i = 0; i < camadaOculta2.length; i++) {
            tmp = new double[camadaOculta1.length + 1]; // quantidade de pesos dos neurônios da camada oculta
            for (int j = 0; j < camadaOculta1.length + 1; j++) {
                tmp[j] = pesos[k];
                k++;

            }
            camadaOculta2[i] = new Neuronio(tmp);
        }
        // Setando os pesos da camada oculta3
        for (int i = 0; i < camadaOculta3.length; i++) {
            tmp = new double[camadaOculta2.length + 1]; // quantidade de pesos dos neurônios da camada oculta
            for (int j = 0; j < camadaOculta2.length + 1; j++) {
                tmp[j] = pesos[k];
                k++;

            }
            camadaOculta3[i] = new Neuronio(tmp);
        }
        // Setando os pesos da camada de saida
        for (int i = 0; i < camadaSaida.length; i++) {
            tmp = new double[camadaOculta3.length + 1]; // quantidade de pesos dos neurônios da camada oculta
            for (int j = 0; j < camadaOculta3.length + 1; j++) {
                tmp[j] = pesos[k];
                k++;

            }
            camadaSaida[i] = new Neuronio(tmp);
        }
    }

    // Propagação da rede neural
    public double[] propagacao(double[] x) {
        if (x == null)
            return null;

        double[] saidaOculta1 = new double[camadaOculta1.length];
        double[] saidaOculta2 = new double[camadaOculta2.length];
        double[] saidaOculta3 = new double[camadaOculta3.length];
        saida = new double[camadaSaida.length];
        for (int i = 0; i < camadaOculta1.length; i++) {
            saidaOculta1[i] = camadaOculta1[i].calculaY(x);
        }
        for (int i = 0; i < camadaOculta2.length; i++) {
            saidaOculta2[i] = camadaOculta2[i].calculaY(saidaOculta1);
        }
        for (int i = 0; i < camadaOculta3.length; i++) {
            saidaOculta3[i] = camadaOculta3[i].calculaY(saidaOculta2);
        }
        for (int i = 0; i < camadaSaida.length; i++) {
            saida[i] = camadaSaida[i].calculaY(saidaOculta3);
        }
        return saida;
    }

    public String toString() {
        String msg = "Pesos da rede\n";
        // msg = msg + "Camada Oculta\n";
        // for (int i = 0; i < camadaOculta.length; i++) {
        // msg = msg + "Neuronio " + i + ": " + camadaOculta[i] + "\n";
        // }
        // msg = msg + "Camada Saida\n";
        // for (int i = 0; i < camadaSaida.length; i++) {
        // msg = msg + "Neuronio " + i + ": " + camadaSaida[i] + "\n";
        // }
        return msg;
    }
}
