package util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import exception.RegraDeNegocioException;
import org.junit.Test;

public class CpfTest {

    @Test
    public void formataApenasDigitos() {
        assertEquals("123.456.789-09", Cpf.formatar("12345678909"));
    }

    @Test
    public void mantemCpfJaFormatado() {
        assertEquals("123.456.789-09", Cpf.formatar("123.456.789-09"));
    }

    @Test
    public void ignoraEspacosELetras() {
        assertEquals("123.456.789-09", Cpf.formatar("  123 456 789 09 "));
    }

    @Test
    public void menosDeOnzeDigitosEhInvalido() {
        assertThrows(RegraDeNegocioException.class, () -> Cpf.formatar("123"));
    }

    @Test
    public void maisDeOnzeDigitosEhInvalido() {
        assertThrows(RegraDeNegocioException.class, () -> Cpf.formatar("123456789012"));
    }

    @Test
    public void nuloEVazioSaoInvalidos() {
        assertThrows(RegraDeNegocioException.class, () -> Cpf.formatar(null));
        assertThrows(RegraDeNegocioException.class, () -> Cpf.formatar(""));
    }
}
