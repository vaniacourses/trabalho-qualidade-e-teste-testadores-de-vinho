# Plano de Teste — WaiterApp
**Disciplina:** TCC00346 – Qualidade e Teste de Software  
**Instituição:** Universidade Federal Fluminense (UFF)  
**Versão:** 1.0 
**Data:** 2026-06-16  

---

## Histórico de Versões

| Versão | Data       | Autor(es)        | Descrição                        |
|--------|------------|------------------|----------------------------------|
| 1.0    | 2026-04-26 | Grupo (4 alunos) | Criação inicial – Entrega 1      |
| 1.1    | 2026-06-16 | Grupo (4 alunos) | Novos casos PT-15 a PT-17 em PedidoServiceTest |
| 1.2    | 2026-06-16 | Grupo (4 alunos) | Novo caso IP-08 em ItemPedidoTest |
| 1.3    | 2026-06-16 | Grupo (4 alunos) | Novos casos CS-01 a CS-03 em ClienteServiceTest |

---

## 1. Introdução

### 1.1 Objetivo
Este documento descreve o plano de teste para o sistema **WaiterApp**, uma API REST de gerenciamento de pedidos de restaurante, desenvolvida em Java com Spring Boot. O objetivo é garantir a qualidade do software por meio de testes unitários automatizados, cobrindo os principais componentes de lógica de negócio.

### 1.2 Escopo
O plano cobre a **camada de serviços** e as **entidades com lógica de negócio** do sistema WaiterApp. Estão fora do escopo inicial: camada de controladores (endpoints REST), testes de integração com banco de dados real e testes de interface de usuário.

### 1.3 Sistema em Teste
- **Nome:** WaiterApp
- **Tipo:** API REST (backend)
- **Tecnologia:** Java 11+, Spring Boot 2.7.1, JPA/Hibernate, PostgreSQL
- **Repositório:** disponível no GitHub Classroom do grupo

---

## 2. Itens de Teste

Os componentes selecionados para teste estão listados abaixo, priorizados por complexidade e criticalidade de negócio:

| Componente | Tipo | Complexidade | Justificativa |
|---|---|---|---|
| `PedidoService` | Service | Alta | Fluxo principal do sistema; lógica de criação de pedido com múltiplas dependências e laços |
| `Pedido` | Entity | Média | Cálculo de preço total via stream; transição de estados |
| `ItemPedido` | Entity | Média | Cálculo de subtotal; chave composta; comportamento de preço |
| `Prato` | Entity | Média | Cálculo de calorias via stream sobre lista de ingredientes |
| `ItemService` | Service | Média | CRUD com tratamento de exceções e mapeamento DTO |
| `ClienteService` | Service | Média | Gerenciamento de clientes e relacionamento com pedidos |
| `GarcomService` | Service | Média | Gerenciamento de garçons com tratamento de exceções |
| `CardapioService` | Service | Média | CRUD de cardápio com tratamento de exceções |

---

## 3. Funcionalidades a Testar

### 3.1 PedidoService
- Listagem de todos os pedidos (`listaPedidos`)
- Listagem de pedidos por cliente (`listaPedidosByIdCliente`)
- Busca de pedido por ID (`retornaPedidoById`) — encontrado e não encontrado
- Criação de pedido com cálculo de preço total (`inserePedido`)
- Persistência dos itens do pedido via `itemPedidoRepository.saveAll` (`inserePedido`)
- Criação de pedido com múltiplos itens e busca individual de cada item (`inserePedido`)
- Propagação de `ObjectNotFoundException` quando item do pedido não existe (`inserePedido`)
- Atualização de pedido (`atualizaPedido`)
- Exclusão de pedido com tratamento de integridade referencial (`apagaPedido`)

### 3.2 Pedido (entidade)
- Cálculo do preço total a partir dos itens (`setPrecoTotal`)
- Fechamento de pedido com mudança de estado (`fecharPedido`)
- Adição de item extra (`adicionarItemExtra`)
- Igualdade e hash code baseados no ID

