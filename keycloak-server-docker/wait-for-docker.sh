#!/bin/sh

# when no arguments was given
if [ $# -eq 0 ]
then
    echo "Usage: $0 <host> [port]"
    exit 1
fi

HOST=$1
# port defaults to 8080
PORT=${2:-8080}
RETRIES=50

echo -n "Waiting for keycloak to start on ${HOST}:${PORT}"
# loop until we connect successfully or failed
until curl -f -v "http://${HOST}:${PORT}/auth/realms/test/.well-known/openid-configuration" >/dev/null 2>/dev/null
do
    RETRIES=$(($RETRIES - 1))
    if [ $RETRIES -eq 0 ]
    then
        echo "Failed to connect"
        exit 1
    fi
    # wait a bit
    echo -n "."
    sleep 1
done
echo
