-- =====================================================
-- Criação do banco de dados
-- =====================================================
DROP DATABASE IF EXISTS dentalFlow;
CREATE DATABASE IF NOT EXISTS dentalFlow
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE dentalFlow;

-- =====================================================
-- Tabela: clinica
-- =====================================================
CREATE TABLE clinica (
                         id_clinica INT NOT NULL AUTO_INCREMENT,
                         nome       VARCHAR(100) NOT NULL,
                         cnpj       VARCHAR(18),
                         endereco   VARCHAR(150),
                         telefone   VARCHAR(20),
                         PRIMARY KEY (id_clinica),
                         UNIQUE KEY uk_clinica_cnpj (cnpj)
) ENGINE=InnoDB;

-- =====================================================
-- Tabela: usuario (login e senha, sem e-mail)
-- =====================================================
CREATE TABLE usuario (
                         id_usuario   INT NOT NULL AUTO_INCREMENT,
                         id_clinica   INT NOT NULL,
                         id_dentista  INT NULL,
                         nome         VARCHAR(100) NOT NULL,
                         login        VARCHAR(50) NOT NULL,
                         senha_hash   VARCHAR(255) NOT NULL,
                         nivel_acesso VARCHAR(20) NOT NULL,
                         ativo        TINYINT(1) NOT NULL DEFAULT 1,
                         PRIMARY KEY (id_usuario),
                         UNIQUE KEY uk_usuario_login (login),
                         UNIQUE KEY uk_usuario_dentista (id_dentista),
                         CONSTRAINT chk_usuario_persona CHECK (
                             (nivel_acesso = 'DENTISTA' AND id_dentista IS NOT NULL)
                             OR (nivel_acesso = 'RECEPCIONISTA' AND id_dentista IS NULL)
                         ),
                         KEY idx_usuario_clinica (id_clinica),
                         CONSTRAINT fk_usuario_clinica
                             FOREIGN KEY (id_clinica) REFERENCES clinica (id_clinica)
                                 ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =====================================================
-- Tabela: paciente (CPF, endereço, CEP e bairro obrigatórios)
-- =====================================================
CREATE TABLE paciente (
                          id_paciente     INT NOT NULL AUTO_INCREMENT,
                          id_clinica      INT NOT NULL,
                          nome            VARCHAR(100) NOT NULL,
                          cpf             VARCHAR(14) NOT NULL,
                          endereco        VARCHAR(150) NOT NULL,
                          bairro          VARCHAR(80) NOT NULL,
                          cep             VARCHAR(9) NOT NULL,
                          telefone        VARCHAR(20),
                          email           VARCHAR(100),
                          data_nascimento DATE,
                          PRIMARY KEY (id_paciente),
                          UNIQUE KEY uk_paciente_clinica_cpf (id_clinica, cpf),
                          KEY idx_paciente_clinica (id_clinica),
                          CONSTRAINT fk_paciente_clinica
                              FOREIGN KEY (id_clinica) REFERENCES clinica (id_clinica)
                                  ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =====================================================
-- Tabela: dentista
-- =====================================================
CREATE TABLE dentista (
                          id_dentista   INT NOT NULL AUTO_INCREMENT,
                          id_clinica    INT NOT NULL,
                          nome          VARCHAR(100) NOT NULL,
                          cro           VARCHAR(20) NOT NULL,
                          especialidade VARCHAR(50),
                          telefone      VARCHAR(20),
                          PRIMARY KEY (id_dentista),
                          UNIQUE KEY uk_dentista_cro (cro),
                          KEY idx_dentista_clinica (id_clinica),
                          CONSTRAINT fk_dentista_clinica
                              FOREIGN KEY (id_clinica) REFERENCES clinica (id_clinica)
                                  ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

ALTER TABLE usuario
    ADD CONSTRAINT fk_usuario_dentista
        FOREIGN KEY (id_dentista) REFERENCES dentista (id_dentista)
            ON DELETE RESTRICT ON UPDATE CASCADE;

-- =====================================================
-- Tabela: procedimento
-- =====================================================
CREATE TABLE procedimento (
                              id_procedimento INT NOT NULL AUTO_INCREMENT,
                              id_clinica      INT NOT NULL,
                              nome            VARCHAR(100) NOT NULL,
                              valor           DECIMAL(10,2),
                              duracao_minutos INT,
                              PRIMARY KEY (id_procedimento),
                              KEY idx_procedimento_clinica (id_clinica),
                              CONSTRAINT fk_procedimento_clinica
                                  FOREIGN KEY (id_clinica) REFERENCES clinica (id_clinica)
                                      ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =====================================================
-- Tabela: insumo (produtos e materiais de consumo)
-- =====================================================
CREATE TABLE insumo (
                        id_insumo      INT NOT NULL AUTO_INCREMENT,
                        id_clinica     INT NOT NULL,
                        nome           VARCHAR(100) NOT NULL,
                        unidade        VARCHAR(20) NOT NULL,
                        custo_unitario DECIMAL(12,2) NOT NULL DEFAULT 0,
                        estoque_atual  DECIMAL(12,3) NOT NULL DEFAULT 0,
                        estoque_minimo DECIMAL(12,3) NOT NULL DEFAULT 0,
                        ativo          TINYINT(1) NOT NULL DEFAULT 1,
                        PRIMARY KEY (id_insumo),
                        UNIQUE KEY uk_insumo_clinica_nome (id_clinica, nome),
                        KEY idx_insumo_clinica_ativo (id_clinica, ativo),
                        CONSTRAINT fk_insumo_clinica
                            FOREIGN KEY (id_clinica) REFERENCES clinica (id_clinica)
                                ON DELETE RESTRICT ON UPDATE CASCADE,
                        CONSTRAINT chk_insumo_valores CHECK (
                            custo_unitario >= 0 AND estoque_atual >= 0 AND estoque_minimo >= 0
                        )
) ENGINE=InnoDB;

-- =====================================================
-- Tabela: movimentacao_insumo (histórico imutável de estoque)
-- =====================================================
CREATE TABLE movimentacao_insumo (
                                     id_movimentacao       INT NOT NULL AUTO_INCREMENT,
                                     id_insumo             INT NOT NULL,
                                     id_usuario            INT NOT NULL,
                                     tipo                  VARCHAR(10) NOT NULL,
                                     quantidade            DECIMAL(12,3) NOT NULL,
                                     custo_unitario_na_data DECIMAL(12,2) NOT NULL DEFAULT 0,
                                     motivo                VARCHAR(150),
                                     data_movimentacao     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                     PRIMARY KEY (id_movimentacao),
                                     KEY idx_mov_insumo_data (id_insumo, data_movimentacao),
                                     CONSTRAINT fk_mov_insumo
                                         FOREIGN KEY (id_insumo) REFERENCES insumo (id_insumo)
                                             ON DELETE RESTRICT ON UPDATE CASCADE,
                                     CONSTRAINT fk_mov_usuario
                                         FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario)
                                             ON DELETE RESTRICT ON UPDATE CASCADE,
                                     CONSTRAINT chk_mov_tipo CHECK (tipo IN ('ENTRADA', 'SAIDA', 'AJUSTE')),
                                     CONSTRAINT chk_mov_quantidade CHECK (quantidade >= 0)
) ENGINE=InnoDB;

-- =====================================================
-- Tabela: equipamento
-- =====================================================
CREATE TABLE equipamento (
                             id_equipamento  INT NOT NULL AUTO_INCREMENT,
                             id_clinica      INT NOT NULL,
                             nome            VARCHAR(100) NOT NULL,
                             descricao       VARCHAR(255),
                             valor_aquisicao DECIMAL(12,2) NOT NULL DEFAULT 0,
                             vida_util_usos  INT NOT NULL,
                             usos_realizados INT NOT NULL DEFAULT 0,
                             custo_por_uso   DECIMAL(12,2) NOT NULL DEFAULT 0,
                             ativo           TINYINT(1) NOT NULL DEFAULT 1,
                             PRIMARY KEY (id_equipamento),
                             UNIQUE KEY uk_equipamento_clinica_nome (id_clinica, nome),
                             KEY idx_equipamento_clinica_ativo (id_clinica, ativo),
                             CONSTRAINT fk_equipamento_clinica
                                 FOREIGN KEY (id_clinica) REFERENCES clinica (id_clinica)
                                     ON DELETE RESTRICT ON UPDATE CASCADE,
                             CONSTRAINT chk_equipamento_valores CHECK (
                                 valor_aquisicao >= 0 AND vida_util_usos > 0
                                 AND usos_realizados >= 0 AND custo_por_uso >= 0
                             )
) ENGINE=InnoDB;

-- =====================================================
-- Tabela: agendamento
-- =====================================================
CREATE TABLE agendamento (
                             id_agendamento  INT NOT NULL AUTO_INCREMENT,
                             id_clinica      INT NOT NULL,
                             id_paciente     INT NOT NULL,
                             id_dentista     INT NOT NULL,
                             id_procedimento INT NOT NULL,
                             id_usuario      INT NULL,
                             data_hora       DATETIME NOT NULL,
                             status          VARCHAR(20) NOT NULL DEFAULT 'AGENDADO',
                             observacoes     VARCHAR(255),
                             PRIMARY KEY (id_agendamento),
                             KEY idx_agend_clinica (id_clinica),
                             KEY idx_agend_paciente (id_paciente),
                             KEY idx_agend_dentista_data (id_dentista, data_hora),
                             KEY idx_agend_dashboard (id_clinica, data_hora, id_dentista, status),
                             KEY idx_agend_procedimento (id_procedimento),
                             KEY idx_agend_usuario (id_usuario),
                             CONSTRAINT fk_agend_clinica
                                 FOREIGN KEY (id_clinica) REFERENCES clinica (id_clinica)
                                     ON DELETE RESTRICT ON UPDATE CASCADE,
                             CONSTRAINT fk_agend_paciente
                                 FOREIGN KEY (id_paciente) REFERENCES paciente (id_paciente)
                                     ON DELETE RESTRICT ON UPDATE CASCADE,
                             CONSTRAINT fk_agend_dentista
                                 FOREIGN KEY (id_dentista) REFERENCES dentista (id_dentista)
                                     ON DELETE RESTRICT ON UPDATE CASCADE,
                             CONSTRAINT fk_agend_procedimento
                                 FOREIGN KEY (id_procedimento) REFERENCES procedimento (id_procedimento)
                                     ON DELETE RESTRICT ON UPDATE CASCADE,
                             CONSTRAINT fk_agend_usuario
                                 FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario)
                                     ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =====================================================
-- Tabela: mapa_diario (um mapa por dentista por dia)
-- =====================================================
CREATE TABLE mapa_diario (
                             id_mapa_diario           INT NOT NULL AUTO_INCREMENT,
                             id_clinica               INT NOT NULL,
                             id_dentista              INT NOT NULL,
                             id_usuario_atendente     INT NULL,
                             id_usuario_conferente    INT NULL,
                             id_usuario_escritorio    INT NULL,
                             data_mapa                DATE NOT NULL,
                             dia_semana               VARCHAR(15),
                             data_conferencia         DATE,

                             total_clinico            DECIMAL(10,2) DEFAULT 0,
                             total_protese            DECIMAL(10,2) DEFAULT 0,
                             bruto_clinico            DECIMAL(10,2) DEFAULT 0,
                             bruto_pediatria_cirurgia DECIMAL(10,2) DEFAULT 0,
                             bruto_geral              DECIMAL(10,2) DEFAULT 0,
                             total_despesas           DECIMAL(10,2) DEFAULT 0,
                             valor_liquido            DECIMAL(10,2) DEFAULT 0,

                             desconto_cartoes         DECIMAL(10,2) DEFAULT 0,
                             desconto_cheque_pre      DECIMAL(10,2) DEFAULT 0,
                             desconto_cheque_vista    DECIMAL(10,2) DEFAULT 0,
                             desconto_vales           DECIMAL(10,2) DEFAULT 0,
                             desconto_outros          DECIMAL(10,2) DEFAULT 0,
                             total_liquido            DECIMAL(10,2) DEFAULT 0,
                             saldo_final              DECIMAL(10,2) DEFAULT 0,

                             qtd_pacientes            INT DEFAULT 0,
                             qtd_moldagens            INT DEFAULT 0,
                             qtd_mordidas             INT DEFAULT 0,
                             qtd_provas               INT DEFAULT 0,
                             qtd_entregas             INT DEFAULT 0,
                             qtd_repeticoes           INT DEFAULT 0,

                             observacoes              TEXT,
                             status                   VARCHAR(15) NOT NULL DEFAULT 'ABERTO',
                             data_fechamento          DATETIME NULL,
                             PRIMARY KEY (id_mapa_diario),
                             UNIQUE KEY uk_mapa_diario_clinica_dentista_data (id_clinica, id_dentista, data_mapa),
                             KEY idx_mapa_diario_clinica (id_clinica),
                             KEY idx_mapa_diario_dentista (id_dentista),
                             KEY idx_mapa_diario_data (data_mapa),
                             KEY idx_mapa_dashboard (id_clinica, data_mapa, id_dentista),
                             KEY idx_mapa_diario_atendente (id_usuario_atendente),
                             KEY idx_mapa_diario_conferente (id_usuario_conferente),
                             KEY idx_mapa_diario_escritorio (id_usuario_escritorio),
                             CONSTRAINT fk_mapa_diario_clinica
                                 FOREIGN KEY (id_clinica) REFERENCES clinica (id_clinica)
                                     ON DELETE RESTRICT ON UPDATE CASCADE,
                             CONSTRAINT fk_mapa_diario_dentista
                                 FOREIGN KEY (id_dentista) REFERENCES dentista (id_dentista)
                                     ON DELETE RESTRICT ON UPDATE CASCADE,
                             CONSTRAINT fk_mapa_diario_atendente
                                 FOREIGN KEY (id_usuario_atendente) REFERENCES usuario (id_usuario)
                                     ON DELETE SET NULL ON UPDATE CASCADE,
                             CONSTRAINT fk_mapa_diario_conferente
                                 FOREIGN KEY (id_usuario_conferente) REFERENCES usuario (id_usuario)
                                     ON DELETE SET NULL ON UPDATE CASCADE,
                             CONSTRAINT fk_mapa_diario_escritorio
                                 FOREIGN KEY (id_usuario_escritorio) REFERENCES usuario (id_usuario)
                                     ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =====================================================
-- Tabela: atendimento (linhas do mapa; o que realmente aconteceu)
-- tipo_atendimento: CLINICO, PROTESE ou PEDIATRIA_CIRURGIA
-- =====================================================
CREATE TABLE atendimento (
                             id_atendimento   INT NOT NULL AUTO_INCREMENT,
                             id_mapa_diario   INT NOT NULL,
                             id_agendamento   INT NULL,
                             id_paciente      INT NULL,
                             id_dentista      INT NULL,
                             id_procedimento  INT NULL,
                             numero_linha     INT NOT NULL,
                             numero_ficha     VARCHAR(20),
                             nome_paciente    VARCHAR(100),
                             hora_agendada    TIME,
                             hora_entrada     TIME,
                             hora_saida       TIME,
                             hora_retorno     TIME,
                             codigo_pagamento VARCHAR(10),
                             valor            DECIMAL(10,2) DEFAULT 0,
                             tipo_atendimento VARCHAR(20) NOT NULL DEFAULT 'CLINICO',
                             conferido        TINYINT(1) DEFAULT 0,
                             PRIMARY KEY (id_atendimento),
                             UNIQUE KEY uk_atendimento_mapa_linha (id_mapa_diario, numero_linha),
                             KEY idx_atendimento_mapa (id_mapa_diario),
                             KEY idx_atendimento_agendamento (id_agendamento),
                             KEY idx_atendimento_paciente (id_paciente),
                             KEY idx_atendimento_dentista (id_dentista),
                             KEY idx_atendimento_procedimento (id_procedimento),
                             CONSTRAINT fk_atendimento_mapa
                                 FOREIGN KEY (id_mapa_diario) REFERENCES mapa_diario (id_mapa_diario)
                                     ON DELETE CASCADE ON UPDATE CASCADE,
                             CONSTRAINT fk_atendimento_agendamento
                                 FOREIGN KEY (id_agendamento) REFERENCES agendamento (id_agendamento)
                                     ON DELETE SET NULL ON UPDATE CASCADE,
                             CONSTRAINT fk_atendimento_paciente
                                 FOREIGN KEY (id_paciente) REFERENCES paciente (id_paciente)
                                     ON DELETE SET NULL ON UPDATE CASCADE,
                             CONSTRAINT fk_atendimento_dentista
                                 FOREIGN KEY (id_dentista) REFERENCES dentista (id_dentista)
                                     ON DELETE SET NULL ON UPDATE CASCADE,
                             CONSTRAINT fk_atendimento_procedimento
                                 FOREIGN KEY (id_procedimento) REFERENCES procedimento (id_procedimento)
                                     ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =====================================================
-- Tabela: vale (vales administrativos, cheques e outros)
-- =====================================================
CREATE TABLE vale (
                      id_vale          INT NOT NULL AUTO_INCREMENT,
                      id_mapa_diario   INT NOT NULL,
                      nome_responsavel VARCHAR(100),
                      numero_ficha     VARCHAR(20),
                      valor            DECIMAL(10,2) DEFAULT 0,
                      descricao        VARCHAR(150),
                      conferido        TINYINT(1) DEFAULT 0,
                      PRIMARY KEY (id_vale),
                      KEY idx_vale_mapa (id_mapa_diario),
                      CONSTRAINT fk_vale_mapa
                          FOREIGN KEY (id_mapa_diario) REFERENCES mapa_diario (id_mapa_diario)
                              ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =====================================================
-- Tabela: despesa (discriminação de despesas do dia)
-- =====================================================
CREATE TABLE despesa (
                         id_despesa     INT NOT NULL AUTO_INCREMENT,
                         id_mapa_diario INT NOT NULL,
                         descricao      VARCHAR(150) NOT NULL,
                         valor          DECIMAL(10,2) DEFAULT 0,
                         PRIMARY KEY (id_despesa),
                         KEY idx_despesa_mapa (id_mapa_diario),
                         CONSTRAINT fk_despesa_mapa
                             FOREIGN KEY (id_mapa_diario) REFERENCES mapa_diario (id_mapa_diario)
                                 ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =====================================================
-- Tabela: escala_equipe (horários da equipe no dia)
-- =====================================================
CREATE TABLE escala_equipe (
                               id_escala_equipe  INT NOT NULL AUTO_INCREMENT,
                               id_mapa_diario    INT NOT NULL,
                               id_usuario        INT NULL,
                               nome_funcionario  VARCHAR(100),
                               funcao            VARCHAR(30) NOT NULL,
                               hora_inicio       TIME,
                               hora_saida_almoco TIME,
                               hora_volta_almoco TIME,
                               hora_final        TIME,
                               PRIMARY KEY (id_escala_equipe),
                               KEY idx_escala_equipe_mapa (id_mapa_diario),
                               KEY idx_escala_equipe_usuario (id_usuario),
                               CONSTRAINT fk_escala_equipe_mapa
                                   FOREIGN KEY (id_mapa_diario) REFERENCES mapa_diario (id_mapa_diario)
                                       ON DELETE CASCADE ON UPDATE CASCADE,
                               CONSTRAINT fk_escala_equipe_usuario
                                   FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario)
                                       ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =====================================================
-- Tabela: auditoria (alterações financeiras e fechamentos)
-- =====================================================
CREATE TABLE auditoria (
                           id_auditoria  INT NOT NULL AUTO_INCREMENT,
                           tabela        VARCHAR(50) NOT NULL,
                           id_registro   INT NOT NULL,
                           acao          VARCHAR(20) NOT NULL,
                           valor_anterior TEXT,
                           valor_novo     TEXT,
                           id_usuario    INT NOT NULL,
                           data_hora     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           PRIMARY KEY (id_auditoria),
                           KEY idx_auditoria_registro (tabela, id_registro, data_hora),
                           KEY idx_auditoria_usuario (id_usuario),
                           CONSTRAINT fk_auditoria_usuario
                               FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario)
                                   ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =====================================================
-- View: vw_dashboard_mensal
-- Soma os mapas diários por clínica, dentista e mês
-- =====================================================
CREATE VIEW vw_dashboard_mensal AS
SELECT
    id_clinica,
    id_dentista,
        YEAR(data_mapa)                   AS ano,
        MONTH(data_mapa)                  AS mes,
        COUNT(*)                          AS dias_registrados,
        SUM(total_clinico)                AS total_clinico,
        SUM(total_protese)                AS total_protese,
        SUM(bruto_clinico)                AS bruto_clinico,
        SUM(bruto_pediatria_cirurgia)     AS bruto_pediatria_cirurgia,
        SUM(bruto_geral)                  AS bruto_geral,
        SUM(total_despesas)               AS total_despesas,
        SUM(desconto_cartoes)             AS desconto_cartoes,
        SUM(desconto_cheque_pre)          AS desconto_cheque_pre,
        SUM(desconto_cheque_vista)        AS desconto_cheque_vista,
        SUM(desconto_vales)               AS desconto_vales,
        SUM(desconto_outros)              AS desconto_outros,
        SUM(total_liquido)                AS total_liquido,
        SUM(saldo_final)                  AS saldo_final,
        SUM(qtd_pacientes)                AS qtd_pacientes,
        SUM(qtd_moldagens)                AS qtd_moldagens,
        SUM(qtd_mordidas)                 AS qtd_mordidas,
        SUM(qtd_provas)                   AS qtd_provas,
        SUM(qtd_entregas)                 AS qtd_entregas,
        SUM(qtd_repeticoes)               AS qtd_repeticoes
        FROM mapa_diario
        GROUP BY id_clinica, id_dentista, YEAR(data_mapa), MONTH(data_mapa);
