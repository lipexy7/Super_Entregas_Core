package testes;

import config.FabricaDeServicos;
import dao.memoria.EncomendaMemoriaDAO;
import dao.memoria.PontoDeControleMemoriaDAO;
import dao.memoria.RastreioMemoriaDAO;
import dao.memoria.UsuarioMemoriaDAO;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import model.Encomenda;
import model.PontoDeControle;
import model.Usuario;
import seguranca.SenhaEmTextoPlano;

/**
 * Fixture compartilhada: monta os services com DAOs em memória carregados com os
 * mesmos dados do superentregas.sql. Cada teste cria um cenário novo (@Before),
 * então um teste nunca interfere em outro.
 */
public class CenarioPadrao {

    public static final String CPF_GERENTE = "123.456.789-09";
    public static final String SENHA_GERENTE = "cruzeirocabuloso123";
    public static final String CPF_COMUM = "132.457.680-90";
    public static final String SENHA_COMUM = "senhamuitoboa1234";
    public static final String CODIGO_COM_HISTORICO = "XQTRLPZE92WJMCDAKHSY";
    public static final String CODIGO_SEM_MOVIMENTO = "ZKLMQYRHUPXADTE12NB";
    public static final String CODIGO_SEM_DONO = "YUJCNXWM3TRLZQVAO";

    public final FabricaDeServicos app;
    public final EncomendaMemoriaDAO encomendas = new EncomendaMemoriaDAO();
    public final PontoDeControleMemoriaDAO pontos = new PontoDeControleMemoriaDAO();
    public final Usuario gerente;
    public final Usuario comum;

    public CenarioPadrao() {
        UsuarioMemoriaDAO usuarios = new UsuarioMemoriaDAO();
        usuarios.adicionar(new Usuario(1, "Pedro Henrrique", "pedrinhogames123@gmail.com", CPF_COMUM, false), SENHA_COMUM);
        usuarios.adicionar(new Usuario(2, "Breno Moura", "beemoura@gmail.com", "987.654.321-00", true), "galodoido12345");
        usuarios.adicionar(new Usuario(3, "Roberta Coelho", "coelhorobert@gmail.com", "111.222.333-96", false), "coelhodapascoa123");
        usuarios.adicionar(new Usuario(5, "Felipe Reis", "reisfelipe@gmail.com", CPF_GERENTE, true), SENHA_GERENTE);

        PontoDeControle p1 = new PontoDeControle(1, "Casa Nova", "Rua Galvão Bueno 09", "São Paulo");
        PontoDeControle p2 = new PontoDeControle(2, "Casa Grande", "Rua Primeiro de Março 103", "Rio de Janeiro");
        PontoDeControle p3 = new PontoDeControle(3, "Casinha", "Avenida Otacílio Negrão de Lima 900", "Belo Horizonte");
        PontoDeControle p5 = new PontoDeControle(5, "Moradia", "Rua XV de Novembro 15", "Curitiba");
        for (PontoDeControle p : new PontoDeControle[]{p1, p2, p3, p5}) {
            pontos.adicionar(p);
        }

        encomendas.adicionar(new Encomenda(1, CODIGO_COM_HISTORICO, new BigDecimal("2.44"), 1));
        encomendas.adicionar(new Encomenda(3, "TRAWPMZKLEQX48BUDHC", new BigDecimal("8.67"), 3));
        encomendas.adicionar(new Encomenda(4, CODIGO_SEM_DONO, new BigDecimal("4.03"), null));
        encomendas.adicionar(new Encomenda(5, CODIGO_SEM_MOVIMENTO, new BigDecimal("6.90"), 1));

        RastreioMemoriaDAO rastreio = new RastreioMemoriaDAO();
        rastreio.registrar(1, p1, LocalDateTime.of(2025, 2, 12, 7, 43, 0));
        rastreio.registrar(1, p2, LocalDateTime.of(2025, 5, 21, 13, 58, 0));
        rastreio.registrar(3, p5, LocalDateTime.of(2025, 7, 21, 22, 31, 56));

        app = FabricaDeServicos.com(usuarios, encomendas, pontos, rastreio, new SenhaEmTextoPlano());
        gerente = usuarios.buscarPorId(5).get();
        comum = usuarios.buscarPorId(1).get();
    }
}
