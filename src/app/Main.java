package app;

import teste.TesteIntegracaoMySQL;
import teste.TestesDeServico;
import teste.Verificador;

/**
 * Ponto de entrada de verificação do projeto (substitui as telas Swing).
 *
 * Sem argumentos: roda os testes das regras de negócio com DAOs em memória.
 * Com --mysql   : roda, além disso, um teste de leitura no MySQL real.
 *
 * Termina com código de saída 1 se algum teste falhar.
 */
public class Main {

    public static void main(String[] args) {
        Verificador v = new Verificador();

        System.out.println("Super Entregas Core - testes de verificação");
        new TestesDeServico(v).executar();

        boolean usarMySQL = false;
        for (String a : args) {
            if ("--mysql".equals(a)) {
                usarMySQL = true;
            }
        }
        if (usarMySQL) {
            new TesteIntegracaoMySQL(v).executar();
        } else {
            System.out.println("\n(Teste com MySQL real ignorado. Execute com o argumento --mysql para incluí-lo.)");
        }

        v.resumo("RESULTADO FINAL");
        if (!v.sucesso()) {
            System.exit(1);
        }
    }
}
