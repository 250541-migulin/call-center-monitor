#!/bin/bash
echo "🌊 Поток вызовов (каждые 5 сек). Остановить: Ctrl+C"
while true; do
  TIMESTAMP=$(date +%Y%m%d_%H%M%S)
  LOG="/logs/stream-${TIMESTAMP}.log"
  echo "[$(date '+%H:%M:%S')] 📞 Новый вызов → ${LOG}"
  docker compose exec sipp sipp asterisk:5060 \
    -sf /scenarios/queue.xml \
    -m 1 -d 5000 \
    -log_file "${LOG}" > /dev/null 2>&1
  sleep 5
done