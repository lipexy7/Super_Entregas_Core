package util;

import exception.RegraDeNegocioException;

/** Regras de formato do código postal (coluna VARCHAR(20), valor único). */
public final class CodigoPostal {

    public static final int TAMANHO_MAXIMO = 20;

    private CodigoPostal() { }

    /** Remove espaços, converte para maiúsculas e valida; usado no cadastro e na consulta. */
    public static String normalizar(String entrada) {
        if (entrada == null || entrada.trim().isEmpty()) {
            throw new RegraDeNegocioException("Informe o código postal.");
        }
        String codigo = entrada.trim().toUpperCase();
        if (codigo.length() > TAMANHO_MAXIMO || !codigo.matches("[A-Z0-9]+")) {
            throw new RegraDeNegocioException(
                    "Código postal inválido: use apenas letras e números (até " + TAMANHO_MAXIMO + " caracteres).");
        }
        return codigo;
    }
}
