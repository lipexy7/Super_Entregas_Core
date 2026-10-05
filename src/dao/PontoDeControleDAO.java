package dao;

import java.util.List;
import model.PontoDeControle;

public interface PontoDeControleDAO {

    PontoDeControle inserir(PontoDeControle ponto);

    List<PontoDeControle> listarTodos();
}
