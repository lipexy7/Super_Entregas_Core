package model;

import java.time.LocalDateTime;

/** Passagem de uma encomenda por um ponto de controle (linha da tabela rastreamento). */
public class Movimentacao {

    private final PontoDeControle ponto;
    private final LocalDateTime dataHora;

    public Movimentacao(PontoDeControle ponto, LocalDateTime dataHora) {
        this.ponto = ponto;
        this.dataHora = dataHora;
    }

    public PontoDeControle getPonto() { return ponto; }
    public LocalDateTime getDataHora() { return dataHora; }
}
