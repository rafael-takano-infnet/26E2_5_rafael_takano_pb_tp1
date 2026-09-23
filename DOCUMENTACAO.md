# Catálogo de Livros - Documentação de Arquitetura

## Visão Geral

O **Catálogo de Livros** é uma aplicação monolítica desenvolvida com Spring Boot (backend) e React (frontend), criada como parte do Teste de Performance 1 (TP1) da disciplina de Engenharia de Softwares Escaláveis.

A aplicação permite gerenciar um catálogo de livros com informações sobre autores, editoras e gêneros literários.

---

## Arquitetura da Solução

### Diagrama de Componentes

```
┌─────────────────────────────────────────────────────────────────────┐
│                         FRONTEND (React)                            │
│  ┌─────────────┐  ┌──────────────┐  ┌───────────────┐  ┌─────────┐ │
│  │  BookList   │  │ AuthorList   │  │ PublisherList │  │GenreList│ │
│  └──────┬──────┘  └──────┬───────┘  └───────┬───────┘  └────┬────┘ │
│         └────────────────┴──────────────────┴────────────────┘      │
│                              │                                      │
│                        ┌─────┴─────┐                                │
│                        │  api.js   │ (Axios HTTP Client)            │
│                        └─────┬─────┘                                │
└──────────────────────────────┼──────────────────────────────────────┘
                               │ HTTP/REST
                               ▼
┌─────────────────────────────────────────────────────────────────────┐
│                        BACKEND (Spring Boot)                        │
│                                                                     │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │                    Controller Layer                         │   │
│  │  ┌─────────────┐  ┌──────────────┐  ┌──────────────┐       │   │
│  │  │BookController│  │AuthorController│ │PublisherCtrl │       │   │
│  │  └──────┬──────┘  └──────┬───────┘  └──────┬───────┘       │   │
│  │         └────────────────┴─────────────────┘                │   │
│  └────────────────────────┬────────────────────────────────────┘   │
│                           │                                        │
│  ┌────────────────────────▼────────────────────────────────────┐   │
│  │                      Service Layer                          │   │
│  │  ┌─────────────┐  ┌──────────────┐  ┌──────────────┐       │   │
│  │  │ BookService │  │ AuthorService│  │PublisherSvc  │       │   │
│  │  │   (Impl)    │  │   (Impl)     │  │   (Impl)     │       │   │
│  │  └──────┬──────┘  └──────┬───────┘  └──────┬───────┘       │   │
│  │         └────────────────┴─────────────────┘                │   │
│  └────────────────────────┬────────────────────────────────────┘   │
│                           │                                        │
│  ┌────────────────────────▼────────────────────────────────────┐   │
│  │                    Repository Layer                         │   │
│  │  ┌─────────────┐  ┌──────────────┐  ┌──────────────┐       │   │
│  │  │BookRepository│ │AuthorRepo    │  │PublisherRepo │       │   │
│  │  └─────────────┘  └──────────────┘  └──────────────┘       │   │
│  └────────────────────────┬────────────────────────────────────┘   │
│                           │                                        │
│  ┌────────────────────────▼────────────────────────────────────┐   │
│  │                     Domain Layer                            │   │
│  │  ┌────────┐  ┌────────┐  ┌──────────┐  ┌────────┐         │   │
│  │  │  Book  │  │ Author │  │Publisher │  │ Genre  │         │   │
│  │  └────────┘  └────────┘  └──────────┘  └────────┘         │   │
│  └─────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │   H2 Database       │
                    │   (In-Memory)       │
                    └─────────────────────┘
```

---

## Diagrama de Sequência - Criar Livro

```
Frontend            BookController         BookService         AuthorService       PublisherRepository
    │                    │                      │                    │                      │
    │  POST /api/books   │                      │                    │                      │
    │───────────────────>│                      │                    │                      │
    │                    │                      │                    │                      │
    │                    │  findById(publisherId)│                    │                      │
    │                    │─────────────────────────────────────────────────────────────────>│
    │                    │                      │                    │                      │
    │                    │      Publisher       │                    │                      │
    │                    │<─────────────────────────────────────────────────────────────────│
    │                    │                      │                    │                      │
    │                    │  findById(authorId)  │                    │                      │
    │                    │───────────────────────────────────────────>│                      │
    │                    │                      │                    │                      │
    │                    │       Author         │                    │                      │
    │                    │<───────────────────────────────────────────│                      │
    │                    │                      │                    │                      │
    │                    │  create(book)        │                    │                      │
    │                    │─────────────────────>│                    │                      │
    │                    │                      │                    │                      │
    │                    │  save(book)          │                    │                      │
    │                    │                      │─────────────────────────────────────────>│
    │                    │                      │                    │                      │
    │                    │       Book           │                    │                      │
    │                    │<─────────────────────│                    │                      │
    │                    │                      │                    │                      │
    │   201 Created      │                      │                    │                      │
    │   BookResponseDTO  │                      │                    │                      │
    │<───────────────────│                      │                    │                      │
    │                    │                      │                    │                      │
```

