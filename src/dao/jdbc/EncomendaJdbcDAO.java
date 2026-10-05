package dao.jdbc;

import conexao.FabricaDeConexao;
import dao.EncomendaDAO;
import exception.PersistenciaException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Optional;
import model.Encomenda;

public class EncomendaJdbcDAO implements EncomendaDAO {

    private final FabricaDeConexao fabrica;

    public EncomendaJdbcDAO(FabricaDeConexao fabrica) {
        this.fabrica = fabrica;
    }

    @Override
    public Encomenda inserir(Encomenda e) {
        String sql = "INSERT INTO encomenda (codigo_postal, peso, encomenda_idusuario) VALUES (?, ?, ?)";
        try (Connection c = fabrica.criarConexao();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, e.getCodigoPostal());
            ps.setBigDecimal(2, e.getPeso());
            if (e.getIdUsuario() == null) {
                ps.setNull(3, Types.INTEGER);
            } else {
                ps.setInt(3, e.getIdUsuario());
            }
            ps.executeUpdate();
            try (ResultSet chaves = ps.getGeneratedKeys()) {
                return chaves.next() ? e.comId(chaves.getInt(1)) : e;
            }
        } catch (SQLException ex) {
            throw new PersistenciaException("Erro ao salvar a encomenda.", ex);
        }
    }

    @Override
    public Optional<Encomenda> buscarPorCodigoPostal(String codigoPostal) {
        String sql = "SELECT idEncomenda, codigo_postal, peso, encomenda_idusuario "
                   + "FROM encomenda WHERE codigo_postal = ?";
        try (Connection c = fabrica.criarConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, codigoPostal);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                int idUsuario = rs.getInt("encomenda_idusuario");
                Integer dono = rs.wasNull() ? null : idUsuario;
                return Optional.of(new Encomenda(
                        rs.getInt("idEncomenda"),
                        rs.getString("codigo_postal"),
                        rs.getBigDecimal("peso"),
                        dono));
            }
        } catch (SQLException ex) {
            throw new PersistenciaException("Erro ao consultar a encomenda.", ex);
        }
    }
}
