FROM maven:3.8.3-openjdk-17 as build

WORKDIR "/code"

COPY "./server" "./server"
COPY "./arduino" "./arduino"
COPY "./app" "./app"
COPY "./pom.xml" "./pom.xml"
RUN mvn clean package -T 4C -pl arduino -pl app
RUN mkdir --parents ./server/src/main/resources/static/download/
RUN cp ./app/target/app-1.3.0.jar ./server/src/main/resources/static/download/gamasenseit-app.jar
RUN mvn package -T 4C

FROM openjdk:17-oraclelinux8 as prod

USER root
RUN id spring &>/dev/null || groupadd spring && useradd spring -g spring
USER spring:spring


WORKDIR "/app"
COPY --from=build "/code/server/target/server-1.3.0.jar" "/app/gamasenseit-server.jar"
EXPOSE 8080
EXPOSE 8443

ENTRYPOINT ["java", "-jar", "gamasenseit-server.jar"]