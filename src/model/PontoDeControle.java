package model;

/** Unidade física da rede logística (armazém, centro de distribuição...). */
public class PontoDeControle {

    private final int id;                 // 0 enquanto não persistido
    private final String nome;
    private final String endereco;
    private final String cidade;

    public PontoDeControle(int id, String nome, String endereco, String cidade) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.cidade = cidade;
    }

    public static PontoDeControle novo(String nome, String endereco, String cidade) {
        return new PontoDeControle(0, nome, endereco, cidade);
    }

    public PontoDeControle comId(int novoId) {
        return new PontoDeControle(novoId, nome, endereco, cidade);
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getEndereco() { return endereco; }
    public String getCidade() { return cidade; }

    /** Texto no formato "Nome - Endereço - Cidade" que a tela Swing montava na mão. */
    public String getDescricaoCompleta() {
        return nome + " - " + endereco + " - " + cidade;
    }
}
