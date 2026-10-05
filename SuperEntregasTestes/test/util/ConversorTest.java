package util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import exception.RegraDeNegocioException;
import java.math.BigDecimal;
import org.junit.Test;

public class ConversorTest {

    @Test
    public void aceitaPontoDecimal() {
        assertEquals(new BigDecimal("2.5"), Conversor.paraPeso("2.5"));
    }

    @Test
    public void aceitaVirgulaDecimal() {
        assertEquals(new BigDecimal("2.5"), Conversor.paraPeso("2,5"));
    }

    @Test
    public void aparaEspacos() {
        assertEquals(new BigDecimal("10"), Conversor.paraPeso("  10 "));
    }

    @Test
    public void textoNaoNumericoEhRegraDeNegocio() {
        assertThrows(RegraDeNegocioException.class, () -> Conversor.paraPeso("abc"));
    }

    @Test
    public void vazioENuloSaoRegraDeNegocio() {
        assertThrows(RegraDeNegocioException.class, () -> Conversor.paraPeso(""));
        assertThrows(RegraDeNegocioException.class, () -> Conversor.paraPeso(null));
    }
}
