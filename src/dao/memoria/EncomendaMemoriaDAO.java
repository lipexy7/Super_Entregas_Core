package dao.memoria;

import dao.EncomendaDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import model.Encomenda;

public class EncomendaMemoriaDAO implements EncomendaDAO {

    private final List<Encomenda> encomendas = new ArrayList<>();
    private int proximoId = 1;

    /** Carrega um registro já com id (dados de teste equivalentes ao superentregas.sql). */
    public void adicionar(Encomenda e) {
        encomendas.add(e);
        proximoId = Math.max(proximoId, e.getId() + 1);
    }

    @Override
    public Encomenda inserir(Encomenda e) {
        Encomenda salva = e.comId(proximoId++);
        encomendas.add(salva);
        return salva;
    }

    @Override
    public Optional<Encomenda> buscarPorCodigoPostal(String codigoPostal) {
        return encomendas.stream().filter(e -> e.getCodigoPostal().equals(codigoPostal)).findFirst();
    }

    public int quantidade() {
        return encomendas.size();
    }
}
