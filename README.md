# WaiterApp

**Disciplina:** TCC00346 — Qualidade e Teste de Software | UFF — 2026/1

---

## Projeto relacionado

Este repositorio contem a documentacao e os testes do **WaiterApp**. O **segundo software** avaliado no trabalho possui um README proprio e esta disponivel no repositorio:

- https://github.com/BielPimentaDev/Trabalho-POO-Simula-o-de-combate

## Descricao do Sistema

O **WaiterApp** e uma API REST de gerenciamento de pedidos para restaurantes, com frontend Angular embutido. O sistema permite que garcons registrem e acompanhem pedidos de clientes, consultem o cardapio, gerenciem itens e processem pagamentos.

### Funcionalidades principaisp

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

- **Linguagem:** Java 9+
- **Framework:** Spring Boot 2.7.1
- **Persistencia:** Spring Data JPA / Hibernate + PostgreSQL
- **Frontend:** Angular (bundled em `src/main/resources/static/`)
- **Documentacao da API:** springdoc-openapi 1.7.0
- **Build:** Maven
- **Containerizacao:** Docker + Docker Compose

### Modulos testados neste trabalho

Os modulos selecionados para teste sao os que contem logica de negocio nao trivial (calculos, transicoes de estado, tratamento de excecoes):

- `PedidoService` — logica de criacao de pedido com laco sobre itens e calculo de total
- `Pedido` — calculo de preco total via stream, transicao de estado (`fecharPedido`)
- `ItemPedido` — calculo de subtotal (quantidade x preco)
- `Prato` — soma de calorias dos ingredientes via stream
- `ItemService`, `ClienteService`, `GarcomService`, `CardapioService` — CRUD com tratamento de excecoes
- `Pagamento`, `PagamentoComCartao`, `PagamentoComDinheiro` — hierarquia de pagamento com polimorfismo

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

### 3. Casos de Teste Manuais

| Artefato | Link |
|---|---|
| Caso de Teste — Adicionar Item ao Carrinho | [`docs/caso_de_teste_add_item_carrinho.pdf`](docs/caso_de_teste_add_item_carrinho.pdf) |
| Caso de Teste — Remover Item do Carrinho | [`docs/caso_de_teste_rem_item_carrinho.pdf`](docs/caso_de_teste_rem_item_carrinho.pdf) |

---

### 4. Codigo-Fonte Original

| Modulo | Link |
|---|---|
| Codigo principal (todos os modulos) | [`src/main/java/com/example/waiterapp/`](src/main/java/com/example/waiterapp/) |
| Pedido (entidade + servico + repositorio) | [`src/main/java/com/example/waiterapp/Pedido/`](src/main/java/com/example/waiterapp/Pedido/) |
| Cliente | [`src/main/java/com/example/waiterapp/Cliente/`](src/main/java/com/example/waiterapp/Cliente/) |
| Garcom | [`src/main/java/com/example/waiterapp/Garcom/`](src/main/java/com/example/waiterapp/Garcom/) |
| Cardapio | [`src/main/java/com/example/waiterapp/Cardapio/`](src/main/java/com/example/waiterapp/Cardapio/) |
| Item / Prato / Bebida | [`src/main/java/com/example/waiterapp/Item/`](src/main/java/com/example/waiterapp/Item/) |
| ItemPedido | [`src/main/java/com/example/waiterapp/ItemPedido/`](src/main/java/com/example/waiterapp/ItemPedido/) |
| Pagamento | [`src/main/java/com/example/waiterapp/Pagamento/`](src/main/java/com/example/waiterapp/Pagamento/) |
| Ingrediente | [`src/main/java/com/example/waiterapp/Ingrediente/`](src/main/java/com/example/waiterapp/Ingrediente/) |
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
| `PedidoTest.java` | `Pedido` | 25 | [`src/test/.../Pedido/PedidoTest.java`](src/test/java/com/example/waiterapp/Pedido/PedidoTest.java) |
| `PedidoServiceTest.java` | `PedidoService` | 20 | [`src/test/.../Pedido/PedidoServiceTest.java`](src/test/java/com/example/waiterapp/Pedido/PedidoServiceTest.java) |
| `ItemPedidoTest.java` | `ItemPedido` | 15 | [`src/test/.../ItemPedido/ItemPedidoTest.java`](src/test/java/com/example/waiterapp/ItemPedido/ItemPedidoTest.java) |
| `PratoTest.java` | `Prato` | 14 | [`src/test/.../Item/Prato/PratoTest.java`](src/test/java/com/example/waiterapp/Item/Prato/PratoTest.java) |
| `ItemServiceTest.java` | `ItemService` | 20 | [`src/test/.../Item/ItemServiceTest.java`](src/test/java/com/example/waiterapp/Item/ItemServiceTest.java) |
| `ClienteServiceTest.java` | `ClienteService` | 23 | [`src/test/.../Cliente/ClienteServiceTest.java`](src/test/java/com/example/waiterapp/Cliente/ClienteServiceTest.java) |
| `GarcomServiceTest.java` | `GarcomService` | 17 | [`src/test/.../Garcom/GarcomServiceTest.java`](src/test/java/com/example/waiterapp/Garcom/GarcomServiceTest.java) |
| `CardapioServiceTest.java` | `CardapioService` | 18 | [`src/test/.../Cardapio/CardapioServiceTest.java`](src/test/java/com/example/waiterapp/Cardapio/CardapioServiceTest.java) |
| `PagamentoTest.java` | `Pagamento`, `PagamentoComCartao`, `PagamentoComDinheiro` | 24 | [`src/test/.../Pagamento/PagamentoTest.java`](src/test/java/com/example/waiterapp/Pagamento/PagamentoTest.java) |

