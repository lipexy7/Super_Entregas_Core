package seguranca;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/** Implementação compatível com os dados atuais (texto puro). Comparação em tempo constante. */
public class SenhaEmTextoPlano implements VerificadorDeSenha {

    @Override
    public boolean confere(String senhaInformada, String senhaArmazenada) {
        if (senhaInformada == null || senhaArmazenada == null) {
            return false;
        }
        return MessageDigest.isEqual(
                senhaInformada.getBytes(StandardCharsets.UTF_8),
                senhaArmazenada.getBytes(StandardCharsets.UTF_8));
    }
}
