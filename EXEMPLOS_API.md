# Exemplos de Requisições e Respostas da API Argos

Este documento contém exemplos de JSON para todas as requisições e respostas dos endpoints da API.

---

## 1. AUTENTICAÇÃO

### 1.1 Registrar Novo Usuário

**Endpoint:** `POST /api/auth/registro`

**Descrição:** Cria uma nova conta de usuário com email e senha.

**Requisição:**
```json
{
  "nome": "João Silva",
  "email": "joao.silva@example.com",
  "senha": "MinhaSenh@123"
}
```

**Resposta (201 - Created):**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqb2FvLnNpbHZhQGV4YW1wbGUuY29tIiwiaWF0IjoxNzE3MjQ1NjAwLCJleHAiOjE3MTcyNDkyMDB9.abc123def456...",
  "tipo": "Bearer",
  "id": 1,
  "email": "joao.silva@example.com",
  "nome": "João Silva"
}
```

**Resposta (409 - Conflito - Email já existe):**
```json
{
  "erro": "Este email já está cadastrado"
}
```

**Resposta (400 - Bad Request - Dados inválidos):**
```json
{
  "erro": "Email deve ser válido",
  "campo": "email"
}
```

---

### 1.2 Login

**Endpoint:** `POST /api/auth/login`

**Descrição:** Autentica um usuário e retorna um token JWT.

**Requisição:**
```json
{
  "email": "joao.silva@example.com",
  "senha": "MinhaSenh@123"
}
```

**Resposta (200 - OK):**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqb2FvLnNpbHZhQGV4YW1wbGUuY29tIiwiaWF0IjoxNzE3MjQ1NjAwLCJleHAiOjE3MTcyNDkyMDB9.abc123def456...",
  "tipo": "Bearer",
  "id": 1,
  "email": "joao.silva@example.com",
  "nome": "João Silva"
}
```

**Resposta (401 - Unauthorized - Credenciais inválidas):**
```json
{
  "erro": "Email ou senha inválidos"
}
```

---

## 2. MISSÕES

### 2.1 Criar Missão

**Endpoint:** `POST /api/missoes`

**Descrição:** Cadastra uma nova missão. Status inicial: PLANEJADA | Nível de risco inicial: BAIXO

**Autenticação:** ✅ Requerida (Bearer Token)

**Requisição:**
```json
{
  "nome": "Operação Lunar 2026",
  "descricao": "Missão de exploração e coleta de amostras na região lunar.",
  "areaOperacao": "Lua - Crateras da Região Polar"
}
```

**Resposta (201 - Created):**
```json
{
  "id": 1,
  "nome": "Operação Lunar 2026",
  "descricao": "Missão de exploração e coleta de amostras na região lunar.",
  "status": "PLANEJADA",
  "nivelRisco": "BAIXO",
  "areaOperacao": "Lua - Crateras da Região Polar",
  "dataCriacao": "2026-06-01T10:30:00",
  "dataAtualizacao": "2026-06-01T10:30:00"
}
```

---

### 2.2 Listar Todas as Missões

**Endpoint:** `GET /api/missoes`

**Descrição:** Lista todas as missões, com filtro opcional por status.

**Autenticação:** ❌ Não requerida

**Parâmetros de Query (opcional):**
- `status` - Filtro por status (PLANEJADA, ATIVA, CONCLUIDA, CANCELADA)

**Exemplos de URL:**
- `/api/missoes` - Lista todas as missões
- `/api/missoes?status=ATIVA` - Lista apenas missões ativas

**Resposta (200 - OK):**
```json
[
  {
    "id": 1,
    "nome": "Operação Lunar 2026",
    "descricao": "Missão de exploração e coleta de amostras na região lunar.",
    "status": "ATIVA",
    "nivelRisco": "MÉDIO",
    "areaOperacao": "Lua - Crateras da Região Polar",
    "dataCriacao": "2026-06-01T10:30:00",
    "dataAtualizacao": "2026-06-02T14:15:00"
  },
  {
    "id": 2,
    "nome": "Operação Marciana 2026",
    "descricao": "Sondagem atmosférica de Marte.",
    "status": "PLANEJADA",
    "nivelRisco": "BAIXO",
    "areaOperacao": "Marte - Região de Hellas Planitia",
    "dataCriacao": "2026-06-01T11:00:00",
    "dataAtualizacao": "2026-06-01T11:00:00"
  }
]
```

