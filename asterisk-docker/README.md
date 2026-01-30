# Реализация учебного проекта (колл-центр)

## Инфраструктура Asterisk
- Образ: `andrius/asterisk:latest` (Asterisk 22)
- Очередь вызовов: `support` (стратегия `ringall`)
- Операторы: `operator1` (1001), `operator2` (1002)
- AMI: порт 5038, логин `monitor`, пароль `mysecret123`

# 1. Запустить Asterisk
cd ~/asterisk-docker
docker compose up -d

# 2. Проверить работоспособность
docker compose exec asterisk asterisk -rx "queue show support"
docker compose logs asterisk | grep "Asterisk Ready"

# 3. Запустить Spring Boot приложение
cd ~/code_dump/call-center-monitor/code
mvn spring-boot:run -Dspring-boot.run.profiles=dev

## Проверка работоспособности
```bash
# Статус Asterisk
docker compose exec asterisk asterisk -rx "core show uptime"

# Просмотр очереди
docker compose exec asterisk asterisk -rx "queue show support"

# Проверка AMI (должен вернуть "Authentication accepted")
echo -e "Action: Login\r\nUsername: monitor\r\nSecret: mysecret123\r\n\r\n" | nc localhost 5038 | grep "Authentication accepted"

