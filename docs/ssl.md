# Web
## Generate Self-signed Certificate
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
- ext san=dns:localhost : friendly with localhost

## See content of file
```
keytool -list -v -keystore gamasenseit.p12
```

## Install certificate on localhost
```
server.ssl.key-store=classpath:gamasenseit.p12
server.ssl.key-store-password=password
server.ssl.key-store-type=pkcs12
server.ssl.key-alias=springboot
server.port=8443
```

# Mysql
```sh
sudo -i
mkdir -p /etc/mysql/newcerts  
chown -R mysql:mysql /etc/mysql/newcerts
cd /etc/mysql/newcerts  

openssl genrsa 4096 > ca-key.pem
openssl req -new -x509 -nodes -days 1000 -key ca-key.pem > ca-cert.pem

openssl req -newkey rsa:4096 -days 1000 -nodes -keyout server-key.pem > server-req.pem
openssl x509 -req -in server-req.pem -days 1000 -CA ca-cert.pem -CAkey ca-key.pem -set_serial 01 > server-cert.pem

openssl req -newkey rsa:4096 -days 1000 -nodes -keyout client-key.pem > client-req.pem 
openssl x509 -req -in client-req.pem -days 1000 -CA ca-cert.pem -CAkey ca-key.pem -set_serial 01 > client-cert.pem

openssl rsa -in client-key.pem -out client-key2.pem
# for reading by mysql workbench
chmod 777 client-key2.pem

chown -R mysql:mysql /etc/mysql/newcerts

sudo mysql_ssl_rsa_setup --uid=mysql
```
`vi /etc/my.cnf`
```
[mysqld]
ssl
ssl-capath=/etc/mysql/newcerts
ssl-ca=/etc/mysql/newcerts/ca-cert.pem
ssl-cert=/etc/mysql/newcerts/server-cert.pem
ssl-key=/etc/mysql/newcerts/server-key.pem
require_secure_transport=ON

[client]
ssl-ca=/etc/mysql/newcerts/ca-cert.pem
ssl-cert=/etc/mysql/newcerts/client-cert.pem
ssl-key=/etc/mysql/newcerts/client-key2.pem
```

`service mysqld restart / systemctl restart mysql`

```sql
SHOW STATUS LIKE 'Ssl_cipher';  
SHOW VARIABLES LIKE '%%ssl%%';
```

```shell
-Djavax.net.ssl.keyStore=/path/to/keystore/keystore.jks
-Djavax.net.ssl.keyStorePassword=password
-Djavax.net.ssl.trustStore=/path/to/keystore/truststore.jks
-Djavax.net.ssl.trustStorePassword=password
```