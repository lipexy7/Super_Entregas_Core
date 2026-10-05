package model;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Resultado de uma consulta de rastreamento. Substitui o antigo
 * {@code String[]{dono, peso, ponto, endereco, cidade}} devolvido pela
 * RastreioDAO, cujos índices "mágicos" (res[2], res[3]...) eram frágeis.
 */
public class RastreioResultado {

    private final Encomenda encomenda;
    private final Usuario destinatario;          // pode ser null
    private final List<Movimentacao> historico;  // da mais recente para a mais antiga

    public RastreioResultado(Encomenda encomenda, Usuario destinatario, List<Movimentacao> historico) {
        this.encomenda = encomenda;
        this.destinatario = destinatario;
        this.historico = Collections.unmodifiableList(historico);
    }

    public Encomenda getEncomenda() { return encomenda; }

    public Optional<Usuario> getDestinatario() { return Optional.ofNullable(destinatario); }

    public List<Movimentacao> getHistorico() { return historico; }

    public boolean temMovimentacao() { return !historico.isEmpty(); }

    /** Última localização conhecida (primeiro item do histórico). */
    public Optional<Movimentacao> getUltimaMovimentacao() {
        return historico.isEmpty() ? Optional.empty() : Optional.of(historico.get(0));
    }
}
