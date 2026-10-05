package util;

import exception.RegraDeNegocioException;
import java.math.BigDecimal;

/** Conversões de texto (vindo de telas ou formulários) para tipos do domínio. */
public final class Conversor {

    private Conversor() { }

    /**
     * Converte "2.5" ou "2,5" em BigDecimal. Antes, a tela chamava
     * Double.parseDouble sem tratar NumberFormatException (a tela quebrava
     * com "2,5", formato comum no Brasil).
     */
    public static BigDecimal paraPeso(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new RegraDeNegocioException("Informe o peso da encomenda.");
        }
        try {
            return new BigDecimal(texto.trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new RegraDeNegocioException("Peso inválido: '" + texto.trim() + "'.");
        }
    }
}
