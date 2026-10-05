package teste;

import config.FabricaDeServicos;
import dao.memoria.EncomendaMemoriaDAO;
import dao.memoria.PontoDeControleMemoriaDAO;
import dao.memoria.RastreioMemoriaDAO;
import dao.memoria.UsuarioMemoriaDAO;
import exception.AcessoNegadoException;
import exception.RegraDeNegocioException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import model.Encomenda;
import model.PontoDeControle;
import model.RastreioResultado;
import model.Usuario;
import seguranca.SenhaEmTextoPlano;
import util.Conversor;

/**
 * Testa as regras de negócio (camada service) com DAOs em memória, usando os
 * mesmos dados do superentregas.sql. Não precisa de MySQL nem de interface gráfica.
 */
public class TestesDeServico {

    private final Verificador v;
    private FabricaDeServicos app;
    private Usuario gerente;
    private Usuario comum;

    public TestesDeServico(Verificador v) {
        this.v = v;
    }

    public void executar() {
        montarCenario();
        testarAutenticacao();
        testarEncomenda();
        testarPontoDeControle();
        testarRastreio();
    }

    private void montarCenario() {
        UsuarioMemoriaDAO usuarios = new UsuarioMemoriaDAO();
        usuarios.adicionar(new Usuario(1, "Pedro Henrrique", "pedrinhogames123@gmail.com", "132.457.680-90", false), "senhamuitoboa1234");
        usuarios.adicionar(new Usuario(2, "Breno Moura", "beemoura@gmail.com", "987.654.321-00", true), "galodoido12345");
        usuarios.adicionar(new Usuario(3, "Roberta Coelho", "coelhorobert@gmail.com", "111.222.333-96", false), "coelhodapascoa123");
        usuarios.adicionar(new Usuario(5, "Felipe Reis", "reisfelipe@gmail.com", "123.456.789-09", true), "cruzeirocabuloso123");

        PontoDeControle p1 = new PontoDeControle(1, "Casa Nova", "Rua Galvão Bueno 09", "São Paulo");
        PontoDeControle p2 = new PontoDeControle(2, "Casa Grande", "Rua Primeiro de Março 103", "Rio de Janeiro");
        PontoDeControle p3 = new PontoDeControle(3, "Casinha", "Avenida Otacílio Negrão de Lima 900", "Belo Horizonte");
        PontoDeControle p5 = new PontoDeControle(5, "Moradia", "Rua XV de Novembro 15", "Curitiba");
        PontoDeControleMemoriaDAO pontos = new PontoDeControleMemoriaDAO();
        for (PontoDeControle p : new PontoDeControle[]{p1, p2, p3, p5}) {
            pontos.adicionar(p);
        }

        EncomendaMemoriaDAO encomendas = new EncomendaMemoriaDAO();
        encomendas.adicionar(new Encomenda(1, "XQTRLPZE92WJMCDAKHSY", new BigDecimal("2.44"), 1));
        encomendas.adicionar(new Encomenda(3, "TRAWPMZKLEQX48BUDHC", new BigDecimal("8.67"), 3));
        encomendas.adicionar(new Encomenda(4, "YUJCNXWM3TRLZQVAO", new BigDecimal("4.03"), null));
        encomendas.adicionar(new Encomenda(5, "ZKLMQYRHUPXADTE12NB", new BigDecimal("6.90"), 1));

        RastreioMemoriaDAO rastreio = new RastreioMemoriaDAO();
        rastreio.registrar(1, p1, LocalDateTime.of(2025, 2, 12, 7, 43, 0));
        rastreio.registrar(1, p2, LocalDateTime.of(2025, 5, 21, 13, 58, 0));
        rastreio.registrar(3, p5, LocalDateTime.of(2025, 7, 21, 22, 31, 56));

        app = FabricaDeServicos.com(usuarios, encomendas, pontos, rastreio, new SenhaEmTextoPlano());
        gerente = usuarios.buscarPorId(5).get();
        comum = usuarios.buscarPorId(1).get();
    }

