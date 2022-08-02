#!/bin/bash

set -e
./mvnw clean package -T 4C -pl arduino -pl app
mkdir --parents ./server/src/main/resources/static/download/
cp ./app/target/app-jar-with-dependencies.jar ./server/src/main/resources/static/download/gamasenseit-app.jar
