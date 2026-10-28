USE dentalFlow;

START TRANSACTION;

-- Clínica usada na demonstração
INSERT INTO clinica (nome, cnpj, endereco, telefone)
VALUES ('DentalFlow Clínica Demo', '12.345.678/0001-90', 'Rua das Flores, 100', '(11) 4000-1000')
ON DUPLICATE KEY UPDATE
    id_clinica = LAST_INSERT_ID(id_clinica),
    nome = VALUES(nome),
    endereco = VALUES(endereco),
    telefone = VALUES(telefone);
SET @clinic_id = LAST_INSERT_ID();

-- Dentista vinculado ao usuário principal
INSERT INTO dentista (id_clinica, nome, cro, especialidade, telefone)
VALUES (@clinic_id, 'Gabriel Fontes', 'CRO-SP 99999', 'Clínico Geral', '(11) 99999-1000')
ON DUPLICATE KEY UPDATE
    id_dentista = LAST_INSERT_ID(id_dentista),
    id_clinica = VALUES(id_clinica),
    nome = VALUES(nome),
    especialidade = VALUES(especialidade),
    telefone = VALUES(telefone);
SET @dentist_id = LAST_INSERT_ID();

-- Login: gabrielfontes / senha: 1234
INSERT INTO usuario (id_clinica, id_dentista, nome, login, senha_hash, nivel_acesso, ativo)
VALUES (
    @clinic_id,
    @dentist_id,
    'Gabriel Fontes',
    'gabrielfontes',
    '$2a$10$ilRtzZKHEXbc.FNDHbM6X.rbN8WuPEUAS5sqNOihGTQXzhWT09sFS',
    'DENTISTA',
    1
)
ON DUPLICATE KEY UPDATE
    id_usuario = LAST_INSERT_ID(id_usuario),
    id_clinica = VALUES(id_clinica),
    id_dentista = VALUES(id_dentista),
    nome = VALUES(nome),
    senha_hash = VALUES(senha_hash),
    nivel_acesso = VALUES(nivel_acesso),
    ativo = VALUES(ativo);
SET @user_id = LAST_INSERT_ID();

-- Procedimentos
INSERT INTO procedimento (id_clinica, nome, valor, duracao_minutos)
SELECT @clinic_id, 'Consulta e avaliação', 150.00, 30
WHERE NOT EXISTS (
    SELECT 1 FROM procedimento WHERE id_clinica = @clinic_id AND nome = 'Consulta e avaliação'
);
INSERT INTO procedimento (id_clinica, nome, valor, duracao_minutos)
SELECT @clinic_id, 'Limpeza', 250.00, 45
WHERE NOT EXISTS (
    SELECT 1 FROM procedimento WHERE id_clinica = @clinic_id AND nome = 'Limpeza'
);
INSERT INTO procedimento (id_clinica, nome, valor, duracao_minutos)
SELECT @clinic_id, 'Prótese dentária', 800.00, 60
WHERE NOT EXISTS (
    SELECT 1 FROM procedimento WHERE id_clinica = @clinic_id AND nome = 'Prótese dentária'
);
SET @procedure_consultation = (
    SELECT id_procedimento FROM procedimento
    WHERE id_clinica = @clinic_id AND nome = 'Consulta e avaliação' LIMIT 1
);
SET @procedure_cleaning = (
    SELECT id_procedimento FROM procedimento
    WHERE id_clinica = @clinic_id AND nome = 'Limpeza' LIMIT 1
);
SET @procedure_prosthesis = (
    SELECT id_procedimento FROM procedimento
    WHERE id_clinica = @clinic_id AND nome = 'Prótese dentária' LIMIT 1
);

-- Pacientes
INSERT INTO paciente (id_clinica, nome, cpf, endereco, bairro, cep, telefone, email, data_nascimento)
VALUES (@clinic_id, 'Maria Silva', '111.111.111-11', 'Rua A, 10', 'Centro', '01001-000',
        '(11) 98888-1001', 'maria@example.com', '1990-05-10')
ON DUPLICATE KEY UPDATE id_paciente = LAST_INSERT_ID(id_paciente), nome = VALUES(nome);
SET @patient_maria = LAST_INSERT_ID();

INSERT INTO paciente (id_clinica, nome, cpf, endereco, bairro, cep, telefone, email, data_nascimento)
VALUES (@clinic_id, 'João Santos', '222.222.222-22', 'Rua B, 20', 'Jardins', '01401-000',
        '(11) 98888-1002', 'joao@example.com', '1985-08-22')
