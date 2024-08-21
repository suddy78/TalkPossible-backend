# switch.sh

#!/bin/bash

# Crawl current connected port of WAS
CURRENT_PORT=$(cat /home/ubuntu/service_url.inc  | grep -Po '[0-9]+' | tail -1)
TARGET_PORT=0

echo "> Nginx currently proxies to ${CURRENT_PORT}."

# Toggle port number
if [ ${CURRENT_PORT} -eq 8081 ]; then
    TARGET_PORT=8082
elif [ ${CURRENT_PORT} -eq 8082 ]; then
    TARGET_PORT=8081
else
    echo "> No WAS is connected to nginx"
    exit 1
fi

echo "> Target port will be ${TARGET_PORT}."

# Change proxying port into target port.
echo "set \$service_url http://127.0.0.1:${TARGET_PORT};" | sudo tee /home/ubuntu/service_url.inc

echo "> Now Nginx proxies to ${TARGET_PORT}."

# Reload nginx
if sudo service nginx reload; then
    echo "> Nginx reloaded successfully."
else
    echo "> Nginx reload failed."
    exit 1
fi
