package conexao;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Parâmetros de conexão. Ordem de precedência (a última vence):
 * valores padrão -> arquivo superentregas.properties (classpath) -> variáveis
 * de ambiente SUPERENTREGAS_DB_URL / _USUARIO / _SENHA.
 * Elimina a URL, o usuário e a senha escritos no código (e duplicados) nas
 * antigas classes Conexao e ConexaoJDBC.
 */
public final class ConfiguracaoBanco {

    private static final String ARQUIVO = "/superentregas.properties";

    private final String url;
    private final String usuario;
    private final String senha;

    public ConfiguracaoBanco(String url, String usuario, String senha) {
        this.url = url;
        this.usuario = usuario;
        this.senha = senha;
    }

    public static ConfiguracaoBanco carregar() {
        Properties p = new Properties();
        p.setProperty("db.url", "jdbc:mysql://localhost:3306/superentregas?serverTimezone=UTC");
        p.setProperty("db.usuario", "root");
        p.setProperty("db.senha", "");
        try (InputStream in = ConfiguracaoBanco.class.getResourceAsStream(ARQUIVO)) {
            if (in != null) {
                p.load(new java.io.InputStreamReader(in, java.nio.charset.StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            throw new exception.PersistenciaException("Não foi possível ler " + ARQUIVO, e);
        }
        return new ConfiguracaoBanco(
                ambiente("SUPERENTREGAS_DB_URL", p.getProperty("db.url")),
                ambiente("SUPERENTREGAS_DB_USUARIO", p.getProperty("db.usuario")),
                ambiente("SUPERENTREGAS_DB_SENHA", p.getProperty("db.senha")));
    }

    private static String ambiente(String nome, String padrao) {
        String v = System.getenv(nome);
        return (v == null || v.isEmpty()) ? padrao : v;
    }

    public String getUrl() { return url; }
    public String getUsuario() { return usuario; }
    public String getSenha() { return senha; }
}
