package service;

import dao.EncomendaDAO;
import dao.UsuarioDAO;
import exception.RegraDeNegocioException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import model.Encomenda;
import model.Usuario;
import util.CodigoPostal;
import util.Cpf;

/** Caso de uso: registrar encomenda associada a um cliente pelo CPF. */
public class EncomendaService {

    private static final BigDecimal PESO_MAXIMO = new BigDecimal("999.99");   // DECIMAL(5,2)

    private final EncomendaDAO encomendaDAO;
    private final UsuarioDAO usuarioDAO;

    public EncomendaService(EncomendaDAO encomendaDAO, UsuarioDAO usuarioDAO) {
        this.encomendaDAO = encomendaDAO;
        this.usuarioDAO = usuarioDAO;
    }

    public Encomenda cadastrar(String codigoPostal, BigDecimal peso, String cpfCliente) {
        String codigo = CodigoPostal.normalizar(codigoPostal);
        BigDecimal pesoValido = validarPeso(peso);
        Usuario cliente = usuarioDAO.buscarPorCpf(Cpf.formatar(cpfCliente))
                .orElseThrow(() -> new RegraDeNegocioException("CPF não cadastrado no sistema."));
        if (encomendaDAO.buscarPorCodigoPostal(codigo).isPresent()) {
            throw new RegraDeNegocioException("Já existe uma encomenda com o código " + codigo + ".");
        }
        return encomendaDAO.inserir(Encomenda.nova(codigo, pesoValido, cliente.getId()));
    }

    private BigDecimal validarPeso(BigDecimal peso) {
        if (peso == null || peso.signum() <= 0) {
            throw new RegraDeNegocioException("O peso deve ser maior que zero.");
        }
        BigDecimal arredondado = peso.setScale(2, RoundingMode.HALF_UP);
        if (arredondado.signum() <= 0 || arredondado.compareTo(PESO_MAXIMO) > 0) {
            throw new RegraDeNegocioException("O peso deve estar entre 0,01 e " + PESO_MAXIMO + " kg.");
        }
        return arredondado;
    }
}
