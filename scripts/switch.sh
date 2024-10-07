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

# Reload nginx
if sudo service nginx reload; then
    echo "> Nginx reloaded successfully. Now Nginx proxies to ${TARGET_PORT}."
else
    echo "> Nginx reload failed."
    exit 1
fi

# Terminate the old version running on the previous port
OLD_PID=$(lsof -t -i TCP:${CURRENT_PORT})

if [ -n "${OLD_PID}" ]; then
    echo "> Trying to kill process running on port ${CURRENT_PORT} (PID: ${OLD_PID})"
    
    sudo kill ${OLD_PID}
    sleep 5

    if lsof -i TCP:${CURRENT_PORT} > /dev/null; then
        echo "> Process on port ${CURRENT_PORT} is still running."
    else
        echo "> Process on port ${CURRENT_PORT} has been successfully terminated."
else
    echo "> No process found on port ${CURRENT_PORT}."
fi