---

### 2.3 Buscar Missão por ID

**Endpoint:** `GET /api/missoes/{id}`

**Descrição:** Retorna os detalhes de uma missão específica.

**Autenticação:** ❌ Não requerida

**Resposta (200 - OK):**
```json
{
  "id": 1,
  "nome": "Operação Lunar 2026",
  "descricao": "Missão de exploração e coleta de amostras na região lunar.",
  "status": "ATIVA",
  "nivelRisco": "MÉDIO",
  "areaOperacao": "Lua - Crateras da Região Polar",
  "dataCriacao": "2026-06-01T10:30:00",
  "dataAtualizacao": "2026-06-02T14:15:00"
}
```

**Resposta (404 - Not Found):**
```json
{
  "erro": "Missão não encontrada"
}
```

---

### 2.4 Buscar Missões por Área

**Endpoint:** `GET /api/missoes/area/{area}`

**Descrição:** Retorna todas as missões de uma área de operação específica.

**Autenticação:** ❌ Não requerida

**Resposta (200 - OK):**
```json
[
  {
    "id": 1,
    "nome": "Operação Lunar 2026",
    "descricao": "Missão de exploração e coleta de amostras na região lunar.",
    "status": "ATIVA",
    "nivelRisco": "MÉDIO",
    "areaOperacao": "Lua - Crateras da Região Polar",
    "dataCriacao": "2026-06-01T10:30:00",
    "dataAtualizacao": "2026-06-02T14:15:00"
  }
]
```

---

### 2.5 Atualizar Missão

**Endpoint:** `PUT /api/missoes/{id}`

**Descrição:** Atualiza nome, descrição e área de operação. Não permite alterar missão com status CANCELADA.

**Autenticação:** ✅ Requerida (Bearer Token)

**Requisição:**
```json
{
  "nome": "Operação Lunar 2026 - Fase 2",
  "descricao": "Missão de exploração e coleta de amostras avançadas na região lunar.",
  "areaOperacao": "Lua - Craters das Montanhas Leibnitz"
}
```

**Resposta (200 - OK):**
```json
{
  "id": 1,
  "nome": "Operação Lunar 2026 - Fase 2",
  "descricao": "Missão de exploração e coleta de amostras avançadas na região lunar.",
  "status": "ATIVA",
  "nivelRisco": "MÉDIO",
  "areaOperacao": "Lua - Craters das Montanhas Leibnitz",
  "dataCriacao": "2026-06-01T10:30:00",
  "dataAtualizacao": "2026-06-02T15:45:00"
}
```

**Resposta (409 - Conflict - Operação não permitida):**
```json
{
  "erro": "Não é possível atualizar uma missão com status CANCELADA"
}
```

---

### 2.6 Atualizar Status da Missão

**Endpoint:** `PATCH /api/missoes/{id}/status`

**Descrição:** Altera o status seguindo a máquina de estados: PLANEJADA → ATIVA → (CONCLUIDA ou CANCELADA)

**Autenticação:** ✅ Requerida (Bearer Token)

**Requisição:**
```json
{
  "status": "ATIVA"
}
```

**Respostas possíveis - Transições válidas:**

- PLANEJADA → ATIVA
- ATIVA → CONCLUIDA
- ATIVA → CANCELADA
- PLANEJADA → CANCELADA

**Resposta (200 - OK):**
```json
{
  "id": 1,
  "nome": "Operação Lunar 2026",
  "descricao": "Missão de exploração e coleta de amostras na região lunar.",
  "status": "ATIVA",
  "nivelRisco": "MÉDIO",
  "areaOperacao": "Lua - Crateras da Região Polar",
  "dataCriacao": "2026-06-01T10:30:00",
  "dataAtualizacao": "2026-06-02T16:20:00"
}
```

**Resposta (400 - Bad Request - Transição inválida):**
```json
{
  "erro": "Transição de status inválida: de CONCLUIDA para ATIVA"
}
```

---

### 2.7 Excluir Missão

