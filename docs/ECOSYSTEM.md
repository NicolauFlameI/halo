# 🌐 Ecossistema de Observabilidade e Testes — Visão Geral

Este workspace é composto por três aplicações Spring Boot integradas que simulam um ambiente corporativo real de observabilidade e depuração:

1. **🛰️ HALO (Host Analysis & Log Observer)**
   - **Papel:** O núcleo central de monitoramento, dashboard e análise inteligente de logs[cite: 8].
   - **Banco de Dados:** Armazena eventos e análises no PostgreSQL[cite: 8].
   - **Diferencial:** Possui integração com IA (Ollama/API) para interpretar incidentes e gerar resumos automáticos[cite: 8].

2. **🧪 LogLab (Gerador de Logs para o HALO)**
   - **Papel:** Gerador de carga controlada que simula falhas reais (Timeouts, Deadlocks, Erros de Autenticação, Database Connection Refused) para testar o HALO[cite: 7].
   - **Integração:** Envia logs estruturados ou grava em diretórios compartilhados para o HALO ingerir[cite: 7, 8].

3. **🔬 API Lab (Laboratório de APIs com IA)**
   - **Papel:** Ferramenta para criar, disparar e depurar requisições HTTP (GET, POST, etc.) com análise de erros integrada por IA[cite: 6].
   - **Integração:** Utilizado para testar endpoints e validar respostas JSON do ecossistema[cite: 6].
