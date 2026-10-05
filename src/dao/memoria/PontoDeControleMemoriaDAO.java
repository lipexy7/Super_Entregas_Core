package dao.memoria;

import dao.PontoDeControleDAO;
import java.util.ArrayList;
import java.util.List;
import model.PontoDeControle;

public class PontoDeControleMemoriaDAO implements PontoDeControleDAO {

    private final List<PontoDeControle> pontos = new ArrayList<>();
    private int proximoId = 1;

    public void adicionar(PontoDeControle p) {
        pontos.add(p);
        proximoId = Math.max(proximoId, p.getId() + 1);
    }

    @Override
    public PontoDeControle inserir(PontoDeControle p) {
        PontoDeControle salvo = p.comId(proximoId++);
        pontos.add(salvo);
        return salvo;
    }

    @Override
    public List<PontoDeControle> listarTodos() {
        return new ArrayList<>(pontos);
    }
}
