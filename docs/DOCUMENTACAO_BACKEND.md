# Documentacao do Backend

## Visao geral

Este projeto e um backend REST desenvolvido com Spring Boot para gerenciamento de tarefas academicas. A aplicacao organiza o codigo em camadas de controller, service, repository, model, dto, seguranca e tratamento de excecoes.

O backend oferece:

- autenticacao com JWT
- cadastro e consulta de usuarios autenticados
- CRUD de tarefas
- CRUD de categorias
- CRUD de prioridades
- controle de acesso por perfil `USER` e `ADMIN`

## Stack e tecnologias

- Java 17
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Spring Security
- PostgreSQL
- JWT com `java-jwt`
- Lombok
- Springdoc OpenAPI / Swagger UI

## Estrutura do projeto

```text
src/main/java/jala/University/Tarefas
|-- TodoApplication.java
|-- controller
|   |-- AuthController.java
|   |-- ControllerTarefas.java
|   |-- ControllerCategoria.java
|   `-- ControllerPrioridade.java
|-- service
|   |-- CurrentUserService.java
|   |-- ServiceTarefas.java
|   |-- ServiceCategoria.java
|   `-- ServicePrioridade.java
|-- repository
|   |-- RepositoryUser.java
|   |-- RepositoryTarefas.java
|   |-- RepositoryCategoria.java
|   `-- RepositoryPrioridade.java
|-- model
|   |-- User.java
|   |-- Tarefa.java
|   |-- Categoria.java
|   |-- Prioridade.java
|   |-- StatusTarefa.java
|   `-- Factory.java
|-- dto
|   |-- LoginRequestDTO.java
|   |-- ResgisterRequestDTO.java
|   |-- ResponseDTO.java
|   |-- CategoriaDTO.java
|   `-- PrioridadeDTO.java
|-- Infra/security
|   |-- SecurityConfig.java
|   |-- SecurityFilter.java
|   |-- TokenService.java
|   `-- CustomUserDetailsService.java
`-- exception
    |-- GlobalExceptionHandler.java
    |-- ApiErrorResponse.java
    |-- BusinessException.java
    `-- ResourceNotFoundException.java
```

## Classe principal

### `TodoApplication`

Ponto de entrada da aplicacao. A anotacao `@SpringBootApplication(scanBasePackages = "jala.University.Tarefas")` inicia o contexto Spring e registra todos os componentes do pacote do projeto.

## Camada de modelos e tabelas

As entidades JPA representam a estrutura principal persistida no banco.

### `User`

Responsavel por representar o usuario do sistema.

Campos principais:

- `id`: identificador UUID, mapeado como `id_usuario`
- `name`: nome do usuario
- `email`: email unico
- `password`: senha persistida
- `provider`: origem/autenticacao do usuario
- `roles`: lista de papeis do usuario, persistida na tabela `user_roles`
- `tarefas`: lista de tarefas vinculadas ao usuario

Responsabilidade:

- guardar os dados de autenticacao e autorizacao
- manter a relacao do usuario com suas tarefas

Tabelas relacionadas:

- `Users`
- `user_roles`

### `Tarefa`

Entidade principal do dominio. Representa uma tarefa cadastrada por um usuario.

Campos principais:

- `id`
- `titulo`
- `descricao`
- `dataInicio`
- `dataFim`
- `dataCriacao`
- `notaTarefa`
- `status`
- `prioridade`
- `categoria`
- `user`

Relacionamentos:

- muitas tarefas para uma `Prioridade`
- muitas tarefas para uma `Categoria`
- muitas tarefas para um `User`

Responsabilidade:

- armazenar os dados operacionais da tarefa
- ligar uma tarefa ao usuario dono
- registrar classificacao por categoria, prioridade e status

Comportamento adicional:

- o metodo `@PrePersist` preenche `dataCriacao` automaticamente no momento da insercao se o campo estiver vazio

Tabela:

- `Tarefa`

Indices definidos:

- `idx_status`
- `idx_categoria`
- `idx_prioridade`
- `idx_status_categoria`

### `Categoria`

Representa a classificacao tematica da tarefa.

Campos principais:

- `id`, mapeado como `id_categoria`
- `tipoCategoria`
- `tarefas`

Responsabilidade:

- agrupar tarefas por tipo de categoria

Tabela:

- `Categoria`

### `Prioridade`

Representa o nivel de prioridade da tarefa.

Campos principais:

- `id`, mapeado como `id_prioridade`
- `nivel`
- `Tarefas`

Responsabilidade:

- classificar o grau de prioridade de uma tarefa

Tabela:

- `Prioridade`

### `StatusTarefa`

Enum que define os estados aceitos para uma tarefa.

Valores:

- `PENDENTE`
- `EM_ANDAMENTO`
- `CONCLUIDO`

Responsabilidade:

- padronizar o status das tarefas no dominio e na persistencia

### `Factory`

Classe utilitaria simples com o metodo `FactoryUser()`, usada para instanciar `User` durante o registro de novos usuarios.

## Relacao entre as tabelas

Resumo da modelagem persistida:

- `Users` possui varios registros em `Tarefa`
- `Categoria` possui varios registros em `Tarefa`
- `Prioridade` possui varios registros em `Tarefa`
- `user_roles` armazena os papeis vinculados a cada usuario

## Camada de DTOs

Os DTOs sao usados para entrada e saida de dados em partes especificas da API.

### `LoginRequestDTO`

Entrada do login.

Campos:

- `email`
- `password`

### `ResgisterRequestDTO`

Entrada do cadastro de usuario.

Campos:

- `name`
- `email`
- `role`
- `password`

### `ResponseDTO`

Saida padronizada do fluxo de autenticacao e do endpoint `/auth/me`.

Campos:

- `name`
- `email`
- `token`
- `role`

### `CategoriaDTO`

Entrada enxuta para criacao de categoria.

Campo:

- `tipoCategoria`

### `PrioridadeDTO`

Entrada enxuta para criacao de prioridade.

Campo:

- `nivelPrioridade`

## Camada de repository

Os repositories fazem a comunicacao com o banco usando Spring Data JPA.

### `RepositoryUser`

Responsabilidades:

- buscar usuario por email
- salvar e consultar usuarios por id
- executar a consulta nativa de login

Consultas importantes:

- `findByEmail(String email)`
- `login(String email, String password)`

O metodo `login` usa SQL nativo e compara email e senha no banco.

### `RepositoryTarefas`

Responsabilidades:

- persistir tarefas
- consultar tarefas por usuario autenticado
- consultar tarefas por categoria e status
- contar referencias de categoria e prioridade
- executar procedure de atualizacao de status

Consultas importantes:

- `findByUserId`
- `findByIdAndUserId`
- `findByCategoriaIdAndUserId`
- `findByStatusAndUserId`
- `countByCategoriaId`
- `countByPrioridadeId`
- `atualizarStatusComProcedure`

### `RepositoryCategoria`

Responsabilidades:

- persistir categorias
- localizar categoria por id
- localizar categoria por nome ignorando maiusculas/minusculas

Consulta adicional:

- `findByTipoCategoriaIgnoreCase`

### `RepositoryPrioridade`

Responsabilidades:

- persistir prioridades
- localizar prioridade por id
- localizar prioridade pelo nivel ignorando maiusculas/minusculas

Consulta adicional:

- `findByNivelIgnoreCase`

## Camada de services

Os services concentram regras de negocio, validacoes e composicao entre repositorios.

### `CurrentUserService`

Responsabilidade:

- obter o usuario autenticado a partir do `SecurityContextHolder`

O service:

- verifica se existe autenticacao valida
- le o principal autenticado
- recarrega o usuario no banco por id ou email

Esse componente e a base para vincular operacoes de tarefas ao usuario logado.

### `ServiceTarefas`

Responsabilidades:

- criar tarefa para o usuario autenticado
- listar tarefas do usuario autenticado
- buscar tarefa individual com controle por dono
- atualizar tarefa existente
- excluir tarefa
- consultar tarefas por categoria
- consultar tarefas por status
- acionar procedure de atualizacao de status

Comportamento da criacao:

- resolve a categoria enviada
- resolve a prioridade enviada
- associa automaticamente a tarefa ao usuario autenticado
- salva a entidade