**Total: 177 testes unitarios** (incluindo 1 teste de contexto em `WaiterAppApplicationTests.java`)

### 5.1 Cobertura de Testes (JaCoCo)

Resultados de cobertura do JaCoCo (testes unitarios + integracao; E2E excluidos por requererem Chrome):

| Grupo | Classes | Metodos | Linhas | Branches (decisoes) |
|---|---:|---:|---:|---:|
| `com.example.waiterapp` | **97%** (32/33) | **79%** (239/302) | **78.6%** (609/775) | **38.3%** (23/60) |

**Cobertura de branches por classe (classes com logica nao-trivial):**

| Classe | Branches cobertos | % Branches | Status |
|---|---|---|---|
| `PedidoService` | 4/4 | **100%** | ✅ |
| `Pedido` | 5/6 | **83.3%** | ✅ |
| `Pagamento` | 5/6 | **83.3%** | ✅ |
| `ClienteController` | 3/4 | **75%** | ⚠️ |
| `Ingrediente` | 3/6 | **50%** | ⚠️ |
| `Item` | 1/6 | **16.7%** | ❌ |
| `Cardapio` | 1/6 | **16.7%** | ❌ |
| `Cliente` | 1/6 | **16.7%** | ❌ |
| `Garcom` | 0/6 | **0%** | ❌ |
| `ItemPedidoPK` | 0/10 | **0%** | ❌ |

> **Observacao:** Os branches baixos nas entidades (`Item`, `Cardapio`, `Cliente`, `Garcom`, `ItemPedidoPK`) sao gerados pelo Hibernate/JPA (metodos `equals`/`hashCode` com comparacoes de `null`). As classes de logica de negocio relevantes (`PedidoService`, `Pedido`, `Pagamento`) atingem o criterio de >=80% de branches (todas-arestas).

**Como visualizar o relatorio (HTML):**

```bash
./mvnw clean test jacoco:report -Dmaven.test.failure.ignore=true -Dexcludes=**/e2e/**
# Abrir: target/site/jacoco/index.html
```

### 5.2 Sugestao: exigir cobertura minima (80%) via CI/CD

Para evitar regressao na qualidade dos testes (e incentivar a cobertura de branches/linhas ao longo do tempo), uma boa pratica e configurar o pipeline de CI/CD para **falhar o build** quando a cobertura ficar abaixo de um limite minimo.

**Sugestao de politica (exemplo):**

| Metrica | Limite minimo sugerido |
|---|---:|
| Linhas (line coverage) | 80% |
| Branches (branch coverage) | 80% |

**Como implementar (alto nivel):**

- Adicionar o **JaCoCo** no `pom.xml` com a meta `check`, definindo os limites desejados.
- No CI (ex.: GitHub Actions/GitLab CI), executar `mvn clean verify`.
- Se a cobertura ficar abaixo do limite, o job falha e a PR nao pode ser mesclada ate corrigir ou justificar.

> Observacao: como a metrica de *branches* costuma ser a mais dificil, um plano realista pode ser adotar 80% primeiro para **linhas** e aumentar gradualmente o limite de **branches** (por exemplo: 40% → 60% → 80%) a cada iteracao.

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
| `pom.xml` | [`pom.xml`](pom.xml) | Dependencias do projeto (JUnit 5, Mockito, H2, Selenium, JaCoCo, PITest) |

