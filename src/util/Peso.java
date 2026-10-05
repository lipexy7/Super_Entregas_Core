package util;

import exception.RegraDeNegocioException;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Regra do peso da encomenda (coluna DECIMAL(5,2)): entre 0,01 e 999,99 kg,
 * com duas casas decimais. Extraída de EncomendaService para ser reutilizada
 * pela CalculadoraDeFrete e testada isoladamente, sem banco de dados.
 */
public final class Peso {

    public static final BigDecimal MINIMO = new BigDecimal("0.01");
    public static final BigDecimal MAXIMO = new BigDecimal("999.99");

    private Peso() { }

    /** Valida e arredonda (HALF_UP) para 2 casas. */
    public static BigDecimal normalizar(BigDecimal peso) {
        if (peso == null || peso.signum() <= 0) {
            throw new RegraDeNegocioException("O peso deve ser maior que zero.");
        }
        BigDecimal arredondado = peso.setScale(2, RoundingMode.HALF_UP);
        if (arredondado.compareTo(MINIMO) < 0 || arredondado.compareTo(MAXIMO) > 0) {
            throw new RegraDeNegocioException("O peso deve estar entre 0,01 e 999,99 kg.");
        }
        return arredondado;
    }
}
