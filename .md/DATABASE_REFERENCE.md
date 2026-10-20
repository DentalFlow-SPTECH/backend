# Referência do banco de dados — DentalFlow

## 1. Origem e confiabilidade

Este documento foi elaborado a partir do schema SQL fornecido para o projeto DentalFlow. Ele descreve o DDL apresentado, não comprova que o script tenha sido executado no banco em uso nem que todas as tabelas estejam mapeadas no backend Java.

Antes de implementar ou alterar persistência:
- Confira o DDL e as entidades JPA existentes.
- Confirme o schema efetivamente utilizado no ambiente-alvo.
- Não execute o `DROP DATABASE` deste script em um ambiente com dados que devam ser preservados.
- Não invente colunas, relações ou regras que não estejam declaradas no schema ou em requisitos confirmados.

## 2. Banco de dados

O script:
- remove o banco existente com `DROP DATABASE IF EXISTS dentalFlow`;
- cria `dentalFlow` com charset `utf8mb4` e collation `utf8mb4_unicode_ci`;
- seleciona o banco com `USE dentalFlow`;
- cria as tabelas usando `ENGINE=InnoDB`.

**Atenção:** o `DROP DATABASE` é destrutivo. Não execute o script completo em desenvolvimento compartilhado, homologação ou produção sem autorização e backup adequado.

## 3. Tabelas

### 3.1 `clinica`

| Coluna | Tipo | Restrições |
|---|---|---|
| `id_clinica` | `INT` | PK, `NOT NULL`, `AUTO_INCREMENT` |
| `nome` | `VARCHAR(100)` | `NOT NULL` |
| `cnpj` | `VARCHAR(18)` | Índice único `uk_clinica_cnpj`; aceita `NULL` |
| `endereco` | `VARCHAR(150)` | Opcional |
| `telefone` | `VARCHAR(20)` | Opcional |

### 3.2 `usuario`

| Coluna | Tipo | Restrições |
|---|---|---|
| `id_usuario` | `INT` | PK, `NOT NULL`, `AUTO_INCREMENT` |
| `id_clinica` | `INT` | `NOT NULL`, FK para `clinica.id_clinica` |
| `nome` | `VARCHAR(100)` | `NOT NULL` |
| `email` | `VARCHAR(100)` | `NOT NULL`, único por `uk_usuario_email` |
| `senha_hash` | `VARCHAR(255)` | `NOT NULL` |
| `nivel_acesso` | `VARCHAR(20)` | `NOT NULL` |
| `ativo` | `TINYINT(1)` | Default `1`; não foi declarado `NOT NULL` |

Índice adicional: `idx_usuario_clinica` em `id_clinica`.

### 3.3 `paciente`

| Coluna | Tipo | Restrições |
|---|---|---|
| `id_paciente` | `INT` | PK, `NOT NULL`, `AUTO_INCREMENT` |
| `id_clinica` | `INT` | `NOT NULL`, FK para `clinica.id_clinica` |
| `nome` | `VARCHAR(100)` | `NOT NULL` |
| `cpf` | `VARCHAR(14)` | `NOT NULL` |
| `telefone` | `VARCHAR(20)` | Opcional |
| `email` | `VARCHAR(100)` | Opcional |
| `data_nascimento` | `DATE` | Opcional |

Restrição única composta: `uk_paciente_clinica_cpf` em (`id_clinica`, `cpf`). Índice adicional: `idx_paciente_clinica` em `id_clinica`.

### 3.4 `dentista`

| Coluna | Tipo | Restrições |
|---|---|---|
| `id_dentista` | `INT` | PK, `NOT NULL`, `AUTO_INCREMENT` |
| `id_clinica` | `INT` | `NOT NULL`, FK para `clinica.id_clinica` |
| `nome` | `VARCHAR(100)` | `NOT NULL` |
| `cro` | `VARCHAR(20)` | `NOT NULL`, único por `uk_dentista_cro` |
| `especialidade` | `VARCHAR(50)` | Opcional |
| `telefone` | `VARCHAR(20)` | Opcional |

Índice adicional: `idx_dentista_clinica` em `id_clinica`.

### 3.5 `procedimento`

| Coluna | Tipo | Restrições |
|---|---|---|
| `id_procedimento` | `INT` | PK, `NOT NULL`, `AUTO_INCREMENT` |
| `id_clinica` | `INT` | `NOT NULL`, FK para `clinica.id_clinica` |
| `nome` | `VARCHAR(100)` | `NOT NULL` |
| `valor` | `DECIMAL(10,2)` | Opcional |
| `duracao_minutos` | `INT` | Opcional |

Índice adicional: `idx_procedimento_clinica` em `id_clinica`.

### 3.6 `agendamento`

| Coluna | Tipo | Restrições |
|---|---|---|
| `id_agendamento` | `INT` | PK, `NOT NULL`, `AUTO_INCREMENT` |
| `id_clinica` | `INT` | `NOT NULL`, FK para `clinica.id_clinica` |
| `id_paciente` | `INT` | `NOT NULL`, FK para `paciente.id_paciente` |
| `id_dentista` | `INT` | `NOT NULL`, FK para `dentista.id_dentista` |
| `id_procedimento` | `INT` | `NOT NULL`, FK para `procedimento.id_procedimento` |
| `id_usuario` | `INT` | Opcional, FK para `usuario.id_usuario` |
| `data_hora` | `DATETIME` | `NOT NULL` |
| `status` | `VARCHAR(20)` | `NOT NULL`, default `'AGENDADO'` |
| `observacoes` | `VARCHAR(255)` | Opcional |

