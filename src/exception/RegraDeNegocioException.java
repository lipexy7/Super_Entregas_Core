package exception;

/**
 * Violação de uma regra de negócio ou dado de entrada inválido. A mensagem é
 * escrita em português e pode ser exibida diretamente ao usuário final.
 */
public class RegraDeNegocioException extends SuperEntregasException {

    private static final long serialVersionUID = 1L;

    public RegraDeNegocioException(String mensagem) {
        super(mensagem);
    }
}
