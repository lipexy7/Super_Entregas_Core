# Super Entregas Core

Núcleo de regras de negócio do **Super Entregas**, extraído e refatorado a partir do
sistema desktop (Java Swing + JDBC + MySQL) para ser reutilizado pela versão **web**.
Não existe nenhuma classe de interface gráfica aqui: nada de `javax.swing`, `JOptionPane` ou `JFrame`.

## Arquitetura (camadas)

```
 Swing / Servlets+JSP  (apresentação - fora deste projeto)
          │ chamam
          ▼
 service/   AutenticacaoService · EncomendaService · PontoDeControleService · RastreioService
          │ dependem de INTERFACES
          ▼
 dao/       UsuarioDAO · EncomendaDAO · PontoDeControleDAO · RastreioDAO
   ├─ dao/jdbc/     implementações MySQL (PreparedStatement + try-with-resources)
   └─ dao/memoria/  implementações em memória (testes)
          │ usam
          ▼
 conexao/   FabricaDeConexao (interface) · FabricaDeConexaoMySQL · ConfiguracaoBanco
```

Outros pacotes: `model` (entidades imutáveis e `RastreioResultado`), `exception`
(`RegraDeNegocioException`, `AcessoNegadoException`, `PersistenciaException`),
`util` (`Cpf`, `CodigoPostal`, `Conversor`), `seguranca` (`VerificadorDeSenha`),
`config` (`FabricaDeServicos`, a raiz de composição) e `teste` + `app.Main` (verificações).

## Como executar

1. Importe `superentregas.sql` (está no projeto desktop) no MySQL.
2. Copie `mysql-connector-j-9.6.0.jar` para a pasta `lib/` (veja `lib/LEIA-ME.txt`).
3. Abra a pasta no **NetBeans** (File > Open Project) e use *Run*.
   - Sem argumentos: roda 49 verificações das regras de negócio com DAOs em memória (não precisa de banco).
   - Com `--mysql` (Project Properties > Run > Arguments): inclui um teste de leitura no MySQL real.
4. Banco em outro endereço/usuário? Edite `src/superentregas.properties` ou defina as variáveis de
   ambiente `SUPERENTREGAS_DB_URL`, `SUPERENTREGAS_DB_USUARIO` e `SUPERENTREGAS_DB_SENHA`.

Requer JDK 17 ou superior.

## Uso na versão web (exemplo)

```java
// na inicialização (ServletContextListener)
FabricaDeServicos app = FabricaDeServicos.mysql();
context.setAttribute("app", app);

// em um Servlet de login
try {
    Usuario u = app.autenticacao().autenticar(req.getParameter("cpf"), req.getParameter("senha"));
    req.getSession().setAttribute("usuario", u);
} catch (RegraDeNegocioException e) {
    req.setAttribute("erro", e.getMessage());      // mensagem pronta para o usuário
}
```

## Documentação

O relatório com os princípios SOLID, refatorações e padrões aplicados acompanha a entrega.
O guia para criar o repositório no GitHub está em `docs/GUIA_GITHUB.md`.
