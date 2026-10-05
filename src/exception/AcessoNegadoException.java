package exception;

/** O usuário autenticado não tem perfil suficiente para executar a operação. */
public class AcessoNegadoException extends RegraDeNegocioException {

    private static final long serialVersionUID = 1L;

    public AcessoNegadoException(String mensagem) {
        super(mensagem);
    }
}
