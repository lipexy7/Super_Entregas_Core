package dao.jdbc;

import conexao.FabricaDeConexao;
import dao.PontoDeControleDAO;
import exception.PersistenciaException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.PontoDeControle;

public class PontoDeControleJdbcDAO implements PontoDeControleDAO {

    private final FabricaDeConexao fabrica;

    public PontoDeControleJdbcDAO(FabricaDeConexao fabrica) {
        this.fabrica = fabrica;
    }

    @Override
    public PontoDeControle inserir(PontoDeControle p) {
        String sql = "INSERT INTO pontodecontrole (nome, endereco, cidade) VALUES (?, ?, ?)";
        try (Connection c = fabrica.criarConexao();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getNome());
            ps.setString(2, p.getEndereco());
            ps.setString(3, p.getCidade());
            ps.executeUpdate();
            try (ResultSet chaves = ps.getGeneratedKeys()) {
                return chaves.next() ? p.comId(chaves.getInt(1)) : p;
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao salvar o ponto de controle.", e);
        }
    }

    @Override
    public List<PontoDeControle> listarTodos() {
        String sql = "SELECT idPonto, nome, endereco, cidade FROM pontodecontrole ORDER BY cidade, nome";
        List<PontoDeControle> lista = new ArrayList<>();
        try (Connection c = fabrica.criarConexao();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new PontoDeControle(rs.getInt("idPonto"), rs.getString("nome"),
                        rs.getString("endereco"), rs.getString("cidade")));
            }
            return lista;
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao listar os pontos de controle.", e);
        }
    }
}
