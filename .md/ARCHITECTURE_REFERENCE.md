# Referência de arquitetura e padrões — DentalFlow

## 1. Propósito e como usar este documento

Este documento explicita a **arquitetura do backend** do DentalFlow e os padrões que toda nova rota deve seguir. Ele complementa:

- `DATABASE_REFERENCE.md`: tabelas, colunas, chaves e relacionamentos.
- `PROJECT_REFERENCE.md`: contexto de negócio, escopo e roadmap (consulta humana; não é necessário para implementar uma rota).

Cada afirmação aqui tem uma de duas origens:

| Marca | Significado |
|---|---|
| **[OBSERVADO]** | Confirmado na análise do código existente (hoje, só o fluxo `RegisterUser`). |
| **[DEFINIDO]** | Decisão do projeto para as próximas rotas, tomada porque o código atual ainda não mostra o padrão. Deve ser confirmada pela equipe. |

Regra de ouro: **reproduza o padrão do `RegisterUser*`**. Se algo não estiver coberto por nenhum dos dois tipos acima, pergunte antes de decidir.

---

## 2. Visão geral da arquitetura

**[OBSERVADO]** API Spring Boot em camadas, com estilo hexagonal (portas e adaptadores) de forma simples e explícita.

```
HTTP
 │
 ▼
Controller ──► WebMapper ──► Command
 │
 ▼
UseCase (interface, porta de entrada)
 │
 ▼
Service (implementa o UseCase; regras de negócio)
 │
 ▼
Repository (interface, porta de saída)
 │
 ▼
RepositoryImpl (adaptador) ──► PersistenceMapper ──► JpaRepository ──► Entity ──► MySQL
```

Regras de dependência:
- O `Service` conhece apenas a **interface** do repositório, nunca a implementação.
- O domínio não conhece Spring, JPA nem HTTP.
- Conversões entre camadas são **explícitas**, em mappers estáticos (sem MapStruct ou ModelMapper).

---

## 3. Estrutura de pacotes

**[OBSERVADO]** Base: `api/src/main/java/dental/flow/api`

| Pacote | Responsabilidade |
|---|---|
| `adapters.rest.in.controller` | Controllers (entrada HTTP) |
| `adapters.rest.in.controller.dto.request` | DTOs de requisição |
| `adapters.rest.in.controller.dto.response` | DTOs de resposta e `ApiResponse` |
| `adapters.rest.in.controller.mapper` | Mappers da camada web (`*WebMapper`) |
| `adapters.rest.out.persistence.entity` | Entidades JPA (`*Entity`) |
| `adapters.rest.out.persistence.mapper` | Mappers de persistência (`*PersistenceMapper`) |
| `adapters.rest.out.persistence.repository` | `*RepositoryImpl` e `*JpaRepository` |
| `application.command` | Commands (records) |
| `application.in` | Interfaces dos casos de uso |
| `application.out` | Interfaces dos repositórios (portas de saída) |
| `application.service` | Implementação dos casos de uso |
| `domain.<contexto>` | Modelo de domínio (hoje `domain.user`) |
| `boot` | Bootstrap da aplicação |
| `boot.exception` | Exceções e handler global |

**[DEFINIDO]** Novos contextos ganham seu próprio pacote de domínio, por exemplo `domain.patient`, `domain.dentist`, `domain.appointment`, `domain.dailymap`. As demais camadas continuam **por tipo**, como já é feito.

---

## 4. Tecnologia e configuração

**[OBSERVADO]** (`api/pom.xml` e `application.properties`)

| Item | Valor |
|---|---|
| Spring Boot | 4.1.1 (`spring-boot-starter-parent`) |
| Java | 26 (`java.version` e `maven.compiler.release`) |
| Web | `spring-boot-starter-webmvc` |
| Persistência | `spring-boot-starter-data-jpa` + `mysql-connector-j` |
| H2 | Presente como runtime, mas a configuração aponta para MySQL |
| Porta e contexto | `8080` e `/dentalflow-api/v1` |
| JPA | `ddl-auto=update`, `show-sql=true`, `format_sql=true` |
| Datasource | `jdbc:mysql://localhost:3306/dentalflow...` (usuário `root`) |

**[DEFINIDO]** Ajustes de configuração para alinhar código e banco:

1. **O banco passa a ser criado pelo `schema.sql`**, não pelo Hibernate.
2. **Trocar `ddl-auto=update` por `validate`**: o Hibernate apenas confere se entidades e tabelas batem, sem criar nem alterar nada. Hoje, sem `@Table`, o Hibernate tende a criar tabelas próprias (ex.: `user_entity`) paralelas a `usuario`.
3. **Padronizar o nome do banco**: o script cria `dentalFlow` e a URL usa `dentalflow`. Em MySQL no Linux os nomes de banco diferenciam maiúsculas e minúsculas. Escolher um e usar nos dois lugares.
4. O `schema.sql` contém `DROP DATABASE`. **Nunca executá-lo** em ambiente com dados a preservar.

