package service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import exception.RegraDeNegocioException;
import java.math.BigDecimal;
import model.RastreioResultado;
import org.junit.Before;
import org.junit.Test;
import testes.CenarioPadrao;

public class RastreioServiceTest {

    private RastreioService servico;

    @Before
    public void preparar() {
        servico = new CenarioPadrao().app.rastreio();
    }

    @Test
    public void devolveDestinatarioEPeso() {
        RastreioResultado r = servico.rastrear(CenarioPadrao.CODIGO_COM_HISTORICO);
        assertEquals("Pedro Henrrique", r.getDestinatario().get().getNome());
        assertEquals(new BigDecimal("2.44"), r.getEncomenda().getPeso());
    }

    @Test
    public void historicoVemDoMaisRecenteParaOMaisAntigo() {
        RastreioResultado r = servico.rastrear(CenarioPadrao.CODIGO_COM_HISTORICO);
        assertEquals(2, r.getHistorico().size());
        assertEquals("Casa Grande", r.getHistorico().get(0).getPonto().getNome());
        assertEquals("Casa Nova", r.getHistorico().get(1).getPonto().getNome());
    }

    @Test
    public void ultimaMovimentacaoEhAMaisRecente() {
        RastreioResultado r = servico.rastrear(CenarioPadrao.CODIGO_COM_HISTORICO);
        assertEquals("Casa Grande - Rua Primeiro de Março 103 - Rio de Janeiro",
                r.getUltimaMovimentacao().get().getPonto().getDescricaoCompleta());
    }

    @Test
    public void buscaIgnoraCaixaEEspacos() {
        assertEquals(1, servico.rastrear("  " + CenarioPadrao.CODIGO_COM_HISTORICO.toLowerCase() + " ").getEncomenda().getId());
    }

    @Test
    public void encomendaSemMovimentacaoEhEncontrada() {
        RastreioResultado r = servico.rastrear(CenarioPadrao.CODIGO_SEM_MOVIMENTO);
        assertFalse(r.temMovimentacao());
        assertFalse(r.getUltimaMovimentacao().isPresent());
    }

    @Test
    public void encomendaSemDonoEhEncontradaSemDestinatario() {
        assertFalse(servico.rastrear(CenarioPadrao.CODIGO_SEM_DONO).getDestinatario().isPresent());
    }

    @Test
    public void codigoInexistenteEhRecusado() {
        assertThrows(RegraDeNegocioException.class, () -> servico.rastrear("NAOEXISTE"));
    }

    @Test
    public void codigoVazioOuNuloEhRecusado() {
        assertThrows(RegraDeNegocioException.class, () -> servico.rastrear(""));
        assertThrows(RegraDeNegocioException.class, () -> servico.rastrear(null));
    }

    @Test
    public void historicoDevolvidoNaoPodeSerAlterado() {
        RastreioResultado r = servico.rastrear(CenarioPadrao.CODIGO_COM_HISTORICO);
        assertThrows(UnsupportedOperationException.class, () -> r.getHistorico().clear());
        assertTrue(r.temMovimentacao());
    }
}
