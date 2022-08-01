# Development
If you are a developer, you can use the indications below to have a maximum of information when you develop.

## Setup

First clone the repository

```sh
git clone https://github.com/CoFab-in-Bondy/gamaSenseIt.git gamaSenseIt
cd gamaSenseIt
```

Download the javadoc from maven
```sh
./mvnw dependency:sources
```

Install npm package 
```sh
(cd ui && npm install)
```

## Run

First you can run angular with development server for instant refresh change.
```sh
(cd ui && npm start)
```

After you can run the server without embed angular for fastest build.
```sh
./mvnw spring-boot:run -T 2C -Dspring-boot.run.arguments=--gamaSenseIt.cors-url=http://localhost:4200 -P -front
```

If you want rebuild the maven wrapper use the command bellow.
```
mvn -N io.takari:maven:wrapper -Dmaven=3.8.4
```
