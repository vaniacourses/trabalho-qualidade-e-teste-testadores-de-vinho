# WaiterApp

**Disciplina:** TCC00346 — Qualidade e Teste de Software | UFF — 2026/1

---

## Descricao do Sistema

O **WaiterApp** e uma API REST de gerenciamento de pedidos para restaurantes, com frontend Angular embutido. O sistema permite que garcons registrem e acompanhem pedidos de clientes, consultem o cardapio, gerenciem itens e processem pagamentos.

### Funcionalidades principais

| Modulo | Descricao |
|---|---|
| **Pedido** | Criacao, atualizacao, consulta e exclusao de pedidos; calculo automatico do preco total |
| **Cliente** | Cadastro de clientes, consulta por CPF, historico de pedidos |
| **Garcom** | Cadastro e gerenciamento de garcons vinculados a pedidos |
| **Cardapio** | Criacao e manutencao de cardapios com lista de itens |
| **Item (Prato/Bebida)** | Itens do menu com heranca: Prato (com ingredientes e calorias) e Bebida (com volume) |
| **ItemPedido** | Associacao entre pedido e item, com quantidade e calculo de subtotal |
| **Pagamento** | Suporte a pagamento com cartao e dinheiro (polimorfismo) |

### Tecnologias do sistema

- **Linguagem:** Java 11 (runtime Docker; nivel de compilacao `source`/`target` 9 no `pom.xml`)
- **Framework:** Spring Boot 2.7.1
- **Persistencia:** Spring Data JPA / Hibernate + PostgreSQL
- **Frontend:** Angular (bundled em `src/main/resources/static/`)
- **Documentacao da API:** springdoc-openapi 1.7.0
- **Build:** Maven
- **Containerizacao:** Docker + Docker Compose

### Modulos testados neste trabalho

Os modulos selecionados para teste incluem as classes com logica de negocio nao trivial (calculos, transicoes de estado, tratamento de excecoes) e tambem as entidades de dominio:

- `PedidoService` — logica de criacao de pedido com laco sobre itens e calculo de total
- `Pedido` — calculo de preco total via stream, transicao de estado (`fecharPedido`)
- `ItemPedido` / `ItemPedidoPK` — calculo de subtotal (quantidade x preco) e chave composta
- `Prato` — soma de calorias dos ingredientes via stream
- `ItemService`, `ClienteService`, `GarcomService`, `CardapioService` — CRUD com tratamento de excecoes
- `Pagamento`, `PagamentoComCartao`, `PagamentoComDinheiro` — hierarquia de pagamento com polimorfismo
- Entidades e DTOs (`Cliente`, `Garcom`, `Cardapio`, `Item`, `Bebida`, `ItemDTO`, `PedidoDTO`) — construtores, getters/setters, `equals`/`hashCode`
- `ObjectNotFoundException` — excecao customizada

---

## Artefatos da Entrega 1 (27/04/2026)

> Todos os artefatos estao na branch `main` deste repositorio.

### 1. Plano de Teste

| Artefato | Link |
|---|---|
| Plano de Teste (Markdown) | [`docs/plano-de-teste.md`](docs/plano-de-teste.md) |

O plano contem: escopo, abordagem, tecnicas, ferramentas, casos de teste por classe, criterios de entrada/saida e bugs documentados.

### 2. Medidas de Qualidade (ISO/IEC 25010)

| Artefato | Link |
|---|---|
| Medidas de Qualidade — ISO/IEC 25010 (Markdown) | [`docs/medidas-iso-25010.md`](docs/medidas-iso-25010.md) |

O documento define as metas de qualidade dos atributos da norma ISO/IEC 25010 para o WaiterApp, com escala de prioridade, medidas esperadas e justificativas por subcaracteristica (funcionalidade, desempenho, usabilidade da interface web, seguranca, entre outras).

### 3. Testes Manuais e Casos de Teste (Testlink)

