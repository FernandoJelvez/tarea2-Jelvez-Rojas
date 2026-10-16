FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /build
COPY pom.xml .
RUN mvn -B -q dependency:go-offline
COPY src ./src
RUN mvn -B -q package -DskipTests

FROM eclipse-temurin:21-jdk-alpine AS jre
RUN jlink \
      --add-modules java.base,java.compiler,java.desktop,java.instrument,java.logging,java.management,java.naming,java.net.http,java.prefs,java.rmi,java.scripting,java.security.jgss,java.security.sasl,java.sql,java.xml,jdk.charsets,jdk.crypto.ec,jdk.management,jdk.naming.dns,jdk.unsupported,jdk.zipfs \
      --strip-debug --no-man-pages --no-header-files --compress=zip-6 \
      --output /javaruntime

FROM alpine:3.22

ARG APP_PORT=3001
ENV JAVA_HOME=/opt/java \
    PATH="/opt/java/bin:${PATH}" \
    APP_ENV=production \
    APP_PORT=${APP_PORT} \
    CSV_PATH=/data/compras.csv

RUN addgroup -S app && adduser -S -G app -H -s /sbin/nologin app \
    && mkdir -p /app /data && chown app:app /app /data

COPY --from=jre /javaruntime /opt/java
COPY --from=build --chown=app:app /build/target/*.jar /app/app.jar

WORKDIR /app
USER app
EXPOSE ${APP_PORT}

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "/app/app.jar"]