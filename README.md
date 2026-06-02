# 🚀 Projeto Argos
### Plataforma de Manutenção Preditiva para Operações Espaciais

> Global Solution — Engenharia de Software — 2ESPR  
> Java Spring Boot + Oracle SQL

---

## 📋 Sobre o Projeto

O **Projeto Argos** é uma API REST para monitoramento e manutenção preditiva de missões espaciais. O sistema permite cadastrar missões, registrar leituras de sensores, gerar alertas automáticos e calcular o nível de risco operacional de cada missão em tempo real.

Quando uma leitura de sensor ultrapassa os limiares de segurança, o sistema detecta a anomalia automaticamente, gera um alerta e recalcula o nível de risco da missão — simulando o comportamento de uma plataforma de inteligência preditiva para ambientes de missão crítica.

Todos os endpoints são protegidos por **autenticação JWT**. Para acessar a API é necessário primeiro registrar um usuário e obter um token via `/api/auth`.

---

## 🌍 ODS Relacionados

| ODS | Descrição |
|---|---|
| **ODS 9** | Indústria, Inovação e Infraestrutura |
| **ODS 11** | Cidades e Comunidades Sustentáveis |

---

## 👥 Integrantes

| Nome | RM |
|---|---|
| Davis Junior | RM 560723 |
| Felipe Molinari | RM 559885 |
| Francisco Vargas | RM 560322 |
| Matheus Eiki | RM 55948 |
| Matheus Machado Caposse | RM 560340 |

---

## 🛠️ Tecnologias

- **Java 17**
- **Spring Boot 3.x**
- **Spring Security + JWT** (autenticação e autorização)
- **Spring Data JPA + Hibernate**
- **Oracle SQL** (credenciais acadêmicas FIAP)
- **Maven**
- **Springdoc OpenAPI 2.8.9** (Swagger UI)
- **Lombok**
- **Bean Validation (Jakarta)**

---

## 📁 Arquitetura

O projeto segue arquitetura em camadas:

```
br.com.argos
├── controller      → Endpoints REST (recebe e responde requisições HTTP)
│   └── AutenticacaoController.java  → registro e login (públicos)
├── service         → Regras de negócio (anomalias, risco, máquina de estados)
├── repository      → Interfaces JPA (acesso ao banco de dados)
├── model           → Entidades JPA + Enums
├── dto             → Objetos de entrada (Request) e saída (Response)
├── exception       → Exceções customizadas + GlobalExceptionHandler
└── config          → Swagger/OpenAPI + SecurityConfig + JwtFilter
```

---

## 🗄️ Modelo de Dados

4 tabelas no Oracle SQL:

- **TB_USUARIO** — usuários da plataforma (login/senha para autenticação JWT)
- **TB_MISSAO** — missões espaciais com status e nível de risco
- **TB_LEITURA_SENSOR** — leituras de sensores vinculadas a missões
- **TB_ALERTA** — alertas gerados automaticamente ao detectar anomalias

---

## 🔐 Autenticação JWT

Todos os endpoints (exceto `/api/auth/registrar` e `/api/auth/login`) exigem um token JWT válido no header da requisição.

### Fluxo de autenticação

```
1. POST /api/auth/registrar  →  cria o usuário
2. POST /api/auth/login      →  retorna o token JWT
3. Usar o token no header:   Authorization: Bearer <token>
```

### Endpoints públicos — `/api/auth`

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/api/auth/registrar` | Registra novo usuário |
| `POST` | `/api/auth/login` | Autentica e retorna token JWT |

### Como usar no Swagger UI

1. Faça login em `POST /api/auth/login`
2. Copie o token retornado
3. Clique em **Authorize** (cadeado 🔒 no topo da página)
4. Cole o token no campo `Bearer Token` e confirme
5. Todos os endpoints passam a enviar o token automaticamente

---

## 📡 Endpoints

> ⚠️ Todos os endpoints abaixo exigem `Authorization: Bearer <token>` no header.

### Missões — `/api/missoes`

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/api/missoes` | Criar missão |
| `GET` | `/api/missoes` | Listar missões (filtro `?status=ATIVA`) |
| `GET` | `/api/missoes/{id}` | Buscar por ID |
| `GET` | `/api/missoes/area/{area}` | Buscar por área de operação |
| `PUT` | `/api/missoes/{id}` | Atualizar missão |
| `PATCH` | `/api/missoes/{id}/status` | Alterar status |
| `DELETE` | `/api/missoes/{id}` | Excluir missão |
| `GET` | `/api/missoes/resumo` | Dashboard geral |