---

## Diagrama de Sequência - Listar Livros

```
Frontend            BookController         BookService         BookRepository
    │                    │                      │                    │
    │  GET /api/books    │                      │                    │
    │───────────────────>│                      │                    │
    │                    │                      │                    │
    │                    │  findAll()           │                    │
    │                    │─────────────────────>│                    │
    │                    │                      │                    │
    │                    │  findAll()           │                    │
    │                    │                      │───────────────────>│
    │                    │                      │                    │
    │                    │   List<Book>         │                    │
    │                    │<─────────────────────│                    │
    │                    │                      │                    │
    │                    │   List<Book>         │                    │
    │                    │<─────────────────────│                    │
    │                    │                      │                    │
    │   200 OK           │                      │                    │
    │   List<BookDTO>    │                      │                    │
    │<───────────────────│                      │                    │
    │                    │                      │                    │
```

---

## Modelagem de Domínio (DDD)

### Bounded Context: Catálogo de Livros

**Entidades:**

1. **Book** (Agregado Principal)
   - Atributos: id, title, isbn, publicationYear, pages, price
   - Relacionamentos:
     - ManyToOne com Publisher
     - ManyToMany com Author
     - ManyToMany com Genre

2. **Author**
   - Atributos: id, name, biography

3. **Publisher**
   - Atributos: id, name, country

4. **Genre**
   - Atributos: id, name, description

---

## Tecnologias Utilizadas

### Backend
- **Java 17** - Linguagem de programação
- **Spring Boot 3.2.0** - Framework principal
- **Spring Data JPA** - Persistência de dados
- **Spring Validation** - Validação de dados
- **H2 Database** - Banco de dados em memória (desenvolvimento)
- **Maven** - Gerenciamento de dependências

### Frontend
- **React 18** - Biblioteca de interface
- **Vite** - Build tool e dev server
- **Axios** - Cliente HTTP

---

## Estrutura do Projeto

```
├── backend/
│   ├── pom.xml
│   └── src/
│       └── main/
│           ├── java/com/bookcatalog/
│           │   ├── BookCatalogApplication.java
│           │   ├── controller/
│           │   │   ├── BookController.java
│           │   │   ├── AuthorController.java
│           │   │   ├── PublisherController.java
│           │   │   ├── GenreController.java
│           │   │   └── GlobalExceptionHandler.java
│           │   ├── dto/
│           │   │   ├── BookRequestDTO.java
│           │   │   ├── BookResponseDTO.java
│           │   │   ├── AuthorDTO.java
│           │   │   ├── AuthorRequestDTO.java
│           │   │   ├── PublisherDTO.java
│           │   │   ├── PublisherRequestDTO.java
│           │   │   ├── GenreDTO.java
│           │   │   └── GenreRequestDTO.java
│           │   ├── domain/
│           │   │   ├── Book.java
│           │   │   ├── Author.java
│           │   │   ├── Publisher.java
│           │   │   └── Genre.java
│           │   ├── repository/
│           │   │   ├── BookRepository.java
│           │   │   ├── AuthorRepository.java
│           │   │   ├── PublisherRepository.java
│           │   │   └── GenreRepository.java
│           │   └── service/
│           │       ├── BookService.java
│           │       ├── AuthorService.java
│           │       ├── PublisherService.java
│           │       ├── GenreService.java
│           │       └── impl/
│           │           ├── BookServiceImpl.java
│           │           ├── AuthorServiceImpl.java
│           │           ├── PublisherServiceImpl.java
│           │           └── GenreServiceImpl.java
│           └── resources/
│               └── application.properties
├── frontend/
│   ├── package.json
│   ├── vite.config.js
│   ├── index.html
│   └── src/
│       ├── main.jsx
│       ├── App.jsx
│       ├── index.css
│       ├── services/
│       │   └── api.js
│       └── components/
│           ├── BookList.jsx
│           ├── AuthorList.jsx
│           ├── PublisherList.jsx
│           └── GenreList.jsx
└── TP1.md
```

---

## API Endpoints

