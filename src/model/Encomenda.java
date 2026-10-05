package model;

import java.math.BigDecimal;

/**
 * Encomenda postada. O peso usa BigDecimal porque a coluna é DECIMAL(5,2);
 * double acumularia erros de arredondamento.
 */
public class Encomenda {

    private final int id;                 // 0 enquanto não persistida
    private final String codigoPostal;
    private final BigDecimal peso;
    private final Integer idUsuario;      // null = encomenda sem dono associado

    public Encomenda(int id, String codigoPostal, BigDecimal peso, Integer idUsuario) {
        this.id = id;
        this.codigoPostal = codigoPostal;
        this.peso = peso;
        this.idUsuario = idUsuario;
    }

    /** Cria uma encomenda ainda sem identificador (antes do INSERT). */
    public static Encomenda nova(String codigoPostal, BigDecimal peso, Integer idUsuario) {
        return new Encomenda(0, codigoPostal, peso, idUsuario);
    }

    /** Devolve uma cópia com o identificador gerado pelo banco. */
    public Encomenda comId(int novoId) {
        return new Encomenda(novoId, codigoPostal, peso, idUsuario);
    }

    public int getId() { return id; }
    public String getCodigoPostal() { return codigoPostal; }
    public BigDecimal getPeso() { return peso; }
    public Integer getIdUsuario() { return idUsuario; }
}
