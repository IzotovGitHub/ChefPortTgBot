#!/bin/sh

VERSION=1.0-SNAPSHOT
DISPATCHER_PATH=Dispatcher
NODE_PATH=Node
AUTH_SERVICE_PATH=Auth-Service

java -jar ./${NODE_PATH}/target/Node-${VERSION}.jar &
java -jar ./${AUTH_SERVICE_PATH}/target/Auth-Service-${VERSION}.jar &
java -jar ./${DISPATCHER_PATH}/target/Dispatcher-${VERSION}.jar;