# Backend-Tarefas

Resumo rápido
- Aplicação REST para gerenciar tarefas (Spring Boot + JPA + PostgreSQL).
- Contém endpoints para CRUD de tarefas, prioridades e categorias, autenticação básica JWT e integração com procedures SQL.

Pré-requisitos
- Java 17+ instalado
- Maven 3.x
- Docker & Docker Compose (opcional, para subir Postgres)

Estrutura importante
- Código fonte: `src/main/java/jala/University/Tarefas`
- Configuração Spring: `src/main/resources/application.properties`
- Docker compose (Postgres): `src/main/java/jala/University/Tarefas/docker/docker-compose.yml`
- Script de inicialização SQL (procedures, views, seed): `src/main/resources/schema.sql`

Variáveis de ambiente
- O projeto carrega variáveis de `src/main/java/jala/University/Tarefas/docker/.env` quando usado com Docker.
- Principais variáveis:
  - `POSTGRES_DB` (padrão `Tarefas`)
  - `POSTGRES_USER` (padrão `postgres`)
  - `POSTGRES_PASSWORD` (padrão `Aluno123`)
  - `POSTGRES_PORT` (padrão `5433`)

Subir banco com Docker (recomendado)
1. Entre na pasta do docker-compose:

```bash
cd src/main/java/jala/University/Tarefas/docker
docker compose up -d
```

2. Verifique que o container `DB_TAREFAS` está rodando:

```bash
docker ps --filter name=DB_TAREFAS
```

Build e execução da aplicação
1. Build com Maven:

```bash
mvn clean package -DskipTests
```

2. Executar jar:

```bash
java -jar target/Backend-Tarefas.jar
```

ou executar via IDE / `mvn spring-boot:run`.

Inicialização do esquema e seeds
- A aplicação está configurada para deixar o Hibernate atualizar/criar o esquema (`spring.jpa.hibernate.ddl-auto=update`) e depois executar `src/main/resources/schema.sql`.
- `schema.sql` cria (com identificadores entre aspas) as tabelas, insere prioridades iniciais e cria a procedure `sp_atualizar_status_tarefa` e a view `tarefas_por_prioridade`.

Endpoints principais (resumo)
- Base URL: `http://localhost:8080`
- Tarefas
  - `POST /Tarefa` — criar tarefa (body JSON com campos da entidade `Tarefa`)
  - `GET /Tarefa` — listar tarefas do usuário corrente
  - `GET /Tarefa/Categoria/{id}` — tarefas por categoria
  - `GET /Tarefa/Status/{status}` — tarefas por status
  - `PUT /Tarefa/{id}` — atualizar tarefa
  - `DELETE /Tarefa/{id}` — deletar tarefa
  - `PATCH /Tarefa/{id}/status?status=VALOR` — atualiza status usando *stored procedure* `sp_atualizar_status_tarefa`

- Prioridades
  - `POST /Prioridade` — criar prioridade
  - `GET /Prioridade` — listar

Exemplos de uso (curl)
- Atualizar status via endpoint (usuário autenticado):

```bash
curl -X PATCH "http://localhost:8080/Tarefa/123/status?status=CONCLUIDO" \
  -H "Authorization: Bearer <TOKEN>"
```

- Chamar procedure diretamente no DB (psql):

```sql
CALL sp_atualizar_status_tarefa(123, 'CONCLUIDO');
```

Observações importantes
- Nomes das tabelas no código Java usam `@Table(name = "Tarefa")` (e similares). Para garantir correspondência literal, `schema.sql` usa identificadores entre aspas (ex.: `"Tarefa"`). Se preferir deixar o Hibernate controlar totalmente a criação do esquema, remova ou comente as DDL do `schema.sql`.
- A procedure atualiza a tabela diretamente; se você precisa que o contexto JPA reflita imediatamente a mudança, considere usar `EntityManager#refresh` ou recarregar a entidade após a chamada.
- `sp_atualizar_status_tarefa` usa sintaxe `CREATE PROCEDURE`/`CALL`, compatível com PostgreSQL 11+.

População via JPA
- Há um `DataInitializer` (`Infra/security/DataInitializer.java`) que cria um usuário admin e prioridades iniciais via repositório JPA. Isso é executado automaticamente na inicialização da aplicação.

Testes e verificação
- Após iniciar a aplicação, verifique as tabelas e objetos no Postgres:

```sql
\dt
SELECT * FROM "Prioridade";
SELECT * FROM "Categoria";
SELECT * FROM tarefas_por_prioridade;
```

Problemas comuns
- Erro de conexão: verifique `application.properties` e se o container Postgres está exposto na porta correta.
- Erro de procedure: verifique versão do Postgres (11+) e se `schema.sql` foi executado (logs de inicialização do Spring mostram execução de scripts SQL).

Contribuições
- Faça um fork, crie branch, implemente e abra PR. Siga os padrões de código do projeto.

Contato
- Qualquer dúvida, me peça para executar a build aqui e reportar erros.
# todo-corrigido

Projeto Spring Boot para gerenciamento de tarefas, mantido no pacote original `jala.University.Tarefas`.

## Estrutura

O projeto segue a organizacao original:

- `controller`
- `service`
- `repository`
- `model`
- `dto`
- `Infra/security`
- `exception`

## Correcoes incorporadas

As correcoes de autenticacao foram aplicadas diretamente na estrutura original:

- comparacao correta da senha no login
- `SecurityConfig` configurada para JWT stateless
- `SecurityFilter` lendo o token `Bearer` corretamente
- `CustomUserDetailsService` usando email como identificador
- `TokenService` validando token corretamente
- `User.roles` com mapeamento persistivel
- tratamento global de excecoes para respostas HTTP padronizadas

## Executar

```bash
mvn spring-boot:run
```

Ou com o artefato gerado:

```bash
java -jar target/todo-corrigido-0.0.1-SNAPSHOT.jar
```

## Configuracao

Por padrao o projeto usa H2 em arquivo local, em `./data/tarefas`.

Isso significa que:

- categorias, prioridades, usuarios e tarefas continuam salvos apos reiniciar o backend
- o console H2 fica disponivel em `/h2-console`
- se quiser outro banco, basta sobrescrever `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `DB_DRIVER` e `DB_DIALECT`

Variaveis opcionais:

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `DB_DRIVER`
- `DB_DIALECT`
- `JWT_SECRET`
