package service;

import dao.UsuarioDAO;
import exception.RegraDeNegocioException;
import model.Usuario;
import seguranca.VerificadorDeSenha;
import util.Cpf;

/** Caso de uso: login. Antes era UsuarioDAO.validarLogin(), que só devolvia boolean. */
public class AutenticacaoService {

    private static final String MENSAGEM = "CPF ou senha incorretos.";

    private final UsuarioDAO usuarioDAO;
    private final VerificadorDeSenha verificador;

    public AutenticacaoService(UsuarioDAO usuarioDAO, VerificadorDeSenha verificador) {
        this.usuarioDAO = usuarioDAO;
        this.verificador = verificador;
    }

    /**
     * @return o usuário autenticado (com o perfil de gerente, necessário para
     *         autorizar operações administrativas na camada web)
     * @throws RegraDeNegocioException credenciais inválidas; a mesma mensagem é
     *         usada para CPF inexistente e senha errada (não revela quais CPFs existem)
     */
    public Usuario autenticar(String cpf, String senha) {
        if (senha == null || senha.trim().isEmpty()) {
            throw new RegraDeNegocioException("Informe a senha.");
        }
        String cpfNormalizado = Cpf.formatar(cpf);
        String armazenada = usuarioDAO.buscarSenhaPorCpf(cpfNormalizado)
                .orElseThrow(() -> new RegraDeNegocioException(MENSAGEM));
        if (!verificador.confere(senha.trim(), armazenada)) {
            throw new RegraDeNegocioException(MENSAGEM);
        }
        return usuarioDAO.buscarPorCpf(cpfNormalizado)
                .orElseThrow(() -> new RegraDeNegocioException(MENSAGEM));
    }
}
