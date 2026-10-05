package service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import exception.AcessoNegadoException;
import exception.RegraDeNegocioException;
import model.PontoDeControle;
import org.junit.Before;
import org.junit.Test;
import testes.CenarioPadrao;

public class PontoDeControleServiceTest {

    private CenarioPadrao cenario;
    private PontoDeControleService servico;

    @Before
    public void preparar() {
        cenario = new CenarioPadrao();
        servico = cenario.app.pontos();
    }

    @Test
    public void gerenteCadastraPontoERecebeId() {
        PontoDeControle p = servico.cadastrar(cenario.gerente, "Armazém Sul", "Rua A 1", "Porto Alegre");
        assertTrue(p.getId() > 0);
    }

    @Test
    public void camposSaoAparados() {
        assertEquals("Armazém Sul", servico.cadastrar(cenario.gerente, "  Armazém Sul ", "Rua A 1", "Porto Alegre").getNome());
    }

    @Test
    public void listaCresceEmUmAposCadastro() {
        int antes = servico.listar().size();
        servico.cadastrar(cenario.gerente, "X", "Y", "Z");
        assertEquals(antes + 1, servico.listar().size());
    }

    @Test
    public void usuarioComumNaoPodeCadastrar() {
        int antes = servico.listar().size();
        assertThrows(AcessoNegadoException.class, () -> servico.cadastrar(cenario.comum, "X", "Y", "Z"));
        assertEquals("Nada pode ser gravado quando o acesso é negado", antes, servico.listar().size());
    }

    @Test
    public void semLoginNaoPodeCadastrar() {
        assertThrows(AcessoNegadoException.class, () -> servico.cadastrar(null, "X", "Y", "Z"));
    }

    @Test
    public void acessoNegadoTambemEhUmaRegraDeNegocio() {
        assertThrows(RegraDeNegocioException.class, () -> servico.cadastrar(cenario.comum, "X", "Y", "Z"));
    }

    @Test
    public void camposVaziosOuNulosSaoRecusados() {
        assertThrows(RegraDeNegocioException.class, () -> servico.cadastrar(cenario.gerente, " ", "Y", "Z"));
        assertThrows(RegraDeNegocioException.class, () -> servico.cadastrar(cenario.gerente, "X", null, "Z"));
        assertThrows(RegraDeNegocioException.class, () -> servico.cadastrar(cenario.gerente, "X", "Y", ""));
    }

    @Test
    public void camposAcimaDoTamanhoDaColunaSaoRecusados() {
        assertThrows(RegraDeNegocioException.class, () -> servico.cadastrar(cenario.gerente, "N".repeat(201), "Y", "Z"));
        assertThrows(RegraDeNegocioException.class, () -> servico.cadastrar(cenario.gerente, "X", "E".repeat(256), "Z"));
        assertThrows(RegraDeNegocioException.class, () -> servico.cadastrar(cenario.gerente, "X", "Y", "C".repeat(201)));
    }
}
