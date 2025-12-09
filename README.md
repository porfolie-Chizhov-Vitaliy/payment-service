# payment-service
Микросервис для обработки платежей - Java, Spring Boot, Kafka


## Что делает данный сервис ? 
 - Принимает и проверяет платежи через REST API
 - Сохраняет в Postgres
 - Принимает и отправляет события в Kafka
 - Предоставляет метрики ля мониторинга (Prometheus)

## Основные команды:

**Bash:**
- **Сборка** ```./mvnw clean package -DskipTests```
- **Проверить работоспособность сервиса**
`curl http://localhost:8080/actuator/health`
- **Создать платеж**
```
curl -X POST http://localhost:8081/api/payments \
            -H "Content-Type: application/json" \
            -d "{  "amount": 100,  "fromAccount": 40817810099910004328,  "toAccount": 40817810099910004328, "currency": "RUB","description": "Test payment #1"}" 
```
**Docker:**
  - **Сборка образа**
    docker build -t payment-service .

  - Публикация в Docker Hub
    ```
    docker tag payment-service chizhovvm/payment-service
    docker push chizhovvm/payment-service
    ```
