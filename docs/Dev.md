# Development

you can also launch separately the back and the front end:
 
 - Run the backend without the embed front with allowed cors
```sh
./mvnw spring-boot:run -P=-front -Dspring-boot.run.arguments=--gamaSenseIt.cors-url=http://localhost:4200
```

- Run the development server for angular 
```sh
cd ui
npm install
ng serve --port 4200
```

If you want rebuild the maven wrapper use the command bellow.
```
mvn -N io.takari:maven:wrapper -Dmaven=3.8.4
```