### 3.3 ItemPedido (entidade)
- Cálculo do subtotal (`getSubTotal` = quantidade × preço do item)
- Subtotal com preço armazenado em `ItemPedido` diferente do preço do item (`getSubTotal` ignora campo `preco`)
- Comportamento dos construtores
- Mutação de preço e quantidade

### 3.4 Prato (entidade)
- Soma de calorias dos ingredientes (`getCaloriaTotal`)
- Herança de campos de `Item`
- Comportamento com lista vazia de ingredientes

### 3.5 ItemService
- Listagem, busca, inserção, atualização e exclusão de itens
- `ObjectNotFoundException` ao buscar item inexistente
- `DataIntegrityViolationException` ao excluir item com pedidos associados
- Mapeamento DTO → entidade (`transformarDTO`)

### 3.6 ClienteService
- CRUD completo de clientes
- Busca por CPF
- Retorno de pedidos do cliente
- `NullPointerException` em `retornaPedidosCliente` e `inserePedidosCliente` quando cliente não existe
- `atualizaCliente` sem validação de existência do ID
- `apagaCliente` sem verificação prévia de existência (diferente de `GarcomService`)
- Tratamento de violação referencial na exclusão

### 3.7 GarcomService
- CRUD completo de garçons
- `ObjectNotFoundException` para IDs inexistentes
- `DataIntegrityViolationException` na exclusão com pedidos ativos
- Mapeamento DTO → entidade

### 3.8 CardapioService
- CRUD completo de cardápios
- `ObjectNotFoundException` para IDs inexistentes
- `DataIntegrityViolationException` na exclusão com itens associados
- Mapeamento DTO → entidade

---

## 4. Funcionalidades Fora do Escopo (Entrega 1)

- Controladores REST (serão cobertos em testes de sistema na Entrega 2)
- Testes de integração com banco de dados (Entrega 2)
- Testes de interface Selenium (Entrega 2)
- `PagamentoComCartao` e `PagamentoComDinheiro` (métodos `confirmarPagamento` ainda não implementados — stubs)

---

## 5. Abordagem de Teste

### 5.1 Tipos de Teste
- **Testes Unitários Automatizados** com isolamento total via mocks

### 5.2 Técnicas Aplicadas
- **Particionamento em Classes de Equivalência:** valores válidos, limites e inválidos
- **Análise de Valor de Fronteira (BVA):** valores mínimos, máximos e fronteiros
- **Baseado em Especificação (caixa preta):** comportamento esperado por contrato
- **Padrão AAA:** Arrange / Act / Assert em todos os testes

### 5.3 Cobertura de Cenários
Para cada método testado são cobertos:
| Categoria | Exemplos |
|---|---|
| **Happy Path** | Operação bem-sucedida com dados válidos |
| **Edge Cases** | Listas vazias, quantidade zero, preço decimal |
| **Negative Cases** | ID inexistente → exceção; FK violation → exceção |
| **Boundary Values** | Notas 1 e 10, grande quantidade de itens, calorias zero |

---

## 6. Ferramentas

| Ferramenta | Versão | Finalidade |
|---|---|---|
| **JUnit 5** (JUnit Jupiter) | 5.8.x | Framework de testes unitários |
| **Mockito** | 4.x | Criação de mocks e verificação de comportamento |
| **AssertJ** | 3.x | Assertions fluentes (disponível via `spring-boot-starter-test`) |
| **Spring Boot Test** | 2.7.1 | Infraestrutura de testes Spring |
| **H2 Database** | — | Banco em memória para testes de contexto Spring |
| **Maven Surefire** | — | Execução dos testes no ciclo Maven |

---

## 7. Critérios de Entrada e Saída

### 7.1 Critérios de Entrada
- Código-fonte compilando sem erros
- Dependências de teste disponíveis no `pom.xml`
- Ambiente Java configurado (JDK 11+, Maven)

