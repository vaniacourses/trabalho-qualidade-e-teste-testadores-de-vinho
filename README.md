# WaiterApp

**Disciplina:** TCC00346 — Qualidade e Teste de Software | UFF — 2026/1

---

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
| `PedidoTest.java` | `Pedido` | 27 | [`src/test/.../Pedido/PedidoTest.java`](src/test/java/com/example/waiterapp/Pedido/PedidoTest.java) |
| `PedidoServiceTest.java` | `PedidoService` | 20 | [`src/test/.../Pedido/PedidoServiceTest.java`](src/test/java/com/example/waiterapp/Pedido/PedidoServiceTest.java) |
| `ItemPedidoTest.java` | `ItemPedido` | 15 | [`src/test/.../ItemPedido/ItemPedidoTest.java`](src/test/java/com/example/waiterapp/ItemPedido/ItemPedidoTest.java) |
| `ItemPedidoPKTest.java` | `ItemPedidoPK` | 13 | [`src/test/.../ItemPedido/ItemPedidoPKTest.java`](src/test/java/com/example/waiterapp/ItemPedido/ItemPedidoPKTest.java) |
| `PratoTest.java` | `Prato` | 14 | [`src/test/.../Item/Prato/PratoTest.java`](src/test/java/com/example/waiterapp/Item/Prato/PratoTest.java) |
| `ItemTest.java` | `Item` | 10 | [`src/test/.../Item/ItemTest.java`](src/test/java/com/example/waiterapp/Item/ItemTest.java) |
| `ItemServiceTest.java` | `ItemService` | 20 | [`src/test/.../Item/ItemServiceTest.java`](src/test/java/com/example/waiterapp/Item/ItemServiceTest.java) |
| `ClienteTest.java` | `Cliente` | 10 | [`src/test/.../Cliente/ClienteTest.java`](src/test/java/com/example/waiterapp/Cliente/ClienteTest.java) |
| `ClienteServiceTest.java` | `ClienteService` | 23 | [`src/test/.../Cliente/ClienteServiceTest.java`](src/test/java/com/example/waiterapp/Cliente/ClienteServiceTest.java) |
| `GarcomTest.java` | `Garcom` | 10 | [`src/test/.../Garcom/GarcomTest.java`](src/test/java/com/example/waiterapp/Garcom/GarcomTest.java) |
| `GarcomServiceTest.java` | `GarcomService` | 17 | [`src/test/.../Garcom/GarcomServiceTest.java`](src/test/java/com/example/waiterapp/Garcom/GarcomServiceTest.java) |
| `CardapioTest.java` | `Cardapio` | 10 | [`src/test/.../Cardapio/CardapioTest.java`](src/test/java/com/example/waiterapp/Cardapio/CardapioTest.java) |
| `CardapioServiceTest.java` | `CardapioService` | 18 | [`src/test/.../Cardapio/CardapioServiceTest.java`](src/test/java/com/example/waiterapp/Cardapio/CardapioServiceTest.java) |
| `IngredienteTest.java` | `Ingrediente` | 10 | [`src/test/.../Ingrediente/IngredienteTest.java`](src/test/java/com/example/waiterapp/Ingrediente/IngredienteTest.java) |
| `PagamentoTest.java` | `Pagamento`, `PagamentoComCartao`, `PagamentoComDinheiro` | 27 | [`src/test/.../Pagamento/PagamentoTest.java`](src/test/java/com/example/waiterapp/Pagamento/PagamentoTest.java) |

**Total: ~265 testes (unitarios + integracao, excluindo E2E)**

### 3.1 Cobertura de Testes (JaCoCo)

Resultados de cobertura do JaCoCo (testes unitarios + integracao; E2E excluidos):

| Grupo | Classes | Metodos | Linhas | Branches (decisoes) |
|---|---:|---:|---:|---:|
| `com.example.waiterapp` | **94%** (32/34) | **78%** (255/327) | **81%** (650/848) | **90%** (54/60) |

**Cobertura de branches por classe (criterio todas-arestas >= 80%):**