**Endpoint:** `DELETE /api/missoes/{id}`

**Descrição:** Remove uma missão. Não permite excluir missão com status ATIVA.

**Autenticação:** ✅ Requerida (Bearer Token)

**Resposta (204 - No Content):**
```
[Sem corpo de resposta]
```

**Resposta (409 - Conflict - Operação não permitida):**
```json
{
  "erro": "Não é possível excluir uma missão com status ATIVA"
}
```

---

### 2.8 Dashboard - Resumo

**Endpoint:** `GET /api/missoes/resumo`

**Descrição:** Retorna um resumo com total de missões, agrupamentos por status e área, e missões críticas.

**Autenticação:** ❌ Não requerida

**Resposta (200 - OK):**
```json
{
  "totalMissoes": 5,
  "porStatus": {
    "PLANEJADA": 1,
    "ATIVA": 2,
    "CONCLUIDA": 1,
    "CANCELADA": 1
  },
  "porArea": {
    "Lua - Crateras da Região Polar": 2,
    "Marte - Região de Hellas Planitia": 2,
    "Vênus - Atmosfera Superior": 1
  },
  "missoesCriticas": [
    {
      "id": 2,
      "nome": "Operação Marciana 2026",
      "descricao": "Sondagem atmosférica de Marte.",
      "status": "ATIVA",
      "nivelRisco": "CRÍTICO",
      "areaOperacao": "Marte - Região de Hellas Planitia",
      "dataCriacao": "2026-06-01T11:00:00",
      "dataAtualizacao": "2026-06-02T17:30:00"
    }
  ]
}
```

---

## 3. LEITURAS DE SENSORES

### 3.1 Registrar Leitura de Sensor

**Endpoint:** `POST /api/leituras`

**Descrição:** Registra uma leitura de sensor. Avalia anomalia automaticamente, gera alerta se necessário e recalcula o nível de risco da missão. A missão deve estar com status ATIVA.

**Autenticação:** ✅ Requerida (Bearer Token)

**Requisição:**
```json
{
  "missaoId": 1,
  "tipoSensor": "TEMPERATURA",
  "valorLido": 45.8,
  "unidade": "°C"
}
```

**Tipos de Sensores disponíveis:** TEMPERATURA, UMIDADE, PRESSAO, RADIACAO, VELOCIDADE

**Resposta (201 - Created):**
```json
{
  "id": 1,
  "missaoId": 1,
  "tipoSensor": "TEMPERATURA",
  "valorLido": 45.8,
  "unidade": "°C",
  "dataLeitura": "2026-06-02T10:15:30",
  "anomalia": false
}
```

**Resposta (201 - Created - Com Anomalia):**
```json
{
  "id": 2,
  "missaoId": 1,
  "tipoSensor": "RADIACAO",
  "valorLido": 850.5,
  "unidade": "mSv",
  "dataLeitura": "2026-06-02T10:20:15",
  "anomalia": true
}
```

**Resposta (400 - Bad Request - Missão não ativa):**
```json
{
  "erro": "A missão deve estar com status ATIVA para registrar leituras"
}
```

**Resposta (404 - Not Found):**
```json
{
  "erro": "Missão não encontrada"
}
```

---

### 3.2 Listar Leituras por Missão

**Endpoint:** `GET /api/leituras/missao/{id}`

**Descrição:** Retorna todas as leituras de uma missão, ordenadas da mais recente para a mais antiga.

**Autenticação:** ❌ Não requerida

**Resposta (200 - OK):**
```json
[
  {
    "id": 2,
    "missaoId": 1,
    "tipoSensor": "RADIACAO",
    "valorLido": 850.5,
    "unidade": "mSv",
    "dataLeitura": "2026-06-02T10:20:15",
    "anomalia": true
  },
  {
    "id": 1,
    "missaoId": 1,
    "tipoSensor": "TEMPERATURA",
    "valorLido": 45.8,
    "unidade": "°C",
    "dataLeitura": "2026-06-02T10:15:30",
    "anomalia": false
  }
]
```

---

### 3.3 Listar Leituras Anômalas por Missão

**Endpoint:** `GET /api/leituras/missao/{id}/anomalias`

