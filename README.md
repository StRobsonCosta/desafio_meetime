# Desafio HubSpot Contacts API

Este projeto é uma API desenvolvida em **Java com Spring Boot** para realizar a autenticação OAuth2 com o HubSpot e permitir a **criação e listagem de contatos** de forma segura e resiliente.

---

## Objetivo

- Autenticar usuários via OAuth2 com a API do HubSpot.
- Armazenar tokens em cache (Redis).
- Criar e listar contatos diretamente do HubSpot.
- Controlar o número de requisições usando um **Rate Limiter** com o Resilience4j.
- Garantir segurança, performance e tratativas de erro adequadas para consumo da API externa.

---

## Tecnologias Utilizadas

- Java 17
- Spring Boot 3
- Spring Web
- Spring Data Redis
- Spring Validation
- Resilience4j (Rate Limiting)
- Docker & Docker Compose
- JUnit 5 + Mockito
- HubSpot OAuth2
- Redis

---

## Como Executar a API

Você pode executar a aplicação de 3 formas:

---

### 1. Execução via IDE (local)

#### Pré-requisitos:
- Java 17+
- Maven 3.8+
- Redis rodando em `localhost:6379`

#### Passos:
1. Clone o projeto:
   ```bash
   git clone https://github.com/seu-usuario/desafio-hubspot.git
   cd desafio-hubspot
   ```

2. Configure as variáveis de ambiente (via `.env` ou export):
   ```env
   HUBSPOT_CLIENT_ID=seu-client-id
   HUBSPOT_CLIENT_SECRET=seu-client-secret
   ```

3. Ajuste o application.properties:
   - Aponte para dev

4. Rode o Redis:
   - Preferencialmente pelo Docker

5. Execute o projeto:
   ```bash
   ./mvnw spring-boot:run
   ```
   ou na IDE

---

### 2. Execução com Docker

#### Pré-requisitos:
- Docker instalado

#### Passos:

```bash
# Build da imagem
mvn clean package
docker build -t hubspot-api .

# Execute o container
docker run -p 8081:8081 \
  -e HUBSPOT_CLIENT_ID=seu-client-id \
  -e HUBSPOT_CLIENT_SECRET=seu-client-secret \
  hubspot-api
```

---

### 3. Execução com Docker Compose

#### Pré-requisitos:
- Docker e Docker Compose

#### Passos:

1. Certifique-se de que as variáveis estejam configuradas num `.env` ou exportadas:

```env
HUBSPOT_CLIENT_ID=seu-client-id
HUBSPOT_CLIENT_SECRET=seu-client-secret
```

2. Gerar o jar da API:
   - mvn clean package

3. Execute o docker compose:
```bash
docker-compose up --build -d
```

---

## Endpoints Disponíveis

### Auth
- `GET /api/oauth/authorize`: Redireciona para o login do HubSpot.
- `GET /api/oauth/callback`: Callback para tratamento do `code` recebido pelo HubSpot.

### Contacts
- `POST /api/contacts`: Cria um novo contato no HubSpot.
- `GET /api/contacts`: Lista contatos existentes via API do HubSpot.

---

## Controle de Erros

A API utiliza exceções personalizadas para tratar diferentes cenários:

- `TokenNotFoundException`: Token não encontrado no cache.
- `RateLimitExceededException`: Limite de requisições atingido.
- `ExternalApiException`: Erro ao chamar a API externa do HubSpot.
- `ContactException`: Problemas de validação com os dados do contato.
- `InvalidOAuthStateException`: Token state inválido ou expirado.

---

## Futuras Melhorias
- Integração com banco de dados relacional para persistência.
- Monitoramento com Spring Actuator + Prometheus + Grafana.
- UI para gerenciar e visualizar contatos.

---
## SWAGGER
- Documentação e Mapeamento de Endpoints:

http://localhost:8081/swagger-ui/index.html#/



