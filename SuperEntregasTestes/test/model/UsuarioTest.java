package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import org.junit.Test;

public class UsuarioTest {

    @Test
    public void guardaOsDadosInformados() {
        Usuario u = new Usuario(7, "Ana", "ana@x.com", "111.222.333-96", true);
        assertEquals(7, u.getId());
        assertEquals("Ana", u.getNome());
        assertTrue(u.isGerente());
    }

    @Test
    public void naoExpoeSenhaParaAsCamadasDeApresentacao() {
        for (Method m : Usuario.class.getMethods()) {
            assertFalse("Usuario não deve ter getter de senha: " + m.getName(),
                    m.getName().toLowerCase().contains("senha"));
        }
        assertNotNull(new Usuario(1, "A", "a@a", "000", false).toString());
        assertFalse(new Usuario(1, "A", "a@a", "000", false).toString().toLowerCase().contains("senha"));
    }
}
