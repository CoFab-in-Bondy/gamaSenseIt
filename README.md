# GamaSenseIt
Application for sensors.

## Table of Contents
1. [Setup MySQL](#setup-mysql)
2. [Configure Mosquito](#configure-mosquito)
3. [Install and run GamaSenseIt](#install-and-run-gamasenseit)
4. [License](#license)

## Setup MySQL

Install for Manjaro
```bash
yay -Syyu
yay -S mysql
sudo mysql_install_db --user=mysql --basedir=/usr --datadir=/var/lib/mysql
```

Install for Ubuntu
```bash
sudo apt update
sudo apt upgrade
sudo apt install mysql-server
```

***
Make MySQL a service
```bash
sudo systemctl enable mysqld
sudo systemctl restart mysqld
systemctl status mysqld
```

Install and login as root
```bash
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
Set `spring.jpa.hibernate.ddl-auto=create` in `application.properties` for create tables

This will create tables following the model below

![Model representing the schema](https://github.com/CoFab-in-Bondy/gamaSenseIt/blob/master/model.png?raw=true)

## Configure Mosquito

Install for Manjaro
```bash
yay -Syyu
yay -S mosquitto
```

Install for Ubuntu
```bash
sudo apt update
sudo apt upgrade
sudo apt install mosquitto
sudo apt install mosquitto-clients
```

***

Make mosquitto a service
```bash
sudo systemctl enable mosquitto
sudo systemctl restart mosquitto
systemctl status mosquitto
# iptables -t filter -A INPUT -p tcp --dport 1883 -j LOGACCEPT
```

Set up authentication
 * Make a password file with user gamaseniseit
    ```bash
    sudo mosquitto_passwd -c /etc/mosquitto/passwd gamasenseiy
    ```
 * Create a configuration file for gamesenseit
    ```bash
    cp /etc/mosquitto/mosquitto.conf /etc/mosquitto/gamasenseit.conf
    sudo nano /etc/mosquitto/gamasenseit.conf
    ```
 * Uncomment the lines below and put the values there
   * `listeer 1883` for set the port on 183
   * `password_file /etc/mosquitto/passswd` : path to passwords file
   * `allow_anonymous false` allow only authenticated users
 * Assign configuration to mosquitto
    ```````bash
    mosquitto -v -c '/etc/mosquitto/gamasenseit.conf'
    ```````
 
For subscribe and publish (_see more with `man mosquitto_sub` and `man mosquitto_pub`_)
```bash
mosquitto_sub [-h host] [-u user] [-P password] [-t topic]
mosquitto_pub [-h host] [-u user] [-P password] [-t topic] -m [message]
```

## Install and run GamaSenseIt

Build standalone JAR package
```bash
git clone https://github.com/CoFab-in-Bondy/gamaSenseIt.git gamaSenseIt
cd gamaSenseIt
mvn clean package
```

Run the server
```bash
java -jar target/gamasenseit-0.0.1-SNAPSHOT.jar
```

And that's all !

## License

GamaSenseIt is licensed under the terms of the GNU License (see the file LICENSE).
