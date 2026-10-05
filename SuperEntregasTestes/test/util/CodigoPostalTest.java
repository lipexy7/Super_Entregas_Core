package util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import exception.RegraDeNegocioException;
import org.junit.Test;

public class CodigoPostalTest {

    @Test
    public void converteParaMaiusculasEAparaEspacos() {
        assertEquals("ABC123", CodigoPostal.normalizar("  abc123 "));
    }

    @Test
    public void aceitaExatamenteVinteCaracteres() {
        String vinte = "A".repeat(20);
        assertEquals(vinte, CodigoPostal.normalizar(vinte));
    }

    @Test
    public void rejeitaVinteEUmCaracteres() {
        assertThrows(RegraDeNegocioException.class, () -> CodigoPostal.normalizar("A".repeat(21)));
    }

    @Test
    public void rejeitaSimbolosEEspacosInternos() {
        assertThrows(RegraDeNegocioException.class, () -> CodigoPostal.normalizar("AB-12"));
        assertThrows(RegraDeNegocioException.class, () -> CodigoPostal.normalizar("AB 12"));
    }

    @Test
    public void rejeitaNuloEVazio() {
        assertThrows(RegraDeNegocioException.class, () -> CodigoPostal.normalizar(null));
        assertThrows(RegraDeNegocioException.class, () -> CodigoPostal.normalizar("   "));
    }
}
