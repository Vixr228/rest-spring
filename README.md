Приложение представляет собой Rest API реализованное при помощи Spring.

Запуск: 
./mvnw clean package -DskipTests && docker compose build && docker compose up -d

Остановка: 
docker compose down

Swagger: 
http://localhost:8080/swagger-ui/index.html