    private void testarAutenticacao() {
        v.secao("AutenticacaoService");
        Usuario u = app.autenticacao().autenticar("123.456.789-09", "cruzeirocabuloso123");
        v.igual("login de gerente devolve o usuário correto", "Felipe Reis", u.getNome());
        v.verdadeiro("usuário gerente vem com perfil de gerente", u.isGerente());
        v.verdadeiro("usuário comum vem sem perfil de gerente",
                !app.autenticacao().autenticar("132.457.680-90", "senhamuitoboa1234").isGerente());
        v.igual("CPF só com dígitos é aceito (formulário web)", 5,
                app.autenticacao().autenticar("12345678909", "cruzeirocabuloso123").getId());
        v.igual("mensagem de senha errada é genérica", "CPF ou senha incorretos.",
                v.lanca("senha errada é recusada", RegraDeNegocioException.class,
                        () -> app.autenticacao().autenticar("123.456.789-09", "errada")));
        v.igual("CPF inexistente não revela que o CPF não existe", "CPF ou senha incorretos.",
                v.lanca("CPF inexistente é recusado", RegraDeNegocioException.class,
                        () -> app.autenticacao().autenticar("000.000.000-00", "qualquer")));
        v.lanca("senha vazia é recusada", RegraDeNegocioException.class,
                () -> app.autenticacao().autenticar("123.456.789-09", "  "));
        v.lanca("CPF malformado é recusado", RegraDeNegocioException.class,
                () -> app.autenticacao().autenticar("123", "x"));
        v.lanca("CPF nulo é recusado", RegraDeNegocioException.class,
                () -> app.autenticacao().autenticar(null, "x"));
    }

    private void testarEncomenda() {
        v.secao("EncomendaService");
        Encomenda e = app.encomendas().cadastrar("abc123xyz", new BigDecimal("3.5"), "132.457.680-90");
        v.verdadeiro("cadastro gera id", e.getId() > 0);
        v.igual("código é normalizado para maiúsculas", "ABC123XYZ", e.getCodigoPostal());
        v.igual("encomenda é vinculada ao cliente pelo CPF", Integer.valueOf(1), e.getIdUsuario());
        v.igual("peso é guardado com 2 casas", new BigDecimal("3.50"), e.getPeso());

        Encomenda virgula = app.encomendas().cadastrar("VIRGULA1", Conversor.paraPeso("2,5"), "111.222.333-96");
        v.igual("peso digitado com vírgula (2,5) é aceito", new BigDecimal("2.50"), virgula.getPeso());

        v.lanca("CPF não cadastrado é recusado", RegraDeNegocioException.class,
                () -> app.encomendas().cadastrar("NOVO1", BigDecimal.ONE, "000.000.000-00"));
        v.lanca("código duplicado é recusado", RegraDeNegocioException.class,
                () -> app.encomendas().cadastrar("XQTRLPZE92WJMCDAKHSY", BigDecimal.ONE, "132.457.680-90"));
        v.lanca("código duplicado em minúsculas também é recusado", RegraDeNegocioException.class,
                () -> app.encomendas().cadastrar("xqtrlpze92wjmcdakhsy", BigDecimal.ONE, "132.457.680-90"));
        v.lanca("peso zero é recusado", RegraDeNegocioException.class,
                () -> app.encomendas().cadastrar("P0", BigDecimal.ZERO, "132.457.680-90"));
        v.lanca("peso negativo é recusado", RegraDeNegocioException.class,
                () -> app.encomendas().cadastrar("P1", new BigDecimal("-1"), "132.457.680-90"));
        v.lanca("peso acima de 999,99 é recusado (DECIMAL(5,2))", RegraDeNegocioException.class,
                () -> app.encomendas().cadastrar("P2", new BigDecimal("1000"), "132.457.680-90"));
        v.lanca("peso nulo é recusado", RegraDeNegocioException.class,
                () -> app.encomendas().cadastrar("P3", null, "132.457.680-90"));
        v.lanca("código vazio é recusado", RegraDeNegocioException.class,
                () -> app.encomendas().cadastrar("   ", BigDecimal.ONE, "132.457.680-90"));
        v.lanca("código com símbolos é recusado", RegraDeNegocioException.class,
                () -> app.encomendas().cadastrar("AB-12!", BigDecimal.ONE, "132.457.680-90"));
        v.lanca("código com mais de 20 caracteres é recusado", RegraDeNegocioException.class,
                () -> app.encomendas().cadastrar("A".repeat(21), BigDecimal.ONE, "132.457.680-90"));
        v.lanca("peso 'abc' é recusado na conversão", RegraDeNegocioException.class,
                () -> Conversor.paraPeso("abc"));
        v.lanca("peso vazio é recusado na conversão", RegraDeNegocioException.class,
                () -> Conversor.paraPeso(""));
    }