Índices:
- `idx_agend_clinica` em `id_clinica`;
- `idx_agend_paciente` em `id_paciente`;
- `idx_agend_dentista_data` em (`id_dentista`, `data_hora`);
- `idx_agend_procedimento` em `id_procedimento`;
- `idx_agend_usuario` em `id_usuario`.

## 4. Relacionamentos e regras de integridade

| FK | Referência | Regra de exclusão | Regra de atualização |
|---|---|---|---|
| `fk_usuario_clinica` | `usuario.id_clinica` → `clinica.id_clinica` | `RESTRICT` | `CASCADE` |
| `fk_paciente_clinica` | `paciente.id_clinica` → `clinica.id_clinica` | `RESTRICT` | `CASCADE` |
| `fk_dentista_clinica` | `dentista.id_clinica` → `clinica.id_clinica` | `RESTRICT` | `CASCADE` |
| `fk_procedimento_clinica` | `procedimento.id_clinica` → `clinica.id_clinica` | `RESTRICT` | `CASCADE` |
| `fk_agend_clinica` | `agendamento.id_clinica` → `clinica.id_clinica` | `RESTRICT` | `CASCADE` |
| `fk_agend_paciente` | `agendamento.id_paciente` → `paciente.id_paciente` | `RESTRICT` | `CASCADE` |
| `fk_agend_dentista` | `agendamento.id_dentista` → `dentista.id_dentista` | `RESTRICT` | `CASCADE` |
| `fk_agend_procedimento` | `agendamento.id_procedimento` → `procedimento.id_procedimento` | `RESTRICT` | `CASCADE` |
| `fk_agend_usuario` | `agendamento.id_usuario` → `usuario.id_usuario` | `SET NULL` | `CASCADE` |

O schema estabelece as chaves estrangeiras acima. Não estabelece, por si só, regras adicionais como impedir sobreposição de horários de dentistas, validar valores de `status`, ou garantir que paciente, dentista, procedimento e usuário pertençam todos à mesma clínica do agendamento. Essas regras não devem ser presumidas sem requisitos de negócio confirmados.

## 5. Observações importantes para implementação

1. **Escopo por clínica:** várias tabelas contêm `id_clinica`. Ao consultar ou modificar dados, confirme os requisitos de isolamento entre clínicas. Não presuma que a presença da coluna, sozinha, implemente autorização ou isolamento.
2. **Unicidade:** `usuario.email` é único globalmente no DDL. `paciente.cpf` é único por clínica. `dentista.cro` é único globalmente. `clinica.cnpj` possui índice único e aceita `NULL`.
3. **Valores nulos:** colunas sem `NOT NULL` podem aceitar `NULL`, salvo restrições adicionais não exibidas no script. Não transforme automaticamente esses campos em obrigatórios na API.
4. **Tipos Java:** confirme o mapeamento adequado para `INT`, `TINYINT(1)`, `DECIMAL(10,2)`, `DATE` e `DATETIME` no padrão já usado pelo projeto. Não altere os tipos públicos da API sem verificar os contratos existentes.
5. **Nomes físicos:** o DDL usa nomes de tabelas e colunas em português e `snake_case`. Confirme como as entidades JPA mapeiam esses nomes.
6. **Banco configurado:** o documento de arquitetura registra uma URL JDBC com `dentalflow` em minúsculas, enquanto o script cria `dentalFlow`. Verifique o comportamento do ambiente MySQL e a configuração efetiva antes de concluir que são equivalentes.
7. **JPA versus DDL:** a análise original do código encontrou `UserEntity` com atributos como `nome`, `email`, `password` e `clinicId`, enquanto o DDL define `usuario` com `senha_hash` e `id_clinica`. Essa diferença precisa ser conciliada no mapeamento; não presuma que o schema atual e a entidade estejam alinhados.
8. **Segurança:** `senha_hash` é dado sensível. Não exponha esse campo em DTOs de resposta, logs ou mensagens de erro. A estratégia de autenticação e autorização deve ser confirmada no código e nos requisitos.

## 6. Resumo dos relacionamentos

- `clinica` é referenciada por `usuario`, `paciente`, `dentista`, `procedimento` e `agendamento`.
- `agendamento` referencia `paciente`, `dentista`, `procedimento` e, opcionalmente, `usuario`.
- O schema não declara relacionamento direto entre `paciente` e `dentista`, nem entre `procedimento` e `dentista`.

## 7. Orientação para Skills de implementação e auditoria

Antes de implementar ou revisar um endpoint que acessa dados:

1. Consulte `ARCHITECTURE_REFERENCE.md` para identificar o fluxo de camadas e os padrões de código.
2. Consulte este documento para verificar nomes, tipos e restrições declarados no DDL.
3. Confirme a correspondência com entidades JPA, repositories e consultas existentes.
4. Se houver divergência entre o DDL, o código e o banco em execução, registre a divergência e peça esclarecimento antes de assumir uma solução.
5. Não execute o script de criação completo automaticamente: ele contém `DROP DATABASE IF EXISTS dentalFlow`.
