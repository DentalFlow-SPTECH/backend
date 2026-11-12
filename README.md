# Dental Flow — backend

API Spring Boot com Java 21. O projeto Maven fica em `api/`; código em `api/src/main/` e testes em `api/src/test/`.

## Desenvolvimento e verificação

```bash
cd api
sh ./mvnw -B -ntp verify
sh ./mvnw spring-boot:run
```

`verify` executa testes, empacota o JAR e aplica os limites existentes do JaCoCo: 90% de linhas e 80% de branches. Para iniciar a API, configure um MySQL acessível pelas variáveis `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME` e `SPRING_DATASOURCE_PASSWORD`. O contexto HTTP é `/dentalflow-api/v1`, na porta 8080. Não use credenciais reais nos arquivos versionados.

## Imagem Docker e GitHub Actions

O `Dockerfile` na raiz compila o projeto com Java 21 e executa o JAR com JRE, usuário sem privilégios e verificação HTTP do endpoint público `/dentalflow-api/v1/v3/api-docs`. Essa verificação indica disponibilidade HTTP; não substitui testes das operações de banco. Build local opcional:

```bash
docker build -t dentalflow-backend:local .
```

O workflow `.github/workflows/docker.yml` valida testes, cobertura e Dockerfile em pull requests, pushes na `main` e execução manual. Somente execuções da `main` aprovadas publicam no Docker Hub:

- `jorglcs/dentalflow-backend:sha-<commit completo>` identifica o código publicado.
- `jorglcs/dentalflow-backend:latest` acompanha o topo validado da `main`.
- Ambas incluem `linux/amd64` e `linux/arm64`.

Publicações são serializadas. Depois do build, o workflow consulta a `main` e só promove `latest` se o commit continuar atual; execuções antigas mantêm apenas sua tag por commit. Cadastre os secrets `DOCKERHUB_USERNAME` e `DOCKERHUB_TOKEN` nas configurações do GitHub. O repositório `jorglcs/dentalflow-backend` deve ser público. Tokens ficam exclusivamente nos secrets.

## Executar o sistema completo

Após a primeira publicação das imagens, clone somente [infra](https://github.com/DentalFlow-SPTECH/infra) e rode `docker compose up`. A infra fornece MySQL, inicialização da demonstração, variáveis da API e frontend em `http://localhost:5180`. O Compose mantém o esquema existente (`ddl-auto=none`) e preserva o volume do banco; uma imagem nova não migra automaticamente o esquema.

A documentação de operação e as evidências da entrega ficam no repositório irmão `documentos/`. A existência do workflow não comprova publicação: confirme a execução no GitHub Actions e as tags no Docker Hub.
