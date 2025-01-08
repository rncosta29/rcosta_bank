#!/bin/bash

# Definindo variaveis de ambiente para o host e porta do servidor gateway
GATEWAY_HOST="gateway"
GATEWAY_PORT="8765"
APP_HOST="localhost"
APP_PORT="8080"

# Mensagem informativa
echo "Aguardando o Gateway (${GATEWAY_HOST}:${GATEWAY_PORT}) ficar disponivel..."

# Chamando o wait-for-it.sh para aguardar o Gateway
/app/wait-for-it.sh ${GATEWAY_HOST}:${GATEWAY_PORT} --strict --timeout=60

# Verificando o status de retorno do wait-for-it
if [ $? -eq 0 ]; then
  echo "Servidor Gateway está disponivel. Iniciando o Serviço..."
  
  touch /app/api-account.log

  # Iniciando a aplicacao com nohup e redirecionando logs
  nohup java -jar api-account.jar > /app/api-account.log 2>&1 &

  # Aguardando o servico da aplicacao responder com status "OK" no endpoint /actuator/health
  echo "Aguardando o servico ficar pronto em ${APP_HOST}:${APP_PORT}/actuator/health..."

  while true; do
    # Verificando se o endpoint de saude está disponivel e retornando 200
    HTTP_STATUS=$(curl -s -o /dev/null -w "%{http_code}" http://${APP_HOST}:${APP_PORT}/actuator/health)
    
    if [ "$HTTP_STATUS" -eq 200 ]; then
      echo "Aplicacao esta pronta. Iniciando o sonar-scanner..."
      sonar-scanner
      break
    else
      echo "Aguardando o servico responder com status 200... Status: $HTTP_STATUS"
    fi
    
    # Aguardando 5 segundos antes de tentar novamente
    sleep 5
  done

  # Mantendo o container ativo com os logs da aplicacao
  tail -f /app/api-account.log
else
  echo "Erro: O servidor Gateway nao ficou disponivel no tempo limite."
  exit 1
fi