| Artefato | Link |
|---|---|
| Documento completo de Testes Manuais (QA) | [Google Docs](https://docs.google.com/document/d/1kNf6tNtkxJ-7kMwsXe0t5jLLEZdVhzWCr9jAtzic4jI/edit?usp=sharing) |
| Caso de teste: **Adicionar** item ao carrinho | [`docs/caso_de_teste_add_item_carrinho.pdf`](docs/caso_de_teste_add_item_carrinho.pdf) |
| Caso de teste: **Remover** item do carrinho | [`docs/caso_de_teste_rem_item_carrinho.pdf`](docs/caso_de_teste_rem_item_carrinho.pdf) |

---

### 4. Codigo-Fonte Original

| Modulo | Link |
|---|---|
| Codigo principal (todos os modulos) | [`src/main/java/com/example/waiterapp/`](src/main/java/com/example/waiterapp/) |
| Pedido (entidade + servico + repositorio) | [`src/main/java/com/example/waiterapp/pedido/`](src/main/java/com/example/waiterapp/pedido/) |
| Cliente | [`src/main/java/com/example/waiterapp/cliente/`](src/main/java/com/example/waiterapp/cliente/) |
| Garcom | [`src/main/java/com/example/waiterapp/garcom/`](src/main/java/com/example/waiterapp/garcom/) |
| Cardapio | [`src/main/java/com/example/waiterapp/cardapio/`](src/main/java/com/example/waiterapp/cardapio/) |
| Item / Prato / Bebida | [`src/main/java/com/example/waiterapp/item/`](src/main/java/com/example/waiterapp/item/) |
| ItemPedido | [`src/main/java/com/example/waiterapp/itempedido/`](src/main/java/com/example/waiterapp/itempedido/) |
| Pagamento | [`src/main/java/com/example/waiterapp/pagamento/`](src/main/java/com/example/waiterapp/pagamento/) |
| Ingrediente | [`src/main/java/com/example/waiterapp/ingrediente/`](src/main/java/com/example/waiterapp/ingrediente/) |
| Enums (Estado) | [`src/main/java/com/example/waiterapp/enums/Estado.java`](src/main/java/com/example/waiterapp/enums/Estado.java) |
| Excecoes customizadas | [`src/main/java/com/example/waiterapp/exceptions/`](src/main/java/com/example/waiterapp/exceptions/) |

---

### 5. Testes Unitarios Automatizados

**Ferramentas utilizadas nos testes:**

| Ferramenta | Versao | Finalidade |
|---|---|---|
| **JUnit 5** (JUnit Jupiter) | 5.8.x | Framework principal de testes unitarios |
| **Mockito** | 4.x | Criacao de mocks e verificacao de comportamento |
| **AssertJ** | 3.x | Assertions fluentes |
| **Spring Boot Test** | 2.7.1 | Infraestrutura de testes Spring (inclui JUnit + Mockito) |
| **H2 Database** | embutido | Banco em memoria para testes de contexto Spring |
| **Maven Surefire Plugin** | embutido | Execucao dos testes no ciclo `mvn test` |

**Como executar:**

```bash
mvn test
```

**Arquivos de teste unitario:**

| Arquivo | Classe Testada | N. de Testes | Link Direto |
|---|---|---|---|
| `PedidoTest.java` | `Pedido` | 27 | [`pedido/PedidoTest.java`](src/test/java/com/example/waiterapp/pedido/PedidoTest.java) |
| `PedidoServiceTest.java` | `PedidoService` | 20 | [`pedido/PedidoServiceTest.java`](src/test/java/com/example/waiterapp/pedido/PedidoServiceTest.java) |
| `PedidoDTOTest.java` | `PedidoDTO` | 12 | [`pedido/PedidoDTOTest.java`](src/test/java/com/example/waiterapp/pedido/PedidoDTOTest.java) |
| `ItemPedidoTest.java` | `ItemPedido` | 12 | [`itempedido/ItemPedidoTest.java`](src/test/java/com/example/waiterapp/itempedido/ItemPedidoTest.java) |
| `ItemPedidoPKTest.java` | `ItemPedidoPK` | 14 | [`itempedido/ItemPedidoPKTest.java`](src/test/java/com/example/waiterapp/itempedido/ItemPedidoPKTest.java) |
| `PratoTest.java` | `Prato` | 14 | [`item/prato/PratoTest.java`](src/test/java/com/example/waiterapp/item/prato/PratoTest.java) |
| `BebidaTest.java` | `Bebida` | 7 | [`item/bebida/BebidaTest.java`](src/test/java/com/example/waiterapp/item/bebida/BebidaTest.java) |
| `ItemTest.java` | `Item` | 15 | [`item/ItemTest.java`](src/test/java/com/example/waiterapp/item/ItemTest.java) |
| `ItemServiceTest.java` | `ItemService` | 20 | [`item/ItemServiceTest.java`](src/test/java/com/example/waiterapp/item/ItemServiceTest.java) |
| `ItemDTOTest.java` | `ItemDTO` | 5 | [`item/ItemDTOTest.java`](src/test/java/com/example/waiterapp/item/ItemDTOTest.java) |
| `ClienteTest.java` | `Cliente` | 14 | [`cliente/ClienteTest.java`](src/test/java/com/example/waiterapp/cliente/ClienteTest.java) |
| `ClienteServiceTest.java` | `ClienteService` | 23 | [`cliente/ClienteServiceTest.java`](src/test/java/com/example/waiterapp/cliente/ClienteServiceTest.java) |
| `GarcomTest.java` | `Garcom` | 14 | [`garcom/GarcomTest.java`](src/test/java/com/example/waiterapp/garcom/GarcomTest.java) |
| `GarcomServiceTest.java` | `GarcomService` | 17 | [`garcom/GarcomServiceTest.java`](src/test/java/com/example/waiterapp/garcom/GarcomServiceTest.java) |
| `CardapioTest.java` | `Cardapio` | 14 | [`cardapio/CardapioTest.java`](src/test/java/com/example/waiterapp/cardapio/CardapioTest.java) |
| `CardapioServiceTest.java` | `CardapioService` | 18 | [`cardapio/CardapioServiceTest.java`](src/test/java/com/example/waiterapp/cardapio/CardapioServiceTest.java) |
| `PagamentoTest.java` | `Pagamento`, `PagamentoComCartao`, `PagamentoComDinheiro` | 27 | [`pagamento/PagamentoTest.java`](src/test/java/com/example/waiterapp/pagamento/PagamentoTest.java) |
| `ObjectNotFoundExceptionTest.java` | `ObjectNotFoundException` | 3 | [`exceptions/ObjectNotFoundExceptionTest.java`](src/test/java/com/example/waiterapp/exceptions/ObjectNotFoundExceptionTest.java) |

**Total: 277 testes unitarios** (incluindo 1 teste de contexto em `WaiterAppApplicationTests.java`)

### 5.1 Cobertura de Testes (JaCoCo)

Resultados de cobertura do JaCoCo medidos no ambiente do projeto (Java 21) com os 277 testes unitarios + 43 de integracao (E2E excluidos por requererem Chrome):

| Grupo | Classes | Metodos | Linhas | Branches (decisoes) |
|---|---:|---:|---:|---:|
| `com.example.waiterapp` | **97%** (32/33) | **98%** (327/332) | **97%** (873/896) | **100%** (70/70) |

**Cobertura de branches por classe (criterio todas-arestas >= 80%):**

| Classe | Branches cobertos | % Branches | Status |
|---|---|---|---|
| `PedidoService` | 2/2 | **100%** | ✅ |
| `Pedido` | 6/6 | **100%** | ✅ |
| `Item` | 6/6 | **100%** | ✅ |
| `ClienteController` | 4/4 | **100%** | ✅ |
| `ItemPedidoPK` | 10/10 | **100%** | ✅ |
| `Cliente` | 6/6 | **100%** | ✅ |
| `Pagamento` | 6/6 | **100%** | ✅ |
| `Garcom` | 6/6 | **100%** | ✅ |
| `Cardapio` | 6/6 | **100%** | ✅ |
| `Ingrediente` | 6/6 | **100%** | ✅ |

> **Observacao:** A meta de **>= 80% de branches (todas-arestas)** foi atingida no total do projeto com **100% (70/70)**. Os testes de `equals`/`hashCode` nas entidades JPA cobrem todos os ramos condicionais.

| Artefato | Link |
|---|---|
| Evidencias — relatorios JaCoCo (prints) | [`docs/evidencias-jacoco/`](docs/evidencias-jacoco/) |
| Script para regenerar evidencias | [`docs/gerar-evidencias-jacoco.ps1`](docs/gerar-evidencias-jacoco.ps1) |

A pasta `docs/evidencias-jacoco/` contem capturas de tela do relatorio HTML (visao geral, pacotes, classes-chave e sessoes de execucao), alem de um resumo textual das metricas.

**Como visualizar o relatorio (HTML):**

```powershell
.\mvnw.cmd test
start target\site\jacoco\index.html
```

### 5.2 Gate de cobertura minima (80% de branches) no build

O `pom.xml` configura a meta `jacoco:check` exigindo **>= 80% de cobertura de branches (todas-arestas)**. Se a cobertura ficar abaixo desse limite, o build **falha**, evitando regressao na qualidade dos testes ao longo do tempo.

```bash
# Falha o build se a cobertura de branches ficar abaixo de 80%
./mvnw clean verify -Dmaven.test.failure.ignore=true
```

**Cobertura de cenarios em cada classe:**

| Categoria | Exemplos cobertos |
|---|---|
| Happy Path | Operacao bem-sucedida com dados validos |
| Edge Cases | Lista de itens vazia, quantidade zero, preco decimal |
| Negative Cases | ID inexistente lancando excecao, violacao de integridade |
| Boundary Values | Nota minima (1) e maxima (10), quantidade 0 e 1000 |

**Boas praticas aplicadas:**
- Padrao **AAA** (Arrange / Act / Assert) em todos os testes
- **Mockito** isola 100% das dependencias externas (sem banco de dados real)
- Testes independentes entre si (sem `@TestMethodOrder` nem estado compartilhado)
- Nomes no padrao `metodo_cenario_resultadoEsperado`

---

### 6. Configuracao de Teste

| Arquivo | Link | Descricao |
|---|---|---|
| `application.properties` (test) | [`src/test/resources/application.properties`](src/test/resources/application.properties) | Configuracao H2 in-memory para testes Spring |
| `pom.xml` | [`pom.xml`](pom.xml) | Dependencias do projeto (JUnit 5, Mockito, H2, Selenium, JaCoCo com gate de 80% branches, PITest) |

---

## Artefatos da Entrega 2 (17/06/2026)

### 7. Testes de Integracao

Testes com Spring Boot + H2 em memoria, testando a camada Controller → Service → Repository real (MockMvc) e Repository → JPA (`@DataJpaTest`):

| Arquivo | Modulo Testado | Testes | Link |
|---|---|---|---|
| `ClienteIntegrationTest.java` | Cliente (CRUD + busca por CPF) | 9 | [`integration/ClienteIntegrationTest.java`](src/test/java/com/example/waiterapp/integration/ClienteIntegrationTest.java) |
| `CardapioIntegrationTest.java` | Cardapio (CRUD + 404) | 8 | [`integration/CardapioIntegrationTest.java`](src/test/java/com/example/waiterapp/integration/CardapioIntegrationTest.java) |
| `GarcomIntegrationTest.java` | Garcom (CRUD) | 8 | [`integration/GarcomIntegrationTest.java`](src/test/java/com/example/waiterapp/integration/GarcomIntegrationTest.java) |
| `ItemIntegrationTest.java` | Item (CRUD) | 8 | [`integration/ItemIntegrationTest.java`](src/test/java/com/example/waiterapp/integration/ItemIntegrationTest.java) |
| `PedidoIntegrationTest.java` | Pedido (CRUD via API) | 7 | [`integration/PedidoIntegrationTest.java`](src/test/java/com/example/waiterapp/integration/PedidoIntegrationTest.java) |
| `PedidoRepositoryIntegrationTest.java` | PedidoRepository (persistencia + query customizada) | 3 | [`pedido/PedidoRepositoryIntegrationTest.java`](src/test/java/com/example/waiterapp/pedido/PedidoRepositoryIntegrationTest.java) |

**Total de testes de integracao: 43**

**Como executar (unitarios + integracao):**

O `pom.xml` ja configura o Maven Surefire para **excluir os testes E2E** (pelo grupo `e2e` e pelo pacote `**/e2e/**`). Portanto, `mvn test` roda apenas unitarios e integracao, sem necessidade de Chrome:

```bash
mvn test
```

---

### 8. Testes de Sistema / E2E (Selenium) — Teste Caixa-Preta

**Abordagem: Teste Caixa-Preta (Black-Box)**

Os testes E2E adotam a abordagem de **caixa-preta**: o sistema e tratado como uma caixa fechada, sem acesso ou conhecimento do codigo-fonte interno. Os testes interagem exclusivamente com a interface do usuario (navegador) e com os endpoints HTTP expostos, verificando se as saidas produzidas correspondem ao comportamento esperado para cada entrada.

**Tecnicas de caixa-preta utilizadas:**

| Tecnica | Descricao | Testes que aplicam |
|---|---|---|
| **Teste baseado em caso de uso** | Simula fluxos completos do usuario do inicio ao fim (login → selecionar prato → adicionar ao carrinho → finalizar pedido → excluir pedido) | `ClienteLoginE2ETest`, `ClientePedidoE2ETest`, `FinalizarPedidoE2ETest`, `ExcluirPedidoE2ETest`, `AplicacaoE2ETest` |
| **Particao de equivalencia** | Divide as entradas em classes de equivalencia (validas e invalidas) e testa um representante de cada — ex.: endpoint existente vs. inexistente (404), resposta JSON valida vs. erro 5xx | `PedidoE2ETest`, `ClienteE2ETest`, `CardapioE2ETest`, `GarcomE2ETest` |
| **Analise de valor-limite** | Verifica os limites aceitaveis de requisitos nao funcionais — ex.: tempo de resposta < 3s para APIs, carregamento de pagina < 5s | `PedidoE2ETest`, `ClienteE2ETest`, `GarcomE2ETest` |

**Ferramentas:** Selenium 4.23.0 + WebDriverManager 5.9.3 (Chrome headless). Requerem a aplicacao rodando em `localhost:8080`.

**Arquivos de teste:**

| Arquivo | Cenarios | Tecnica caixa-preta | Testes | Link |
|---|---|---|---|---|
| `AplicacaoE2ETest.java` | Carregamento inicial da aplicacao | Caso de uso | 1 | [`e2e/AplicacaoE2ETest.java`](src/test/java/com/example/waiterapp/e2e/AplicacaoE2ETest.java) |
| `ClienteLoginE2ETest.java` | Login do cliente (nome + CPF) | Caso de uso | 1 | [`e2e/ClienteLoginE2ETest.java`](src/test/java/com/example/waiterapp/e2e/ClienteLoginE2ETest.java) |
| `ClientePedidoE2ETest.java` | Adicionar prato ao carrinho | Caso de uso | 1 | [`e2e/ClientePedidoE2ETest.java`](src/test/java/com/example/waiterapp/e2e/ClientePedidoE2ETest.java) |
| `FinalizarPedidoE2ETest.java` | Finalizar pedido com item no carrinho | Caso de uso | 1 | [`e2e/FinalizarPedidoE2ETest.java`](src/test/java/com/example/waiterapp/e2e/FinalizarPedidoE2ETest.java) |
| `ExcluirPedidoE2ETest.java` | Criar e excluir pedido | Caso de uso | 1 | [`e2e/ExcluirPedidoE2ETest.java`](src/test/java/com/example/waiterapp/e2e/ExcluirPedidoE2ETest.java) |
| `PedidoE2ETest.java` | API de pedidos, tempo de resposta | Particao de equivalencia + Valor-limite | 5 | [`e2e/PedidoE2ETest.java`](src/test/java/com/example/waiterapp/e2e/PedidoE2ETest.java) |
| `ClienteE2ETest.java` | API de clientes, elementos Angular, performance | Particao de equivalencia + Valor-limite | 5 | [`e2e/ClienteE2ETest.java`](src/test/java/com/example/waiterapp/e2e/ClienteE2ETest.java) |
| `CardapioE2ETest.java` | API de cardapios, 404, itens | Particao de equivalencia | 5 | [`e2e/CardapioE2ETest.java`](src/test/java/com/example/waiterapp/e2e/CardapioE2ETest.java) |
| `GarcomE2ETest.java` | API de garcons, raiz, performance | Particao de equivalencia + Valor-limite | 4 | [`e2e/GarcomE2ETest.java`](src/test/java/com/example/waiterapp/e2e/GarcomE2ETest.java) |

**Total de testes E2E: 24** (classe base `BaseSeleniumTest.java` compartilhada)

**Como executar os testes E2E:**

Como o `pom.xml` exclui os E2E por padrao (grupo `e2e` e pacote `**/e2e/**`), e necessario sobrescrever ambos os excludes para roda-los:

```bash
# 1. Subir a aplicacao
docker-compose up -d

# 2. Executar apenas os testes E2E (anulando os excludes do Surefire)
mvn test -Dsurefire.excludes='' -Dsurefire.excludedGroups='' -Dtest='com.example.waiterapp.e2e.*'
```

### 8.1 Testes de Requisitos Nao Funcionais

Alem dos requisitos funcionais, os testes de sistema (E2E) verificam **requisitos nao funcionais**, mapeados para subcaracteristicas da ISO/IEC 25010. O atributo principal coberto e a **eficiencia de desempenho** (comportamento temporal).

| ID | Requisito Nao Funcional | Subcaracteristica (ISO 25010) | Metodo de teste | Criterio de Aceite |
|---|---|---|---|---|
| RNF-01 | Tempo de carregamento da pagina | Eficiencia de desempenho → Comportamento temporal | `paginaPrincipal_tempoCarregamento_deveSerAceitavel` (`PedidoE2ETest`) | < 5 segundos |
| RNF-02 | Tempo de resposta `GET /api/clientes` | Eficiencia de desempenho → Comportamento temporal | `apiClientes_tempoResposta_deveSerAceitavel` (`ClienteE2ETest`) | < 3 segundos |
| RNF-03 | Tempo de resposta `GET /api/garcons` | Eficiencia de desempenho → Comportamento temporal | `apiGarcons_tempoResposta_deveSerAceitavel` (`GarcomE2ETest`) | < 3 segundos |
| RNF-04 | Ausencia de erros 5xx | Confiabilidade → Disponibilidade | `apiPedidos_semDados_naoRetornaErro500`, `apiGarcons_getEndpoint_naoRetornaErro5xx`, `apiClientes_getEndpoint_naoRetornaErro5xx` | Nenhum erro de servidor |
| RNF-05 | Frontend carrega com backend | Compatibilidade → Coexistencia | `aplicativo_paginaPrincipal_contemElementoAngular` (`ClienteE2ETest`) | Elementos Angular presentes na pagina |

---

### 9. Teste Estrutural / Caixa-Branca (JaCoCo) — Criterio Todas-Arestas

**Abordagem: Teste Caixa-Branca (White-Box)**

Diferente do teste caixa-preta (que nao conhece a implementacao), o **teste caixa-branca** (ou teste estrutural) utiliza o conhecimento do codigo-fonte para derivar os casos de teste. O objetivo e garantir que a estrutura interna do programa — seus caminhos, decisoes e ramificacoes — seja exercitada adequadamente.

**Criterio adotado: Todas-Arestas (Branch Coverage / All-Edges)**

O criterio **todas-arestas** exige que cada aresta do grafo de fluxo de controle (CFG) seja percorrida ao menos uma vez. Na pratica, isso significa que todo desvio condicional (`if`, `else`, `switch`, `for`, ternarios) deve ter tanto o ramo verdadeiro quanto o falso exercitados pelos testes. Este criterio e mais rigoroso que "todas-instrucoes" (line coverage) pois garante que ambos os lados de cada decisao foram testados.

**Ferramenta de medicao:** JaCoCo 0.8.11 (integrado ao Maven via `jacoco-maven-plugin`). O JaCoCo instrumenta o bytecode e mede a metrica "Branches", que corresponde diretamente ao criterio todas-arestas. O `pom.xml` impoe um gate de **80% de branches** (ver secao 5.2).

**Resultado:** o criterio todas-arestas atingiu **100% (70/70)** no total do projeto, superando a meta de 80%. A tabela por classe e os totais gerais estao na [secao 5.1](#51-cobertura-de-testes-jacoco).

```powershell
.\mvnw.cmd test
start target\site\jacoco\index.html
```

---

### 10. Teste de Mutacao (PITest)

Ferramenta: **PITest 1.15.3** com plugin JUnit 5.

Classes-alvo: `Pedido`, `PedidoService`, `ItemService`, `Pagamento`, `PagamentoComCartao`, `PagamentoComDinheiro`.

| Metrica | Valor |
|---|---|
| Mutantes gerados | 62 |
| Mutantes mortos | 59 (95%) |
| Test Strength | **100%** |

**Como executar:**

```bash
mvn pitest:mutationCoverage
# Relatorio em: target/pit-reports/index.html
```

---

### 11. Inspecao de Codigo (SonarCloud) — Analise Estatica

**Metodo: Analise Estatica de Codigo (Static Analysis)**

O SonarCloud/SonarQube utiliza a tecnica de **analise estatica**, que examina o codigo-fonte *sem executa-lo*. A ferramenta percorre a AST (Abstract Syntax Tree) do codigo e aplica um conjunto de regras pre-definidas para detectar:

- **Bugs** — defeitos que podem causar comportamento incorreto em tempo de execucao
- **Vulnerabilidades** — falhas de seguranca (ex.: SQL injection, XSS, credenciais hardcoded)
- **Code Smells** — problemas de manutenibilidade (complexidade ciclomatica alta, duplicacao, nomes ruins)
- **Hotspots de seguranca** — trechos que exigem revisao manual

A analise estatica complementa os testes dinamicos (unitarios, integracao, E2E) pois detecta problemas que testes podem nao exercitar — como codigo morto, tratamento de excecoes ausente ou violacoes de boas praticas.

| Artefato | Link |
|---|---|
| Evidencias — inspecao SonarQube (apresentacao) | [`docs/WaiterApp_SonarQube.pptx`](docs/WaiterApp_SonarQube.pptx) |

**Como executar a analise localmente:**

```bash
mvn sonar:sonar \
  -Dsonar.projectKey=SEU_PROJECT_KEY \
  -Dsonar.organization=SEU_ORG \
  -Dsonar.host.url=https://sonarcloud.io \
  -Dsonar.login=SEU_TOKEN
```

---

### 12. ISO 25010 — Atributos de Qualidade

| Artefato | Link |
|---|---|
| Medidas de Qualidade — ISO/IEC 25010 | [`docs/medidas-iso-25010.md`](docs/medidas-iso-25010.md) |

---

## Resumo de Testes

| Tipo | Quantidade |
|---|---:|
| Testes unitarios | 277 |
| Testes de integracao | 43 |
| Testes E2E (Selenium) | 24 |
| **Total** | **344** |

---

## Estrutura do Repositorio

```
waiterapp/
├── docs/
│   ├── plano-de-teste.md                       # Plano de Teste
│   ├── medidas-iso-25010.md                     # Medidas de Qualidade ISO/IEC 25010
│   ├── evidencias-jacoco/                       # Prints do relatorio JaCoCo
│   ├── gerar-evidencias-jacoco.ps1              # Script para regenerar evidencias JaCoCo
│   ├── WaiterApp_SonarQube.pptx                 # Evidencias da inspecao SonarQube
│   ├── caso_de_teste_add_item_carrinho.pdf      # Caso de teste manual — adicionar item
│   └── caso_de_teste_rem_item_carrinho.pdf      # Caso de teste manual — remover item
├── src/
│   ├── main/
│   │   ├── java/com/example/waiterapp/
│   │   │   ├── cardapio/                        # Cardapio (entidade, servico, repositorio, DTO, controller)
│   │   │   ├── cliente/                         # Cliente
│   │   │   ├── garcom/                          # Garcom
│   │   │   ├── ingrediente/                     # Ingrediente
│   │   │   ├── item/                            # Item base + bebida/ + prato/
│   │   │   ├── itempedido/                      # ItemPedido (chave composta)
│   │   │   ├── pagamento/                       # Pagamento (pagamentocomcartao/ e pagamentocomdinheiro/)
│   │   │   ├── pedido/                          # Pedido
│   │   │   ├── config/                          # Configuracao Swagger
│   │   │   ├── enums/                           # Estado do pedido
│   │   │   └── exceptions/                      # ObjectNotFoundException
│   │   └── resources/
│   │       ├── application.properties           # Config PostgreSQL
│   │       └── static/                          # Frontend Angular (bundled)
│   └── test/java/com/example/waiterapp/
│       ├── cardapio/{CardapioServiceTest, CardapioTest}.java
│       ├── cliente/{ClienteServiceTest, ClienteTest}.java
│       ├── garcom/{GarcomServiceTest, GarcomTest}.java
│       ├── item/{ItemServiceTest, ItemTest, ItemDTOTest}.java
│       ├── item/bebida/BebidaTest.java
│       ├── item/prato/PratoTest.java
│       ├── itempedido/{ItemPedidoTest, ItemPedidoPKTest}.java
│       ├── pagamento/PagamentoTest.java
│       ├── pedido/{PedidoTest, PedidoServiceTest, PedidoDTOTest, PedidoRepositoryIntegrationTest}.java
│       ├── exceptions/ObjectNotFoundExceptionTest.java
│       ├── integration/                         # Testes de integracao (MockMvc + H2)
│       │   ├── ClienteIntegrationTest.java
│       │   ├── CardapioIntegrationTest.java
│       │   ├── GarcomIntegrationTest.java
│       │   ├── ItemIntegrationTest.java
│       │   └── PedidoIntegrationTest.java
│       └── e2e/                                 # Testes E2E (Selenium)
│           ├── BaseSeleniumTest.java
│           ├── AplicacaoE2ETest.java
│           ├── ClienteLoginE2ETest.java
│           ├── ClientePedidoE2ETest.java
│           ├── FinalizarPedidoE2ETest.java
│           ├── ExcluirPedidoE2ETest.java
│           ├── PedidoE2ETest.java
│           ├── ClienteE2ETest.java
│           ├── CardapioE2ETest.java
│           └── GarcomE2ETest.java
├── Dockerfile                                   # Build multi-stage (Java 11)
├── docker-compose.yml                           # PostgreSQL + App
├── pom.xml                                      # JaCoCo (gate 80% branches) + PITest + Selenium
└── .github/ISSUE_TEMPLATE/                      # Templates de issue (bug, feature, custom)
```

---

## Como Executar

```bash
# Clonar o repositorio
git clone <url-do-repositorio>
cd waiterapp

# Executar testes unitarios e de integracao (E2E ja sao excluidos pelo pom; sem Chrome necessario)
mvn test

# Executar os testes E2E (requer Chrome + app rodando em localhost:8080)
mvn test -Dsurefire.excludes='' -Dsurefire.excludedGroups='' -Dtest='com.example.waiterapp.e2e.*'

# Executar a aplicacao completa com Docker (PostgreSQL incluido)
docker-compose up

# Gerar relatorio de cobertura JaCoCo
.\mvnw.cmd test
start target\site\jacoco\index.html

# Regenerar evidencias (prints) do JaCoCo
.\docs\gerar-evidencias-jacoco.ps1

# Validar o gate de 80% de branches
./mvnw clean verify

# Executar teste de mutacao (PITest)
mvn pitest:mutationCoverage
# Relatorio em: target/pit-reports/index.html
```

---

## Historico

| Versao | Data | Descricao |
|---|---|---|
| 1.0 | 2026-04-26 | Entrega 1: testes unitarios e plano de teste |
| 1.1 | 2026-06-12 | Medidas de qualidade ISO/IEC 25010 |
| 2.0 | 2026-06-17 | Entrega 2: integracao, E2E, mutacao, cobertura estrutural, inspecao SonarQube |

---

## Particao de Responsabilidades

| Integrante | Responsabilidade | Status |
| :--- | :--- | :---: |
| **Gabriel Pimenta** | Testes unitarios, Documentacao plano de testes, Reporte de issues, Documentacao do projeto | ✅ |
| **Guilherme Coelho** | Testes manuais WaiterApp, Testes E2E com Selenium, Casos de teste Testlink, Documentacao plano de teste | ✅ |
| **Kauan Christofaro** | Documentacao plano de teste, Indicacao das medidas da ISO 25010, Revisao e melhoria de testes de integracao, Resolucao de issues do SonarQube | ✅ |
| **Rafael Langsch** | Testes de integracao, Cobertura estrutural com JaCoCo, Teste de mutacao com PIT (Pedido/PedidoService), Inspecao de codigo SonarCloud | ✅ |
