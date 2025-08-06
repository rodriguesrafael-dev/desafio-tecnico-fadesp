# Aplicação de simulação de Pagamento
Desafio técnico, Desenvolvedor Java Fullstack, Fadesp.

## 🚀 Tecnologias Utilizadas

### Backend
- **Java 17.0.15**
- **Spring Boot 3.5.4**
- **Maven 3.9.11**
- **MongoDB 8.0.12**
- **Apache Kafka 2.12-3.9.1**

### Frontend
- **Angular 12.2.2**
- **Node.js 14.21.3**
- **Nginx**

### Infraestrutura
- **Docker & Docker Compose**
- **Alpine Linux**

## 📋 Pré-requisitos

### Windows/Mac
- Docker Desktop
- Docker Compose (incluído no Docker Desktop)

### Linux
- Docker Engine
- Docker Compose
```bash
# Ubuntu/Debian
$ sudo apt-get update
$ sudo apt-get install docker-compose-plugin

# CentOS/RHEL/Fedora
$ sudo yum update
$ sudo yum install docker-compose-plugin
```

1 - Baixar e instalar Docker Compose CLI plugin, executar:
```bash
$ DOCKER_CONFIG=${DOCKER_CONFIG:-$HOME/.docker}
$ mkdir -p $DOCKER_CONFIG/cli-plugins
$ curl -SL https://github.com/docker/compose/releases/download/v2.39.1/docker-compose-linux-x86_64 -o $DOCKER_CONFIG/cli-plugins/docker-compose
```

2 - Aplicar permissões:
```bash
$ chmod +x $DOCKER_CONFIG/cli-plugins/docker-compose

# Se preferir para todos os usuarios
$ sudo chmod +x /usr/local/lib/docker/cli-plugins/docker-compose
```

### Fontes
	https://docs.docker.com/compose/install/linux

### Recursos
- Portas disponíveis: 4200, 8089, 27017, 9092

## 🐳 Como executar com Docker

### 1. Clone o repositório
```bash
$ git clone https://github.com/rodriguesrafael-dev/desafio-tecnico-fadesp.git
$ cd desafio-tecnico-fadesp
```

### 2. Construa e suba o ambiente completo

**Windows/Mac:**
```bash
$ docker-compose up --build -d
```

**Linux:**
```bash
# Se precisar de sudo
$ sudo docker-compose -f "docker-compose.yml" up -d --build

# Ou adicione seu usuário ao grupo docker (recomendado)
sudo usermod -aG docker $USER

# Faça logout e login novamente, depois execute:
$ sudo docker-compose -f "docker-compose.yml" up -d --build
```

### 3. Aguarde a inicialização

**Windows/Mac:**
```bash
$ docker-compose logs -f
```

**Linux:**
```bash
# Com sudo (se necessário)
$ sudo docker-compose logs -f
# Ou após configurar grupo docker
$ docker-compose logs -f
```

## 🌐 Acesso aos Serviços

- **Frontend**: http://localhost:4200
- **Backend API**: http://localhost:8089
- **MongoDB**: localhost:27017
- **Kafka**: localhost:9092

## 📝 Comandos Úteis

### Windows/Mac
```bash
# Subir todos os serviços
$ docker-compose up -d

# Parar todos os serviços
$ docker-compose down

# Ver logs
$ docker-compose logs -f

# Status dos containers
$ docker-compose ps

# Rebuild específico
$ docker-compose up --build -d backend
$ docker-compose up --build -d frontend
```

### Linux
```bash
# Parar todos os serviços
$ sudo docker-compose down
# ou (após configurar grupo docker)
$ docker-compose down

# Ver logs
$ sudo docker-compose logs -f
# ou
$ docker-compose logs -f

# Status dos containers
$ sudo docker-compose ps
# ou
$ docker-compose ps

# Rebuild específico
$ sudo docker-compose up --build -d backend
$ sudo docker-compose up --build -d frontend
# ou
$ docker-compose up --build -d backend
$ docker-compose up --build -d frontend
```

### Configurar Docker no Linux (recomendado)
```bash
# Adicionar usuário ao grupo docker
$ sudo usermod -aG docker $USER

# Reiniciar sessão ou executar
$ newgrp docker

# Testar se funciona sem sudo
$ docker --version
```

## 🏗️ Estrutura Macro do Projeto

```
├── docker-compose.yml
├── backend/
│   └── pagamento/
│       ├── Dockerfile
│       ├── src/
│       └── pom.xml
└── frontend/
    └── pagamento/
        ├── Dockerfile
        ├── src/
        └── package.json
```

## 🔧 Configuração

O ambiente Docker está configurado com:
- Rede isolada entre serviços
- Volumes persistentes para dados
- Health checks automáticos
- Variáveis de ambiente pré-configuradas

---
**Fim**