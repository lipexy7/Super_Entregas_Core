package service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import exception.RegraDeNegocioException;
import model.Usuario;
import org.junit.Before;
import org.junit.Test;
import testes.CenarioPadrao;

public class AutenticacaoServiceTest {

    private AutenticacaoService servico;

    @Before
    public void preparar() {
        servico = new CenarioPadrao().app.autenticacao();
    }

    @Test
    public void loginDeGerenteDevolveUsuarioComPerfil() {
        Usuario u = servico.autenticar(CenarioPadrao.CPF_GERENTE, CenarioPadrao.SENHA_GERENTE);
        assertEquals("Felipe Reis", u.getNome());
        assertTrue(u.isGerente());
    }

    @Test
    public void loginDeUsuarioComumNaoTemPerfilDeGerente() {
        assertFalse(servico.autenticar(CenarioPadrao.CPF_COMUM, CenarioPadrao.SENHA_COMUM).isGerente());
    }

    @Test
    public void cpfSemMascaraEAceito() {
        assertEquals(5, servico.autenticar("12345678909", CenarioPadrao.SENHA_GERENTE).getId());
    }

    @Test
    public void senhaComEspacosNasPontasEAparada() {
        assertEquals(5, servico.autenticar(CenarioPadrao.CPF_GERENTE, "  " + CenarioPadrao.SENHA_GERENTE + " ").getId());
    }

    @Test
    public void senhaErradaEhRecusada() {
        assertThrows(RegraDeNegocioException.class, () -> servico.autenticar(CenarioPadrao.CPF_GERENTE, "errada"));
    }

    @Test
    public void senhaEmCaixaDiferenteEhRecusada() {
        assertThrows(RegraDeNegocioException.class,
                () -> servico.autenticar(CenarioPadrao.CPF_GERENTE, CenarioPadrao.SENHA_GERENTE.toUpperCase()));
    }

    @Test
    public void cpfInexistenteTemMesmaMensagemDeSenhaErrada() {
        String msgSenha = assertThrows(RegraDeNegocioException.class,
                () -> servico.autenticar(CenarioPadrao.CPF_GERENTE, "errada")).getMessage();
        String msgCpf = assertThrows(RegraDeNegocioException.class,
                () -> servico.autenticar("000.000.000-00", "qualquer")).getMessage();
        assertEquals("Mensagens diferentes revelariam quais CPFs existem", msgSenha, msgCpf);
    }

    @Test
    public void senhaVaziaOuNulaEhRecusada() {
        assertThrows(RegraDeNegocioException.class, () -> servico.autenticar(CenarioPadrao.CPF_GERENTE, "   "));
        assertThrows(RegraDeNegocioException.class, () -> servico.autenticar(CenarioPadrao.CPF_GERENTE, null));
    }

    @Test
    public void cpfMalformadoOuNuloEhRecusado() {
        assertThrows(RegraDeNegocioException.class, () -> servico.autenticar("123", "x"));
        assertThrows(RegraDeNegocioException.class, () -> servico.autenticar(null, "x"));
    }

    @Test
    public void tentativaDeInjecaoSqlNoCpfEhRecusada() {
        assertThrows(RegraDeNegocioException.class, () -> servico.autenticar("' OR '1'='1", "x"));
    }
}
