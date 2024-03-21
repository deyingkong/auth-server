#!/usr/bin/env bash
cd /home/ec2-user/server
sudo java -Xms256m -Xmx512m -jar *.jar > /dev/null 2> /dev/null < /dev/null &