| Classe | Branches cobertos | % Branches | Status |
|---|---|---|---|
| `PedidoService` | 4/4 | **100%** | ✅ |
| `Pedido` | 10/10 | **100%** | ✅ |
| `Item` | 6/6 | **100%** | ✅ |
| `ClienteController` | 4/4 | **100%** | ✅ |
| `ItemPedidoPK` | 9/10 | **90%** | ✅ |
| `Cliente` | 9/10 | **90%** | ✅ |
| `Pagamento` | 5/6 | **83.3%** | ✅ |
| `Garcom` | 5/6 | **83.3%** | ✅ |
| `Cardapio` | 5/6 | **83.3%** | ✅ |
| `Ingrediente` | 5/6 | **83.3%** | ✅ |

> **Observacao:** A meta de **>= 80% de branches (todas-arestas)** foi atingida no total do projeto. Os branches restantes (6/60) concentram-se em arestas pontuais de `equals`/`hashCode` em entidades JPA.

**Como visualizar o relatorio (HTML):**

```bash
.\mvnw.cmd clean test jacoco:report -Dtest=!**/e2e/**
# Abrir: target/site/jacoco/index.html
```

**Como validar o gate minimo de 80% de branches:**

```bash
.\mvnw.cmd clean verify -Dtest=!**/e2e/**
# Falha o build se a cobertura de branches ficar abaixo de 80%
```

### 3.2 Gate de cobertura minima (JaCoCo — implementado)

O `pom.xml` configura o **JaCoCo Maven Plugin** com a meta `check` na fase `verify`, exigindo cobertura minima de branches:

| Metrica | Limite minimo |
|---|---:|
| Branches (branch coverage) | **80%** |

**Como funciona:**

- Executar `mvn clean verify` (ou `.\mvnw.cmd clean verify -Dtest=!**/e2e/**` para excluir E2E).
- Se a cobertura de branches ficar abaixo de 80%, o build falha com `Rule violated`.
- Recomendado integrar o comando `verify` no pipeline de CI/CD para evitar regressao.

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


# Testes Manuais - WaiterApp

Este documento detalha os processos de QA do sistema.

