package exception;

/**
 * Raiz das exceções do sistema. É não-verificada (RuntimeException) para que as
 * camadas superiores (Swing, Servlets/JSP) decidam onde e como tratá-la, sem
 * poluir as assinaturas dos métodos de serviço.
 */
public class SuperEntregasException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public SuperEntregasException(String mensagem) {
        super(mensagem);
    }

    public SuperEntregasException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
