#!/bin/bash

# Definindo variáveis de ambiente para o host e porta do servidor Eureka
EUREKA_HOST="server"
EUREKA_PORT="8761"

# Mensagem informativa
echo "Aguardando o servidor Eureka (${EUREKA_HOST}:${EUREKA_PORT}) ficar disponível..."

# Chamando o wait-for-it.sh para aguardar o Eureka
/app/wait-for-it.sh ${EUREKA_HOST}:${EUREKA_PORT} --strict --timeout=60

# Verificando o status de retorno do wait-for-it
if [ $? -eq 0 ]; then
  echo "Servidor Eureka está disponível. Iniciando o Gateway..."
  
  touch /app/gateway.log
  
  # Iniciando a aplicação com nohup e redirecionando logs
  nohup java -jar gateway.jar > /app/gateway.log 2>&1 &
  
  # Mantendo o container ativo
  tail -f /app/gateway.log
else
  echo "Erro: O servidor Eureka não ficou disponível no tempo limite."
  exit 1
fi
