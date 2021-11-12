# Generate Self-signed Certificate
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

# See content of file
```
keytool -list -v -keystore gamasenseit.p12
```

# Install certificate on localhost
```
server.ssl.key-store=classpath:gamasenseit.p12
server.ssl.key-store-password=password
server.ssl.key-store-type=pkcs12
server.ssl.key-alias=springboot
server.port=8443
```
