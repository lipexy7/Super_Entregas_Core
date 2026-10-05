package service;

import dao.PontoDeControleDAO;
import exception.AcessoNegadoException;
import exception.RegraDeNegocioException;
import java.util.List;
import model.PontoDeControle;
import model.Usuario;

/** Caso de uso: administração de pontos de controle (restrita a gerentes). */
public class PontoDeControleService {

    private final PontoDeControleDAO pontoDAO;

    public PontoDeControleService(PontoDeControleDAO pontoDAO) {
        this.pontoDAO = pontoDAO;
    }

    /**
     * @param solicitante usuário logado; precisa ser gerente. A versão desktop
     *                    não verificava is_gerente, contrariando o requisito de
     *                    "diferenciação entre usuários comuns e administradores".
     */
    public PontoDeControle cadastrar(Usuario solicitante, String nome, String endereco, String cidade) {
        if (solicitante == null || !solicitante.isGerente()) {
            throw new AcessoNegadoException("Apenas gerentes podem cadastrar pontos de controle.");
        }
        PontoDeControle novo = PontoDeControle.novo(
                obrigatorio(nome, "nome", 200),
                obrigatorio(endereco, "endereço", 255),
                obrigatorio(cidade, "cidade", 200));
        return pontoDAO.inserir(novo);
    }

    public List<PontoDeControle> listar() {
        return pontoDAO.listarTodos();
    }

    private String obrigatorio(String valor, String campo, int tamanhoMaximo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new RegraDeNegocioException("Por favor, preencha o campo " + campo + ".");
        }
        String limpo = valor.trim();
        if (limpo.length() > tamanhoMaximo) {
            throw new RegraDeNegocioException("O campo " + campo + " aceita até " + tamanhoMaximo + " caracteres.");
        }
        return limpo;
    }
}
