# WaiterApp

**Disciplina:** TCC00346 — Qualidade e Teste de Software | UFF — 2026/1

---

## Projeto relacionado

Este repositorio contem a documentacao e os testes do **WaiterApp**. O **segundo software** avaliado no trabalho possui um README proprio e esta disponivel no repositorio:

- https://github.com/BielPimentaDev/Trabalho-POO-Simula-o-de-combate

## Descricao do Sistema

O **WaiterApp** e uma API REST de gerenciamento de pedidos para restaurantes. O sistema permite que garcons registrem e acompanhem pedidos de clientes, consultem o cardapio, gerenciem itens e processem pagamentos.

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

- **Linguagem:** Java 11+
- **Framework:** Spring Boot 2.7.1
- **Persistencia:** Spring Data JPA / Hibernate + PostgreSQL
- **Documentacao da API:** Swagger (Springfox 2.9.2)
- **Build:** Maven

### Modulos testados neste trabalho

Os modulos selecionados para teste sao os que contem logica de negocio nao trivial (calculos, transicoes de estado, tratamento de excecoes):

- `PedidoService` — logica de criacao de pedido com laco sobre itens e calculo de total
- `Pedido` — calculo de preco total via stream, transicao de estado (`fecharPedido`)
- `ItemPedido` — calculo de subtotal (quantidade x preco)
- `Prato` — soma de calorias dos ingredientes via stream
- `ItemService`, `ClienteService`, `GarcomService`, `CardapioService` — CRUD com tratamento de excecoes

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

---

### 3. Codigo-Fonte Original

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

### 4. Testes Unitarios Automatizados

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

**Arquivos de teste:**

| Arquivo | Classe Testada | N. de Testes | Link Direto |
|---|---|---|---|
| `PedidoTest.java` | `Pedido` | 15 | [`src/test/.../Pedido/PedidoTest.java`](src/test/java/com/example/waiterapp/Pedido/PedidoTest.java) |
| `PedidoServiceTest.java` | `PedidoService` | 17 | [`src/test/.../Pedido/PedidoServiceTest.java`](src/test/java/com/example/waiterapp/Pedido/PedidoServiceTest.java) |
| `ItemPedidoTest.java` | `ItemPedido` | 15 | [`src/test/.../ItemPedido/ItemPedidoTest.java`](src/test/java/com/example/waiterapp/ItemPedido/ItemPedidoTest.java) |
| `PratoTest.java` | `Prato` | 13 | [`src/test/.../Item/Prato/PratoTest.java`](src/test/java/com/example/waiterapp/Item/Prato/PratoTest.java) |
| `ItemServiceTest.java` | `ItemService` | 15 | [`src/test/.../Item/ItemServiceTest.java`](src/test/java/com/example/waiterapp/Item/ItemServiceTest.java) |
| `ClienteServiceTest.java` | `ClienteService` | 19 | [`src/test/.../Cliente/ClienteServiceTest.java`](src/test/java/com/example/waiterapp/Cliente/ClienteServiceTest.java) |
| `GarcomServiceTest.java` | `GarcomService` | 16 | [`src/test/.../Garcom/GarcomServiceTest.java`](src/test/java/com/example/waiterapp/Garcom/GarcomServiceTest.java) |
| `CardapioServiceTest.java` | `CardapioService` | 16 | [`src/test/.../Cardapio/CardapioServiceTest.java`](src/test/java/com/example/waiterapp/Cardapio/CardapioServiceTest.java) |

**Total: ~126 testes unitarios**

### 3.1 Cobertura de Testes (JaCoCo)

