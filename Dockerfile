# syntax=docker/dockerfile:1
FROM --platform=$BUILDPLATFORM eclipse-temurin:21-jdk AS build
WORKDIR /build
COPY api/.mvn .mvn
COPY api/mvnw api/pom.xml ./
RUN chmod +x mvnw
COPY api/src src
RUN ./mvnw -B -ntp -DskipTests package && cp target/api-*.jar /build/app.jar

FROM eclipse-temurin:21-jre-jammy AS runtime
RUN apt-get update && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/* \
    && groupadd --system app && useradd --system --gid app app
WORKDIR /app
COPY --from=build --chown=app:app /build/app.jar app.jar
USER app
EXPOSE 8080
# Existing public endpoint; checks HTTP readiness without storing credentials.
HEALTHCHECK --interval=10s --timeout=5s --start-period=60s --retries=12 \
    CMD curl --fail --silent --output /dev/null --max-time 4 http://127.0.0.1:8080/dentalflow-api/v1/v3/api-docs || exit 1
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
