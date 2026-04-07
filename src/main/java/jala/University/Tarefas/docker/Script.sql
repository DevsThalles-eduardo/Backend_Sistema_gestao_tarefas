-- Script.sql: apenas cria o banco e define dono/privilegios
-- Execute dentro do container: psql -U postgres -f /docker-entrypoint-initdb.d/Script.sql

-- Ajuste o nome do DB/USUARIO se necessário (coincide com .env)
DO
$$
BEGIN
   IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'Tarefas') THEN
      PERFORM pg_sleep(0); -- nop
      EXECUTE format('CREATE DATABASE %I OWNER %I;', 'Tarefas', 'postgres');
   END IF;
END
$$;

-- Garantir privilégios ao usuário dono
GRANT ALL PRIVILEGES ON DATABASE "Tarefas" TO "postgres";
