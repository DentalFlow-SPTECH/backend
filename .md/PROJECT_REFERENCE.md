# DentalFlow — Project Reference

> Documento de referência do projeto. Serve como fonte única de contexto para a equipe e para qualquer IA que ajude no desenvolvimento.
> Banco de dados: MySQL (`dentalFlow`). Arquivo de schema: `schema.sql`.

---

## 1. Visão geral

O **DentalFlow** é um sistema web para uma clínica odontológica, com dois focos principais:

1. **Agendamento** e tudo o que gira em torno dele: pacientes, dentistas, procedimentos e retornos.
2. **Financeiro**: dar visibilidade de *por que* e *para onde* o dinheiro da clínica está indo.

O sistema **não** pretende digitalizar tudo. Alguns processos continuam no papel por vontade do cliente. O objetivo é que, no fim do mês, a gestão consiga abrir um dashboard e enxergar o que precisa mudar.

---

## 2. Problema

O maior problema da clínica é **financeiro**: perda de dinheiro na gestão, causada por processos manuais, escritos em papel e pouco seguros.

| # | Problema | Consequência |
|---|----------|--------------|
| P1 | Equipamentos e produtos usados no dia a dia **não são computados** | Custo real invisível; dinheiro some sem rastro |
| P2 | Processos feitos a mão, em papel | Erros de soma, perda de folhas, falta de histórico e de segurança |
| P3 | Dentistas **não registram o antes e depois** dos procedimentos | O convênio exige esse registro para pagar; sem ele, o procedimento não é pago |
| P4 | Falta de visão consolidada | A gestão não consegue saber onde está o prejuízo |
| P5 | Agenda e retornos sem controle centralizado | Faltas, retornos esquecidos e horários ociosos |

---

## 3. Objetivos

- Centralizar **agendamentos**, com procedimentos e retornos.
- Digitalizar o **Mapa Clínico Diário** (hoje em papel) para alimentar o financeiro.
- Controlar o **consumo de produtos e o uso de equipamentos**, para que o custo real apareça.
- **Reduzir glosas do convênio**, exigindo o registro de antes e depois.
- Entregar um **dashboard mensal** que mostre receita, despesas, descontos e onde está o vazamento de dinheiro.

---

## 4. Escopo

### 4.1 Dentro do escopo
- Cadastro de clínica, usuários, pacientes, dentistas e procedimentos.
- Agenda, com procedimentos e retornos.
- Mapa Clínico Diário digital (atendimentos, vales/cheques, despesas e escala da equipe).
- Controle de insumos (produtos) e equipamentos.
- Registro de antes e depois, com alerta de pendência.
- Dashboard mensal responsivo.

### 4.2 Fora do escopo (continua no papel)
- **Orçamentos**: o cliente quer manter no papel. Por isso, `qtd_orcamentos`, `qtd_orcamentos_contratados`, `qtd_orcamentos_perdidos` e `numero_orcamento` **foram removidos** do schema.
- Outros processos que o cliente pedir para manter em papel. A regra é: o sistema recebe o **resultado** do processo, não o substitui à força.

---

## 5. Módulos

### 5.1 Agendamento
**Responsividade: não obrigatória.** Uso principal pela recepção, em computador.

Funcionalidades:
- Criar, remarcar, cancelar e concluir agendamentos.
- Agenda por dentista e por dia (índice `idx_agend_dentista_data`).
- Cada agendamento tem **paciente, dentista, procedimento, data/hora e status**.
- **Retornos**: um procedimento pode exigir retorno (ex.: prova, entrega, revisão). Ao concluir o atendimento, o sistema sugere ou cria o agendamento de retorno.
- Prevenção de conflito de horário para o mesmo dentista.
- Status sugeridos: `AGENDADO`, `CONFIRMADO`, `EM_ATENDIMENTO`, `CONCLUIDO`, `CANCELADO`, `FALTOU`.

### 5.2 Mapa Clínico Diário (financeiro operacional)
Um mapa por dentista por dia, preenchido durante o expediente e **somado no fim do mês**.

Seções do mapa:
- **Atendimentos**: ficha, paciente, horários (agendado, entrada, saída, retorno), código de pagamento, valor, tipo (`CLINICO`, `PROTESE`, `PEDIATRIA_CIRURGIA`) e conferido.
- **Vales, cheques e outros**: responsável, ficha, valor, descrição, conferido.
- **Despesas do dia**: descrição e valor.
- **Escala da equipe**: nome, função, início, saída/volta do almoço e final.
- **Fechamento**: totais bruto, descontos (cartões, cheque pré, cheque à vista, vales, outros), líquido, saldo final e contadores (pacientes, moldagens, mordidas, provas, entregas, repetições).
- **Assinaturas**: atendente, conferente e escritório (com data de conferência).

> O `atendimento` pode ser ligado ao `agendamento` (`id_agendamento`). Isso permite comparar o que foi marcado com o que aconteceu, e achar faltas e perdas.

### 5.3 Controle de insumos e equipamentos (novo)
Ataca diretamente o problema P1.

