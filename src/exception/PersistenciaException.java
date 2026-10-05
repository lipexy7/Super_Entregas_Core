package exception;

/**
 * Falha técnica de infraestrutura (banco indisponível, SQL inválido etc.).
 * Substitui o antigo hábito de exibir JOptionPane ou engolir a SQLException
 * dentro das DAOs: a causa original é preservada e a camada de apresentação
 * decide como informar o usuário.
 */
public class PersistenciaException extends SuperEntregasException {

    private static final long serialVersionUID = 1L;

    public PersistenciaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
