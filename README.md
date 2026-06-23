# GoCoffe API

API REST para gerenciamento de uma cafeteria — catálogo de produtos, categorias, pedidos, upload de imagens e controle de usuários com autenticação JWT.

## Qual problema essa API resolve?

Gerenciar as operações do dia a dia de uma cafeteria: cadastrar produtos e categorias, receber pedidos dos clientes, acompanhar o status de cada pedido (pendente, preparando, pronto) e visualizar métricas de vendas. A API atua como o backend de um sistema de PDV (Ponto de Venda) para o segmento de food service.

## Quem usa essa API?

- **Administradores (ADMIN)** — gerenciam o catálogo completo (produtos, categorias, imagens), criam usuários e acompanham métricas de pedidos.
- **Funcionários (USER)** — visualizam pedidos e atualizam o status de preparação.
- **Clientes** — realizam pedidos (via aplicação front-end que consome esta API).

## Funcionalidades

- Autenticação JWT com login/logout e revogação de tokens
- CRUD completo de usuários, categorias e produtos
- Upload de imagens de produtos via Cloudinary
- Cálculo automático do preço total dos pedidos
- Geração automática de código de pedido (formato `Pedido-XXXXXX`)
- Paginação em listagens de produtos e pedidos
- Métricas de pedidos (total, em preparação, prontos)
- Rate limiting no endpoint de login
- CORS configurado para front-end local
- Headers de segurança (HSTS, XSS protection, X-Frame-Options, Referrer Policy)
- Admin padrão criado automaticamente no startup

## Arquitetura

Clean Architecture com separação em camadas:

```
src/main/java/github/com/diegogrlima/gocoffe/
├── domain/                  # Regra de negócio pura
│   ├── auth/                # Autenticação (use cases, repository interface)
│   ├── category/            # Categorias (entity, use cases, repository interface)
│   ├── order/               # Pedidos (entities, use cases, repository interfaces)
│   ├── product/             # Produtos (entities, use cases, repository interfaces)
│   └── user/                # Usuários (entity, use cases, repository interface)
├── application/             # DTOs (records Java)
│   └── dto/                 # Input/Output para cada domínio
├── infrastructure/          # Implementações técnicas
│   ├── persistence/         # Entidades JPA, repositórios Spring Data
│   ├── repository/          # Implementações das interfaces do domínio
│   └── security/            # JWT filter e service
├── presentation/            # Camada de apresentação
│   └── controller/          # Controllers REST
└── config/                  # Configurações transversais
    ├── exception/           # Exceções customizadas e handler global
    ├── SecurityConfig       # Spring Security + JWT filter chain
    ├── WebConfig            # CORS + interceptors
    ├── RateLimitInterceptor # Rate limiting no login
    ├── CloudinaryConfig     # Bean do Cloudinary
    └── DefaultAdminRunner   # Cria admin padrão no startup
```

**Princípio:** O domínio não depende de nenhuma camada externa. A infraestrutura implementa as interfaces definidas no domínio. Os controllers delegam tudo para use cases.

## Tecnologias

| Tecnologia | Versão/Uso |
|---|---|
| Java | 21 |
| Spring Boot | 4.1.0 |
| Spring Data JPA | Hibernate ORM |
| Spring Security | JWT Stateless |
| PostgreSQL | Banco de dados (produção/teste local) |
| H2 Database | Banco em memória (testes) |
| Auth0 java-jwt | 4.5.0 — geração e validação JWT |
| BCrypt | Criptografia de senhas |
| Cloudinary | 2.0.0 — upload e armazenamento de imagens |
| Lombok | Boilerplate reduction |
| Jakarta Bean Validation | Validação de DTOs |
| spring-dotenv / dotenv-java | Variáveis de ambiente via `.env` |
| Spring Boot Actuator | Health checks e métricas |
| Maven | Build tool |

## Como funciona a autenticação

1. **Login:** `POST /auth/login` com `{ email, password }`. Retorna um JWT token (validade de 2 horas).
2. **Uso do token:** Enviar `Authorization: Bearer <token>` no header de cada requisição autenticada.
3. **Validação:** O `JwtAuthenticationFilter` intercepta cada request, valida o token, extrai o email e a role, e injeta no `SecurityContext` do Spring.
4. **Logout:** `POST /auth/logout` — o token é revogado armazenando seu `jti` (ID) na tabela `revoked_tokens`. Requests com token revogado são rejeitados.
5. **Roles:** `ADMIN` (acesso total) e `USER` (visualizar pedidos, atualizar status). O admin padrão é criado automaticamente no startup via variáveis de ambiente.

