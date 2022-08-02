FROM maven:3.8.3-openjdk-17 as build

WORKDIR "/code"

COPY "./server" "."
COPY "./arduino" "."
COPY "./app" "."
COPY "./script" "."
COPY "./pom.xml" "."
COPY "./.mvnw" "."
RUN ./script/arduino.sh

RUN mvn package -T 4C

FROM openjdk:17-oraclelinux8 as prod

USER root
RUN groupadd spring; exit 0
RUN useradd spring -g spring; exit 0
USER spring:spring


WORKDIR "/app"
COPY --from=build "/code/server/target/server.jar" "/app/gamasenseit-server.jar"
EXPOSE 8080
EXPOSE 8443

ENTRYPOINT ["java", "-jar", "gamasenseit-server.jar"]