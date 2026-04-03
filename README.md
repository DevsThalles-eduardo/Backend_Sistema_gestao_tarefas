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