ON DUPLICATE KEY UPDATE id_paciente = LAST_INSERT_ID(id_paciente), nome = VALUES(nome);
SET @patient_joao = LAST_INSERT_ID();

INSERT INTO paciente (id_clinica, nome, cpf, endereco, bairro, cep, telefone, email, data_nascimento)
VALUES (@clinic_id, 'Ana Oliveira', '333.333.333-33', 'Rua C, 30', 'Moema', '04001-000',
        '(11) 98888-1003', 'ana@example.com', '2000-02-15')
ON DUPLICATE KEY UPDATE id_paciente = LAST_INSERT_ID(id_paciente), nome = VALUES(nome);
SET @patient_ana = LAST_INSERT_ID();

-- Agenda de outubro de 2026 para alimentar o funil da dashboard
INSERT INTO agendamento
    (id_clinica, id_paciente, id_dentista, id_procedimento, id_usuario, data_hora, status, observacoes)
SELECT @clinic_id, @patient_maria, @dentist_id, @procedure_cleaning, @user_id,
       '2026-10-28 09:00:00', 'CONCLUIDO', 'Atendimento finalizado'
WHERE NOT EXISTS (
    SELECT 1 FROM agendamento
    WHERE id_dentista = @dentist_id AND data_hora = '2026-10-28 09:00:00'
);
SET @appointment_maria = (
    SELECT id_agendamento FROM agendamento
    WHERE id_dentista = @dentist_id AND data_hora = '2026-10-28 09:00:00' LIMIT 1
);

INSERT INTO agendamento
    (id_clinica, id_paciente, id_dentista, id_procedimento, id_usuario, data_hora, status, observacoes)
SELECT @clinic_id, @patient_joao, @dentist_id, @procedure_prosthesis, @user_id,
       '2026-10-28 10:30:00', 'CONCLUIDO', 'Primeira etapa da prótese'
WHERE NOT EXISTS (
    SELECT 1 FROM agendamento
    WHERE id_dentista = @dentist_id AND data_hora = '2026-10-28 10:30:00'
);
SET @appointment_joao = (
    SELECT id_agendamento FROM agendamento
    WHERE id_dentista = @dentist_id AND data_hora = '2026-10-28 10:30:00' LIMIT 1
);

INSERT INTO agendamento
    (id_clinica, id_paciente, id_dentista, id_procedimento, id_usuario, data_hora, status, observacoes)
SELECT @clinic_id, @patient_ana, @dentist_id, @procedure_consultation, @user_id,
       '2026-10-29 14:00:00', 'CONFIRMADO', 'Paciente confirmou por telefone'
WHERE NOT EXISTS (
    SELECT 1 FROM agendamento
    WHERE id_dentista = @dentist_id AND data_hora = '2026-10-29 14:00:00'
);

INSERT INTO agendamento
    (id_clinica, id_paciente, id_dentista, id_procedimento, id_usuario, data_hora, status, observacoes)
SELECT @clinic_id, @patient_maria, @dentist_id, @procedure_consultation, @user_id,
       '2026-10-30 11:00:00', 'AGENDADO', 'Retorno de acompanhamento'
WHERE NOT EXISTS (
    SELECT 1 FROM agendamento
    WHERE id_dentista = @dentist_id AND data_hora = '2026-10-30 11:00:00'
);

INSERT INTO agendamento
    (id_clinica, id_paciente, id_dentista, id_procedimento, id_usuario, data_hora, status, observacoes)
SELECT @clinic_id, @patient_joao, @dentist_id, @procedure_cleaning, @user_id,
       '2026-10-26 16:00:00', 'FALTOU', 'Paciente não compareceu'
WHERE NOT EXISTS (
    SELECT 1 FROM agendamento
    WHERE id_dentista = @dentist_id AND data_hora = '2026-10-26 16:00:00'
);

-- Inventário de produtos
INSERT INTO insumo (id_clinica, nome, unidade, custo_unitario, estoque_atual, estoque_minimo, ativo)
VALUES (@clinic_id, 'Luva descartável', 'CAIXA', 35.00, 12.000, 5.000, 1)
ON DUPLICATE KEY UPDATE id_insumo = LAST_INSERT_ID(id_insumo), estoque_atual = VALUES(estoque_atual);
SET @supply_gloves = LAST_INSERT_ID();

