package dao;

import java.util.Optional;
import model.Usuario;

public interface UsuarioDAO {

    Optional<Usuario> buscarPorCpf(String cpf);

    Optional<Usuario> buscarPorId(int id);

    /** Senha armazenada (usada só pela autenticação, nunca exposta ao model). */
    Optional<String> buscarSenhaPorCpf(String cpf);
}