**Descrição:** Retorna apenas as leituras com anomalia detectada de uma missão.

**Autenticação:** ❌ Não requerida

**Resposta (200 - OK):**
```json
[
  {
    "id": 2,
    "missaoId": 1,
    "tipoSensor": "RADIACAO",
    "valorLido": 850.5,
    "unidade": "mSv",
    "dataLeitura": "2026-06-02T10:20:15",
    "anomalia": true
  }
]
```

---

### 3.4 Filtrar Leituras por Tipo de Sensor

**Endpoint:** `GET /api/leituras/missao/{id}/sensor/{tipo}`

**Descrição:** Retorna leituras de um tipo específico de sensor para uma missão.

**Autenticação:** ❌ Não requerida

**Tipos de Sensores:** TEMPERATURA, UMIDADE, PRESSAO, RADIACAO, VELOCIDADE

**Exemplo:** `GET /api/leituras/missao/1/sensor/TEMPERATURA`

**Resposta (200 - OK):**
```json
[
  {
    "id": 1,
    "missaoId": 1,
    "tipoSensor": "TEMPERATURA",
    "valorLido": 45.8,
    "unidade": "°C",
    "dataLeitura": "2026-06-02T10:15:30",
    "anomalia": false
  },
  {
    "id": 5,
    "missaoId": 1,
    "tipoSensor": "TEMPERATURA",
    "valorLido": 48.2,
    "unidade": "°C",
    "dataLeitura": "2026-06-02T11:30:45",
    "anomalia": false
  }
]
```

---

## 4. ALERTAS

### 4.1 Listar Alertas por Missão

**Endpoint:** `GET /api/alertas/missao/{id}`

**Descrição:** Retorna todos os alertas de uma missão, ordenados do mais recente para o mais antigo.

**Autenticação:** ❌ Não requerida

**Resposta (200 - OK):**
```json
[
  {
    "id": 1,
    "missaoId": 1,
    "mensagem": "Leitura de radiação acima do limite normal (850.5 mSv)",
    "severidade": "CRÍTICO",
    "resolvido": false,
    "dataAlerta": "2026-06-02T10:20:15"
  },
  {
    "id": 2,
    "missaoId": 1,
    "mensagem": "Temperatura elevada detectada (45.8°C)",
    "severidade": "ALTO",
    "resolvido": true,
    "dataAlerta": "2026-06-02T10:15:30"
  }
]
```

---

### 4.2 Listar Alertas Pendentes por Missão

**Endpoint:** `GET /api/alertas/missao/{id}/pendentes`

**Descrição:** Retorna apenas os alertas não resolvidos de uma missão.

**Autenticação:** ❌ Não requerida

**Resposta (200 - OK):**
```json
[
  {
    "id": 1,
    "missaoId": 1,
    "mensagem": "Leitura de radiação acima do limite normal (850.5 mSv)",
    "severidade": "CRÍTICO",
    "resolvido": false,
    "dataAlerta": "2026-06-02T10:20:15"
  }
]
```

---

### 4.3 Resolver Alerta

**Endpoint:** `PATCH /api/alertas/{id}/resolver`

**Descrição:** Marca um alerta como resolvido. Se o alerta for CRÍTICO, recalcula o nível de risco da missão automaticamente.

**Autenticação:** ✅ Requerida (Bearer Token)

**Resposta (200 - OK):**
```json
{
  "id": 1,
  "missaoId": 1,
  "mensagem": "Leitura de radiação acima do limite normal (850.5 mSv)",
  "severidade": "CRÍTICO",
  "resolvido": true,
  "dataAlerta": "2026-06-02T10:20:15"
}
```

**Resposta (400 - Bad Request - Alerta já resolvido):**
```json
{
  "erro": "Este alerta já foi resolvido anteriormente"
}
```

**Resposta (404 - Not Found):**
```json
{
  "erro": "Alerta não encontrado"
}
```

---

### 4.4 Listar Todos os Alertas Críticos

**Endpoint:** `GET /api/alertas/criticos`

**Descrição:** Retorna todos os alertas com severidade CRÍTICO ainda não resolvidos de todas as missões.

**Autenticação:** ❌ Não requerida

