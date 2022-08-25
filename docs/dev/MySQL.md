# Setup MySQL

## Install for Windows

Go to MariaDB [download page](https://mariadb.org/download).

Then search for MySQL Client and run it.

## Install for Ubuntu.
```sh
sudo apt install mysql-server
sudo mysql_secure_installation
sudo mysql
```

***

## Create GamasenSeIt user

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
mysql[.exe] -u gamasenseit -pgamasenseit -h localhost -P 3306 -D gamasenseit [--skip-ssl]
```

Set `spring.sql.init.mode=always` in `application.properties` for create tables.