INSERT INTO insumo (id_clinica, nome, unidade, custo_unitario, estoque_atual, estoque_minimo, ativo)
VALUES (@clinic_id, 'Anestésico', 'TUBETE', 4.50, 8.000, 10.000, 1)
ON DUPLICATE KEY UPDATE id_insumo = LAST_INSERT_ID(id_insumo), estoque_atual = VALUES(estoque_atual);
SET @supply_anesthetic = LAST_INSERT_ID();

INSERT INTO insumo (id_clinica, nome, unidade, custo_unitario, estoque_atual, estoque_minimo, ativo)
VALUES (@clinic_id, 'Máscara cirúrgica', 'CAIXA', 22.00, 20.000, 5.000, 1)
ON DUPLICATE KEY UPDATE id_insumo = LAST_INSERT_ID(id_insumo), estoque_atual = VALUES(estoque_atual);
SET @supply_masks = LAST_INSERT_ID();

INSERT INTO movimentacao_insumo
    (id_insumo, id_usuario, tipo, quantidade, custo_unitario_na_data, motivo, data_movimentacao)
SELECT @supply_gloves, @user_id, 'ENTRADA', 12.000, 35.00, 'Carga inicial demonstrativa', '2026-10-28 08:00:00'
WHERE NOT EXISTS (
    SELECT 1 FROM movimentacao_insumo
    WHERE id_insumo = @supply_gloves AND motivo = 'Carga inicial demonstrativa'
);
INSERT INTO movimentacao_insumo
    (id_insumo, id_usuario, tipo, quantidade, custo_unitario_na_data, motivo, data_movimentacao)
SELECT @supply_anesthetic, @user_id, 'ENTRADA', 8.000, 4.50, 'Carga inicial demonstrativa', '2026-10-28 08:00:00'
WHERE NOT EXISTS (
    SELECT 1 FROM movimentacao_insumo
    WHERE id_insumo = @supply_anesthetic AND motivo = 'Carga inicial demonstrativa'
);

-- Equipamentos
INSERT INTO equipamento
    (id_clinica, nome, descricao, valor_aquisicao, vida_util_usos, usos_realizados, custo_por_uso, ativo)
VALUES (@clinic_id, 'Raio X digital', 'Equipamento de radiografia digital', 30000.00, 10000, 850, 3.00, 1)
ON DUPLICATE KEY UPDATE descricao = VALUES(descricao), usos_realizados = VALUES(usos_realizados);
INSERT INTO equipamento
    (id_clinica, nome, descricao, valor_aquisicao, vida_util_usos, usos_realizados, custo_por_uso, ativo)
VALUES (@clinic_id, 'Autoclave', 'Esterilização dos instrumentos', 12000.00, 5000, 1250, 2.40, 1)
ON DUPLICATE KEY UPDATE descricao = VALUES(descricao), usos_realizados = VALUES(usos_realizados);

-- Mapa diário de 28/10/2026
INSERT INTO mapa_diario (
    id_clinica, id_dentista, id_usuario_atendente, id_usuario_conferente,
    data_mapa, dia_semana, data_conferencia,
    total_clinico, total_protese, bruto_clinico, bruto_pediatria_cirurgia,
    bruto_geral, total_despesas, valor_liquido,
    desconto_cartoes, desconto_cheque_pre, desconto_cheque_vista,
    desconto_vales, desconto_outros, total_liquido, saldo_final,
    qtd_pacientes, qtd_moldagens, qtd_mordidas, qtd_provas,
    qtd_entregas, qtd_repeticoes, observacoes, status, data_fechamento
)
VALUES (
    @clinic_id, @dentist_id, @user_id, @user_id,
    '2026-10-28', 'QUARTA-FEIRA', '2026-10-28',
    350.00, 500.00, 850.00, 0.00,
    850.00, 120.00, 800.00,
    20.00, 0.00, 0.00,
    30.00, 0.00, 800.00, 680.00,
    2, 1, 0, 1,
    0, 0, 'Mapa demonstrativo com dois atendimentos', 'FECHADO', '2026-10-28 18:00:00'
)
ON DUPLICATE KEY UPDATE
    id_mapa_diario = LAST_INSERT_ID(id_mapa_diario),
    bruto_geral = VALUES(bruto_geral),
    total_liquido = VALUES(total_liquido),
    saldo_final = VALUES(saldo_final),
    observacoes = VALUES(observacoes);
SET @map_id = LAST_INSERT_ID();

