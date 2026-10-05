package dao;

import java.util.List;
import model.Movimentacao;

public interface RastreioDAO {

    /** Histórico completo da encomenda, da movimentação mais recente para a mais antiga. */
    List<Movimentacao> buscarHistorico(int idEncomenda);
}
