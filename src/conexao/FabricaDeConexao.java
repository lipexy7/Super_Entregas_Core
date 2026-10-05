package conexao;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Abstração de "onde obter uma conexão". As DAOs dependem desta interface e não
 * de DriverManager/MySQL (Inversão de Dependência). Quem usa a conexão é
 * responsável por fechá-la (try-with-resources).
 */
public interface FabricaDeConexao {

    Connection criarConexao() throws SQLException;
}