INSERT INTO atendimento (
    id_mapa_diario, id_agendamento, id_paciente, id_dentista, id_procedimento,
    numero_linha, numero_ficha, nome_paciente, hora_agendada, hora_entrada,
    hora_saida, codigo_pagamento, valor, tipo_atendimento, conferido
)
VALUES (
    @map_id, @appointment_maria, @patient_maria, @dentist_id, @procedure_cleaning,
    1, 'F-1001', 'Maria Silva', '09:00:00', '09:05:00',
    '09:50:00', 'PIX', 350.00, 'CLINICO', 1
)
ON DUPLICATE KEY UPDATE valor = VALUES(valor), conferido = VALUES(conferido);

INSERT INTO atendimento (
    id_mapa_diario, id_agendamento, id_paciente, id_dentista, id_procedimento,
    numero_linha, numero_ficha, nome_paciente, hora_agendada, hora_entrada,
    hora_saida, codigo_pagamento, valor, tipo_atendimento, conferido
)
VALUES (
    @map_id, @appointment_joao, @patient_joao, @dentist_id, @procedure_prosthesis,
    2, 'F-1002', 'João Santos', '10:30:00', '10:35:00',
    '11:30:00', 'CARTAO', 500.00, 'PROTESE', 1
)
ON DUPLICATE KEY UPDATE valor = VALUES(valor), conferido = VALUES(conferido);

INSERT INTO vale (id_mapa_diario, nome_responsavel, numero_ficha, valor, descricao, conferido)
SELECT @map_id, 'João Santos', 'F-1002', 30.00, 'Vale concedido no atendimento', 1
WHERE NOT EXISTS (
    SELECT 1 FROM vale WHERE id_mapa_diario = @map_id AND numero_ficha = 'F-1002'
);

INSERT INTO despesa (id_mapa_diario, descricao, valor)
SELECT @map_id, 'Compra emergencial de materiais', 120.00
WHERE NOT EXISTS (
    SELECT 1 FROM despesa
    WHERE id_mapa_diario = @map_id AND descricao = 'Compra emergencial de materiais'
);

INSERT INTO escala_equipe (
    id_mapa_diario, id_usuario, nome_funcionario, funcao,
    hora_inicio, hora_saida_almoco, hora_volta_almoco, hora_final
)
SELECT @map_id, @user_id, 'Gabriel Fontes', 'DENTISTA',
       '08:00:00', '12:00:00', '13:00:00', '18:00:00'
WHERE NOT EXISTS (
    SELECT 1 FROM escala_equipe WHERE id_mapa_diario = @map_id AND id_usuario = @user_id
);

-- Segundo dia para tornar os gráficos mensais mais úteis
INSERT INTO mapa_diario (
    id_clinica, id_dentista, id_usuario_atendente, id_usuario_conferente,
    data_mapa, dia_semana, data_conferencia,
    total_clinico, total_protese, bruto_clinico, bruto_pediatria_cirurgia,
    bruto_geral, total_despesas, valor_liquido,
    desconto_cartoes, desconto_cheque_pre, desconto_cheque_vista,
    desconto_vales, desconto_outros, total_liquido, saldo_final,
    qtd_pacientes, qtd_moldagens, qtd_mordidas, qtd_provas,
    qtd_entregas, qtd_repeticoes, observacoes, status, data_fechamento
)
VALUES (
    @clinic_id, @dentist_id, @user_id, @user_id,
    '2026-10-27', 'TERÇA-FEIRA', '2026-10-27',
    600.00, 0.00, 600.00, 250.00,
    850.00, 100.00, 840.00,
    10.00, 0.00, 0.00,
    0.00, 0.00, 840.00, 740.00,
    2, 0, 1, 0,
    1, 0, 'Segundo dia demonstrativo para os gráficos', 'FECHADO', '2026-10-27 18:00:00'
)
ON DUPLICATE KEY UPDATE
    bruto_geral = VALUES(bruto_geral),
    total_liquido = VALUES(total_liquido),
    saldo_final = VALUES(saldo_final),
    observacoes = VALUES(observacoes);

COMMIT;

SELECT
    u.login,
    u.nivel_acesso,
    c.nome AS clinica,
    d.nome AS dentista
FROM usuario u
JOIN clinica c ON c.id_clinica = u.id_clinica
LEFT JOIN dentista d ON d.id_dentista = u.id_dentista
WHERE u.login = 'gabrielfontes';
