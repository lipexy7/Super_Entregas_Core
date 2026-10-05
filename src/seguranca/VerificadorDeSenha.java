package seguranca;

/**
 * Estratégia de conferência de senha. Hoje o banco guarda senhas em texto
 * puro (herança da versão desktop); quando houver migração para hash
 * (PBKDF2, BCrypt...) basta criar uma nova implementação desta interface,
 * sem alterar AutenticacaoService (princípio Aberto/Fechado).
 */
public interface VerificadorDeSenha {

    boolean confere(String senhaInformada, String senhaArmazenada);
}