### Livros
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/books` | Listar todos os livros |
| GET | `/api/books/{id}` | Buscar livro por ID |
| GET | `/api/books/search?title=` | Buscar por título |
| POST | `/api/books` | Criar novo livro |
| PUT | `/api/books/{id}` | Atualizar livro |
| DELETE | `/api/books/{id}` | Deletar livro |

### Autores
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/authors` | Listar todos os autores |
| GET | `/api/authors/{id}` | Buscar autor por ID |
| POST | `/api/authors` | Criar novo autor |
| PUT | `/api/authors/{id}` | Atualizar autor |
| DELETE | `/api/authors/{id}` | Deletar autor |

### Editoras
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/publishers` | Listar todas as editoras |
| GET | `/api/publishers/{id}` | Buscar editora por ID |
| POST | `/api/publishers` | Criar nova editora |
| PUT | `/api/publishers/{id}` | Atualizar editora |
| DELETE | `/api/publishers/{id}` | Deletar editora |

### Gêneros
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/genres` | Listar todos os gêneros |
| GET | `/api/genres/{id}` | Buscar gênero por ID |
| POST | `/api/genres` | Criar novo gênero |
| PUT | `/api/genres/{id}` | Atualizar gênero |
| DELETE | `/api/genres/{id}` | Deletar gênero |

---

## Como Executar

### Backend
```bash
cd backend
mvn spring-boot:run
```
O backend será executado em `http://localhost:8080`

Console H2: `http://localhost:8080/h2-console`

### Frontend
```bash
cd frontend
npm install
npm run dev
```
O frontend será executado em `http://localhost:3000`

---

## Microsserviço de Avaliações (Review Service)

Além do monólito principal, o projeto conta com um microsserviço independente, o **review-service**, responsável por gerenciar as avaliações (reviews) dos livros. Ele possui seu próprio banco H2 (arquivo separado), roda na porta `8081` e não compartilha código ou dependências com o backend principal — a única integração entre eles é via eventos (ver seção seguinte).

### Endpoints

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/reviews` | Listar todas as avaliações |
| GET | `/api/reviews?bookId=` | Listar avaliações de um livro |
| GET | `/api/reviews/{id}` | Buscar avaliação por ID |
| POST | `/api/reviews` | Criar nova avaliação |
| PUT | `/api/reviews/{id}` | Atualizar avaliação |
| DELETE | `/api/reviews/{id}` | Deletar avaliação |

---

## Eventos com RabbitMQ

A comunicação entre o backend principal e o review-service é feita de forma assíncrona via RabbitMQ, evitando acoplamento direto entre os serviços.

Quando um livro é deletado no backend (`DELETE /api/books/{id}`), o `BookService` publica um evento `book.deleted` (contendo o ID do livro) no exchange do tipo tópico `book-events`. O review-service mantém uma fila vinculada a esse exchange com a routing key `book.deleted` e, através de um `@RabbitListener`, consome o evento e remove todas as avaliações associadas àquele livro, mantendo a consistência dos dados entre os dois serviços sem uma chamada síncrona.

---

## Como Executar com Docker Compose

Para subir toda a stack (backend, review-service, RabbitMQ e frontend) de uma vez, execute na raiz do projeto:

```bash
docker compose up --build
```

Serviços disponíveis após a subida:
- Backend: `http://localhost:8080`
- Review Service: `http://localhost:8081`
- RabbitMQ Management: `http://localhost:15672` (usuário/senha: `guest`/`guest`)
- Frontend: `http://localhost:3000`

---

## Princípios de Design Aplicados

### SOLID
- **Single Responsibility**: Cada classe tem uma responsabilidade única (controllers lidam com HTTP, services com regras de negócio, repositories com acesso a dados)
- **Open/Closed**: Interfaces de serviço permitem extensão sem modificação
- **Liskov Substitution**: Implementações de serviço podem substituir suas interfaces
- **Interface Segregation**: Interfaces de serviço específicas por entidade
- **Dependency Inversion**: Controllers dependem de interfaces de serviço, não de implementações

### Clean Code
- Nomes descritivos para classes e métodos
- Injeção de dependência via construtor
- DTOs para separar contratos de API de entidades de domínio
- Validação de entrada com annotations

---

## Histórico de Dados e Auditoria

As entidades `Book`, `Author`, `Publisher` e `Genre` são anotadas com `@Audited` (Hibernate Envers), que registra automaticamente cada revisão (criação, atualização, remoção) em tabelas de auditoria separadas. Além disso, `BaseEntity` adiciona os campos `createdAt`/`updatedAt` via `@CreatedDate`/`@LastModifiedDate`. O endpoint `GET /api/books/{id}/history` expõe o histórico de revisões de um livro específico.

## Integração Contínua

O workflow `.github/workflows/ci.yml` executa `mvn test` para os módulos `backend` e `review-service` a cada push ou pull request na branch `main`.