Comportamento da listagem:

- retorna apenas tarefas do usuario autenticado
- aplica ordenacao por `id` ascendente e `categoria` descendente

Metodos internos importantes:

- `create`
- `FindAll`
- `findById`
- `update`
- `delete`
- `findByIdCategoria`
- `findByStatus`
- `atualizarStatusComProcedure`

Observacao:

- existem metodos internos para filtro por categoria, por status e procedure, mas eles nao estao expostos por endpoint nos controllers atuais

### `ServiceCategoria`

Responsabilidades:

- criar categoria
- listar categorias
- buscar categoria por id
- resolver categoria recebida em uma tarefa
- atualizar categoria
- excluir categoria se ela nao estiver vinculada a tarefas

Validacoes realizadas:

- impede cadastro sem nome de categoria
- impede exclusao de categoria usada por tarefas
- permite resolver categoria por id ou por nome

### `ServicePrioridade`

Responsabilidades:

- criar prioridade
- listar prioridades
- buscar prioridade por id
- resolver prioridade recebida em uma tarefa
- atualizar prioridade
- excluir prioridade se ela nao estiver vinculada a tarefas

Validacoes realizadas:

- impede cadastro sem nivel
- impede exclusao de prioridade usada por tarefas
- permite resolver prioridade por id ou por nivel

## Seguranca e autenticacao

O projeto usa Spring Security com autenticacao stateless baseada em JWT.

### `SecurityConfig`

Responsavel por configurar a seguranca HTTP da aplicacao.

Funcoes principais:

- habilita CORS
- desabilita CSRF
- define sessao como `STATELESS`
- registra o filtro JWT antes do filtro padrao de autenticacao
- define rotas publicas e rotas protegidas por perfil
- registra `PasswordEncoder`, `AuthenticationManager` e `DaoAuthenticationProvider`

Regras de acesso configuradas:

- `POST /auth/login`: publico
- `POST /auth/register`: publico
- `GET /auth/me`: autenticado
- `GET /Categoria` e `GET /Categoria/**`: `USER` ou `ADMIN`
- `POST`, `PUT`, `DELETE` de categoria: `ADMIN`
- `GET /Prioridade` e `GET /Prioridade/**`: `USER` ou `ADMIN`
- `POST`, `PUT`, `DELETE` de prioridade: `ADMIN`
- `/Tarefa/**`: `USER` ou `ADMIN`
- Swagger e endpoints tecnicos liberados

### `SecurityFilter`

Filtro executado a cada requisicao.

Responsabilidade:

- ler o header `Authorization`
- extrair o token no formato `Bearer`
- validar o token
- buscar o usuario correspondente
- montar a autenticacao com as roles do usuario
- gravar a autenticacao no contexto de seguranca

### `TokenService`

Responsavel pela geracao e validacao de JWT.

Funcoes:

- `generateToken(User user)`: gera um token assinado com `HMAC256`
- `validateToken(String token)`: valida o token e retorna o email do usuario

Caracteristicas do token:

- issuer: `todo`
- subject: email do usuario
- expiracao: 2 horas

### `CustomUserDetailsService`

Implementa `UserDetailsService`.

Responsabilidade:

- carregar um usuario pelo email
- converter as roles do dominio para `GrantedAuthority`
- entregar ao Spring Security um `UserDetails`

## Tratamento de excecoes

### `GlobalExceptionHandler`

Centraliza o formato das respostas de erro da API.

Mapeamentos principais:

- `ResourceNotFoundException` -> `404 Not Found`
- `BusinessException` -> `400 Bad Request`
- `BadCredentialsException` e `UsernameNotFoundException` -> `401 Unauthorized`
- `HttpMessageNotReadableException` -> `400 Bad Request`
- `Exception` generica -> `500 Internal Server Error`

### `ApiErrorResponse`

DTO padrao das respostas de erro.

Campos:

- `timestamp`
- `status`
- `error`
- `message`
- `details`

### `BusinessException`

Excecao usada para erros de validacao e regras de negocio.

### `ResourceNotFoundException`