---

## 5. Padrões por camada

### 5.1 Controller **[OBSERVADO]**
- `@RestController` e `@RequestMapping("/user")` na classe; injeção por construtor.
- Um método por endpoint, retornando `ResponseEntity<ApiResponse<T>>`.
- Entrada via `@RequestBody` com DTO específico.
- Fluxo: `RequestDTO` → `WebMapper.toCommand(...)` → `useCase.register(command)` → `ApiResponse.created(...)` → `ResponseEntity.status(201).body(response)`.

### 5.2 Caso de uso e serviço **[OBSERVADO]**
- Interface de entrada com nome da ação: `RegisterUserUseCase`.
- Implementação com `@Service`: `RegisterUserService`.
- O serviço depende da **interface** do repositório e executa a regra antes de persistir.
- Fluxo do `register`: `Command` → `User` (via `PersistenceMapper.toDomain`) → `existsBy...` → `BusinessException` se duplicado → `repository.register(user)` → `ResponseDTO`.

### 5.3 Portas e adaptadores **[OBSERVADO]**
- Porta de saída: interface em `application.out` (`RegisterUserRepository`), **uma por caso de uso**.
- Adaptador: `RegisterUserRepositoryImpl` com `@Repository`, que converte domínio ↔ entidade e chama o JPA.
- `UserJpaRepository extends JpaRepository<UserEntity, Long>`, com consultas derivadas (`existsByEmail`).

### 5.4 Mappers **[OBSERVADO]**
- Classes com métodos **estáticos**, sem bean do Spring.
- `UserWebMapper`: `RequestDTO` → `Command`.
- `UserPersistenceMapper`: `Command` → domínio, e domínio → entidade.

### 5.5 DTOs e commands **[OBSERVADO]**
- `record` Java para `RequestDTO`, `Command` e `ResponseDTO`.
- `ApiResponse<T>` é o envelope de toda resposta de sucesso.

### 5.6 Domínio e entidade **[OBSERVADO]**
- O domínio (`User`) é uma classe com **validação no construtor**. Não usa Bean Validation (`@Valid`, `@NotBlank`).
- Normalizações: `nome` com `trim()`; e-mail em minúsculas.
- `UserEntity`: `@Entity`, `@Id` com `GenerationType.IDENTITY`, getters e setters tradicionais, **sem Lombok**.

### 5.7 Exceções **[OBSERVADO]**
- `ApiException` (`code`, `status`, `message`) é a base; `BusinessException` e `DomainException` a estendem.
- `Failures` (enum): `DOMAIN_ERROR` → 422, `BUSINESS_FAILURE` → 422, `DATABASE_ERROR` → 500.
- `GlobalExceptionHandler` (`@RestControllerAdvice`) converte `ApiException` e `Exception` em `ErrorResponse` (`code`, `status`, `message`, `cause`).

### 5.8 Resposta padronizada **[OBSERVADO]**
`ApiResponse<T>` com `status`, `message`, `data` e `timestamp` (ISO local). Fábricas: `created` (201), `ok` (200) e `noContent` (204).

---

## 6. Fluxo de referência: `POST /dentalflow-api/v1/user` **[OBSERVADO]**

1. `UserController.register` recebe `RegisterUserRequestDTO`.
2. `UserWebMapper.toCommand` gera `RegisterUserCommand`.
3. `RegisterUserUseCase.register(command)` é chamado.
4. `RegisterUserService` converte para `User` (validações do domínio no construtor).
5. Verifica duplicidade; lança `BusinessException` se já existir.
6. `RegisterUserRepositoryImpl` converte para `UserEntity` e chama `saveAndFlush`.
7. O serviço devolve `RegisterUserResponseDTO`.
8. O controller responde `ApiResponse.created("Usuário cadastrado com sucesso.", data)` com HTTP 201.

---

## 7. Mapeamento entre schema e código

### 7.1 Glossário (banco em português, código em inglês) **[DEFINIDO]**

A classe Java, os atributos, DTOs e endpoints ficam em **inglês**. O nome físico em português é declarado com `@Table` e `@Column`.

