package dao;

import java.util.Optional;
import model.Encomenda;

public interface EncomendaDAO {

    /** Persiste e devolve a encomenda com o id gerado. */
    Encomenda inserir(Encomenda encomenda);

    Optional<Encomenda> buscarPorCodigoPostal(String codigoPostal);
}
