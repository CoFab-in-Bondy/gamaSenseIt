#!/bin/sh

if [ "$EUID" -ne 0 ]
  then echo "Please run as root"
  exit
fi

systemctl daemon-reload
mv gamasenseit.service /etc/systemd/system/gamasenseit.service
systemctl enable gamasenseit.service
systemctl status gamasenseit.service