    private void testarPontoDeControle() {
        v.secao("PontoDeControleService");
        int antes = app.pontos().listar().size();
        PontoDeControle p = app.pontos().cadastrar(gerente, "  Armazém Sul ", "Rua A 1", "Porto Alegre");
        v.verdadeiro("gerente cadastra ponto e recebe id", p.getId() > 0);
        v.igual("campos são aparados (trim)", "Armazém Sul", p.getNome());
        v.igual("lista cresce em 1", antes + 1, app.pontos().listar().size());
        v.lanca("usuário comum não pode cadastrar", AcessoNegadoException.class,
                () -> app.pontos().cadastrar(comum, "X", "Y", "Z"));
        v.lanca("solicitante nulo (sem login) não pode cadastrar", AcessoNegadoException.class,
                () -> app.pontos().cadastrar(null, "X", "Y", "Z"));
        v.lanca("nome vazio é recusado", RegraDeNegocioException.class,
                () -> app.pontos().cadastrar(gerente, " ", "Y", "Z"));
        v.lanca("endereço nulo é recusado", RegraDeNegocioException.class,
                () -> app.pontos().cadastrar(gerente, "X", null, "Z"));
        v.lanca("cidade com mais de 200 caracteres é recusada", RegraDeNegocioException.class,
                () -> app.pontos().cadastrar(gerente, "X", "Y", "C".repeat(201)));
    }

    private void testarRastreio() {
        v.secao("RastreioService");
        RastreioResultado r = app.rastreio().rastrear("XQTRLPZE92WJMCDAKHSY");
        v.igual("destinatário correto", "Pedro Henrrique", r.getDestinatario().get().getNome());
        v.igual("peso correto", new BigDecimal("2.44"), r.getEncomenda().getPeso());
        v.igual("histórico tem 2 movimentações", 2, r.getHistorico().size());
        v.igual("última localização é a mais recente (Casa Grande)", "Casa Grande",
                r.getUltimaMovimentacao().get().getPonto().getNome());
        v.igual("descrição completa do ponto", "Casa Grande - Rua Primeiro de Março 103 - Rio de Janeiro",
                r.getUltimaMovimentacao().get().getPonto().getDescricaoCompleta());
        v.igual("histórico vem do mais recente para o mais antigo", "Casa Nova",
                r.getHistorico().get(1).getPonto().getNome());
        v.igual("busca ignora caixa e espaços", 1,
                app.rastreio().rastrear("  xqtrlpze92wjmcdakhsy ").getEncomenda().getId());

        RastreioResultado semMov = app.rastreio().rastrear("ZKLMQYRHUPXADTE12NB");
        v.verdadeiro("encomenda sem movimentação é encontrada", !semMov.temMovimentacao());
        v.verdadeiro("... e não tem última localização", !semMov.getUltimaMovimentacao().isPresent());
        RastreioResultado semDono = app.rastreio().rastrear("YUJCNXWM3TRLZQVAO");
        v.verdadeiro("encomenda sem dono é encontrada, sem destinatário", !semDono.getDestinatario().isPresent());

        v.lanca("código inexistente é recusado", RegraDeNegocioException.class,
                () -> app.rastreio().rastrear("NAOEXISTE"));
        v.lanca("código vazio é recusado", RegraDeNegocioException.class,
                () -> app.rastreio().rastrear(""));
        v.lanca("código nulo é recusado", RegraDeNegocioException.class,
                () -> app.rastreio().rastrear(null));
    }
}
