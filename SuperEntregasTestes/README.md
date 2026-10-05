# Super Entregas - Testes (JUnit)

Projeto NetBeans de testes do **SuperEntregasCore**. Usa JUnit 4.13.2 e Hamcrest 1.3 (já em `lib/`).
Os testes não acessam banco de dados nem interface gráfica: usam os DAOs em memória do núcleo.

## Como executar no NetBeans
1. File > Open Project > selecione esta pasta (`SuperEntregasTestes`).
2. Menu Run > **Test Project** (Alt+F6) executa todos os testes.
   Ou abra `test/testes/TodosOsTestes.java` > Run File (Shift+F6) para rodar a suíte inteira.
3. Resultado esperado: **92 testes, 0 falhas**.

## O que é testado
| Classe de teste | Alvo | Testes |
|---|---|---|
| CalculadoraDeFreteTest | regra de frete (RF06): limites, arredondamento, erros | 17 |
| CalculadoraDeFreteTabelaTest | tabela peso -> frete (JUnit parametrizado) | 12 |
| PesoTest | validação e arredondamento do peso | 4 |
| CpfTest, CodigoPostalTest, ConversorTest | normalização e validação de entradas | 6 + 5 + 5 |
| SenhaEmTextoPlanoTest | conferência de senha | 4 |
| AutenticacaoServiceTest | login e perfis (RF01) | 10 |
| EncomendaServiceTest | cadastro de encomendas (RF02) | 10 |
| RastreioServiceTest | rastreamento (RF03) | 9 |
| PontoDeControleServiceTest | pontos de controle e permissão de gerente (RF04) | 8 |
| UsuarioTest | modelo não expõe senha | 2 |

## Dependência do núcleo
O núcleo entra como `lib/SuperEntregasCore.jar`. Se você alterar o código do núcleo:
no projeto SuperEntregasCore use **Clean and Build** e copie `dist/SuperEntregasCore.jar` para `lib/` (substituindo).

## Plano de testes
O documento `Plano_de_Testes_Super_Entregas.docx` acompanha a entrega (testes automatizados e manuais).
O passo a passo do versionamento no GitHub está em `docs/GUIA_GITHUB_TESTES.md`.