Excecao usada quando um recurso esperado nao e encontrado.

## Controllers e endpoints

Os controllers expõem a API REST do sistema.

### `AuthController`

Base path: `/auth`

Responsabilidade:

- autenticar usuarios
- registrar novos usuarios
- retornar dados do usuario autenticado

#### `POST /auth/login`

Finalidade:

- autenticar um usuario a partir de email e senha

Entrada:

```json
{
  "email": "usuario@exemplo.com",
  "password": "senha"
}
```

Fluxo:

- consulta o usuario no repositorio pelo metodo `login`
- gera um JWT
- devolve dados basicos do usuario e o token

Resposta:

```json
{
  "name": "Nome do Usuario",
  "email": "usuario@exemplo.com",
  "token": "jwt-token",
  "role": "USER"
}
```

#### `POST /auth/register`

Finalidade:

- cadastrar um novo usuario no sistema

Entrada:

```json
{
  "name": "Novo Usuario",
  "email": "novo@exemplo.com",
  "role": "ROLE_USER",
  "password": "senha"
}
```

Fluxo:

- verifica se o email ja existe
- instancia um novo `User`
- preenche nome, email, senha, provider e roles
- salva o usuario
- gera um token JWT de retorno

Resposta:

- retorna um `ResponseDTO` com nome, email, token e papel do usuario

#### `GET /auth/me`

Finalidade:

- retornar os dados do usuario autenticado

Requisito:

- enviar token JWT no header `Authorization`

Resposta:

- `ResponseDTO` com nome, email e papel do usuario

### `ControllerTarefas`

Base path: `/Tarefa`

Responsabilidade:

- operar o CRUD de tarefas vinculadas ao usuario autenticado

#### `POST /Tarefa`

Finalidade:

- criar uma nova tarefa

Entrada:

O body usa a estrutura da entidade `Tarefa`. Os campos principais esperados sao:

- `titulo`
- `descricao`
- `dataInicio`
- `dataFim`
- `notaTarefa`
- `status`
- `categoria`
- `prioridade`

Exemplo:

```json
{
  "titulo": "Entrega do trabalho",
  "descricao": "Finalizar documentacao",
  "dataInicio": "2025-01-10T08:00:00",
  "dataFim": "2025-01-15T23:59:00",
  "notaTarefa": 10.0,
  "status": "PENDENTE",
  "categoria": {
    "id": 1
  },
  "prioridade": {
    "id": 2
  }
}
```

Fluxo:

- o usuario autenticado e associado automaticamente a tarefa
- categoria e prioridade sao resolvidas pelos services correspondentes
- a tarefa e salva no banco

Resposta:

- retorna a tarefa criada

#### `GET /Tarefa`

Finalidade:

- listar as tarefas do usuario autenticado

Fluxo:

- consulta apenas as tarefas associadas ao usuario logado

Resposta:

- lista de objetos `Tarefa`

#### `PUT /Tarefa/{id}`

Finalidade:

- atualizar uma tarefa existente do usuario autenticado

Parametros:

- `id`: identificador da tarefa

Entrada:

- body no formato da entidade `Tarefa`

Fluxo:

- localiza a tarefa do usuario logado
- atualiza os campos editaveis
- resolve novamente categoria e prioridade
- salva a entidade

Resposta:

- retorna a tarefa atualizada

#### `DELETE /Tarefa/{id}`

Finalidade:

- excluir uma tarefa do usuario autenticado

Parametros:

- `id`: identificador da tarefa

Fluxo:

- localiza a tarefa pelo id e pelo usuario atual
- remove o registro do banco

Resposta:

- sem corpo

### `ControllerCategoria`

Base path: `/Categoria`

Responsabilidade:

- cadastrar e manter as categorias do sistema

#### `POST /Categoria`

Finalidade:

- criar uma nova categoria

Permissao:

- `ADMIN`

Entrada:

```json
{
  "tipoCategoria": "Estudos"
}
```

Fluxo:

- transforma o DTO em entidade `Categoria`
- delega a validacao e persistencia para `ServiceCategoria`

Resposta:

- retorna a categoria criada

#### `GET /Categoria`

Finalidade:

- listar todas as categorias

Permissao:

- `USER` ou `ADMIN`

Resposta:

- lista de `Categoria`

#### `GET /Categoria/{id}`

Finalidade:

- buscar uma categoria especifica

Permissao:

- `USER` ou `ADMIN`

Parametros:

- `id`: identificador da categoria

Resposta:

- objeto `Categoria`

#### `PUT /Categoria/{id}`

Finalidade:

- atualizar uma categoria existente

Permissao:

- `ADMIN`

Parametros:

- `id`: identificador da categoria

Entrada:

- body no formato da entidade `Categoria`

Resposta:

- categoria atualizada

#### `DELETE /Categoria/{id}`

Finalidade:

- excluir uma categoria

Permissao:

- `ADMIN`

Regra aplicada:

- a categoria so pode ser excluida se nao estiver vinculada a tarefas

Resposta:

- sem corpo

### `ControllerPrioridade`

Base path: `/Prioridade`

Responsabilidade:

- cadastrar e manter prioridades do sistema

#### `POST /Prioridade`

Finalidade:

- criar uma nova prioridade

Permissao:

- `ADMIN`

Entrada:

```json
{
  "nivelPrioridade": "Alta"
}
```

Resposta:

- retorna a prioridade criada

#### `GET /Prioridade`

Finalidade:

- listar todas as prioridades

Permissao:

- `USER` ou `ADMIN`

Resposta:

- lista de `Prioridade`

#### `PUT /Prioridade/{id}`

Finalidade:

- atualizar uma prioridade existente

Permissao:

- `ADMIN`

Parametros:

- `id`: identificador da prioridade

Resposta:

- prioridade atualizada

#### `DELETE /Prioridade/{id}`

Finalidade:

- excluir uma prioridade

Permissao:

- `ADMIN`

Regra aplicada:

- a prioridade so pode ser excluida se nao estiver vinculada a tarefas

Resposta:

- sem corpo

## Endpoints tecnicos

O projeto tambem deixa acessiveis endpoints de apoio:

- `/swagger-ui/**`: interface do Swagger
- `/v3/api-docs/**`: documento OpenAPI em JSON
- `/error`: rota padrao de erro

## Configuracao da aplicacao

O arquivo `src/main/resources/application.properties` define:

- nome da aplicacao
- porta `8080`
- conexao com PostgreSQL por variaveis de ambiente
- `spring.jpa.hibernate.ddl-auto=update`
- exibicao de SQL no log
- segredo JWT em `api.security.token.secret`

Variaveis relevantes:

- `POSTGRES_PORT`
- `POSTGRES_DB`
- `POSTGRES_USER`
- `POSTGRES_PASSWORD`
- `JWT_SECRET`

## Infraestrutura auxiliar

Existe um arquivo Docker Compose em:

- `src/main/java/jala/University/Tarefas/docker/docker-compose.yml`

Responsabilidade:

- facilitar a subida do banco PostgreSQL usado pelo backend

Tambem existe o arquivo:

- `src/main/java/jala/University/Tarefas/docker/.env`

Ele centraliza as variaveis usadas pela infraestrutura Docker.

## Fluxo geral da aplicacao

O funcionamento do backend segue este fluxo:

1. o usuario se registra ou faz login em `/auth`
2. o backend gera um JWT
3. o frontend envia o token no header `Authorization`
4. o `SecurityFilter` valida o token e autentica o usuario
5. o controller recebe a requisicao
6. o service aplica regras de negocio e validacoes
7. o repository executa a persistencia no banco
8. em caso de erro, o `GlobalExceptionHandler` padroniza a resposta

## Resumo de responsabilidades

### Controllers

Recebem requisicoes HTTP, mapeiam rotas e delegam o processamento.

### Services

Aplicam regras de negocio, validacoes e controle de dono dos dados.

### Repositories

Persistem e consultam dados no banco.

### Models

Representam o dominio e as tabelas persistidas.

### DTOs

Definem estruturas de entrada e saida usadas pela API.

### Security

Controla autenticacao, autorizacao e leitura do JWT.

### Exceptions

Padronizam o tratamento de falhas e respostas de erro.
