#!/bin/sh

# * * * * * crontab_bootup_my_pc.sh

service_host=localhost:5000

bootup_status=$(curl http://$service_host/get-bootup-status)
echo "$bootup_status"

if [ "$bootup_status" = "NeedBootup" ]; then
  curl http://$service_host/has-bootup
  echo "Need to Bootup !"
  javac WOL.java
  # The first arg is MAC address
  java WOL FCAA14D0C108
fi