---

## Artefatos da Entrega 2 (17/06/2026)

### 7. Testes de Integracao

Testes com Spring Boot + H2 em memoria, testando a camada Controller → Service → Repository real (MockMvc) e Repository → JPA (`@DataJpaTest`):

| Arquivo | Modulo Testado | Testes | Link |
|---|---|---|---|
| `ClienteIntegrationTest.java` | Cliente (CRUD + busca por CPF) | 8 | [`src/test/.../integration/ClienteIntegrationTest.java`](src/test/java/com/example/waiterapp/integration/ClienteIntegrationTest.java) |
| `CardapioIntegrationTest.java` | Cardapio (CRUD + 404) | 8 | [`src/test/.../integration/CardapioIntegrationTest.java`](src/test/java/com/example/waiterapp/integration/CardapioIntegrationTest.java) |
| `PedidoRepositoryIntegrationTest.java` | PedidoRepository (persistencia + query customizada) | 3 | [`src/test/.../Pedido/PedidoRepositoryIntegrationTest.java`](src/test/java/com/example/waiterapp/Pedido/PedidoRepositoryIntegrationTest.java) |

**Total de testes de integracao: 19**

**Como executar (excluindo E2E):**

```bash
mvn test -Dgroups='!e2e'
```

---

### 8. Testes de Sistema / E2E (Selenium)

Testes de ponta a ponta com Selenium 4 + WebDriverManager. Requerem a aplicacao rodando em `localhost:8080`.

| Arquivo | Cenarios | Testes | Link |
|---|---|---|---|
| `AplicacaoE2ETest.java` | Carregamento inicial da aplicacao | 1 | [`src/test/.../e2e/AplicacaoE2ETest.java`](src/test/java/com/example/waiterapp/e2e/AplicacaoE2ETest.java) |
| `ClienteLoginE2ETest.java` | Login do cliente (nome + CPF) | 1 | [`src/test/.../e2e/ClienteLoginE2ETest.java`](src/test/java/com/example/waiterapp/e2e/ClienteLoginE2ETest.java) |
| `ClientePedidoE2ETest.java` | Adicionar prato ao carrinho | 1 | [`src/test/.../e2e/ClientePedidoE2ETest.java`](src/test/java/com/example/waiterapp/e2e/ClientePedidoE2ETest.java) |
| `FinalizarPedidoE2ETest.java` | Finalizar pedido com item no carrinho | 1 | [`src/test/.../e2e/FinalizarPedidoE2ETest.java`](src/test/java/com/example/waiterapp/e2e/FinalizarPedidoE2ETest.java) |
| `ExcluirPedidoE2ETest.java` | Criar e excluir pedido | 1 | [`src/test/.../e2e/ExcluirPedidoE2ETest.java`](src/test/java/com/example/waiterapp/e2e/ExcluirPedidoE2ETest.java) |
| `PedidoE2ETest.java` | API de pedidos, tempo de resposta | 5 | [`src/test/.../e2e/PedidoE2ETest.java`](src/test/java/com/example/waiterapp/e2e/PedidoE2ETest.java) |
| `ClienteE2ETest.java` | API de clientes, elementos Angular, performance | 5 | [`src/test/.../e2e/ClienteE2ETest.java`](src/test/java/com/example/waiterapp/e2e/ClienteE2ETest.java) |
| `CardapioE2ETest.java` | API de cardapios, 404, itens | 5 | [`src/test/.../e2e/CardapioE2ETest.java`](src/test/java/com/example/waiterapp/e2e/CardapioE2ETest.java) |
| `GarcomE2ETest.java` | API de garcons, Swagger UI, performance | 5 | [`src/test/.../e2e/GarcomE2ETest.java`](src/test/java/com/example/waiterapp/e2e/GarcomE2ETest.java) |

**Total de testes E2E: 25** (classe base `BaseSeleniumTest.java` compartilhada)

**Como executar os testes E2E:**

```bash
# 1. Subir a aplicacao
docker-compose up -d

# 2. Executar apenas os testes E2E
mvn test -Dgroups=e2e
```

---

### 9. Cobertura Estrutural (JaCoCo — Tecnica Todas-Arestas)

Resultados medidos em 2026-06-14 apos todas as melhorias da Entrega 2 (E2E contabilizados separadamente):