| Tabela | Domínio / Entity | Recurso (endpoint) | Etapa |
|---|---|---|---|
| `clinica` | `Clinic` | `/clinic` | núcleo |
| `usuario` | `User` | `/user` | núcleo |
| `paciente` | `Patient` | `/patient` | núcleo |
| `dentista` | `Dentist` | `/dentist` | núcleo |
| `procedimento` | `Procedure` | `/procedure` | núcleo |
| `agendamento` | `Appointment` | `/appointment` | núcleo |
| `mapa_diario` | `DailyMap` | `/daily-map` | financeiro |
| `atendimento` | `Visit` | filho de `DailyMap` | financeiro |
| `vale` | `Voucher` | filho de `DailyMap` | financeiro |
| `despesa` | `Expense` | filho de `DailyMap` | financeiro |
| `escala_equipe` | `TeamSchedule` | filho de `DailyMap` | financeiro |
| `vw_dashboard_mensal` | `MonthlyDashboard` (somente leitura) | `/dashboard` | dashboard |

### 7.2 Regras de mapeamento **[DEFINIDO]**
- `@Entity` + `@Table(name = "usuario")` em toda entidade.
- `@Column(name = "id_clinica")` em toda coluna cujo nome físico difere do atributo (ex.: `clinicId` → `id_clinica`, `password` → `senha_hash`, `appointmentDate` → `data_hora`).
- Chaves estrangeiras mapeadas como `Long` simples (como `clinicId` hoje), sem `@ManyToOne`, até haver necessidade de navegar entre entidades.
- A view `vw_dashboard_mensal` é só leitura: entidade com `@Immutable` ou projeção, sem operações de escrita.
- Tipos: `INT` → `Long`/`Integer`; `DECIMAL(10,2)` → `BigDecimal`; `DATE` → `LocalDate`; `TIME` → `LocalTime`; `DATETIME` → `LocalDateTime`; `TINYINT(1)` → `Boolean`.

### 7.3 Divergências conhecidas entre código e schema (a corrigir primeiro)

| Item | Código atual **[OBSERVADO]** | Schema atual | Ação |
|---|---|---|---|
| Identificação do usuário | `email` | `login` (sem e-mail) | Trocar a validação e o `existsByEmail` por `existsByLogin` |
| Senha | `password`, mínimo 6 caracteres, **sem encoder identificado** | `senha_hash` | Aplicar hash (BCrypt) antes de persistir; nunca devolver o campo em DTO ou log |
| Clínica | `clinicId` sem `@Column` | `id_clinica` | Mapear com `@Column(name = "id_clinica")` |
| Tabela | sem `@Table` | `usuario` | Declarar `@Table(name = "usuario")` |
| Paciente | não existe | `cpf`, `endereco`, `bairro`, `cep` obrigatórios | Validar no domínio `Patient` |
| Orçamento | n/a | removido do escopo | Não criar nenhum campo ou rota de orçamento |

---

## 8. Padrões para novas rotas **[DEFINIDO]**

### 8.1 Nomenclatura dos arquivos
Seguir `RegisterUser*`, trocando a ação e o recurso:

| Camada | Exemplo (`Patient`, ação `register`) |
|---|---|
| Domínio | `domain.patient.Patient` |
| Request / Response | `RegisterPatientRequestDTO` / `RegisterPatientResponseDTO` |
| Command | `RegisterPatientCommand` |
| Caso de uso | `RegisterPatientUseCase` / `RegisterPatientService` |
| Porta de saída | `RegisterPatientRepository` |
| Adaptador | `RegisterPatientRepositoryImpl` |
| JPA | `PatientJpaRepository` (um por entidade, compartilhado entre os casos de uso) |
| Entidade | `PatientEntity` |
| Mappers | `PatientWebMapper` / `PatientPersistenceMapper` |
| Controller | `PatientController` (`@RequestMapping("/patient")`) |

Ações previstas: `Register`, `Update`, `Get` (por id), `List`, `Cancel`/`Delete` quando aplicável.

### 8.2 Checklist por endpoint (dividir em duas conversas no Copilot)
**Parte 1: persistência**
1. Domínio com validações no construtor
2. `Entity` com `@Table`/`@Column`
3. `JpaRepository` (reaproveitar se já existir)
4. Porta de saída em `application.out`
5. `RepositoryImpl`
6. `PersistenceMapper`

**Parte 2: entrada**
7. `Command`
8. `UseCase` e `Service`
9. `RequestDTO` e `ResponseDTO`
10. `WebMapper`
11. `Controller`
12. Teste do serviço

### 8.3 Respostas e erros
- Sucesso: `ApiResponse.created` (POST), `ApiResponse.ok` (GET/PUT), `ApiResponse.noContent` (DELETE).
- Regra de negócio violada: `BusinessException`; validação de domínio: `DomainException`.
- **Pendente de definição:** recurso não encontrado (404) não existe em `Failures`. Adicionar uma falha `NOT_FOUND` (404) quando surgir o primeiro `Get`.