- **Insumos/produtos**: cadastro com custo unitário e estoque mínimo.
- **Consumo por atendimento**: ao registrar um atendimento, informa-se quais insumos foram usados e em que quantidade. Pode haver um **kit padrão por procedimento**, para reduzir digitação.
- **Entradas de estoque**: compras e reposições.
- **Equipamentos**: cadastro com valor de aquisição, vida útil e manutenções. O custo de uso entra no custo do procedimento.
- **Custo real do procedimento** = insumos + uso de equipamento (+ outros custos definidos). Comparado ao valor cobrado, mostra a **margem**.

### 5.4 Registro de antes e depois (novo)
Ataca diretamente o problema P3.

- Cada procedimento que o **convênio** exige ter antes/depois é marcado como `exige_antes_depois`.
- O dentista anexa **foto do antes** e **foto do depois** ao atendimento.
- **Regra de bloqueio/alerta**: o atendimento só vai para `CONCLUIDO` sem pendência se as duas fotos estiverem anexadas. Caso contrário, ele entra na lista de **pendências de documentação**.
- Painel de pendências por dentista, para a gestão cobrar.
- Indicador no dashboard: **valor em risco** (procedimentos concluídos sem registro, que o convênio pode negar).

### 5.5 Dashboard mensal
**Responsividade: sim.** A gestão deve conseguir consultar pelo celular.

Visão de fim de mês, com filtros por **mês, clínica e dentista**:

| Bloco | Indicadores |
|-------|-------------|
| Receita | Bruto clínico, bruto pediatria/cirurgia, bruto geral, total prótese |
| Saídas | Despesas, descontos por tipo (cartões, cheques, vales, outros) |
| Resultado | Total líquido, saldo final, evolução dia a dia |
| Operação | Pacientes atendidos, moldagens, mordidas, provas, entregas, repetições, faltas |
| Custos | Consumo de insumos, custo por procedimento, margem |
| Documentação | Procedimentos sem antes/depois, valor em risco com convênio |
| Alertas | "O que precisa mudar": maiores desvios e piores indicadores do mês |

Base de dados atual: view `vw_dashboard_mensal`, que soma `mapa_diario` por clínica, dentista, ano e mês. Será estendida com custos e pendências.

---

## 6. Perfis de acesso

Login com **usuário e senha** (sem e-mail). O campo `nivel_acesso` define o perfil.

| Perfil | Pode |
|--------|------|
| `ADMIN` / gestão | Tudo, incluindo o dashboard completo e o cadastro de usuários |
| `DENTISTA` | Ver a própria agenda, registrar atendimento, anexar antes e depois |
| `ATENDENTE` | Agenda, pacientes, preenchimento do mapa diário |
| `CONFERENTE` | Conferir e fechar o mapa diário |
| `ESCRITORIO` | Consultar fechamento e dashboard financeiro |

---

## 7. Regras de negócio

1. Um paciente é único por clínica pelo **CPF**.
2. Paciente exige **CPF, endereço, CEP e bairro**.
3. Usuário não tem e-mail; o `login` é único.
4. Existe **um mapa diário por clínica, dentista e data** (`UNIQUE`).
5. Apagar um mapa apaga seus atendimentos, vales, despesas e escala (`ON DELETE CASCADE`).
6. Atendimento só é considerado **conferido** após o visto do conferente.
7. Procedimento que exige antes/depois **não pode ser concluído sem pendência** sem as duas fotos.
8. Todo consumo de insumo reduz o estoque; abaixo do mínimo, gera alerta.
9. Orçamentos **não são tratados** pelo sistema.
10. Nenhum valor financeiro é alterado ou apagado sem registro de auditoria (ver seção 10).

---

## 8. Modelo de dados

### 8.1 Já existente em `schema.sql`

```
clinica ─┬─ usuario
         ├─ paciente
         ├─ dentista
         ├─ procedimento
         ├─ agendamento (paciente, dentista, procedimento, usuario)
         └─ mapa_diario ─┬─ atendimento (opcional: agendamento, paciente, dentista, procedimento)
                         ├─ vale
                         ├─ despesa
                         └─ escala_equipe

vw_dashboard_mensal  (view: soma mensal de mapa_diario)
```

### 8.2 A criar (propostas)

