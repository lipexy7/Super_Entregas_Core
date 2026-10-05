package dao.jdbc;

import conexao.FabricaDeConexao;
import dao.RastreioDAO;
import exception.PersistenciaException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import model.Movimentacao;
import model.PontoDeControle;

/**
 * Faz apenas a consulta do histórico. Os JOINs com usuario e encomenda que
 * existiam na antiga query foram removidos: cada DAO cuida de uma tabela e a
 * composição do resultado é responsabilidade de RastreioService.
 */
public class RastreioJdbcDAO implements RastreioDAO {

    private final FabricaDeConexao fabrica;

    public RastreioJdbcDAO(FabricaDeConexao fabrica) {
        this.fabrica = fabrica;
    }

    @Override
    public List<Movimentacao> buscarHistorico(int idEncomenda) {
        String sql = "SELECT p.idPonto, p.nome, p.endereco, p.cidade, r.data_hora "
                   + "FROM rastreamento r "
                   + "JOIN pontodecontrole p ON r.rastreamento_idponto = p.idPonto "
                   + "WHERE r.rastreamento_idencomenda = ? "
                   + "ORDER BY r.data_hora DESC";
        List<Movimentacao> historico = new ArrayList<>();
        try (Connection c = fabrica.criarConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idEncomenda);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PontoDeControle ponto = new PontoDeControle(rs.getInt("idPonto"),
                            rs.getString("nome"), rs.getString("endereco"), rs.getString("cidade"));
                    historico.add(new Movimentacao(ponto, rs.getObject("data_hora", LocalDateTime.class)));
                }
            }
            return historico;
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao consultar o histórico de rastreamento.", e);
        }
    }
}
