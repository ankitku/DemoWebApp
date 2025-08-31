# Full-Stack Todo/Greeting Application

## Overview

This is a full-stack web application featuring a React frontend and a Spring Boot backend. The application is fully containerized using Docker and can be launched with a single command. It provides basic CRUD (Create, Read, Update, Delete) functionality for managing "greetings".

The entire stack, including the database and a database management UI, is orchestrated by Docker Compose for easy setup and development.

## Features

- **Backend**: RESTful API built with Java and Spring Boot.
- **Frontend**: Single Page Application (SPA) built with React.
- **Database**: PostgreSQL for persistent data storage.
- **Containerization**: Fully containerized services using Docker and Docker Compose.
- **API Documentation**: Interactive API documentation via Swagger UI.
- **Database Management**: Pre-configured pgAdmin for easy database access and management.
- **Health Checks**: Integrated health checks for backend and database services to ensure a robust startup order.

## Technology Stack

- **Backend**: Java, Spring Boot, Spring Data JPA
- **Frontend**: JavaScript, React
- **Database**: PostgreSQL
- **API Docs**: SpringDoc (Swagger UI)
- **Containerization**: Docker, Docker Compose

## Project Structure

```
todo-app/
├── docker-compose.yml     # Main Docker Compose file to orchestrate all services
├── frontend/              # Contains the React frontend application
│   ├── Dockerfile
│   ├── package.json
│   └── src/
└── todo/                  # Contains the Spring Boot backend application
    ├── Dockerfile
    ├── pom.xml            # (or build.gradle)
    ├── servers.json       # pgAdmin server configuration
    └── src/
```

## Getting Started

### Prerequisites

- [Docker](https://www.docker.com/get-started)
- [Docker Compose](https://docs.docker.com/compose/install/) (usually included with Docker Desktop)

### Running the Application

1.  Clone this repository to your local machine.
2.  Navigate to the root `todo-app` directory in your terminal.
3.  Run the following command to build the container images and start all the services:

    ```sh
    docker-compose up --build -d
    ```

    - `--build` forces a rebuild of the images if there are changes in the `Dockerfile` or application code.
    - `-d` runs the containers in detached mode (in the background).

4.  The application will be available at the ports listed in the section below.

To stop and remove all the running containers, run:

```sh
docker-compose down
```

## Services and Ports

The `docker-compose` setup exposes the following services on your local machine:

| Service        | Local Port | URL                                   | Credentials                 |
| -------------- | ---------- | ------------------------------------- | --------------------------- |
| **Frontend**   | `3000`     | http://localhost:3000                 | N/A                         |
| **Backend**    | `8080`     | http://localhost:8080                 | N/A                         |
| **Swagger UI** | `8080`     | http://localhost:8080/swagger-ui.html | N/A                         |
| **pgAdmin**    | `5050`     | http://localhost:5050                 | `admin@local.com` / `admin` |
| **PostgreSQL** | `5432`     | `localhost:5432` (for local clients)  | `springuser` / `password`   |

## Backend Details

The backend is a Spring Boot application that provides a REST API for CRUD operations on "greetings".

- **API**: The API endpoints are documented in `todo/API_HANDOFF.md` and can be explored interactively using the **Swagger UI**.
- **Database**: The backend connects to the PostgreSQL database service named `postgres` within the Docker network. The database name is `greetings_db`.
- **Health Check**: A health endpoint is available at `/actuator/health`, which is used by Docker Compose to ensure the database is ready before starting the backend.
- **Testing**: The backend includes a standard testing structure for unit and integration tests using frameworks like JUnit and Mockito.
- **Profiles**: Spring Profiles can be used to manage different configurations (e.g., `dev`, `prod`), although this setup uses environment variables from `docker-compose.yml` for configuration.

## Frontend Details

The frontend is a React application that provides the user interface for interacting with the backend API.

- **API Communication**: The frontend is configured during its Docker build to communicate with the backend at `http://localhost:8080`.
- **Development**: To run the frontend in development mode locally (without Docker), you can navigate to the `frontend` directory and run `npm start`.
- **Production Build**: The `Dockerfile` for the frontend creates an optimized production build and serves it using a lightweight web server, making it ready for deployment.
