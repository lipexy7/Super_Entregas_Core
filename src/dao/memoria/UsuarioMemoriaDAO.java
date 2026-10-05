package dao.memoria;

import dao.UsuarioDAO;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import model.Usuario;

/** DAO "falso" em memória: permite testar os services sem MySQL (princípios L e D). */
public class UsuarioMemoriaDAO implements UsuarioDAO {

    private final Map<Integer, Usuario> usuarios = new HashMap<>();
    private final Map<Integer, String> senhas = new HashMap<>();

    public void adicionar(Usuario usuario, String senha) {
        usuarios.put(usuario.getId(), usuario);
        senhas.put(usuario.getId(), senha);
    }

    @Override
    public Optional<Usuario> buscarPorCpf(String cpf) {
        return usuarios.values().stream().filter(u -> u.getCpf().equals(cpf)).findFirst();
    }

    @Override
    public Optional<Usuario> buscarPorId(int id) {
        return Optional.ofNullable(usuarios.get(id));
    }

    @Override
    public Optional<String> buscarSenhaPorCpf(String cpf) {
        return buscarPorCpf(cpf).map(u -> senhas.get(u.getId()));
    }
}
