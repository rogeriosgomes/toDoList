# TodoList API

## 1. Visão Geral

Este projeto consiste em uma **API REST corporativa** desenvolvida em **Spring Boot**, cujo objetivo é fornecer um backend seguro e escalável para gerenciamento de usuários e tarefas (to-do list).

A aplicação foi estruturada seguindo boas práticas de mercado, com separação clara de responsabilidades, validações de segurança e padronização de endpoints REST, estando apta para implantação em ambientes cloud, como **AWS EC2**.

---

## 2. Funcionalidades Implementadas

### 2.1 Gestão de Usuários
- Cadastro de usuários
- Validação de unicidade de usuário
- Criptografia de senha com **BCrypt**

### 2.2 Gestão de Tarefas
- Criação de tarefas associadas ao usuário autenticado
- Listagem de tarefas por usuário
- Atualização de tarefas
- Controle de acesso garantindo que o usuário só altere suas próprias tarefas
- Controle automático de datas de criação e atualização

### 2.3 Segurança
- Filtro de autenticação (`FilterTaskAuth`)
- Validação de usuário via header HTTP
- Proteção dos endpoints sensíveis

### 2.4 Tratamento de Exceções
- Handler global para exceções
- Retorno padronizado de erros HTTP

---

## 3. Tecnologias Utilizadas

- Java 21 (Amazon Corretto)
- Spring Boot 3.4.x
- Spring Web
- Spring Data JPA
- Maven
- Banco de dados H2 (ambiente local)
- BCrypt Password Encoder

---

## 4. Estrutura do Projeto

```
br.com.inovatte.todolist
 ├── task        # Camada de tarefas (Controller, Service, Repository)
 ├── user        # Camada de usuários (Controller, Service, Repository)
 ├── filter      # Filtros de segurança
 ├── errors      # Tratamento global de erros
 └── utils       # Utilitários compartilhados
```

---

## 5. Compilação do Projeto na AWS (Geração do JAR)

### 5.1 Acesso à instância EC2

```bash
ssh 
```

### 5.2 Instalação das dependências

```bash
sudo yum update -y
sudo yum install java-21-amazon-corretto -y
sudo yum install maven -y
```

> **Atenção:** O projeto foi compilado utilizando **Java 21**. Versões inferiores podem gerar erro de incompatibilidade de class version.

### 5.3 Compilação do projeto

```bash
mvn clean package -DskipTests
```

### 5.4 Artefato gerado

```bash
target/todolist-0.0.1-SNAPSHOT.jar
```

---

## 6. Execução da Aplicação

```bash
java -jar target/todolist-0.0.1-SNAPSHOT.jar
```

A aplicação ficará disponível no endereço:

```
http://ec2-54-205-118-6.compute-1.amazonaws.com:8080
```

---

## 7. Documentação das APIs

### 7.1 Cadastro de Usuário

**Endpoint**
```
POST /users
```

**Request Body**
```json
{
  "username": "usuario.teste",
  "password": "123456"
}
```

**Exemplo CURL**
```bash
curl -X POST http://ec2-54-205-118-6.compute-1.amazonaws.com:8080/users \
  -H "Content-Type: application/json" \
  -d '{"username":"usuario.teste","password":"123456"}'
```

---

### 7.2 Autenticação

Após o cadastro, utilize o **UUID do usuário** retornado para autenticar as requisições de tarefas.

**Header obrigatório:**
```
user_id: <UUID>
```

---

### 7.3 Criar Tarefa

**Endpoint**
```
POST /tasks
```

**Request Body**
```json
{
  "title": "Estudar Spring Boot",
  "description": "Criar API REST",
  "priority": "ALTA",
  "startAt": "2025-01-01T10:00:00",
  "endAt": "2025-01-01T12:00:00"
}
```

**Exemplo CURL**
```bash
curl -X POST http://ec2-54-205-118-6.compute-1.amazonaws.com:8080/tasks \
  -H "Content-Type: application/json" \
  -H "user_id: <UUID>" \
  -d '{"title":"Estudar Spring Boot","description":"Criar API REST","priority":"ALTA"}'
```

---

### 7.4 Listar Tarefas

**Endpoint**
```
GET /tasks
```

**Exemplo CURL**
```bash
curl -X GET http://ec2-54-205-118-6.compute-1.amazonaws.com:8080/tasks \
  -H "user_id: <UUID>"
```

---

### 7.5 Atualizar Tarefa

**Endpoint**
```
PUT /tasks/{id}
```

**Request Body**
```json
{
  "title": "Título atualizado",
  "priority": "MEDIA"
}
```

**Exemplo CURL**
```bash
curl -X PUT http://ec2-54-205-118-6.compute-1.amazonaws.com:8080/tasks/{id} \
  -H "Content-Type: application/json" \
  -H "user_id: <UUID>" \
  -d '{"title":"Título atualizado","priority":"MEDIA"}'
```

---

## 8. Testes

```bash
mvn test
```

**Autor:** Rogério Silvera Gomes

