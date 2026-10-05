package service;

import dao.EncomendaDAO;
import dao.RastreioDAO;
import dao.UsuarioDAO;
import exception.RegraDeNegocioException;
import java.util.List;
import model.Encomenda;
import model.Movimentacao;
import model.RastreioResultado;
import model.Usuario;
import util.CodigoPostal;

/** Caso de uso: consultar uma encomenda pelo código postal. */
public class RastreioService {

    private final EncomendaDAO encomendaDAO;
    private final UsuarioDAO usuarioDAO;
    private final RastreioDAO rastreioDAO;

    public RastreioService(EncomendaDAO encomendaDAO, UsuarioDAO usuarioDAO, RastreioDAO rastreioDAO) {
        this.encomendaDAO = encomendaDAO;
        this.usuarioDAO = usuarioDAO;
        this.rastreioDAO = rastreioDAO;
    }

    /**
     * Diferente da query única da versão desktop (INNER JOIN em tudo), uma
     * encomenda sem movimentação ou sem dono ainda é encontrada: o resultado
     * apenas vem com histórico vazio / sem destinatário.
     */
    public RastreioResultado rastrear(String codigoPostal) {
        String codigo = CodigoPostal.normalizar(codigoPostal);
        Encomenda encomenda = encomendaDAO.buscarPorCodigoPostal(codigo)
                .orElseThrow(() -> new RegraDeNegocioException("Código postal não encontrado."));
        Usuario destinatario = encomenda.getIdUsuario() == null
                ? null
                : usuarioDAO.buscarPorId(encomenda.getIdUsuario()).orElse(null);
        List<Movimentacao> historico = rastreioDAO.buscarHistorico(encomenda.getId());
        return new RastreioResultado(encomenda, destinatario, historico);
    }
}