**Resposta (200 - OK):**
```json
[
  {
    "id": 1,
    "missaoId": 1,
    "mensagem": "Leitura de radiação acima do limite normal (850.5 mSv)",
    "severidade": "CRÍTICO",
    "resolvido": false,
    "dataAlerta": "2026-06-02T10:20:15"
  },
  {
    "id": 3,
    "missaoId": 2,
    "mensagem": "Pressão crítica detectada na câmara de combustível",
    "severidade": "CRÍTICO",
    "resolvido": false,
    "dataAlerta": "2026-06-02T12:45:00"
  }
]
```

---

## TRATAMENTO DE ERROS COMUNS

### Erro 401 - Não Autorizado (Token inválido ou ausente)

Ocorre ao tentar acessar um endpoint protegido sem token ou com token inválido.

```json
{
  "erro": "Não autorizado - Token inválido ou ausente"
}
```

**Solução:** Incluir o header de autenticação:
```
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqb2FvLnNpbHZhQGV4YW1wbGUuY29tIiwiaWF0IjoxNzE3MjQ1NjAwLCJleHAiOjE3MTcyNDkyMDB9.abc123def456...
```

### Erro 403 - Acesso Negado

```json
{
  "erro": "Acesso negado"
}
```

### Erro 404 - Recurso Não Encontrado

```json
{
  "erro": "[Recurso] não encontrado"
}
```

### Erro 400 - Bad Request (Validação)

```json
{
  "erros": [
    {
      "campo": "nome",
      "mensagem": "Nome da missão é obrigatório"
    },
    {
      "campo": "email",
      "mensagem": "Email deve ser válido"
    }
  ]
}
```

---

## DICAS DE AUTENTICAÇÃO

### Como usar o token

Após fazer login ou registro, use o token retornado no header `Authorization` para todos os endpoints protegidos:

```
Authorization: Bearer <token_aqui>
```

### Exemplo com cURL

```bash
# Fazer login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao.silva@example.com",
    "senha": "MinhaSenh@123"
  }'

# Usar o token retornado em endpoints protegidos
curl -X POST http://localhost:8080/api/missoes \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..." \
  -d '{
    "nome": "Nova Missão",
    "descricao": "Descrição da missão",
    "areaOperacao": "Área de Operação"
  }'
```

### Exemplo com Postman

1. Faça a requisição de login/registro em `POST /api/auth/login`
2. Copie o valor do campo `token` da resposta
3. Em outro endpoint protegido, vá para a aba **Headers**
4. Adicione uma header: `Authorization` com valor `Bearer <token_aqui>`

---

## RESUMO DOS ENDPOINTS

| Método | Endpoint | Autenticação | Descrição |
|--------|----------|--------------|-----------|
| POST | `/api/auth/registro` | ❌ | Registrar novo usuário |
| POST | `/api/auth/login` | ❌ | Fazer login |
| POST | `/api/missoes` | ✅ | Criar missão |
| GET | `/api/missoes` | ❌ | Listar missões |
| GET | `/api/missoes/{id}` | ❌ | Buscar missão por ID |
| GET | `/api/missoes/area/{area}` | ❌ | Buscar por área |
| PUT | `/api/missoes/{id}` | ✅ | Atualizar missão |
| PATCH | `/api/missoes/{id}/status` | ✅ | Atualizar status |
| DELETE | `/api/missoes/{id}` | ✅ | Excluir missão |
| GET | `/api/missoes/resumo` | ❌ | Dashboard |
| POST | `/api/leituras` | ✅ | Registrar leitura |
| GET | `/api/leituras/missao/{id}` | ❌ | Listar leituras |
| GET | `/api/leituras/missao/{id}/anomalias` | ❌ | Listar anomalias |
| GET | `/api/leituras/missao/{id}/sensor/{tipo}` | ❌ | Filtrar por sensor |
| GET | `/api/alertas/missao/{id}` | ❌ | Listar alertas |
| GET | `/api/alertas/missao/{id}/pendentes` | ❌ | Listar pendentes |
| PATCH | `/api/alertas/{id}/resolver` | ✅ | Resolver alerta |
| GET | `/api/alertas/criticos` | ❌ | Listar críticos |
