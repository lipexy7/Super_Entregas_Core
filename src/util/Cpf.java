package util;

import exception.RegraDeNegocioException;

/**
 * Normalização de CPF. O banco guarda o CPF com máscara (000.000.000-00) porque
 * o JFormattedTextField da versão desktop enviava assim; um formulário web pode
 * enviar só os dígitos. Esta classe garante um único formato nas consultas.
 */
public final class Cpf {

    private Cpf() { }

    public static String formatar(String entrada) {
        if (entrada == null) {
            throw new RegraDeNegocioException("Informe o CPF.");
        }
        String d = entrada.replaceAll("\\D", "");
        if (d.length() != 11) {
            throw new RegraDeNegocioException("CPF inválido: informe 11 dígitos.");
        }
        return d.substring(0, 3) + "." + d.substring(3, 6) + "." + d.substring(6, 9) + "-" + d.substring(9);
    }
}