## Regras de negócio

- **Pedidos** nascem com status `PENDING` e transitam para `PREPARING` → `READY`
- O **código do pedido** é gerado automaticamente no formato `Pedido-XXXXXX` (6 caracteres alfanuméricos)
- O **preço total** é calculado automaticamente a partir dos itens (quantidade × preço unitário)
- **Produto** só pode ter preço positivo; nome com até 100 caracteres; descrição com até 500 caracteres
- **Categoria** nome com até 100 caracteres
- **CPF do cliente** deve ter entre 11 e 14 caracteres
- **Senha** deve ter no mínimo 8 caracteres, com pelo menos uma maiúscula, uma minúscula e um dígito
- **Rate limiting:** máximo de 10 requests por minuto no endpoint de login por IP
- **Token JWT** expira em 2 horas
- **Admin** é criado automaticamente no startup (se não existir) com credenciais das variáveis de ambiente

## Como rodar localmente

### Pré-requisitos

- Java 21
- PostgreSQL rodando localmente na porta 5432
- Maven (ou use o wrapper `mvnw`)

### Passos

1. **Clone o repositório:**

```bash
git clone <url-do-repositorio>
cd api
```

2. **Configure as variáveis de ambiente:**

Copie o `.env.example` para `.env` e preencha os valores:

```bash
cp .env.example .env
```

```env
# Database
DB_URL=jdbc:postgresql://localhost:5432/gocoffe
DB_USERNAME=postgres
DB_PASSWORD=sua_senha

# JWT (mínimo 32 caracteres)
JWT_SECRET=sua_chave_secreta_de_pelo_menos_32_caracteres

# Admin User (criado automaticamente no startup)
ADMIN_EMAIL=admin@gocoffe.com
ADMIN_PASSWORD=sua_senha_segura
ADMIN_NAME=Admin

# Cloudinary (para upload de imagens)
CLOUDINARY_CLOUD_NAME=seu_cloud_name
CLOUDINARY_API_KEY=sua_api_key
CLOUDINARY_API_SECRET=sua_api_secret
```

3. **Crie o banco de dados no PostgreSQL:**

```sql
CREATE DATABASE gocoffe;
```

4. **Execute a aplicação:**

```bash
# Windows (PowerShell)
.\run.ps1

# Ou diretamente com Maven
.\mvnw.cmd spring-boot:run
```

A API estará disponível em `http://localhost:8080`.

### Perfis

- **default** — `ddl-auto: update`, `show-sql: true` (desenvolvimento)
- **prod** — `ddl-auto: validate`, `show-sql: false` (produção)

Para usar o perfil de produção:

```bash
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=prod
```

## Endpoints

### Autenticação

| Método | Endpoint | Autenticação | Descrição |
|---|---|---|---|
| `POST` | `/auth/login` | Pública | Login (retorna JWT) |
| `POST` | `/auth/logout` | Bearer Token | Logout (revoga token) |

### Usuários

| Método | Endpoint | Autenticação | Descrição |
|---|---|---|---|
| `POST` | `/users` | ADMIN | Criar novo usuário |

### Categorias

| Método | Endpoint | Autenticação | Descrição |
|---|---|---|---|
| `GET` | `/categories` | Pública | Listar todas as categorias |
| `POST` | `/categories` | ADMIN | Criar categoria |
| `PUT` | `/categories/{id}` | ADMIN | Atualizar categoria |
| `DELETE` | `/categories/{id}` | ADMIN | Deletar categoria |

### Produtos

| Método | Endpoint | Autenticação | Descrição |
|---|---|---|---|
| `GET` | `/products?page=0&size=10` | Pública | Listar produtos (paginado) |
| `GET` | `/products/{id}` | Pública | Buscar produto por ID |
| `POST` | `/products` | ADMIN | Criar produto |
| `PUT` | `/products/{id}` | ADMIN | Atualizar produto |
| `DELETE` | `/products/{id}` | ADMIN | Deletar produto |
| `POST` | `/products/{id}/images` | ADMIN | Upload de imagem (Cloudinary) |

### Pedidos

| Método | Endpoint | Autenticação | Descrição |
|---|---|---|---|
| `POST` | `/orders` | Pública | Criar pedido |
| `GET` | `/orders?page=0&size=10` | USER / ADMIN | Listar pedidos (paginado) |
| `GET` | `/orders/{id}` | Pública | Buscar pedido por ID |
| `PATCH` | `/orders/{id}/status` | USER | Atualizar status do pedido |
| `GET` | `/orders/metrics` | ADMIN | Métricas de pedidos |