**Totais gerais:**

| Metrica | Resultado |
|---|---:|
| Classes | **97%** (32/33) |
| Metodos | **79.1%** (239/302) |
| Linhas | **78.6%** (609/775) |
| Branches | **38.3%** (23/60) |

**Classes de alta complexidade (criterio todas-arestas >= 80%):**

| Classe | Branches cobertos | % Branches | Status |
|---|---|---|---|
| `PedidoService` | 4/4 | **100%** | ✅ |
| `Pedido` | 5/6 | **83.3%** | ✅ |
| `Pagamento` | 5/6 | **83.3%** | ✅ |

> Os branches restantes nas entidades JPA (`equals`/`hashCode` gerados) sao de baixa relevancia para o criterio todas-arestas de logica de negocio.

**Como gerar o relatorio:**

```bash
./mvnw clean test jacoco:report -Dmaven.test.failure.ignore=true
# Abrir: target/site/jacoco/index.html
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

### 11. Inspecao de Codigo (SonarCloud)

| Artefato | Link |
|---|---|
| Apresentacao SonarQube | [`docs/WaiterApp_SonarQube.pptx`](docs/WaiterApp_SonarQube.pptx) |

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
| Testes unitarios | 177 |
| Testes de integracao | 19 |
| Testes E2E (Selenium) | 25 |
| **Total** | **221** |

---

## Estrutura do Repositorio

```
waiterapp/
├── docs/
│   ├── plano-de-teste.md                       # Plano de Teste
│   ├── medidas-iso-25010.md                     # Medidas de Qualidade ISO/IEC 25010
│   ├── caso_de_teste_add_item_carrinho.pdf      # Caso de teste manual — adicionar item
│   ├── caso_de_teste_rem_item_carrinho.pdf      # Caso de teste manual — remover item
│   └── WaiterApp_SonarQube.pptx                 # Apresentacao SonarQube
├── src/
│   ├── main/
│   │   ├── java/com/example/waiterapp/
│   │   │   ├── Cardapio/                        # Cardapio (entidade, servico, repositorio, DTO, controller)
│   │   │   ├── Cliente/                         # Cliente
│   │   │   ├── Garcom/                          # Garcom
│   │   │   ├── Ingrediente/                     # Ingrediente
│   │   │   ├── Item/                            # Item base + Prato + Bebida
│   │   │   ├── ItemPedido/                      # ItemPedido (chave composta)
│   │   │   ├── Pagamento/                       # Pagamento (cartao e dinheiro)
│   │   │   ├── Pedido/                          # Pedido
│   │   │   ├── config/                          # Configuracao Swagger
│   │   │   ├── enums/                           # Estado do pedido
│   │   │   └── exceptions/                      # ObjectNotFoundException
│   │   └── resources/
│   │       ├── application.properties           # Config PostgreSQL
│   │       └── static/                          # Frontend Angular (bundled)
│   └── test/java/com/example/waiterapp/
│       ├── Cardapio/CardapioServiceTest.java
│       ├── Cliente/ClienteServiceTest.java
│       ├── Garcom/GarcomServiceTest.java
│       ├── Item/ItemServiceTest.java
│       ├── Item/Prato/PratoTest.java
│       ├── ItemPedido/ItemPedidoTest.java
│       ├── Pagamento/PagamentoTest.java
│       ├── Pedido/
│       │   ├── PedidoTest.java
│       │   ├── PedidoServiceTest.java
│       │   └── PedidoRepositoryIntegrationTest.java
│       ├── integration/                         # Testes de integracao (MockMvc + H2)
│       │   ├── ClienteIntegrationTest.java
│       │   └── CardapioIntegrationTest.java
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
├── pom.xml                                      # JaCoCo + PITest + Selenium configurados
└── .github/ISSUE_TEMPLATE/                      # Templates de issue (bug, feature, custom)
```

---

## Como Executar

```bash
# Clonar o repositorio
git clone <url-do-repositorio>
cd waiterapp

# Executar apenas os testes unitarios e de integracao (sem banco de dados necessario)
mvn test -Dgroups='!e2e'

# Executar todos os testes (requer app rodando para E2E)
mvn test

# Executar a aplicacao completa com Docker (PostgreSQL incluido)
docker-compose up

# Gerar relatorio de cobertura JaCoCo
./mvnw clean test jacoco:report
# Abrir: target/site/jacoco/index.html

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
