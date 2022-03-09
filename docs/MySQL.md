# Setup MySQL

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

***

Install and login as root.
```sh
sudo mysql_secure_installation
sudo mysql
```

Create user and database for **GamaSenseIt**.
```sql
CREATE USER 'gamasenseit'@'localhost' IDENTIFIED BY 'gamasenseit';
CREATE OR REPLACE DATABASE gamasenseit;
GRANT ALL PRIVILEGES ON gamasenseit.* TO 'gamasenseit'@'localhost';
FLUSH PRIVILEGES;
\q
```

Then try to connect to the database as gamasenseit.
```sh
mysql -u gamasenseit -pgamasenseit -h localhost -P 3306 -D gamasenseit [--skip-ssl]
```

Set `spring.jpa.hibernate.ddl-auto=create` in `application.properties` for create tables.
```
server.ssl.key-store=classpath:gamasenseit.p12
server.ssl.key-store-password=password
server.ssl.key-store-type=pkcs12
server.ssl.key-alias=springboot
server.port=8443
```
This will create tables following the model below.

![Model representing the schema](https://github.com/CoFab-in-Bondy/gamaSenseIt/blob/master/docs/images/model.svg?raw=true)
