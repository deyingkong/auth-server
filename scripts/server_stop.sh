#!/bin/bash
echo "Current directory: "
echo $PWD

for pid in $(pgrep -u root -a java| awk '/auth-server/ {print $1}'); do sudo kill -9 $pid; done
