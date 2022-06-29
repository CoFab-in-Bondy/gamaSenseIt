# GamaSenseIt
Application for sensors, you can create group or sensor and receive data from mqtt.

## Table of Contents
1. [Introduction](#introduction)
2. [Install JDK 17](docs/Java.md)
3. [Setup MySQL](docs/MySQL.md)
4. [Configure Mosquito](docs/Mosquito.md)
5. [Generate SSL Certificates for localhost](docs/SSL.md)
6. [Install and run GamaSenseIt](#install-and-run-gamasenseit)
7. [Setup for development](#setup-for-development)
8. [Api](docs/Api.md)
9. [License](LICENSE)

## Introduction

Before all please run update on your computer.

Update Ubuntu.
```
sudo apt -y update; sudo apt -y upgrade
```

Update Manjaro.
```
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

## Setup for development

Build lightweight jar
```sh
git clone https://github.com/CoFab-in-Bondy/gamaSenseIt.git gamaSenseIt
cd gamaSenseIt
./mvnw dependency:sources
(cd ui && npm install .)
```

Run the ng serve and fast build
```sh
(cd ui && npm start)
./mvnw spring-boot:run -T 2C -Dspring-boot.run.arguments=--gamaSenseIt.cors-url=http://localhost:4200 -P -front
```

## License

GamaSenseIt is licensed under the terms of the GNU License (see the file LICENSE).
