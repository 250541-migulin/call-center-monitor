#!/bin/bash
TIMESTAMP=$(date +%Y%m%d_%H%M%S)
LOG="/logs/queue-${TIMESTAMP}.log"

echo "📞 Вызов в очередь → ${LOG}"
docker compose exec sipp sipp asterisk:5060 \
  -sf /scenarios/queue.xml \
  -m 1 -d 5000 \
  -log_file "${LOG}"