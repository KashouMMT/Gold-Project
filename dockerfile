# syntax=docker/dockerfile:1.6

############################
# 1) Build stage (Maven + JDK)
############################
FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /workspace

# Download deps first (better layer caching)
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 mvn -q -DskipTests dependency:go-offline

# Build
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn -q -DskipTests package

############################
# 2) Runtime stage (JRE only)
############################
FROM eclipse-temurin:21-jre-alpine AS runner
ARG APP_HOME=/app
WORKDIR ${APP_HOME}

# Non-root user
RUN addgroup -S app && adduser -S app -G app

# Copy the fat jar (use a wildcard so it works with any version)
COPY --from=builder /workspace/target/*.jar app.jar

# Optional logs dir
RUN mkdir -p logs && chown -R app:app ${APP_HOME}
USER app

# App port (Spring reads server.port from env PORT via your properties)
EXPOSE 8080
ENV PORT=8080
ENV SPRING_PROFILES_ACTIVE=prod
# Add any JVM options you want here (memory, GC, etc.)
ENV JAVA_OPTS=""

# Simple healthcheck (change to /actuator/health if you use Spring Actuator)
HEALTHCHECK --interval=30s --timeout=3s --start-period=20s --retries=3 \
  CMD wget -qO- http://127.0.0.1:${PORT}/ || exit 1

ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]
