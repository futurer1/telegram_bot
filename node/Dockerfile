FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app.jar"]






#FROM openjdk:17.0.2-jdk-slim-buster
#khipu/openjdk17-alpine

#ARG APP_NAME="node-service"
#ARG APP_VERSION="0.0.1"
#ARG JAR_FILE="/build/libs/${APP_NAME}-${APP_VERSION}.jar"

#COPY ${JAR_FILE} app.jar
#COPY *.jar app.jar
#ENTRYPOINT ["java","-jar", "/app.jar"]