keytool \
  -genkeypair \
  -alias gamasenseit \
  -keyalg RSA \
  -keysize 4096 \
  -storetype PKCS12 \
  -keystore gamasenseit.p12 \
  -validity 3650
mv gamasenseit.p12 server/src/main/resources/gamasenseit.p12