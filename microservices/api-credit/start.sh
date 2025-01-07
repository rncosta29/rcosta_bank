#!/bin/bash

# Definindo variáveis de ambiente para o host e porta do servidor gateway
GATEWAY_HOST="gateway"
GATEWAY_PORT="8765"

# Mensagem informativa
echo "Aguardando o Gateway (${GATEWAY_HOST}:${GATEWAY_PORT}) ficar disponível..."

# Chamando o wait-for-it.sh para aguardar o Gateway
/app/wait-for-it.sh ${GATEWAY_HOST}:${GATEWAY_PORT} --strict --timeout=60

# Verificando o status de retorno do wait-for-it
if [ $? -eq 0 ]; then
  echo "Servidor Gateway está disponível. Iniciando o Servico..."
  
  touch /app/api-credit.log
  
  # Iniciando a aplicação com nohup e redirecionando logs
  nohup java -jar api-credit.jar > /app/api-credit.log 2>&1 &
  
  # Mantendo o container ativo
  tail -f /app/api-credit.log
else
  echo "Erro: O servidor Gateway não ficou disponível no tempo limite."
  exit 1
fi
