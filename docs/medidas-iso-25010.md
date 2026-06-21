# Medidas de Qualidade — ISO/IEC 25010 (WaiterApp)

---

## Objetivo

Este documento define as **medidas de qualidade** dos atributos da norma **ISO/IEC 25010** para o sistema WaiterApp, indicando **como o sistema deveria ser** (metas esperadas), e **não** como está implementado atualmente.

**Contexto do sistema:** API REST de gerenciamento de pedidos em restaurante (garçons, clientes, cardápio, itens, pagamentos), com interface web Angular em, uso em ambiente operacional de salão e integração via HTTP/JSON.

---

## Escala adotada


| Nível | Denominação | Significado (meta esperada)                                             |
| ----- | ----------- | ----------------------------------------------------------------------- |
| **1** | Muito baixo | Requisito periférico. Desvio aceitável sem impacto relevante ao negócio |
| **2** | Baixo       | Desejável, mas não crítico para a operação do restaurante               |
| **3** | Médio       | Padrão mínimo aceitável para uso em produção.                           |
| **4** | Alto        | Requisito importante. Falhas geram prejuízo operacional ou financeiro   |
| **5** | Muito alto  | Requisito essencial e inegociável. Falha compromete o sistema           |


**Critério de atribuição:** combinação de criticidade de negócio + risco ao usuário/dados + contexto de uso  + natureza do produto.

---

## 1. Adequação funcional


| Subcaracterística        | Meta | Medida / critério esperado                                                                                                                                                                                           | Justificativa                                                                                                                                   |
| ------------------------ | ---- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------- |
| **Completude funcional** | 5    | Todos dos fluxos de negócio previstos implementados: CRUD de pedido, cliente, garçom, cardápio, item; - associação item–pedido - fechamento de pedido - pagamento - consulta de histórico por cliente                | O WaiterApp cobre o ciclo completo do atendimento. Módulo incompleto (ex.: pagamento sem confirmação) impede o fechamento operacional do pedido |
| **Correção funcional**   | 5    | Cálculos de subtotal, preço total e calorias com precisão monetária (2 casas decimais); transições de estado válidas; exceções padronizadas (`ObjectNotFoundException`) em vez de falhas silenciosas ou `null`/`NPE` | Erros em valores financeiros ou estados inválidos causam cobrança incorreta e perda de confiança. É o atributo mais crítico do domínio          |
| **Adequação funcional**  | 4    | Operações alinhadas ao fluxo real do restaurante: pedido inicia em preparação, pode receber itens extras, ser fechado e pago; cardápio separa pratos e bebidas com atributos específicos                             | Funções devem refletir a rotina do garçom, não apenas CRUD genérico. Desvio do fluxo aumenta erros de uso                                       |


---

## 2. Eficiência de desempenho


| Subcaracterística          | Meta | Medida / critério esperado                                                                                                             | Justificativa                                                                                                |
| -------------------------- | ---- | -------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------ |
| **Comportamento temporal** | 4    | Tempo de resposta ≤ **500 ms** (p95) para operações CRUD e consulta de cardápio; ≤ **1 s** para criação de pedido com até 20 itens     | Garçons operam sob pressão no salão. Lentidão reduz produtividade e aumenta fila de atendimento              |
| **Utilização de recursos** | 3    | Consumo de CPU e memória estável em servidor modesto (2 vCPU, 4 GB RAM) com até 30 requisições simultâneas, sem degradação progressiva | Ambiente típico de restaurante pequeno/médio. Não exige cluster, mas não pode vazar memória em turnos longos |
| **Capacidade**             | 3    | Suportar **≥ 50 garçons/dispositivos** concorrentes e **≥ 200 pedidos ativos** sem violação dos SLAs de tempo                          | Escala adequada a restaurante de porte médio em horário de pico, sem over-engineering                        |

### Requisito Não Funcional — Testes de Sistema Implementados

