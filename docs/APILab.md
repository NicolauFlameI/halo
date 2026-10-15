3. 🔬 API Lab — Laboratório de APIs com IA
Objetivo: criar uma ferramenta para testar, analisar e depurar APIs.
Funcionalidades:
- Criar requisições HTTP.
- GET, POST, PUT, PATCH, DELETE.
- Configuração de headers.
- Autenticação.
- Body JSON.
- Visualização da resposta.
- Histórico das requisições.
- Testes de diferentes endpoints.
- Validação de JSON.
- Comparação entre respostas.
- Exportação/importação de requisições.
- IA analisando erros.
Exemplo:
┌─────────────────────────────────────────┐
│ API LAB                                 │
├─────────────────────────────────────────┤
│ POST https://localhost:8080/pedido      │
│                                         │
│ Headers                                 │
│ Authorization: Bearer ...               │
│ Content-Type: application/json          │
│                                         │
│ Body                                    │
│ {                                       │
│   "cliente": 123,                       │
│   "produto": 456                         │
│ }                                       │
│                                         │
│             [ ENVIAR ]                  │
├─────────────────────────────────────────┤
│ 400 Bad Request                         │
│                                         │
│ 🤖 Análise                              │
│ Possível campo obrigatório ausente: X  │
└─────────────────────────────────────────┘

Tecnologias possíveis:
- Java
- Spring Boot
- HTTP Client
- PostgreSQL
- Docker
- JavaFX/LIBGDX ou Web
- IA/Ollama