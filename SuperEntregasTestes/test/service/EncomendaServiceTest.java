package service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import exception.RegraDeNegocioException;
import java.math.BigDecimal;
import model.Encomenda;
import org.junit.Before;
import org.junit.Test;
import testes.CenarioPadrao;

public class EncomendaServiceTest {

    private CenarioPadrao cenario;
    private EncomendaService servico;

    @Before
    public void preparar() {
        cenario = new CenarioPadrao();
        servico = cenario.app.encomendas();
    }

    @Test
    public void cadastroGeraIdEVinculaAoClientePeloCpf() {
        Encomenda e = servico.cadastrar("NOVO123", new BigDecimal("3.5"), CenarioPadrao.CPF_COMUM);
        assertTrue(e.getId() > 0);
        assertEquals(Integer.valueOf(1), e.getIdUsuario());
    }

    @Test
    public void codigoEhNormalizadoParaMaiusculas() {
        assertEquals("ABC123XYZ", servico.cadastrar("abc123xyz", BigDecimal.ONE, CenarioPadrao.CPF_COMUM).getCodigoPostal());
    }

    @Test
    public void pesoEhGuardadoComDuasCasas() {
        assertEquals(new BigDecimal("3.50"), servico.cadastrar("P1", new BigDecimal("3.5"), CenarioPadrao.CPF_COMUM).getPeso());
    }

    @Test
    public void cadastroAumentaAQuantidadeDeEncomendas() {
        int antes = cenario.encomendas.quantidade();
        servico.cadastrar("P2", BigDecimal.TEN, CenarioPadrao.CPF_COMUM);
        assertEquals(antes + 1, cenario.encomendas.quantidade());
    }

    @Test
    public void cpfSemMascaraLocalizaOCliente() {
        assertEquals(Integer.valueOf(5), servico.cadastrar("P3", BigDecimal.ONE, "12345678909").getIdUsuario());
    }

    @Test
    public void cpfNaoCadastradoEhRecusadoESemInserir() {
        int antes = cenario.encomendas.quantidade();
        assertThrows(RegraDeNegocioException.class, () -> servico.cadastrar("P4", BigDecimal.ONE, "000.000.000-00"));
        assertEquals(antes, cenario.encomendas.quantidade());
    }

    @Test
    public void codigoDuplicadoEhRecusado() {
        assertThrows(RegraDeNegocioException.class,
                () -> servico.cadastrar(CenarioPadrao.CODIGO_COM_HISTORICO, BigDecimal.ONE, CenarioPadrao.CPF_COMUM));
    }

    @Test
    public void codigoDuplicadoEmMinusculasTambemEhRecusado() {
        assertThrows(RegraDeNegocioException.class,
                () -> servico.cadastrar(CenarioPadrao.CODIGO_COM_HISTORICO.toLowerCase(), BigDecimal.ONE, CenarioPadrao.CPF_COMUM));
    }

    @Test
    public void pesosInvalidosSaoRecusados() {
        assertThrows(RegraDeNegocioException.class, () -> servico.cadastrar("P5", BigDecimal.ZERO, CenarioPadrao.CPF_COMUM));
        assertThrows(RegraDeNegocioException.class, () -> servico.cadastrar("P6", new BigDecimal("-1"), CenarioPadrao.CPF_COMUM));
        assertThrows(RegraDeNegocioException.class, () -> servico.cadastrar("P7", new BigDecimal("1000"), CenarioPadrao.CPF_COMUM));
        assertThrows(RegraDeNegocioException.class, () -> servico.cadastrar("P8", null, CenarioPadrao.CPF_COMUM));
    }

    @Test
    public void codigosInvalidosSaoRecusados() {
        assertThrows(RegraDeNegocioException.class, () -> servico.cadastrar("   ", BigDecimal.ONE, CenarioPadrao.CPF_COMUM));
        assertThrows(RegraDeNegocioException.class, () -> servico.cadastrar("AB-12!", BigDecimal.ONE, CenarioPadrao.CPF_COMUM));
        assertThrows(RegraDeNegocioException.class, () -> servico.cadastrar("A".repeat(21), BigDecimal.ONE, CenarioPadrao.CPF_COMUM));
        assertThrows(RegraDeNegocioException.class, () -> servico.cadastrar("'; DROP TABLE encomenda;--", BigDecimal.ONE, CenarioPadrao.CPF_COMUM));
    }
}
