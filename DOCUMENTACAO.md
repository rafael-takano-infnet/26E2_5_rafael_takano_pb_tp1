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
