package teste;

import java.util.Objects;

/**
 * Mini-framework de asserções para o main() (o enunciado pede testes no main,
 * sem depender de JUnit). Registra aprovados/reprovados e imprime cada caso.
 */
public class Verificador {

    private int aprovados;
    private int reprovados;

    public void verdadeiro(String descricao, boolean condicao) {
        registrar(descricao, condicao, condicao ? "" : "condição falsa");
    }

    public void igual(String descricao, Object esperado, Object atual) {
        boolean ok = Objects.equals(esperado, atual);
        registrar(descricao, ok, ok ? "" : "esperado <" + esperado + "> mas foi <" + atual + ">");
    }

    /** Verifica que o código lança a exceção esperada e devolve a mensagem para checagens extras. */
    public String lanca(String descricao, Class<? extends Throwable> tipo, Runnable codigo) {
        try {
            codigo.run();
        } catch (Throwable t) {
            boolean ok = tipo.isInstance(t);
            registrar(descricao, ok, ok ? "" : "lançou " + t.getClass().getSimpleName() + ": " + t.getMessage());
            return t.getMessage();
        }
        registrar(descricao, false, "nenhuma exceção foi lançada");
        return null;
    }

    public void secao(String titulo) {
        System.out.println("\n== " + titulo + " ==");
    }

    private void registrar(String descricao, boolean ok, String detalhe) {
        if (ok) {
            aprovados++;
            System.out.println("  [OK]    " + descricao);
        } else {
            reprovados++;
            System.out.println("  [FALHA] " + descricao + (detalhe.isEmpty() ? "" : " -> " + detalhe));
        }
    }

    public int getAprovados() { return aprovados; }
    public int getReprovados() { return reprovados; }
    public boolean sucesso() { return reprovados == 0; }

    public void resumo(String nome) {
        System.out.println("\n" + nome + ": " + aprovados + " aprovado(s), " + reprovados + " reprovado(s).");
    }
}
