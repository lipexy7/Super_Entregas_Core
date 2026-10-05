package teste;

import config.FabricaDeServicos;
import exception.PersistenciaException;
import exception.SuperEntregasException;
import model.RastreioResultado;
import model.Usuario;

/**
 * Teste de fumaça (somente leitura) contra o MySQL real, com os dados do
 * superentregas.sql. Executado apenas com o argumento --mysql. Requer o driver
 * mysql-connector-j em lib/ e o banco importado.
 */
public class TesteIntegracaoMySQL {

    private final Verificador v;

    public TesteIntegracaoMySQL(Verificador v) {
        this.v = v;
    }

    public void executar() {
        v.secao("Integração com MySQL (somente leitura)");
        FabricaDeServicos app = FabricaDeServicos.mysql();
        try {
            Usuario u = app.autenticacao().autenticar("123.456.789-09", "cruzeirocabuloso123");
            v.igual("login real no banco", "Felipe Reis", u.getNome());
            v.verdadeiro("perfil de gerente lido do banco", u.isGerente());

            RastreioResultado r = app.rastreio().rastrear("XQTRLPZE92WJMCDAKHSY");
            v.igual("histórico real tem 2 movimentações", 2, r.getHistorico().size());
            v.igual("última localização real", "Casa Grande",
                    r.getUltimaMovimentacao().get().getPonto().getNome());
            v.igual("destinatário real", "Pedro Henrrique", r.getDestinatario().get().getNome());

            v.verdadeiro("lista de pontos real tem ao menos 4", app.pontos().listar().size() >= 4);
        } catch (PersistenciaException e) {
            v.verdadeiro("conexão/consulta ao MySQL: " + e.getMessage()
                    + " (" + e.getCause().getMessage() + ")", false);
        } catch (SuperEntregasException e) {
            v.verdadeiro("regra de negócio inesperada: " + e.getMessage(), false);
        }
    }
}