### 7.2 Critérios de Saída
- Todos os testes unitários passando (`mvn test`)
- Nenhuma exceção não tratada nos testes
- Documentação de comportamento inesperado (ex: bug `adicionarItemExtra`)

### 7.3 Critérios de Suspensão
- Erro de compilação que impeça a execução dos testes
- Alterações estruturais no código fonte que quebrem contratos de serviço

---

## 8. Ambiente de Teste

- **Sistema Operacional:** Windows 11 / Linux
- **JDK:** 11 ou superior
- **Build Tool:** Maven 3.x
- **IDE:** IntelliJ IDEA / VS Code
- **Banco de dados (testes unitários):** nenhum (Mockito isola dependências)
- **Banco de dados (teste de contexto Spring):** H2 in-memory

### 8.1 Execução

```bash
# Executar todos os testes
mvn test

# Executar classe de teste específica
mvn test -Dtest=PedidoServiceTest

# Executar com relatório de cobertura (Entrega 2)
mvn test jacoco:report
```

---

## 9. Artefatos Produzidos

| Artefato | Localização | Descrição |
|---|---|---|
| Testes unitários — Pedido | `src/test/.../Pedido/PedidoTest.java` | 15 testes para a entidade Pedido |
| Testes unitários — PedidoService | `src/test/.../Pedido/PedidoServiceTest.java` | 17 testes para PedidoService |
| Testes unitários — ItemPedido | `src/test/.../ItemPedido/ItemPedidoTest.java` | 15 testes para ItemPedido |
| Testes unitários — Prato | `src/test/.../Item/Prato/PratoTest.java` | 13 testes para Prato |
| Testes unitários — ItemService | `src/test/.../Item/ItemServiceTest.java` | 15 testes para ItemService |
| Testes unitários — ClienteService | `src/test/.../Cliente/ClienteServiceTest.java` | 19 testes para ClienteService |
| Testes unitários — GarcomService | `src/test/.../Garcom/GarcomServiceTest.java` | 16 testes para GarcomService |
| Testes unitários — CardapioService | `src/test/.../Cardapio/CardapioServiceTest.java` | 16 testes para CardapioService |
| Plano de Teste | `docs/plano-de-teste.md` | Este documento |

---

## 10. Casos de Teste — Resumo

### 10.1 PedidoService

| ID | Método | Cenário | Tipo | Resultado Esperado |
|---|---|---|---|---|
| PT-01 | `listaPedidos` | Dois pedidos cadastrados | Happy Path | Lista com 2 itens |
| PT-02 | `listaPedidos` | Nenhum pedido | Edge Case | Lista vazia |
| PT-03 | `listaPedidosByIdCliente` | Cliente com pedidos | Happy Path | Lista com pedidos do cliente |
| PT-04 | `listaPedidosByIdCliente` | Cliente sem pedidos | Edge Case | Lista vazia |
| PT-05 | `retornaPedidoById` | ID existente | Happy Path | Pedido retornado |
| PT-06 | `retornaPedidoById` | ID inexistente | Negative | `null` retornado |
| PT-07 | `inserePedido` | Pedido com 1 item (qtd 2, preço 35) | Happy Path | Estado EM_PREPARACAO, id=null, preçoTotal=70 |
| PT-08 | `inserePedido` | Pedido sem itens | Edge Case | preçoTotal=0 |
| PT-09 | `inserePedido` | Verificar persistência | Happy Path | `save` chamado 2 vezes |
| PT-10 | `inserePedido` | Verificar busca de item | Happy Path | `retornaItemById` chamado por item |
| PT-11 | `inserePedido` | Verificar associação de cliente | Happy Path | Cliente correto associado |
| PT-12 | `atualizaPedido` | Pedido existente | Happy Path | Pedido salvo e retornado |
| PT-13 | `apagaPedido` | Pedido sem dependências | Happy Path | Excluído sem exceção |
| PT-14 | `apagaPedido` | Violação referencial | Negative | `DataIntegrityViolationException` |
| PT-15 | `inserePedido` | Persistir ItemPedido via `saveAll` | Happy Path | `saveAll` invocado |
| PT-16 | `inserePedido` | Múltiplos itens (35×2 + 10×3) | Happy Path | preçoTotal=100; `retornaItemById` por item |
| PT-17 | `inserePedido` | Item inexistente | Negative | `ObjectNotFoundException` propagada; `saveAll` não chamado |

