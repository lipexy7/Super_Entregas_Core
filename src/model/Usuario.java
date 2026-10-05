package model;

/**
 * Usuário do sistema. Objeto imutável. Propositalmente NÃO carrega a senha:
 * ela nunca deve chegar às camadas de apresentação (tela Swing ou página JSP).
 */
public class Usuario {

    private final int id;
    private final String nome;
    private final String email;
    private final String cpf;
    private final boolean gerente;

    public Usuario(int id, String nome, String email, String cpf, boolean gerente) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.gerente = gerente;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getCpf() { return cpf; }
    public boolean isGerente() { return gerente; }

    @Override
    public String toString() {
        return "Usuario{id=" + id + ", nome=" + nome + ", gerente=" + gerente + "}";
    }
}
