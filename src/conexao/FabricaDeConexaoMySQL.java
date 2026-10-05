package conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Única classe que conhece DriverManager. Não exibe diálogos nem devolve null:
 * em caso de falha propaga a SQLException, que as DAOs convertem em
 * PersistenciaException (a antiga Conexao.conectar() mostrava JOptionPane e
 * devolvia null, causando NullPointerException nas DAOs).
 */
public class FabricaDeConexaoMySQL implements FabricaDeConexao {

    private final ConfiguracaoBanco configuracao;

    public FabricaDeConexaoMySQL(ConfiguracaoBanco configuracao) {
        this.configuracao = configuracao;
    }

    @Override
    public Connection criarConexao() throws SQLException {
        return DriverManager.getConnection(
                configuracao.getUrl(), configuracao.getUsuario(), configuracao.getSenha());
    }
}
