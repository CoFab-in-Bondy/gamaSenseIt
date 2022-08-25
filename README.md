# GamaSenseIt

Application for sensors, you can create group or sensor and receive data from mqtt.

## Table of Contents

1. [Introduction](#introduction)
2. [Install JDK 17](docs/dev/Java.md)
3. [Setup MySQL](docs/dev/MySQL.md)
4. [Configure Mosquito](docs/dev/Mosquito.md)
5. [Generate SSL Certificates for localhost](docs/dev/SSL.md)
6. [Setup for development](docs/dev/Dev.md)
7. [Install and run GamaSenseIt](#install-and-run-gamasenseit)
8. [Api](docs/dev/Api.md)
9. [License](LICENSE)

## Introduction

Before all please run update on your computer.

Update Ubuntu.
```sh
sudo apt -y update; sudo apt -y upgrade
```

Once the update are performed you can install all dependency. please see the [Table of Contents](#table-of-contents). 

## Install and run GamaSenseIt

```sh
ssh USERNAME@HOST
sudo apt -y update; sudo apt -y upgrade
sudo apt install openjdk-17-jre
git clone https://github.com/CoFab-in-Bondy/gamaSenseIt.git gamaSenseIt
cd gamaSenseIt

mv .env.exemple .env
vi .env
mv server/src/main/resources/application.properties.exemple server/src/main/resources/application.properties
vi server/src/main/resources/application.properties
mb app/src/main/resources/settings.properties.exemple app/src/main/resources/settings.properties
vi app/src/main/resources/settings.properties

chmod +x ./script/ssl.sh
./script/ssl.sh
chmod +x ./script/docker.sh
./script/docker.sh

sudo docker-compose up -d --build mysql
exit
mysql -e "source schema.sql;source data.sql" -u gamasenseit -D gamasenseit -pDB_PASSWORD -h HOST -P 3307 --default-character-set=UTF8

ssh USERNAME@HOST
sudo docker-compose up -d --build server
sudo docker-compose logs -f
```

You can generate unique password with `python3 -c "print(__import__('secrets').token_urlsafe(30))"`

## Remove application

```sh
sudo docker-compose stop
sudo docker-compose rm
```

## License

GamaSenseIt is licensed under the terms of the GNU License (see the file LICENSE).
