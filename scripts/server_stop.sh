#!/bin/bash
echo "Current directory: "
echo $PWD

for pid in $(ps -ef | pgrep java | awk '/java/ && /auth-server.jar/ {print $2}'); do sudo kill -9 $pid; done