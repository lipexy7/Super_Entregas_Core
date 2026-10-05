package config;

import conexao.ConfiguracaoBanco;
import conexao.FabricaDeConexao;
import conexao.FabricaDeConexaoMySQL;
import dao.EncomendaDAO;
import dao.PontoDeControleDAO;
import dao.RastreioDAO;
import dao.UsuarioDAO;
import dao.jdbc.EncomendaJdbcDAO;
import dao.jdbc.PontoDeControleJdbcDAO;
import dao.jdbc.RastreioJdbcDAO;
import dao.jdbc.UsuarioJdbcDAO;
import seguranca.SenhaEmTextoPlano;
import seguranca.VerificadorDeSenha;
import service.AutenticacaoService;
import service.CalculadoraDeFrete;
import service.EncomendaService;
import service.PontoDeControleService;
import service.RastreioService;

/**
 * Raiz de composição (composition root): o ÚNICO lugar que conhece as classes
 * concretas e faz a injeção de dependências por construtor. Na fase web basta
 * criar uma instância no início da aplicação (ex.: ServletContextListener) e
 * guardá-la no ServletContext; Servlets chamam apenas os services.
 */
public class FabricaDeServicos {

    private final AutenticacaoService autenticacao;
    private final EncomendaService encomendas;
    private final PontoDeControleService pontos;
    private final RastreioService rastreio;
    private final CalculadoraDeFrete frete = new CalculadoraDeFrete();

    private FabricaDeServicos(UsuarioDAO usuarios, EncomendaDAO encomendas, PontoDeControleDAO pontos,
                              RastreioDAO rastreio, VerificadorDeSenha verificador) {
        this.autenticacao = new AutenticacaoService(usuarios, verificador);
        this.encomendas = new EncomendaService(encomendas, usuarios);
        this.pontos = new PontoDeControleService(pontos);
        this.rastreio = new RastreioService(encomendas, usuarios, rastreio);
    }

    /** Configuração de produção: DAOs JDBC e MySQL, configuração lida do ambiente/arquivo. */
    public static FabricaDeServicos mysql() {
        return mysql(ConfiguracaoBanco.carregar());
    }

    public static FabricaDeServicos mysql(ConfiguracaoBanco configuracao) {
        FabricaDeConexao conexoes = new FabricaDeConexaoMySQL(configuracao);
        return new FabricaDeServicos(
                new UsuarioJdbcDAO(conexoes), new EncomendaJdbcDAO(conexoes),
                new PontoDeControleJdbcDAO(conexoes), new RastreioJdbcDAO(conexoes),
                new SenhaEmTextoPlano());
    }

    /** Configuração livre: usada pelos testes com DAOs em memória. */
    public static FabricaDeServicos com(UsuarioDAO usuarios, EncomendaDAO encomendas,
                                        PontoDeControleDAO pontos, RastreioDAO rastreio,
                                        VerificadorDeSenha verificador) {
        return new FabricaDeServicos(usuarios, encomendas, pontos, rastreio, verificador);
    }

    public AutenticacaoService autenticacao() { return autenticacao; }
    public EncomendaService encomendas() { return encomendas; }
    public PontoDeControleService pontos() { return pontos; }
    public RastreioService rastreio() { return rastreio; }
    public CalculadoraDeFrete frete() { return frete; }
}
