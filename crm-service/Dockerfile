FROM eclipse-temurin:17-jre-alpine

RUN addgroup -S spring && adduser -S spring -G spring

WORKDIR /app

COPY target/*.jar app.jar

RUN chown -R spring:spring /app

USER spring

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=10s \
  CMD wget --spider -q http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]