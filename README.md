# GamaSenseIt
Application for sensors.

## Table of Contents
1. [Introduction](#introduction)
2. [Install JDK 17](#install-jdk-17)
3. [Setup MySQL](#setup-mysql)
4. [Configure Mosquito](#configure-mosquito)
5. [Generate SSL for localhost](#generate-ssl-for-localhost)
6. [Install and run GamaSenseIt](#install-and-run-gamasenseit)
7. [Api](#api)
8. [Convention](#convention)
9. [License](#license)

## Introduction

This application runs with Java 17, mysql and mosquitto.
You therefore need to install them yourself with the help below.
Other dependencies are used, but they are installed and setup automatically.
before doing the maneuvers below, please run the update commands for your systems.

Update Ubuntu.
```
sudo apt -y update; sudo apt -y upgrade
```

Update Manjaro.
```
yay -Syyu
```
## Install JDK 17

Get installer for jdk 17.
```
sudo add-apt-repository ppa:linuxuprising/java
sudo apt -y update
sudo apt install oracle-java17-installer
```

Change the default java used.
```
sudo update-alternatives --config java
```

## Setup MySQL

Install for Manjaro.
```sh
yay -S mysql
sudo mysql_install_db --user=mysql --basedir=/usr --datadir=/var/lib/mysql

sudo systemctl enable mysqld
sudo systemctl restart mysqld
systemctl status mysqld
```

Install for Ubuntu.
```sh
sudo apt install mysql-server
```

Install and login as root.
```sh
sudo mysql_secure_installation
sudo mysql
```

Create user and database for **GamaSenseIt**.
```sql
CREATE USER 'gamasenseit'@'localhost' IDENTIFIED BY 'gamasenseit';
CREATE DATABASE gamasenseit;
GRANT ALL PRIVILEGES ON gamasenseit.* TO 'gamasenseit'@'localhost';
FLUSH PRIVILEGES;
\q
```

Then try to connect to the database as gamasenseit.
```sh
mysql -u gamasenseit -pgamasenseit -h localhost -P 3306 -D gamasenseit
```
Set `spring.jpa.hibernate.ddl-auto=create` in `application.properties` for create tables.

This will create tables following the model below.

![Model representing the schema](https://github.com/CoFab-in-Bondy/gamaSenseIt/blob/master/docs/images/model.png?raw=true)

## Configure Mosquito

Install for Manjaro.
```sh
yay -S mosquitto

sudo systemctl enable mosquitto
sudo systemctl restart mosquitto
systemctl status mosquitto
```

Install for Ubuntu.
```sh
sudo apt install mosquitto

# you can also install mosquitto clients to test / develop
# sudo apt install mosquitto-clients
```

***

Make mosquitto a service.
```sh
# iptables -t filter -A INPUT -p tcp --dport 1883 -j LOGACCEPT
```

Set up authentication.
 * Make a password file with user gamaseniseit.
    ```sh
    sudo mosquitto_passwd -c /etc/mosquitto/passwd gamasenseit
    ```
 * Create a configuration file for gamesenseit.
    ```sh
    sudo cp /etc/mosquitto/mosquitto.conf /etc/mosquitto/gamasenseit.conf
    sudo nano /etc/mosquitto/gamasenseit.conf
    ```
 * Uncomment the lines below / put the values there.
   * `listener 1883` for set the port on 1883
   * `password_file /etc/mosquitto/passswd` : path to passwords file.
   * `allow_anonymous false` allow only authenticated users.
 * Assign configuration to mosquitto.
    ```sh
    mosquitto -v -c '/etc/mosquitto/gamasenseit.conf'
    ```
   
For subscribe and publish (_see more with `man mosquitto_sub` and `man mosquitto_pub`_).
```sh
mosquitto_sub [-h host] [-u user] [-P password] [-t topic]
mosquitto_pub [-h host] [-u user] [-P password] [-t topic] -m [message]
```

## Generate SSL Certificates for localhost
### Generate Self-signed Certificate
```
keytool -genkeypair -alias gamasenseit -keyalg RSA -keysize 4096 -storetype PKCS12 -keystore gamasenseit.p12 -validity 3650
cp gamasenseit src/main/resources/gamasenseit.p12
```

- genkeypair: generates a key pair
- alias: the alias name for the item we are generating
- keyalg: the cryptographic algorithm to generate the key pair
- keysize: the size of the key
- storetype: the type of keystore
- keystore: the name of the keystore
- validity: validity number of days

### See content of PKCS12 files
```
keytool -list -v -keystore gamasenseit.p12
```


## Install and run GamaSenseIt

Build standalone JAR package
```sh
git clone https://github.com/CoFab-in-Bondy/gamaSenseIt.git gamaSenseIt
cd gamaSenseIt
./mvnw -T 1C clean package
```

Run the server
```sh
java -jar target/gamasenseit-0.0.1-SNAPSHOT.jar
```

go on http://localhost:8080/index.html

And that's all !


If you want rebuild the maven wrapper use the command bellow.
```
mvn -N io.takari:maven:wrapper -Dmaven=3.8.1
```

## API

```
GET /public/parameters?sensorId=<ID>         | List<SensorData>
        [&parameterMetadataId=<ID>]
        [&start=MMddyyyy]
        [&end=MMddyyyy]
        [&type=json]

GET /public/sensors                          | List<Sensor>
GET /public/sensors/<ID>                     | Sensor
GET /public/sensors/names                    | List<Sensor.name>

GET /public/sensors/metadata                 | List<SensorMetadata>
GET /public/sensors/metadata/names           | List<name + " -- " + version>
GET /public/sensors/metadata/<ID>/parameters | List<ParameterMetadata>

GET /public/parameters/metadata              | List<ParameterMetadata>
GET /public/parameters/metadata/<ID>         | ParameterMetadata

GET /public/server/date                      | Current time in EPOCH
GET /public/server/separator                 | DEFAULT_DATA_SEPARATOR

POST /private/sensors                        | add Sensor
PATCH /private/sensors                       | update Sensor
POST /private/sensors/metadata               | add SensorMetadata
POST /private/parameters/metadata            | add ParameterMetadata
```

## Convention

- Interface must be named with an `I` in front.
- Enum must be named with an `E` in front.
- Repository : `IParameterRepository` -> `var parameterRepo`
- The names of the collections must be plural.
- Optional var must have optional in their names if their content is extracted in a variable.
- If a variable contains an id then it has the name id at the end.
- Recurrent variable naming convention 
    - | Class               | Name  |
      | :------------------ | :---- | 
      | ParameterMetadata   | `pmd` |
      | Parameter           | `p`   |
      | SensorMetadata      | `smd` |
      | Sensor              | `s`   |
      | User                | `u`   |
      | UserGroup           | `g`   |
      | User                | `u`   |
      | Role                | `r`   |
      | Exception           | `e`   |
      | DataFormat          | `df`  |
      | DataParameter       | `dp`  |
- Variables with a short name (less than 3 letters) must be added to the table above unless it's a unique name

## License

GamaSenseIt is licensed under the terms of the GNU License (see the file LICENSE).
