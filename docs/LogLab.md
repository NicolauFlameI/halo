2\. 🧪 LogLab — Gerador de Logs para o HALO

Objetivo: criar um ambiente controlado que gera logs realistas para testar o HALO.

Funcionalidades:

\- Gerar logs continuamente.

\- Diferentes níveis: INFO, WARN, ERROR.

\- Simular aplicações diferentes.

\- Gerar diferentes tipos de falha:

&#x20; - Timeout

&#x20; - Database connection refused

&#x20; - Deadlock

&#x20; - Falha de autenticação

&#x20; - API indisponível

&#x20; - Exceções

&#x20; - Latência

&#x20; - Serviço parado

\- Configurar intensidade/frequência dos erros.

\- Criar cenários de incidentes.

\- Gerar múltiplos arquivos de log.

\- Possibilidade de ativar uma falha propositalmente.

Exemplo:

┌──────────────────────────────┐

│          LOGLAB              │

├──────────────────────────────┤

│ Aplicação: API               │

│                              │

│ \[✓] INFO                     │

│ \[✓] WARN                     │

│ \[✓] ERROR                    │

│                              │

│ Cenários                     │

│                              │

│ \[ Database Timeout ]         │

│ \[ API Failure ]              │

│ \[ Deadlock ]                 │

│ \[ Authentication ]           │

│                              │

│ \[ INICIAR SIMULAÇÃO ]        │

└──────────────────────────────┘



Tecnologias possíveis:

\- Java

\- Spring Boot

\- Docker

\- PostgreSQL

\- JavaFX/Web

Evolução interessante: colocar o LogLab em containers separados:

Docker

├── API

├── Database

├── Gateway

└── Log Generator



E criar falhas reais nesses componentes para o HALO investigar.