### Leituras de Sensor — `/api/leituras`

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/api/leituras` | Registrar leitura (avalia anomalia automaticamente) |
| `GET` | `/api/leituras/missao/{id}` | Listar leituras por missão |
| `GET` | `/api/leituras/missao/{id}/anomalias` | Listar apenas leituras anômalas |
| `GET` | `/api/leituras/missao/{id}/sensor/{tipo}` | Filtrar por tipo de sensor |

### Alertas — `/api/alertas`

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/api/alertas/missao/{id}` | Listar alertas por missão |
| `GET` | `/api/alertas/missao/{id}/pendentes` | Listar alertas pendentes |
| `PATCH` | `/api/alertas/{id}/resolver` | Resolver alerta |
| `GET` | `/api/alertas/criticos` | Listar todos os alertas críticos abertos |

---

## ⚙️ Regras de Negócio

### Máquina de estados da missão
```
PLANEJADA → ATIVA → CONCLUIDA
                  → CANCELADA
```
- Não é possível editar uma missão `CANCELADA`
- Não é possível excluir uma missão `ATIVA`

### Limiares de anomalia por sensor

| Sensor | Condição de Anomalia |
|---|---|
| `TEMPERATURA` | valor > 150°C |
| `PRESSAO` | valor < 0.5 Bar ou > 300 Bar |
| `VIBRACAO` | valor > 8.5 g |
| `COMBUSTIVEL` | valor < 10% |
| `SINAL` | valor < -90 dBm |

### Cálculo automático de nível de risco

| Alertas não resolvidos | Nível de Risco |
|---|---|
| 0 | `BAIXO` |
| 1 – 2 | `MEDIO` |
| 3 – 5 | `ALTO` |
| > 5 | `CRITICO` |

---

## ▶️ Como Executar

### Pré-requisitos

- Java 17+
- Maven 3.8+
- Acesso à rede da FIAP (VPN se necessário para o Oracle SQL)

### 1. Clone o repositório

```bash
git clone https://github.com/Fleps031/GS-4SEM-Java-Argos.git
cd GS-4SEM-Java-Argos
```

### 2. Configure as credenciais do banco

Edite o arquivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:oracle:thin:@//oracle.fiap.com.br:1521/ORCL
spring.datasource.username=SEU_RM
spring.datasource.password=SUA_SENHA
```

### 3. Execute o projeto

```bash
mvn clean package
mvn spring-boot:run
```

### 4. Acesse o Swagger UI

```
http://localhost:8080/swagger-ui.html
```

### 5. Autentique-se

```
POST http://localhost:8080/api/auth/registrar
POST http://localhost:8080/api/auth/login  →  copie o token retornado
```

Cole o token no botão **Authorize** do Swagger UI para liberar todos os endpoints.

> As tabelas são criadas automaticamente no Oracle SQL ao subir a aplicação via `schema.sql`.

---

## 📂 Estrutura de Arquivos

```
src/
├── main/
│   ├── java/br/com/argos/
│   │   ├── controller/
│   │   │   ├── AutenticacaoController.java
│   │   │   ├── MissaoController.java
│   │   │   ├── LeituraSensorController.java
│   │   │   └── AlertaController.java
│   │   ├── service/
│   │   │   ├── AutenticacaoService.java
│   │   │   ├── MissaoService.java
│   │   │   ├── LeituraSensorService.java
│   │   │   └── AlertaService.java
│   │   ├── repository/
│   │   │   ├── UsuarioRepository.java
│   │   │   ├── MissaoRepository.java
│   │   │   ├── LeituraSensorRepository.java
│   │   │   └── AlertaRepository.java
│   │   ├── model/
│   │   │   ├── Usuario.java
│   │   │   ├── Missao.java
│   │   │   ├── LeituraSensor.java
│   │   │   ├── Alerta.java
│   │   │   ├── StatusMissao.java
│   │   │   ├── NivelRisco.java
│   │   │   ├── TipoSensor.java
│   │   │   └── Severidade.java
│   │   ├── dto/
│   │   ├── exception/
│   │   └── config/
│   │       ├── OpenApiConfig.java
│   │       ├── SecurityConfig.java
│   │       └── JwtFilter.java
│   └── resources/
│       ├── application.properties
│       └── schema.sql
```

---

## 🔗 Links

| Recurso | URL |
|---|---|
| Swagger UI | `http://localhost:8080/swagger-ui.html` |
| OpenAPI JSON | `http://localhost:8080/v3/api-docs` |
| Repositório | https://github.com/Fleps031/GS-4SEM-Java-Argos |
| Vídeo explicativo | https://www.youtube.com/watch?v=DPnwoORV_pc |
