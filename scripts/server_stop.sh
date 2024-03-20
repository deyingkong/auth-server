#!/bin/bash
for pid in $(ps -ef | awk '/java/ && /auth-server.jar/ {print $2}'); do sudo kill -9 $pid; done