package service;

import dao.EncomendaDAO;
import dao.UsuarioDAO;
import exception.RegraDeNegocioException;
import java.math.BigDecimal;
import model.Encomenda;
import model.Usuario;
import util.CodigoPostal;
import util.Cpf;
import util.Peso;

/** Caso de uso: registrar encomenda associada a um cliente pelo CPF. */
public class EncomendaService {

    private final EncomendaDAO encomendaDAO;
    private final UsuarioDAO usuarioDAO;

    public EncomendaService(EncomendaDAO encomendaDAO, UsuarioDAO usuarioDAO) {
        this.encomendaDAO = encomendaDAO;
        this.usuarioDAO = usuarioDAO;
    }

    public Encomenda cadastrar(String codigoPostal, BigDecimal peso, String cpfCliente) {
        String codigo = CodigoPostal.normalizar(codigoPostal);
        BigDecimal pesoValido = Peso.normalizar(peso);
        Usuario cliente = usuarioDAO.buscarPorCpf(Cpf.formatar(cpfCliente))
                .orElseThrow(() -> new RegraDeNegocioException("CPF não cadastrado no sistema."));
        if (encomendaDAO.buscarPorCodigoPostal(codigo).isPresent()) {
            throw new RegraDeNegocioException("Já existe uma encomenda com o código " + codigo + ".");
        }
        return encomendaDAO.inserir(Encomenda.nova(codigo, pesoValido, cliente.getId()));
    }
}
