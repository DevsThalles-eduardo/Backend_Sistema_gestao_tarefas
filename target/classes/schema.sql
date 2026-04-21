-- =========================================
-- EXTENSÕES
-- =========================================
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- =========================================
-- USERS
-- =========================================
CREATE TABLE IF NOT EXISTS users (
    id_usuario VARCHAR(50) PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    email VARCHAR(160) NOT NULL UNIQUE,
    password TEXT NOT NULL,
    provider VARCHAR(100)
);

-- =========================================
-- USER ROLES
-- =========================================
CREATE TABLE IF NOT EXISTS user_roles (
    id_usuario VARCHAR(50) NOT NULL,
    role VARCHAR(100) NOT NULL,
    CONSTRAINT fk_user_roles_user FOREIGN KEY (id_usuario) REFERENCES users(id_usuario) ON DELETE CASCADE
);

-- =========================================
-- PRIORIDADE
-- =========================================
CREATE TABLE IF NOT EXISTS prioridade (
    id_prioridade BIGSERIAL PRIMARY KEY,
    nivel VARCHAR(25) NOT NULL
);

-- =========================================
-- CATEGORIA
-- =========================================
CREATE TABLE IF NOT EXISTS categoria (
    id_categoria BIGSERIAL PRIMARY KEY,
    tipo_categoria VARCHAR(50) NOT NULL
);

-- =========================================
-- TAREFA
-- =========================================
CREATE TABLE IF NOT EXISTS tarefa (
    id_tarefa BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(50) NOT NULL,
    descricao VARCHAR(255) NOT NULL,
    data_inicio TIMESTAMP NOT NULL,
    data_fim TIMESTAMP NOT NULL,
    data_criacao TIMESTAMP NOT NULL DEFAULT now(),
    nota_tarefa DOUBLE PRECISION,
    id_prioridade BIGINT,
    id_categoria BIGINT,
    status VARCHAR(50),
    id_usuario VARCHAR(50) NOT NULL,

    CONSTRAINT fk_tarefa_prioridade FOREIGN KEY (id_prioridade) 
        REFERENCES prioridade(id_prioridade) 
        ON DELETE SET NULL,

    CONSTRAINT fk_tarefa_categoria 
        FOREIGN KEY (id_categoria) 
        REFERENCES categoria(id_categoria) 
        ON DELETE SET NULL,

    CONSTRAINT fk_tarefa_user 
        FOREIGN KEY (id_usuario) 
        REFERENCES users(id_usuario) 
        ON DELETE CASCADE
);

-- =========================================
-- ÍNDICES
-- =========================================
CREATE INDEX IF NOT EXISTS idx_prioridade ON tarefa(id_prioridade);
CREATE INDEX IF NOT EXISTS idx_categoria ON tarefa(id_categoria);
CREATE INDEX IF NOT EXISTS idx_status ON tarefa(status);
CREATE INDEX IF NOT EXISTS idx_status_categoria ON tarefa(status, id_categoria);