### Monitoramento

| Método | Endpoint | Autenticação | Descrição |
|---|---|---|---|
| `GET` | `/actuator/**` | Pública | Health check e métricas |

## Modelo do banco de dados

```
┌─────────────┐       ┌────────────────┐       ┌──────────────┐
│   users      │       │  categories    │       │   products    │
├─────────────┤       ├────────────────┤       ├──────────────┤
│ id (UUID PK)│       │ id (UUID PK)   │◄──┐   │ id (UUID PK) │
│ name        │       │ name           │   │   │ name         │
│ email       │       │ created_at     │   └───│ category_id  │
│ password    │       │ updated_at     │       │ description  │
│ role        │       └────────────────┘       │ price        │
│ created_at  │                                │ available    │
└─────────────┘                                │ created_at   │
                                               │ updated_at   │
┌──────────────────┐                           └──────────────┘
│ product_images    │                                    │
├──────────────────┤                                    │
│ id (UUID PK)     │       ┌──────────────────┐         │
│ image_url        │       │    orders         │         │
│ product_id (FK)──│───────│ id (UUID PK)     │         │
│ created_at       │       │ order_code       │         │
└──────────────────┘       │ customer_cpf     │         │
                           │ status (ENUM)    │         │
                           │ total_price      │         │
                           │ created_at       │         │
                           └────────┬─────────┘         │
                                    │                   │
                           ┌────────┴─────────┐         │
                           │   order_items     │         │
                           ├──────────────────┤         │
                           │ id (UUID PK)     │         │
                           │ order_id (FK)────│──┐      │
                           │ product_id (FK)──│──│──────┘
                           │ quantity         │  │
                           │ price_unit       │  │
                           │ subtotal         │  │
                           └──────────────────┘  │

┌──────────────────┐
│ revoked_tokens   │
├──────────────────┤
│ token_id (PK)    │
│ revoked_at       │
└──────────────────┘
```

### Enums

- **Role:** `ADMIN`, `USER`
- **OrderStatus:** `PENDING`, `PREPARING`, `READY`

## Desafios técnicos que foram resolvidos

1. **Arquitetura limpa em Java** — separar domínio de infraestrutura exigiu criar interfaces de repositório no domínio e implementações concretas na infraestrutura, mantendo o domínio dependente de nada externo.

2. **Logout com JWT Stateless** — como JWT é stateless por natureza, o logout foi implementado com uma blacklist de tokens (`revoked_tokens`), verificando o `jti` de cada token no filtro de autenticação.

3. **Upload de imagens com Cloudinary** — integração de upload de imagens de produtos com armazenamento em nuvem, retornando metadados como URL, dimensões e formato.

4. **Rate limiting customizado** — implementação de rate limiter via interceptor em memória com janela deslizante (10 req/min por IP), focado no endpoint de login para prevenir força bruta.

5. **Cálculo automático de preços** — o total do pedido é calculado automaticamente a partir dos itens, e o subtotal de cada item é derivado de quantidade × preço unitário, garantindo consistência.

6. **Criação automática do admin** — o admin padrão é criado no startup via `CommandLineRunner`, lendo credenciais de variáveis de ambiente e verificando duplicidade.

7. **Headers de segurança** — configuração completa de HSTS, XSS protection, X-Frame-Options (deny), Referrer Policy e CORS para ambientes de desenvolvimento.

## O que eu aprendi construindo isso

- **Clean Architecture na prática:** entender que separar domínio de infraestrutura torna o código mais testável e independente de frameworks.
- **JWT completo:** desde a geração do token com claims customizados até a invalidação por blacklist — não basta gerar, precisa pensar no ciclo de vida.
- **Design de APIs REST:** uso correto de verbos HTTP, status codes, paginação e HATEOAS básico.
- **Segurança em APIs:** layers de proteção — Spring Security, JWT filter, rate limiting, headers HTTP, BCrypt.
- **Integração com serviços externos:** Cloudinary para armazenamento de imagens, com tratamento adequado de erros.
- **Java records para DTOs:** imutabilidade e redução de boilerplate nos dados de entrada/saída.
- **Validação declarativa:** usando Jakarta Bean Validation diretamente nos DTOs para validação consistente em toda a API.
