FROM khipu/openjdk17-alpine
ARG APP_NAME="node"
ARG APP_VERSION="0.0.1"
ARG JAR_FILE="/build/libs/${APP_NAME}-${APP_VERSION}.jar"

COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]