### 10.2 Pedido (entidade)

| ID | Método | Cenário | Tipo | Resultado Esperado |
|---|---|---|---|---|
| PE-01 | `setPrecoTotal` | 2 itens (60+30) | Happy Path | preçoTotal = 90.0 |
| PE-02 | `setPrecoTotal` | Sem itens | Edge Case | preçoTotal = 0.0 |
| PE-03 | `setPrecoTotal` | Um item, quantidade 1 | Boundary | preçoTotal = preço do item |
| PE-04 | `setPrecoTotal` | Quantidade zero | Boundary | preçoTotal = 0.0 |
| PE-05 | `setPrecoTotal` | Preços decimais (9.99+0.01) | Edge Case | preçoTotal = 10.0 |
| PE-06 | `fecharPedido` | Estado EM_PREPARACAO | Happy Path | Estado = FECHADO |
| PE-07 | `fecharPedido` | Estado PENDENTE | Happy Path | Estado = FECHADO |
| PE-08 | `fecharPedido` | Chamado 2 vezes | Edge Case | Estado = FECHADO |
| PE-09 | `adicionarItemExtra` | Item adicionado | Bug documentado | Set não é modificado |
| PE-10 | `equals` | Mesmo ID | Happy Path | `true` |
| PE-11 | `equals` | IDs diferentes | Negative | `false` |
| PE-12 | `equals` | Comparado com `null` | Negative | `false` |

### 10.3 ItemPedido (entidade)

| ID | Método | Cenário | Tipo | Resultado Esperado |
|---|---|---|---|---|
| IP-01 | `getSubTotal` | Qtd 3, preço 40 | Happy Path | 120.0 |
| IP-02 | `getSubTotal` | Quantidade 1 | Boundary | = preço do item |
| IP-03 | `getSubTotal` | Quantidade 0 | Boundary | 0.0 |
| IP-04 | `getSubTotal` | Preço decimal | Edge Case | Correto com tolerância |
| IP-05 | `getSubTotal` | Preço alterado após criação | Edge Case | Usa preço atual do item |
| IP-06 | `getSubTotal` | Preço do item = 0 | Negative | 0.0 |
| IP-07 | `getSubTotal` | Quantidade 1000 | Boundary | 10000.0 |
| IP-08 | `getSubTotal` | Preço armazenado ≠ preço do item | Edge Case | Usa preço do item (campo `preco` ignorado) |

### 10.4 Prato (entidade)

| ID | Método | Cenário | Tipo | Resultado Esperado |
|---|---|---|---|---|
| PR-01 | `getCaloriaTotal` | 3 ingredientes (200+150+50) | Happy Path | 400.0 |
| PR-02 | `getCaloriaTotal` | Sem ingredientes | Edge Case | 0.0 |
| PR-03 | `getCaloriaTotal` | Um ingrediente | Boundary | = caloria do ingrediente |
| PR-04 | `getCaloriaTotal` | Ingrediente com 0 calorias | Edge Case | soma sem aquele |
| PR-05 | `getCaloriaTotal` | Valores decimais | Edge Case | Correto com tolerância |
| PR-06 | `getCaloriaTotal` | 10 ingredientes de 50 cal | Boundary | 500.0 |

### 10.5 ClienteService

| ID | Método | Cenário | Tipo | Resultado Esperado |
|---|---|---|---|---|
| CS-01 | `inserePedidosCliente` | Cliente inexistente | Negative | `NullPointerException` |
| CS-02 | `atualizaCliente` | ID inexistente | Negative | Persiste mesmo assim |
| CS-03 | `apagaCliente` | ID inexistente | Negative | `deleteById` sem verificar existência |

