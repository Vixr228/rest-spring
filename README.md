# Spring REST API
Приложение представляет собой Rest API реализованное при помощи Spring.

## Технологии
- Java 17
- Spring Framework
- Maven
- PostgreSQL
- Liquibase

## Использование
Запустите при помощи команды 
``` sh
./mvnw clean package -DskipTests && docker compose build && docker compose up -d
```

Для остановки используй
``` sh
docker compose down
```
Swagger: 
http://localhost:8080/swagger-ui/index.html
