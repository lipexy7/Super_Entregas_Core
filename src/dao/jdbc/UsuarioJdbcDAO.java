package dao.jdbc;

import conexao.FabricaDeConexao;
import dao.UsuarioDAO;
import exception.PersistenciaException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import model.Usuario;

public class UsuarioJdbcDAO implements UsuarioDAO {

    private static final String COLUNAS = "idUsuario, nome, email, cpf, is_gerente";

    private final FabricaDeConexao fabrica;

    public UsuarioJdbcDAO(FabricaDeConexao fabrica) {
        this.fabrica = fabrica;
    }

    @Override
    public Optional<Usuario> buscarPorCpf(String cpf) {
        return buscar("SELECT " + COLUNAS + " FROM usuario WHERE cpf = ?", cpf);
    }

    @Override
    public Optional<Usuario> buscarPorId(int id) {
        return buscar("SELECT " + COLUNAS + " FROM usuario WHERE idUsuario = ?", id);
    }

    @Override
    public Optional<String> buscarSenhaPorCpf(String cpf) {
        String sql = "SELECT senha FROM usuario WHERE cpf = ?";
        try (Connection c = fabrica.criarConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, cpf);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.ofNullable(rs.getString("senha")) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao consultar a senha do usuário.", e);
        }
    }

    private Optional<Usuario> buscar(String sql, Object parametro) {
        try (Connection c = fabrica.criarConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setObject(1, parametro);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao consultar usuário.", e);
        }
    }

    private Usuario mapear(ResultSet rs) throws SQLException {
        return new Usuario(
                rs.getInt("idUsuario"),
                rs.getString("nome"),
                rs.getString("email"),
                rs.getString("cpf"),
                rs.getBoolean("is_gerente"));   // NULL no banco => false
    }
}