---

## 11. Riscos e Mitigações

| Risco | Probabilidade | Impacto | Mitigação |
|---|---|---|---|
| Métodos `confirmarPagamento` não implementados | Alta | Baixo | Fora do escopo de teste |
| Bug em `adicionarItemExtra` (não adiciona ao Set) | Confirmado | Médio | Documentado; teste expõe o comportamento |
| `retornaPedidoById` chama `findById` duas vezes | Confirmado | Baixo | Documentado; não afeta funcionalidade |
| `retornaPedidosCliente` lança NPE para cliente inexistente | Confirmado | Alto | Documentado; teste verifica comportamento atual |
| `inserePedidosCliente` lança NPE para cliente inexistente | Confirmado | Alto | Documentado; teste CS-01 verifica comportamento atual |
| `atualizaCliente` persiste sem validar existência do ID | Confirmado | Médio | Documentado; teste CS-02 expõe o comportamento |
| `apagaCliente` exclui sem verificar existência | Confirmado | Médio | Documentado; teste CS-03 expõe inconsistência com outros services |
| `getSubTotal` ignora o campo `preco` de `ItemPedido` | Confirmado | Médio | Documentado; teste IP-08 expõe o comportamento |

---

## 12. Bugs Encontrados Durante os Testes

| ID | Classe | Método | Descrição | Severidade |
|---|---|---|---|---|
| BUG-01 | `Pedido` | `adicionarItemExtra` | Cria `ItemPedido` mas não o adiciona ao `Set<ItemPedido> items` | Médio |
| BUG-02 | `ClienteService` | `retornaPedidosCliente` | Lança `NullPointerException` quando cliente não existe (deveria lançar `ObjectNotFoundException`) | Alto |
| BUG-05 | `ClienteService` | `inserePedidosCliente` | Lança `NullPointerException` quando cliente não existe (deveria lançar `ObjectNotFoundException`) | Alto |
| BUG-06 | `ClienteService` | `atualizaCliente` | Ignora retorno de `retornaClienteById` e persiste mesmo com ID inexistente | Médio |
| BUG-07 | `ClienteService` | `apagaCliente` | Exclui sem verificar existência do cliente (inconsistente com `GarcomService`) | Médio |
| BUG-03 | `PedidoService` | `retornaPedidoById` | Chama `findById` duas vezes para o mesmo ID (ineficiência) | Baixo |
| BUG-04 | `ItemPedido` | `getSubTotal` | Ignora o campo `preco` armazenado e usa sempre `item.getPreco()` | Médio |

---

## 13. Responsabilidades

| Aluno | Responsabilidade |
|---|---|
| Aluno 1 | Testes de `PedidoService` e `Pedido` |
| Aluno 2 | Testes de `ItemPedido` e `Prato` |
| Aluno 3 | Testes de `ItemService` e `ClienteService` |
| Aluno 4 | Testes de `GarcomService` e `CardapioService` |

---

## 14. Aprovação

| Função | Nome | Data | Assinatura |
|---|---|---|---|
| Elaborado por | Grupo | 2026-04-26 | — |
| Revisado por | — | — | — |

---

# Testes Manuais - WaiterApp

Este documento detalha os processos de QA do sistema.

* 📄 **Documento Completo:** [Acesse os Testes Manuais aqui](https://docs.google.com/document/d/1kNf6tNtkxJ-7kMwsXe0t5jLLEZdVhzWCr9jAtzic4jI/edit?usp=sharing)

---

# Testlink

Abaixo estão detalhados os fluxos de teste para a gestão do carrinho:

* 📄 [Caso de teste: **Adicionar** ao carrinho](caso_de_teste_add_item_carrinho.pdf)
* 📄 [Caso de teste: **Remover** item do carrinho](caso_de_teste_rem_item_carrinho.pdf)


