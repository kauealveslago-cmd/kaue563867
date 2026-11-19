# Cidade em Dia de Chuva — kaue563867

Global Solution 2026.2 · Object-Oriented Programming and Java Web Development (Prof. Dr. Antonio Marcos Selmini)

## Versão escolhida

**Versão web** (`@Controller` + telas Thymeleaf, testada no navegador). Não há `@RestController`.

## Integrantes

| Nome | RM |
|---|---|
| Felipe Marques de Souza | 563541 |
| Kauê Alves Lago | 563867 |
| Maria Fernanda Platz Silva Caldeira | 561260 |

**RM da equipe:** 563541 + 563867 + 561260 = **1688668**

## Parâmetros da equipe (rm = 1688668)

| Parâmetro | Cálculo | Resultado |
|---|---|---|
| P1 — prefixo das tabelas | 1688668 % 1000 = 668 | `GS668` → `GS668_BAIRRO`, `GS668_TRECHO`, `GS668_LEITURA` |
| P2 — limiar de alerta L | 10 + 2 * (1688668 % 4) = 10 + 2 * 0 | **L = 10 cm** (atenção a partir de 5 cm) |
| P3 — atributo extra de Bairro | (1688668 / 10) % 3 = 168866 % 3 = 2 | `possuiPiscinao` (Boolean) |
| P4 — ordenação dos trechos | (1688668 / 100) % 3 = 16886 % 3 = 2 | nome do bairro (A a Z) e, no empate, logradouro |
| P5 — exclusão de trecho com leituras | (1688668 / 1000) % 2 = 1688 % 2 = 0 | **bloquear** a exclusão, com mensagem de erro |

## Forma dos DTOs

Um único DTO por entidade (`BairroDTO`, `TrechoDTO`, `LeituraDTO`), todos `record`.
O `id` vai no formulário em campo oculto (nulo: inclui; preenchido: altera). `bairroNome`, `trechoDescricao` e `status` só são exibidos; o status é calculado pelo service.
Também há `PainelItemDTO` (cada trecho do painel). O `BairroController` chama `painelService.montarPainel(id)` e conta os trechos em cada status com `painelService.contarPorStatus`.

## Estrutura

Pacote principal `br.com.fiap.kaue563867`, com os subpacotes `domain`, `dto`, `mapper`, `repository`, `service`, `controller` e `config`.

As validações dos dados e as regras de negócio ficam nos services. Quando algo é recusado, o service lança `IllegalArgumentException` com a mensagem; o controller captura a exceção com `try/catch`, coloca `e.getMessage()` no `Model` como `erro` e devolve a mesma tela (os dados digitados são mantidos no formulário), como na calculadora Thymeleaf vista em aula.

Telas em `src/main/resources/templates` (`bairros`, `trechos`, `leituras`) e CSS próprio em `src/main/resources/static/css/estilo.css`.

## Rotas (versão web)

Base: `http://localhost:8080` (`HomeController` abre a página inicial em `/`)

| Método | URL | O que faz |
|---|---|---|
| GET | `/bairros` | lista os bairros por nome (`bairros/lista`) |
| GET | `/bairros/novo` | formulário vazio (`bairros/form`) |
| GET | `/bairros/{id}/editar` | formulário preenchido, com o id em campo oculto |
| POST | `/bairros/salvar` | inclui ou altera e redireciona para `/bairros`; com erro, volta ao formulário com a mensagem |
| POST | `/bairros/{id}/excluir` | exclui e redireciona; se o bairro tem trechos (R2), mostra a lista com a mensagem |
| GET | `/bairros/{id}/painel` | painel do bairro (`bairros/painel`) |

`/trechos` e `/leituras` seguem o mesmo padrão, sem o painel. Em `/trechos`, a exclusão de um trecho com leituras é recusada com mensagem (R3, P5 = 0).

## Regras de negócio

- **R1** (`LeituraService.calcularStatus`): ALERTA se lâmina ≥ 10; ATENCAO se 5 ≤ lâmina < 10; NORMAL se lâmina < 5. O status é sempre recalculado ao incluir ou alterar uma leitura.
- **R2** (`BairroService.excluir`): bairro com trechos não pode ser excluído (mensagem na lista de bairros).
- **R3** (`TrechoService.excluir`): trecho com leituras não pode ser excluído (mensagem na lista de trechos).

## Como executar

1. Defina as variáveis de ambiente `DB_USER` e `DB_PASSWORD` (IntelliJ: *Run > Edit Configurations > Environment variables*, por exemplo `DB_USER=rm563867;DB_PASSWORD=...`).
2. Execute `Kaue563867Application`. Com `ddl-auto=update`, as tabelas `GS668_*` são criadas na primeira execução. Abra `http://localhost:8080`.
3. As credenciais de correção são enviadas no arquivo `credenciais.txt`, somente pelo portal (ele está no `.gitignore`).
