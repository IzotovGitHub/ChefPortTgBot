#!/bin/sh

VERSION=1.0-SNAPSHOT
DISPATCHER_PATH=Dispatcher
NODE_PATH=Node

java -jar ./${NODE_PATH}/target/Node-${VERSION}.jar &
java -jar ./${DISPATCHER_PATH}/target/Dispatcher-${VERSION}.jar;