SIPp — генерация нагрузки для колл-центра
Инструмент для эмуляции множества клиентских вызовов в очередь support.

1. Прямой вызов на оператора 201 (без очереди):
docker compose run --rm sipp sipp asterisk:5060 \
  -sf /scenarios/call-to-201.xml \
  -m 1 -d 10000 \
  -trace_msg -trace_logs -log_file /logs/call-201.log
  
2. Наполнение очереди с ротацией номеров (каждые 5 сек):
# Один вызов каждые 5 секунд с разными номерами
while true; do
  docker compose run --rm sipp sipp asterisk:5060 \
    -sf /scenarios/queue-rotating.xml \
    -inf /scenarios/numbers.csv \
    -m 1 -d 15000 \
    -timeout 20s -trace_logs -log_file /logs/queue-$(date +%s).log
  sleep 5
done

3. Параллельные вызовы в очередь (5 одновременных):
docker compose run --rm sipp sipp asterisk:5060 \
  -sf /scenarios/queue-rotating.xml \
  -inf /scenarios/numbers.csv \
  -r 1 -l 5 -d 30000 \
  -timeout 60s -trace_logs -log_file /logs/parallel.log
  
