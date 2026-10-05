# Guia: versionar o projeto de testes no repositório já criado

Use o **mesmo repositório** da etapa anterior (Super_Entregas_Core). Na raiz do clone local ficam o
núcleo (build.xml, src, ...) e, agora, a pasta nova `SuperEntregasTestes/`.

1. Copie para o seu clone local:
   - a pasta `SuperEntregasTestes/` inteira (projeto de testes);
   - os 4 arquivos do núcleo que mudaram nesta etapa, nos mesmos caminhos de `src/`:
     `util/Peso.java` (novo), `service/CalculadoraDeFrete.java` (novo),
     `service/EncomendaService.java` e `config/FabricaDeServicos.java` (alterados).
2. Na raiz do clone, rode:

```bash
git status

git add src/util/Peso.java src/service/CalculadoraDeFrete.java src/service/EncomendaService.java src/config/FabricaDeServicos.java
git commit -m "Nucleo: extrai regra de peso (util.Peso) e cria CalculadoraDeFrete (RF06)"

git add SuperEntregasTestes/build.xml SuperEntregasTestes/manifest.mf SuperEntregasTestes/nbproject SuperEntregasTestes/lib SuperEntregasTestes/.gitignore SuperEntregasTestes/src SuperEntregasTestes/README.md
git commit -m "Projeto de testes JUnit: estrutura NetBeans e bibliotecas"

git add SuperEntregasTestes/test/testes SuperEntregasTestes/test/util SuperEntregasTestes/test/seguranca SuperEntregasTestes/test/model SuperEntregasTestes/test/service/CalculadoraDeFreteTest.java SuperEntregasTestes/test/service/CalculadoraDeFreteTabelaTest.java
git commit -m "Testes unitarios: frete, peso, CPF, codigo postal, conversor e senha"

git add SuperEntregasTestes/test/service
git commit -m "Testes de servico: autenticacao, encomenda, rastreio e ponto de controle"

git add SuperEntregasTestes/docs SuperEntregasTestes/Plano_de_Testes_Super_Entregas.docx
git commit -m "Plano de testes e guia de versionamento"

git push
git log --oneline
```

3. Tire os prints (visão geral do repositório mostrando a pasta SuperEntregasTestes, a lista de commits no
   GitHub e o terminal com `git log --oneline`) e coloque na pasta `2_Evidencia_Versionamento` do ZIP.
