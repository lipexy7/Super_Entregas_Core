package dao.memoria;

import dao.RastreioDAO;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Movimentacao;
import model.PontoDeControle;

public class RastreioMemoriaDAO implements RastreioDAO {

    private final Map<Integer, List<Movimentacao>> porEncomenda = new HashMap<>();

    public void registrar(int idEncomenda, PontoDeControle ponto, LocalDateTime dataHora) {
        porEncomenda.computeIfAbsent(idEncomenda, k -> new ArrayList<>()).add(new Movimentacao(ponto, dataHora));
    }

    @Override
    public List<Movimentacao> buscarHistorico(int idEncomenda) {
        List<Movimentacao> lista = new ArrayList<>(porEncomenda.getOrDefault(idEncomenda, new ArrayList<>()));
        lista.sort(Comparator.comparing(Movimentacao::getDataHora).reversed());
        return lista;
    }
}
