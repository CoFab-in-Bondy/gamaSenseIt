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

Update Manjaro.
```sh
yay -Syyu
```

Once the update are performed you can install all dependency. please see the [Table of Contents](#table-of-contents). 

## Install and run GamaSenseIt

Build standalone JAR package
```sh
git clone https://github.com/CoFab-in-Bondy/gamaSenseIt.git gamaSenseIt
cd gamaSenseIt
./mvnw -T 2C clean package
```

Run the server
```sh
java -jar target/gamasenseit-0.0.1-SNAPSHOT.jar
```

go on http://localhost:8080/index.html

And that's all !

## Deploy

```sh
ssh USERNAME@HOST
sudo apt-get update
sudo apt-get upgrade
sudo apt install openjdk-17-jre
git clone https://github.com/CoFab-in-Bondy/gamaSenseIt.git gamaSenseIt
cd gamaSenseIt

mv .env.exemple .env
vi .env
mv server/src/main/resources/application.properties.exemple server/src/main/resources/application.properties
vi server/src/main/resources/application.properties
vi app/src/main/resources/settings.properties

chmod +x ./script/ssl.sh
./script/ssl.sh
chmod +x ./script/docker.sh
./script/docker.sh

sudo docker-compose up mysql -d --build
exit
mysql -e "source schema.sql;source data.sql" -u gamasenseit -D gamasenseit -pDB_PASSWORD -h HOST -P 3307 --default-character-set=UTF8

ssh USERNAME@HOST
sudo docker-compose up server -d --build
sudo docker-compose logs -f
```

You can generate unique password with `python3 -c "print(__import__('secrets').token_urlsafe(30))"`

## License

GamaSenseIt is licensed under the terms of the GNU License (see the file LICENSE).
