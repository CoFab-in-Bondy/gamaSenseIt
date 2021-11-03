# GamaSenseIt
Application for sensors.

## Table of Contents
1. [Setup MySQL](#setup-mysql)
2. [Configure Mosquito](#configure-mosquito)
3. [Install and run GamaSenseIt](#install-and-run-gamasenseit)
4. [Api](#api)
5. [Convention](#convention)
6. [License](#license)

```
add mvnw 

sudo apt install git

sudo apt -y update
sudo apt -y upgrade
sudo add-apt-repository ppa:linuxuprising/java
sudo apt -y update
sudo apt install oracle-java17-installer
sudo update-alternatives --config java

sudo vi /etc/profile
JAVA_HOME="/usr/lib/jvm/java-17-oracle"
source /etc/environment
```

```
mvn -N io.takari:maven:wrapper -Dmaven=3.8.2
```

## Setup MySQL

Install for Manjaro
```sh
yay -Syyu
yay -S mysql
sudo mysql_install_db --user=mysql --basedir=/usr --datadir=/var/lib/mysql
```

Install for Ubuntu
```sh
sudo apt update
sudo apt upgrade
sudo apt install mysql-server
```

***
Make MySQL a service
```sh
sudo systemctl enable mysqld
sudo systemctl restart mysqld
systemctl status mysqld
```

Install and login as root
```sh
sudo mysql_secure_installation
mysql -u root -p
```

Create user and database for **GamaSenseIt**
```sql
CREATE USER 'gamasenseit'@'localhost' IDENTIFIED BY 'gamasenseit';
CREATE DATABASE gamasenseit;
GRANT ALL PRIVILEGES ON gamasenseit.* TO 'gamasenseit'@'localhost';
FLUSH PRIVILEGES;
\q
```

Then try to connect to the database as gamasenseit
```sh
mysql -u gamasenseit -pgamasenseit -h 127.0.0.1 -P 3306 -D gamasenseit
```
Set `spring.jpa.hibernate.ddl-auto=create` in `application.properties` for create tables

This will create tables following the model below

![Model representing the schema](https://github.com/CoFab-in-Bondy/gamaSenseIt/blob/master/docs/images/model.png?raw=true)

## Configure Mosquito

Install for Manjaro
```sh
yay -Syyu
yay -S mosquitto
```

Install for Ubuntu
```sh
sudo apt update
sudo apt upgrade
sudo apt install mosquitto
sudo apt install mosquitto-clients
```

***

Make mosquitto a service
```sh
sudo systemctl enable mosquitto
sudo systemctl restart mosquitto
systemctl status mosquitto
# iptables -t filter -A INPUT -p tcp --dport 1883 -j LOGACCEPT
```

Set up authentication
 * Make a password file with user gamaseniseit
    ```sh
    sudo mosquitto_passwd -c /etc/mosquitto/passwd gamasenseiy
    ```
 * Create a configuration file for gamesenseit
    ```sh
    cp /etc/mosquitto/mosquitto.conf /etc/mosquitto/gamasenseit.conf
    sudo nano /etc/mosquitto/gamasenseit.conf
    ```
 * Uncomment the lines below and put the values there
   * `listeer 1883` for set the port on 183
   * `password_file /etc/mosquitto/passswd` : path to passwords file
   * `allow_anonymous false` allow only authenticated users
 * Assign configuration to mosquitto
    ```sh
    mosquitto -v -c '/etc/mosquitto/gamasenseit.conf'
    ```
   
For subscribe and publish (_see more with `man mosquitto_sub` and `man mosquitto_pub`_)
```sh
mosquitto_sub [-h host] [-u user] [-P password] [-t topic]
mosquitto_pub [-h host] [-u user] [-P password] [-t topic] -m [message]
```

## Install and run GamaSenseIt

Build standalone JAR package
```sh
git clone https://github.com/CoFab-in-Bondy/gamaSenseIt.git gamaSenseIt
cd gamaSenseIt
mvn clean package
```

Run the server
```sh
java -jar target/gamasenseit-0.0.1-SNAPSHOT.jar
```

And that's all !

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
