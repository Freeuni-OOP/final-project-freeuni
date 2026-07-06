[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/skmUAHf8)

# OOP Hotel - Final Project

Hotel booking website (Freeuni-OOP final project).
Backend: **Java Servlets + JSP** on **Tomcat**, data in **MySQL**. No server-side framework.

## Run locally with Docker (recommended)

Prerequisite: Docker Desktop.

```bash
docker compose up --build
```

Then open:

- App: <http://localhost:8080/>
- Health check: <http://localhost:8080/api/ping> → `{"status":"ok"}`

MySQL runs on `localhost:3306` (database `oophotel`, user `oophotel` / password `oophotel`).

Stop with `Ctrl+C`, then `docker compose down` (add `-v` to also wipe the database volume).

## Run the backend without Docker (dev)

Prerequisites: JDK 21+, Maven.

```bash
mvn clean package
# then deploy target/oophotel.war to a Tomcat 10.1 server
```

## Project structure

```
src/main/java/com/oophotel/
  servlet/   HTTP layer (servlets)
  model/     domain models
  dao/       SQL queries
  db/        DB connection (DataSource)
src/main/webapp/   frontend (HTML/CSS/JS) + index.jsp
database/schema.sql  MySQL schema + seed data
```
