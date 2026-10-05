package seguranca;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SenhaEmTextoPlanoTest {

    private final VerificadorDeSenha verificador = new SenhaEmTextoPlano();

    @Test
    public void senhaIgualConfere() {
        assertTrue(verificador.confere("abc123", "abc123"));
    }

    @Test
    public void senhaDiferenteNaoConfere() {
        assertFalse(verificador.confere("abc123", "abc124"));
    }

    @Test
    public void diferencaDeCaixaNaoConfere() {
        assertFalse(verificador.confere("ABC", "abc"));
    }

    @Test
    public void nulosNuncaConferem() {
        assertFalse(verificador.confere(null, "abc"));
        assertFalse(verificador.confere("abc", null));
        assertFalse(verificador.confere(null, null));
    }
}