* 📄 **Documento Completo:** [Acesse os Testes Manuais aqui](https://docs.google.com/document/d/1kNf6tNtkxJ-7kMwsXe0t5jLLEZdVhzWCr9jAtzic4jI/edit?usp=sharing)

---

# Testlink

Abaixo estão detalhados os fluxos de teste para a gestão do carrinho:

* 📄 [Caso de teste: **Adicionar** ao carrinho](docs/caso_de_teste_add_item_carrinho.pdf)
* 📄 [Caso de teste: **Remover** item do carrinho](docs/caso_de_teste_rem_item_carrinho.pdf)

### 5. Configuracao de Teste

| Arquivo | Link | Descricao |
|---|---|---|
| `application.properties` (test) | [`src/test/resources/application.properties`](src/test/resources/application.properties) | Configuracao H2 in-memory para testes Spring |
| `pom.xml` | [`pom.xml`](pom.xml) | Dependencias do projeto (JUnit 5, Mockito, H2, JaCoCo com gate de 80% branches) |

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
│   │   ├── Cardapio/CardapioTest.java
│   │   ├── Cliente/ClienteServiceTest.java
│   │   ├── Cliente/ClienteTest.java
│   │   ├── Garcom/GarcomServiceTest.java
│   │   ├── Garcom/GarcomTest.java
│   │   ├── Ingrediente/IngredienteTest.java
│   │   ├── Item/ItemServiceTest.java
│   │   ├── Item/ItemTest.java
│   │   ├── Item/Prato/PratoTest.java
│   │   ├── ItemPedido/ItemPedidoTest.java
│   │   ├── ItemPedido/ItemPedidoPKTest.java
│   │   ├── Pagamento/PagamentoTest.java
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

### 5. Testes Unitarios

| Arquivo | Classe Testada | Testes | Link |
|---|---|---|---|
| `PedidoTest.java` | `Pedido` | 27 | [`src/test/.../Pedido/PedidoTest.java`](src/test/java/com/example/waiterapp/Pedido/PedidoTest.java) |
| `PedidoServiceTest.java` | `PedidoService` | 20 | [`src/test/.../Pedido/PedidoServiceTest.java`](src/test/java/com/example/waiterapp/Pedido/PedidoServiceTest.java) |
| `ItemPedidoTest.java` | `ItemPedido` | 15 | [`src/test/.../ItemPedido/ItemPedidoTest.java`](src/test/java/com/example/waiterapp/ItemPedido/ItemPedidoTest.java) |
| `ItemPedidoPKTest.java` | `ItemPedidoPK` | 13 | [`src/test/.../ItemPedido/ItemPedidoPKTest.java`](src/test/java/com/example/waiterapp/ItemPedido/ItemPedidoPKTest.java) |
| `PratoTest.java` | `Prato` | 14 | [`src/test/.../Item/Prato/PratoTest.java`](src/test/java/com/example/waiterapp/Item/Prato/PratoTest.java) |
| `ItemTest.java` | `Item` | 10 | [`src/test/.../Item/ItemTest.java`](src/test/java/com/example/waiterapp/Item/ItemTest.java) |
| `ItemServiceTest.java` | `ItemService` | 20 | [`src/test/.../Item/ItemServiceTest.java`](src/test/java/com/example/waiterapp/Item/ItemServiceTest.java) |
| `ClienteTest.java` | `Cliente` | 10 | [`src/test/.../Cliente/ClienteTest.java`](src/test/java/com/example/waiterapp/Cliente/ClienteTest.java) |
| `ClienteServiceTest.java` | `ClienteService` | 23 | [`src/test/.../Cliente/ClienteServiceTest.java`](src/test/java/com/example/waiterapp/Cliente/ClienteServiceTest.java) |
| `GarcomTest.java` | `Garcom` | 10 | [`src/test/.../Garcom/GarcomTest.java`](src/test/java/com/example/waiterapp/Garcom/GarcomTest.java) |
| `GarcomServiceTest.java` | `GarcomService` | 17 | [`src/test/.../Garcom/GarcomServiceTest.java`](src/test/java/com/example/waiterapp/Garcom/GarcomServiceTest.java) |
| `CardapioTest.java` | `Cardapio` | 10 | [`src/test/.../Cardapio/CardapioTest.java`](src/test/java/com/example/waiterapp/Cardapio/CardapioTest.java) |
| `CardapioServiceTest.java` | `CardapioService` | 18 | [`src/test/.../Cardapio/CardapioServiceTest.java`](src/test/java/com/example/waiterapp/Cardapio/CardapioServiceTest.java) |
| `IngredienteTest.java` | `Ingrediente` | 10 | [`src/test/.../Ingrediente/IngredienteTest.java`](src/test/java/com/example/waiterapp/Ingrediente/IngredienteTest.java) |
| `PagamentoTest.java` | `Pagamento`, `PagamentoComCartao`, `PagamentoComDinheiro` | 27 | [`src/test/.../Pagamento/PagamentoTest.java`](src/test/java/com/example/waiterapp/Pagamento/PagamentoTest.java) |

**Total: ~265 testes (unitarios + integracao, excluindo E2E)**

---

### 6. Testes de Integracao

Testes com Spring Boot + H2 em memoria, testando a camada Controller → Service → Repository real:

| Arquivo | Modulo Testado | Testes | Link |
|---|---|---|---|
| `ClienteIntegrationTest.java` | Cliente (CRUD + busca por CPF) | 9 | [`src/test/.../integration/ClienteIntegrationTest.java`](src/test/java/com/example/waiterapp/integration/ClienteIntegrationTest.java) |
| `CardapioIntegrationTest.java` | Cardapio (CRUD + 404) | 8 | [`src/test/.../integration/CardapioIntegrationTest.java`](src/test/java/com/example/waiterapp/integration/CardapioIntegrationTest.java) |

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

#### Requisito Não Funcional

Atributo de qualidade coberto por teste de sistema: **eficiencia de desempenho** (comportamento temporal — [ISO/IEC 25010](docs/medidas-iso-25010.md)).

| ID | Arquivo | Metodo | Cenario | Limite | Link |
|---|---|---|---|---|---|
| RNF-01 | `PedidoE2ETest.java` | `paginaPrincipal_tempoCarregamento_deveSerAceitavel` | Carregamento da pagina inicial | < 5 s | [`src/test/.../e2e/PedidoE2ETest.java`](src/test/java/com/example/waiterapp/e2e/PedidoE2ETest.java) |
| RNF-02 | `ClienteE2ETest.java` | `apiClientes_tempoResposta_deveSerAceitavel` | Resposta de `GET /api/clientes` | < 3 s | [`src/test/.../e2e/ClienteE2ETest.java`](src/test/java/com/example/waiterapp/e2e/ClienteE2ETest.java) |
| RNF-03 | `GarcomE2ETest.java` | `apiGarcons_tempoResposta_deveSerAceitavel` | Resposta de `GET /api/garcons` | < 3 s | [`src/test/.../e2e/GarcomE2ETest.java`](src/test/java/com/example/waiterapp/e2e/GarcomE2ETest.java) |

**Como executar os testes E2E:**

```bash
# 1. Subir a aplicacao
docker-compose up -d

# 2. Executar apenas os testes E2E
mvn test -Dgroups=e2e
```

---

### 8. Cobertura Estrutural (JaCoCo — Tecnica Todas-Arestas)

Resultados com 265 testes unitarios + integracao (E2E excluidos):

**Totais gerais:**

| Metrica | Resultado |
|---|---:|
| Classes | **94%** (32/34) |
| Metodos | **78%** (255/327) |
| Linhas | **81%** (650/848) |
| **Branches** | **90%** (54/60) |

**Classes com cobertura de branches >= 80%:**

| Classe | Branches cobertos | % Branches | Status |
|---|---|---|---|
| `PedidoService` | 4/4 | **100%** | ✅ |
| `Pedido` | 10/10 | **100%** | ✅ |
| `Item` | 6/6 | **100%** | ✅ |
| `ClienteController` | 4/4 | **100%** | ✅ |
| `ItemPedidoPK` | 9/10 | **90%** | ✅ |
| `Cliente` | 9/10 | **90%** | ✅ |
| `Pagamento` | 5/6 | **83.3%** | ✅ |
| `Garcom` | 5/6 | **83.3%** | ✅ |
| `Cardapio` | 5/6 | **83.3%** | ✅ |
| `Ingrediente` | 5/6 | **83.3%** | ✅ |


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
│       ├── Cardapio/CardapioTest.java
│       ├── Cliente/ClienteServiceTest.java
│       ├── Cliente/ClienteTest.java
│       ├── Garcom/GarcomServiceTest.java
│       ├── Garcom/GarcomTest.java
│       ├── Ingrediente/IngredienteTest.java
│       ├── Item/ItemServiceTest.java
│       ├── Item/ItemTest.java
│       ├── Item/Prato/PratoTest.java
│       ├── ItemPedido/ItemPedidoTest.java
│       ├── ItemPedido/ItemPedidoPKTest.java
│       ├── Pagamento/PagamentoTest.java
│       ├── Pedido/PedidoTest.java
│       ├── Pedido/PedidoServiceTest.java
│       ├── integration/
│       │   ├── ClienteIntegrationTest.java
│       │   └── CardapioIntegrationTest.java
│       └── e2e/
│           ├── BaseSeleniumTest.java
│           ├── PedidoE2ETest.java
│           ├── ClienteE2ETest.java
│           ├── CardapioE2ETest.java
│           └── GarcomE2ETest.java
└── pom.xml                                    # JaCoCo (gate 80% branches) + PITest + Selenium
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
| **Kauan Christofaro** | Documentação Plano de teste, Indicação das medidas da ISO 25010, Revisão e Melhoria de testes de integração, Resolução de Issues do SonarQube | ✅ |
| **Rafael Langsch** | Testes de integração, Cobertura estrutural com JaCoCo, Teste de mutação com PIT (Pedido/PedidoService), Inspeção de código SonarCloud | ✅ |
