package util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import exception.RegraDeNegocioException;
import java.math.BigDecimal;
import org.junit.Test;

public class PesoTest {

    @Test
    public void arredondaParaDuasCasasComHalfUp() {
        assertEquals(new BigDecimal("2.56"), Peso.normalizar(new BigDecimal("2.555")));
        assertEquals(new BigDecimal("2.55"), Peso.normalizar(new BigDecimal("2.554")));
    }

    @Test
    public void inteiroGanhaDuasCasas() {
        assertEquals(new BigDecimal("7.00"), Peso.normalizar(new BigDecimal("7")));
    }

    @Test
    public void limitesSaoAceitos() {
        assertEquals(Peso.MINIMO, Peso.normalizar(new BigDecimal("0.01")));
        assertEquals(Peso.MAXIMO, Peso.normalizar(new BigDecimal("999.99")));
    }

    @Test
    public void foraDosLimitesELancado() {
        assertThrows(RegraDeNegocioException.class, () -> Peso.normalizar(new BigDecimal("0.004")));
        assertThrows(RegraDeNegocioException.class, () -> Peso.normalizar(new BigDecimal("999.995")));
        assertThrows(RegraDeNegocioException.class, () -> Peso.normalizar(BigDecimal.ZERO));
        assertThrows(RegraDeNegocioException.class, () -> Peso.normalizar(new BigDecimal("-1")));
        assertThrows(RegraDeNegocioException.class, () -> Peso.normalizar(null));
    }
}