| Tabela | Finalidade | Campos principais |
|--------|-----------|-------------------|
| `convenio` | Convênios aceitos | `id_convenio`, `id_clinica`, `nome`, `exige_antes_depois` |
| `insumo` | Produtos usados no dia a dia | `id_insumo`, `id_clinica`, `nome`, `unidade`, `custo_unitario`, `estoque_atual`, `estoque_minimo` |
| `movimentacao_insumo` | Entradas e saídas de estoque | `id_movimentacao`, `id_insumo`, `tipo` (`ENTRADA`/`SAIDA`/`AJUSTE`), `quantidade`, `data_movimentacao`, `id_usuario` |
| `procedimento_insumo` | Kit padrão do procedimento | `id_procedimento`, `id_insumo`, `quantidade_padrao` |
| `atendimento_insumo` | Consumo real por atendimento | `id_atendimento`, `id_insumo`, `quantidade`, `custo_unitario_na_data` |
| `equipamento` | Equipamentos da clínica | `id_equipamento`, `id_clinica`, `nome`, `valor_aquisicao`, `vida_util_usos`, `custo_por_uso`, `ativo` |
| `procedimento_equipamento` | Equipamento usado por procedimento | `id_procedimento`, `id_equipamento` |
| `registro_clinico` | Fotos de antes e depois | `id_registro`, `id_atendimento`, `tipo` (`ANTES`/`DEPOIS`), `caminho_arquivo`, `data_envio`, `id_usuario` |
| `auditoria` | Log de alterações financeiras | `id_auditoria`, `tabela`, `id_registro`, `acao`, `valor_anterior`, `valor_novo`, `id_usuario`, `data_hora` |

### 8.3 Ajustes previstos em tabelas existentes

| Tabela | Ajuste |
|--------|--------|
| `procedimento` | `exige_antes_depois TINYINT(1)`, `exige_retorno TINYINT(1)`, `dias_retorno INT`, `custo_estimado DECIMAL(10,2)` |
| `paciente` | `id_convenio` (opcional) e `numero_carteirinha` |
| `agendamento` | `id_agendamento_origem` (liga o retorno ao agendamento que o gerou) |
| `atendimento` | `custo_total DECIMAL(10,2)` e `status_documentacao` (`OK`, `PENDENTE`) |

> Sempre que o mapa diário guardar valores calculados (`bruto_geral`, `total_liquido`, `saldo_final`...), o ideal é **calcular no backend a partir das linhas** e não confiar só na digitação, para reduzir erro humano.

---

## 9. Dashboard: consultas-base

```sql
-- Resumo de um mês
SELECT * FROM vw_dashboard_mensal WHERE ano = 2026 AND mes = 10;

-- Atendimentos sem antes/depois (valor em risco)
SELECT d.nome AS dentista, COUNT(*) AS pendencias, SUM(a.valor) AS valor_em_risco
FROM atendimento a
JOIN procedimento p ON p.id_procedimento = a.id_procedimento
JOIN dentista d     ON d.id_dentista = a.id_dentista
WHERE p.exige_antes_depois = 1
  AND a.status_documentacao = 'PENDENTE'
GROUP BY d.id_dentista;
```

---

## 10. Requisitos não funcionais

- **Segurança**: senha com hash (bcrypt/argon2), nunca em texto puro; controle de acesso por perfil; auditoria das alterações financeiras.
- **Responsividade**: **dashboard responsivo** (mobile-first); agendamento pensado para desktop.
- **Integridade**: chaves estrangeiras, `UNIQUE` e `ON DELETE` definidos no schema.
- **Armazenamento de fotos**: guardar o arquivo em disco ou storage e salvar só o caminho no banco.
- **Privacidade**: dados de pacientes e fotos clínicas são dados pessoais sensíveis (LGPD). Restringir acesso e registrar quem consultou.
- **Desempenho**: índices apenas onde há motivo claro (agenda por dentista e data, mapa por data).

---

## 11. Roadmap sugerido

| Fase | Entrega |
|------|---------|
| 1 | Schema base, login, cadastros (clínica, usuário, paciente, dentista, procedimento) |
| 2 | Agendamento completo, com retornos |
| 3 | Mapa Clínico Diário digital e fechamento |
| 4 | Dashboard mensal responsivo |
| 5 | Controle de insumos e equipamentos, custo por procedimento |
| 6 | Antes e depois, com pendências e valor em risco |
| 7 | Auditoria, refinamentos e testes com o cliente |

> Dica para o projeto acadêmico: as fases 1 a 4 já entregam um sistema completo e demonstrável. As fases 5 e 6 são o diferencial.

---

## 12. Decisões em aberto

- Qual **stack** será usada (backend, frontend, armazenamento de fotos)?
- O consumo de insumos será registrado **por atendimento** (preciso, mais trabalhoso) ou **por kit padrão** (rápido, menos exato)?
- O bloqueio do antes/depois será **rígido** (não conclui sem foto) ou apenas **alerta**?
- Quais **convênios** exigem antes/depois e para quais procedimentos?
- Quem do time pode **editar** um mapa depois de conferido?
- O cliente quer **exportar** o dashboard (PDF/Excel) para reuniões?

---

## 13. Glossário

| Termo | Significado |
|-------|-------------|
| Mapa Clínico Diário | Formulário em papel do dia, hoje preenchido à mão; passa a ser digital |
| Bruto | Valor recebido antes de despesas e descontos |
| Líquido | Valor após despesas e descontos |
| Vale | Adiantamento ou lançamento administrativo |
| Glosa | Recusa de pagamento pelo convênio |
| Antes e depois | Registro fotográfico do procedimento, exigido por convênios |
| Retorno | Nova consulta decorrente de um procedimento (prova, entrega, revisão) |
