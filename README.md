# Cidade em Dia de Chuva — kaue563867

Global Solution 2026.2 · Object-Oriented Programming and Java Web Development (Prof. Dr. Antonio Marcos Selmini)

## Versão escolhida

**API REST** (`@RestController`, JSON, testada no Insomnia). Não há telas Thymeleaf.

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
| P5 — exclusão de trecho com leituras | (1688668 / 1000) % 2 = 1688 % 2 = 0 | **bloquear** a exclusão, com mensagem de erro (HTTP 409) |

## Forma dos DTOs

Um único DTO por entidade (`BairroDTO`, `TrechoDTO`, `LeituraDTO`), todos `record`, com as validações do Bean Validation.
Campos que só aparecem na resposta (`id`, `bairroNome`, `trechoDescricao`, `status`) são ignorados na entrada.
Também há `PainelItemDTO` e `PainelDTO` (painel do bairro) e `ErroDTO` (corpo das respostas de erro).

## Estrutura

Pacote principal `br.com.fiap.kaue563867`, com os subpacotes `domain`, `dto`, `mapper`, `repository`, `service`, `controller` e `exception` (exceções de negócio).

## Endpoints

Base: `http://localhost:8080`

| Método | URL | Resposta |
|---|---|---|
| GET | `/api/bairros` | 200 — bairros por nome |
| GET | `/api/bairros/{id}` | 200; 404 |
| POST | `/api/bairros` | 201 com o registro criado; 400 |
| PUT | `/api/bairros/{id}` | 200; 400; 404 |
| DELETE | `/api/bairros/{id}` | 204; 409 (R2: bairro com trechos); 404 |
| GET | `/api/bairros/{id}/painel` | 200 com o painel; 404 |

`/api/trechos` e `/api/leituras` seguem o mesmo padrão (sem o painel). Em `/api/trechos`, o DELETE devolve 409 quando o trecho tem leituras (R3, P5 = 0).

Exemplos de corpo (POST e PUT):

```json
{ "nome": "Vila Esperança", "zona": "Leste", "possuiPiscinao": true }
{ "logradouro": "Rua Alta", "tipoCalcada": "Regular", "altitude": 755, "bairroId": 1 }
{ "trechoId": 1, "dataHora": "2026-11-01T17:20:00", "laminaCm": 5 }
```

## Regras de negócio

- **R1** (`LeituraService.calcularStatus`): ALERTA se lâmina ≥ 10; ATENCAO se 5 ≤ lâmina < 10; NORMAL se lâmina < 5. O status é sempre recalculado ao incluir ou alterar uma leitura.
- **R2** (`BairroService.excluir`): bairro com trechos não pode ser excluído (409).
- **R3** (`TrechoService.excluir`): trecho com leituras não pode ser excluído (409).

## Como executar

1. Defina as variáveis de ambiente `DB_USER` e `DB_PASSWORD` (IntelliJ: *Run > Edit Configurations > Environment variables*, por exemplo `DB_USER=rm563867;DB_PASSWORD=...`).
2. Execute `Kaue563867Application`. Com `ddl-auto=update`, as tabelas `GS668_*` são criadas na primeira execução.
3. As credenciais de correção são enviadas no arquivo `credenciais.txt`, somente pelo portal (ele está no `.gitignore`).