A subcaracterística **comportamento temporal** é verificada por testes de sistema (E2E) com Selenium 4. Detalhes completos no [Plano de Teste](plano-de-teste.md#107-requisito-não-funcional--testes-de-sistema-e2e).

| ID | Classe de teste | Cenário | Limite no teste | Meta ISO 25010 |
|---|---|---|---|---|
| RNF-01 | `PedidoE2ETest` | Carregamento da página inicial (`/`) | < 5 s | ≤ 500 ms (p95) para CRUD |
| RNF-02 | `ClienteE2ETest` | `GET /api/clientes` | < 3 s | ≤ 500 ms (p95) para CRUD |
| RNF-03 | `GarcomE2ETest` | `GET /api/garcons` | < 3 s | ≤ 500 ms (p95) para CRUD |

**Execução:** `docker-compose up -d` e `mvn test -Dgroups=e2e`.

---

## 3. Compatibilidade


| Subcaracterística      | Meta | Medida / critério esperado                                                                                                        | Justificativa                                                                             |
| ---------------------- | ---- | --------------------------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------- |
| **Coexistência**       | 3    | Execução estável em container Docker junto ao PostgreSQL, sem conflito de portas, versões ou recursos compartilhados              | O projeto prevê `docker-compose`. A API deve conviver com o banco sem interferência mútua |
| **Interoperabilidade** | 4    | API REST com JSON, códigos HTTP semânticos, contrato documentado em Swagger; integração possível com apps mobile/web de terceiros | O produto *é* uma API; interoperabilidade é requisito central, não opcional               |


---

## 4. Usabilidade

*Aplicada à interface web do WaiterApp, SPA Angular com Angular Material e Bootstrap, voltada ao **cliente do restaurante** no fluxo de identificação, consulta de cardápio, carrinho e pedidos.*

### Interface considerada (comportamento esperado)


| Tela / rota                   | Função esperada                                                                                  |
| ----------------------------- | ------------------------------------------------------------------------------------------------ |
| `/`                           | Página inicial com logo, identidade visual e botão **COMEÇAR** que direciona ao fluxo do cliente |
| `/cliente/login`              | Identificação por **nome** (obrigatório) e **CPF** (opcional), com mensagens de validação claras |
| `/cliente/fazer-pedido`       | Exibição de pratos e bebidas; modal de detalhes do item; adição ao carrinho com contador e total |
| `/cliente/lista-pedidos`      | Listagem de pedidos do cliente com ordenação e acesso ao detalhe                                 |
| `/cliente/pedido-detalhe/:id` | Visualização de itens, valores, data, garçom e nota do pedido                                    |



| Subcaracterística                    | Meta | Medida / critério esperado                                                                                                                                                                                                                                  | Justificativa                                                                                                                                 |
| ------------------------------------ | ---- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------- |
| **Reconhecibilidade de adequação**   | 4    | Em **≤ 10 s** na tela inicial, o usuário identifica que o sistema é do WaiterApp (logo, nome) e compreende que o botão "**COMEÇAR"** inicia o pedido. Navegação entre *Fazer pedido* e *Lista de pedidos* sempre visível após login                         | A home (`/`) e o menu do módulo cliente devem deixar explícito o propósito do sistema e o próximo passo, sem depender de conhecimento técnico |
| **Aprendizagem**                     | 4    | Usuário novo conclui o fluxo completo (login → escolher item → adicionar ao carrinho → finalizar pedido) em **≤ 5 min** sem treinamento formal, conforme estimativa dos casos de teste de interface (CTM-002)                                               | O fluxo é linear e limitado a poucas telas. A curva de aprendizado deve ser baixa para clientes em mesa                                       |
| **Operabilidade**                    | 4    | Ações principais acessíveis em **≤ 3 cliques** (abrir item, adicionar ao carrinho, finalizar); contador do carrinho e valor total sempre visíveis durante a montagem do pedido; botões de confirmação e cancelamento em modais de item e exclusão de pedido | Garçom/cliente opera sob pressão. Cada clique extra aumenta erro e tempo de atendimento                                                       |
| **Proteção contra erros do usuário** | 4    | Bloqueio de finalização sem nome informado. Validação de campos obrigatórios com mensagens em português. Modal de confirmação antes de excluir pedido; feedback explícito em falhas de cadastro ou envio de pedido                                          | Evita pedidos incompletos, exclusões acidentais e estados inconsistentes no carrinho                                                          |
| **Estética da interface**            | 4    | Layout responsivo (`viewport` configurado); tipografia Roboto e ícones Material; identidade visual com logo e imagem de fundo. cards de itens com hierarquia visual clara (nome, preço, tipo prato/bebida)                                                  | Interface deve transmitir profissionalismo do restaurante e facilitar leitura rápida do cardápio em celular ou tablet                         |
| **Acessibilidade**                   | 3    | Elementos interativos com rótulos ou `aria-label`. Contraste adequado em botões de ação. Navegação por teclado nos formulários de login.                                                                                                                    | Requisito médio: há atributos ARIA pontuais, mas o sistema deve evoluir para WCAG 2.1 nível A em formulários, modais e mensagens de erro      |


---

## 5. Confiabilidade


| Subcaracterística       | Meta | Medida / critério esperado                                                                                                                 | Justificativa                                                              |
| ----------------------- | ---- | ------------------------------------------------------------------------------------------------------------------------------------------ | -------------------------------------------------------------------------- |
| **Maturidade**          | 4    | Taxa de falha em operação normal **< 1%** das requisições; ausência de bugs conhecidos em fluxos críticos (cálculo, fechamento, pagamento) | Sistema em uso durante serviço de refeições não pode falhar com frequência |
| **Disponibilidade**     | 4    | Disponibilidade **≥ 99%** no horário de funcionamento do restaurante                                                                       | Indisponibilidade paralisa registro de pedidos e cobrança                  |
| **Tolerância a falhas** | 3    | Falha pontual de requisição não corrompe pedidos em andamento; transações atômicas em operações compostas (`@Transactional`)               | Evita pedidos pela metade quando há erro na criação                        |
| **Recuperabilidade**    | 3    | Recuperação automática após reinício do serviço; dados persistidos no PostgreSQL sem perda de pedidos já confirmados                       | Reinícios de deploy/manutenção não devem apagar histórico operacional      |


---

## 6. Segurança


| Subcaracterística     | Meta | Medida / critério esperado                                                                                                             | Justificativa                                                            |
| --------------------- | ---- | -------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------ |
| **Confidencialidade** | 4    | CPF e dados de pagamento transmitidos apenas via HTTPS. Sem exposição de dados sensíveis em logs ou respostas de erro                  | Dados pessoais e financeiros exigem proteção mesmo em contexto acadêmico |
| **Integridade**       | 5    | Preços congelados no momento do pedido; impossibilidade de alterar totais sem auditoria. Validação de integridade referencial no banco | Integridade financeira é tão crítica quanto correção funcional           |
| **Não-repúdio**       | 3    | Registro de data/hora de criação e estado do pedido; confirmação explícita de pagamento                                                | Permite rastrear quem/quando fechou um pedido em disputas simples        |
| **Responsabilização** | 3    | Associação de pedido a garçom e cliente; logs de operações críticas                                                                    | Suporta accountability operacional no restaurante                        |
| **Autenticidade**     | 4    | Autenticação de garçons/API antes de criar/alterar pedidos e processar pagamentos                                                      | Sem autenticação, qualquer cliente HTTP poderia manipular pedidos        |


---

## 7. Manutenibilidade


| Subcaracterística    | Meta | Medida / critério esperado                                                                                                           | Justificativa                                                                                              |
| -------------------- | ---- | ------------------------------------------------------------------------------------------------------------------------------------ | ---------------------------------------------------------------------------------------------------------- |
| **Modularidade**     | 4    | Separação clara por domínio (Pedido, Cliente, Garçom, Cardápio, Pagamento) com camadas Controller → Service → Repository             | Facilita evolução independente de cada módulo                                                              |
| **Reusabilidade**    | 3    | Serviços e DTOs reutilizáveis entre endpoints; polimorfismo em pagamentos (`IPagamento`)                                             | Evita duplicação ao adicionar novos meios de pagamento                                                     |
| **Analisabilidade**  | 4    | Código legível, exceções centralizadas, logs em pontos de falha; Swagger como documentação viva                                      | Equipe acadêmica e futuros mantenedores precisam localizar defeitos com rapidez                            |
| **Modificabilidade** | 4    | Alteração em regra de negócio (novo estado de pedido por exemplo) exige mudança localizada, sem efeito cascata                       | Cardápio e regras de restaurante mudam com frequência                                                      |
| **Testabilidade**    | 5    | Cobertura de testes unitários nos serviços e entidades com lógica; cenários happy path, borda e negativos. Testes isolados com mocks | Projeto da disciplina de Qualidade e Teste. Testabilidade é requisito explícito e garante regressão segura |


---

## 8. Portabilidade


| Subcaracterística     | Meta | Medida / critério esperado                                                                                                                  | Justificativa                                         |
| --------------------- | ---- | ------------------------------------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------- |
| **Adaptabilidade**    | 3    | Configuração de banco e porta via `application.properties`/variáveis de ambiente, sem alteração de código                                   | Permite deploy em diferentes ambientes                |
| **Instalabilidade**   | 4    | Instalação reproduzível com `docker-compose up` em **≤ 15 min** em máquina com Docker instalado                                             | Facilita demonstração, avaliação e implantação piloto |
| **Substituibilidade** | 3    | Persistência desacoplada via JPA. Possibilidade de trocar PostgreSQL por outro SGBD relacional compatível com ajuste mínimo de configuração | Reduz dependência de fornecedor específico de banco   |


---

## Síntese por característica


| Característica ISO 25010 | Meta média | Prioridade geral |
| ------------------------ | ---------- | ---------------- |
| Adequação funcional      | 4,7        | Crítica          |
| Eficiência de desempenho | 3,3        | Importante       |
| Compatibilidade          | 3,5        | Importante       |
| Usabilidade              | 3,8        | Alta             |
| Confiabilidade           | 3,5        | Importante       |
| Segurança                | 3,8        | Alta             |
| Manutenibilidade         | 4,0        | Alta             |
| Portabilidade            | 3,3        | Importante       |