Resultados de cobertura do JaCoCo apos a Entrega 2 (testes unitarios + integracao; E2E excluidos por requererem Chrome):

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
.\mvnw.cmd clean test jacoco:report -Dmaven.test.failure.ignore=true -Dexcludes=**/e2e/**
# Abrir: target/site/jacoco/index.html
```

### 3.2 Sugestao: exigir cobertura minima (80%) via CI/CD

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

### 5. Configuracao de Teste

| Arquivo | Link | Descricao |
|---|---|---|
| `application.properties` (test) | [`src/test/resources/application.properties`](src/test/resources/application.properties) | Configuracao H2 in-memory para testes Spring |
| `pom.xml` | [`pom.xml`](pom.xml) | Dependencias do projeto (JUnit 5, Mockito, H2) |

---

## Estrutura do Repositorio

```
waiterapp/
├── docs/
│   ├── plano-de-teste.md              # Plano de Teste (Entrega 1)
│   └── medidas-iso-25010.md         # Medidas de Qualidade ISO/IEC 25010
├── src/
│   ├── main/java/com/example/waiterapp/
│   │   ├── Cardapio/                  # Cardapio (entidade, servico, repositorio, DTO)
│   │   ├── Cliente/                   # Cliente
│   │   ├── Garcom/                    # Garcom
│   │   ├── Ingrediente/               # Ingrediente
│   │   ├── Item/                      # Item base + Prato + Bebida
│   │   ├── ItemPedido/                # ItemPedido (chave composta)
│   │   ├── Pagamento/                 # Pagamento (cartao e dinheiro)
│   │   ├── Pedido/                    # Pedido
│   │   ├── config/                    # Configuracao Swagger
│   │   ├── enums/                     # Estado do pedido
│   │   └── exceptions/                # ObjectNotFoundException
│   ├── test/java/com/example/waiterapp/
│   │   ├── Cardapio/CardapioServiceTest.java
│   │   ├── Cliente/ClienteServiceTest.java
│   │   ├── Garcom/GarcomServiceTest.java
│   │   ├── Item/ItemServiceTest.java
│   │   ├── Item/Prato/PratoTest.java
│   │   ├── ItemPedido/ItemPedidoTest.java
│   │   ├── Pedido/PedidoTest.java
│   │   └── Pedido/PedidoServiceTest.java
│   └── test/resources/application.properties
└── pom.xml
```

---

## Como Executar

```bash
# Clonar o repositorio
git clone <url-do-repositorio>
cd waiterapp

# Executar apenas os testes unitarios (sem banco de dados necessario)
mvn test

# Executar a aplicacao completa com Docker (PostgreSQL incluido)
docker-compose up
```

---

## Artefatos da Entrega 2 (17/06/2026)

### 5. Testes Unitarios Melhorados

Novos testes adicionados cobrindo a hierarquia de Pagamento e branches antes descobertos:

| Arquivo | Classe Testada | Testes | Link |
|---|---|---|---|
| `PagamentoTest.java` | `Pagamento`, `PagamentoComCartao`, `PagamentoComDinheiro` | 22 | [`src/test/.../Pagamento/PagamentoTest.java`](src/test/java/com/example/waiterapp/Pagamento/PagamentoTest.java) |
| `PedidoTest.java` | `Pedido` | 16 | [`src/test/.../Pedido/PedidoTest.java`](src/test/java/com/example/waiterapp/Pedido/PedidoTest.java) |
| `PedidoServiceTest.java` | `PedidoService` | 17 | [`src/test/.../Pedido/PedidoServiceTest.java`](src/test/java/com/example/waiterapp/Pedido/PedidoServiceTest.java) |
| `ItemServiceTest.java` | `ItemService` | 17 | [`src/test/.../Item/ItemServiceTest.java`](src/test/java/com/example/waiterapp/Item/ItemServiceTest.java) |

**Total acumulado: ~183 testes unitarios**

---

### 6. Testes de Integracao

Testes com Spring Boot + H2 em memoria, testando a camada Controller → Service → Repository real:

| Arquivo | Modulo Testado | Testes | Link |
|---|---|---|---|
| `ClienteIntegrationTest.java` | Cliente (CRUD + busca por CPF) | 7 | [`src/test/.../integration/ClienteIntegrationTest.java`](src/test/java/com/example/waiterapp/integration/ClienteIntegrationTest.java) |
| `CardapioIntegrationTest.java` | Cardapio (CRUD + 404) | 7 | [`src/test/.../integration/CardapioIntegrationTest.java`](src/test/java/com/example/waiterapp/integration/CardapioIntegrationTest.java) |

**Como executar (excluindo E2E):**

```bash
mvn test -Dgroups='!e2e'
```

---

### 7. Testes de Sistema / E2E (Selenium)

Testes de ponta a ponta com Selenium 4 + WebDriverManager. Requerem a aplicacao rodando em `localhost:8080`.

| Arquivo | Cenarios | Link |
|---|---|---|
| `PedidoE2ETest.java` | Carregamento da app, API de pedidos, tempo de resposta | [`src/test/.../e2e/PedidoE2ETest.java`](src/test/java/com/example/waiterapp/e2e/PedidoE2ETest.java) |
| `ClienteE2ETest.java` | API de clientes, elementos Angular, performance | [`src/test/.../e2e/ClienteE2ETest.java`](src/test/java/com/example/waiterapp/e2e/ClienteE2ETest.java) |
| `CardapioE2ETest.java` | API de cardapios, 404, itens | [`src/test/.../e2e/CardapioE2ETest.java`](src/test/java/com/example/waiterapp/e2e/CardapioE2ETest.java) |
| `GarcomE2ETest.java` | API de garcons, Swagger UI, performance | [`src/test/.../e2e/GarcomE2ETest.java`](src/test/java/com/example/waiterapp/e2e/GarcomE2ETest.java) |

**Como executar os testes E2E:**

```bash
# 1. Subir a aplicacao
docker-compose up -d

# 2. Executar apenas os testes E2E
mvn test -Dgroups=e2e
```

---

### 8. Cobertura Estrutural (JaCoCo — Tecnica Todas-Arestas)

Resultados medidos em 2026-06-14 apos todas as melhorias da Entrega 2 (208 testes; E2E contabilizados separadamente):

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
.\mvnw.cmd clean test jacoco:report -Dmaven.test.failure.ignore=true
# Abrir: target/site/jacoco/index.html
```

---

### 9. Teste de Mutacao (PITest)

Ferramenta: **PITest 1.15.3** com plugin JUnit 5.

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

### 10. Inspecao de Codigo (SonarCloud)

> Configurar em: [sonarcloud.io](https://sonarcloud.io) — conectar ao repositorio GitHub do grupo.

**Como executar a analise localmente:**

```bash
mvn sonar:sonar \
  -Dsonar.projectKey=SEU_PROJECT_KEY \
  -Dsonar.organization=SEU_ORG \
  -Dsonar.host.url=https://sonarcloud.io \
  -Dsonar.login=SEU_TOKEN
```

> Prints da analise e das correcoes por membro devem ser adicionados aqui apos execucao.

---

### 11. ISO 25010 — Atributos de Qualidade

> Documento com medidas e justificativas para cada atributo da ISO 25010:
>
| Medidas de Qualidade — ISO/IEC 25010 | [`docs/medidas-iso-25010.md`](docs/medidas-iso-25010.md) |

---

## Estrutura do Repositorio (Entrega 2)

```
waiterapp/
├── docs/
│   └── plano-de-teste.md
├── src/
│   ├── main/java/com/example/waiterapp/      # Codigo de producao
│   └── test/java/com/example/waiterapp/
│       ├── Cardapio/CardapioServiceTest.java
│       ├── Cliente/ClienteServiceTest.java
│       ├── Garcom/GarcomServiceTest.java
│       ├── Item/ItemServiceTest.java
│       ├── Item/Prato/PratoTest.java
│       ├── ItemPedido/ItemPedidoTest.java
│       ├── Pagamento/PagamentoTest.java       # NOVO
│       ├── Pedido/PedidoTest.java
│       ├── Pedido/PedidoServiceTest.java
│       ├── integration/                       # NOVO — testes de integracao
│       │   ├── ClienteIntegrationTest.java
│       │   └── CardapioIntegrationTest.java
│       └── e2e/                               # NOVO — testes Selenium E2E
│           ├── BaseSeleniumTest.java
│           ├── PedidoE2ETest.java
│           ├── ClienteE2ETest.java
│           ├── CardapioE2ETest.java
│           └── GarcomE2ETest.java
└── pom.xml                                    # JaCoCo + PITest + Selenium configurados
```

---

## Historico

| Versao | Data | Descricao |
|---|---|---|
| 1.0 | 2026-04-26 | Entrega 1: testes unitarios e plano de teste |
| 1.1 | 2026-06-12 | Medidas de qualidade ISO/IEC 25010 |
| 2.0 | 2026-06-17 | Entrega 2: integracao, E2E, mutacao, cobertura estrutural |

---

# 👥 Partição de Responsabilidades

| Integrante | Responsabilidade | Status |
| :--- | :--- | :---: |
| **Gabriel Pimenta** | Testes unitários, Documentação plano de testes, Reporte de issues, Documentação do projeto  | ✅ |
| **Guilherme Coelho** | Teste Manuais WaiterApp, Testes E2E com Selenium, Casos de teste Testlink, documentação Plano de teste | ✅ |
| **Kauan Christofaro** | Teste Manuais Simulador de combate, documentação Plano de teste, Indicação das medidas da ISO 25010 | ✅ |
| **Rafael Langsch** | Testes de integração, Cobertura estrutural com JaCoCo, Teste de mutação com PIT (Pedido/PedidoService), Inspeção de código SonarCloud | ✅ |