### 8.4 Consultas (agenda e dashboard)
O código atual não tem consultas com filtro. Padrão definido:
- Filtros por **query params**: `GET /appointment?date=2026-10-20&dentistId=3`.
- **Sem paginação** nesta fase (volume acadêmico). Registrar como evolução futura.
- Ordenação fixa pelo critério natural (ex.: agenda por `data_hora`).
- Dashboard: `GET /dashboard?year=2026&month=10`, lendo a view `vw_dashboard_mensal`.

### 8.5 Escopo por clínica
Quase todas as tabelas têm `id_clinica`, mas o schema **não impõe** isolamento. Definir como a clínica é obtida em cada requisição (hoje o `clinicId` vem no corpo do cadastro de usuário) e usá-la em toda consulta. Não presumir isolamento automático.

### 8.6 Regras de negócio que o serviço deve garantir
O banco não garante, então ficam no `Service`:
- Paciente, dentista, procedimento e usuário de um agendamento pertencem à **mesma clínica**.
- Sem conflito de horário para o mesmo dentista.
- `status` do agendamento dentro dos valores aceitos: `AGENDADO`, `CONFIRMADO`, `EM_ATENDIMENTO`, `CONCLUIDO`, `CANCELADO`, `FALTOU`.
- `DailyMap` único por clínica, dentista e data.
- Totais do mapa calculados no backend a partir das linhas, e não só digitados.

### 8.7 Validação
Manter o padrão atual: validação **no domínio**, sem `@Valid`. Não introduzir Bean Validation sem decisão da equipe.

### 8.8 Testes
O projeto só tem `ApiApplicationTests.contextLoads()`. Padrão definido:
- **Teste unitário do `Service`** com JUnit 5 e Mockito, mockando a porta de saída.
- Cobrir: caminho feliz, duplicidade (`BusinessException`) e violação de domínio.
- Nunca declarar testes como aprovados sem tê-los executado.

---

## 9. Estado atual da implementação

| Item | Estado |
|---|---|
| `User`: `register` | Implementado (precisa alinhar com `login`/`senha_hash`) |
| `Clinic`, `Patient`, `Dentist`, `Procedure`, `Appointment` | Não implementados |
| `DailyMap` e filhos, dashboard | Não implementados |
| Insumos, equipamentos, antes e depois, auditoria | Planejados no `PROJECT_REFERENCE.md`; **não existem no schema nem no código** |
| Autenticação/autorização | Não identificada |
| Paginação, filtros, testes de endpoint | Não existiam; ver seções 8.4 e 8.8 |

---

## 10. Arquivos-modelo (padrão a copiar)

Base: `api/src/main/java/dental/flow/api/`

- `adapters/rest/in/controller/UserController.java`
- `adapters/rest/in/controller/dto/request/RegisterUserRequestDTO.java`
- `adapters/rest/in/controller/dto/response/RegisterUserResponseDTO.java`
- `adapters/rest/in/controller/dto/response/ApiResponse.java`
- `adapters/rest/in/controller/mapper/UserWebMapper.java`
- `adapters/rest/out/persistence/mapper/UserPersistenceMapper.java`
- `adapters/rest/out/persistence/entity/UserEntity.java`
- `adapters/rest/out/persistence/repository/RegisterUserRepositoryImpl.java`
- `adapters/rest/out/persistence/repository/UserJpaRepository.java`
- `application/in/RegisterUserUseCase.java`
- `application/out/RegisterUserRepository.java`
- `application/service/RegisterUserService.java`
- `application/command/RegisterUserCommand.java`
- `domain/user/User.java`
- `boot/exception/ApiException.java`, `Failures.java`, `GlobalExceptionHandler.java`
- `resources/application.properties`, `pom.xml`

---

## 11. Pendências e dúvidas a confirmar

1. O `schema.sql` está versionado no repositório e foi aplicado ao banco em uso?
2. Qual é o nome final do banco (`dentalFlow` ou `dentalflow`)?
3. Como será a autenticação (JWT, sessão) e como o `clinicId` chega em cada requisição?
4. Confirmar o glossário PT→EN da seção 7.1.
5. Confirmar os padrões **[DEFINIDO]** das seções 8.3 a 8.8 (404, consultas sem paginação, testes com Mockito).
6. O uso de `Procedure` como nome de classe é aceitável, ou prefere `DentalProcedure`?

---

## 12. Conclusão

A arquitetura é Spring Boot em camadas, com portas e adaptadores explícitos:

`controller → webMapper → useCase → service → repository (interface) → repositoryImpl → persistenceMapper → jpaRepository → entity`

Há um único fluxo real (`RegisterUser`) que serve de modelo. Para as próximas rotas, **copie esse fluxo**, mapeie os nomes em inglês para as tabelas em português com `@Table`/`@Column` e confira sempre o `DATABASE_REFERENCE.md`. O que não estiver coberto por este documento deve ser perguntado antes de implementado.
