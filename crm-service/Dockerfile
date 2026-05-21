FROM eclipse-temurin:17-jre-alpine

# Création utilisateur non-root
RUN addgroup -S spring && adduser -S spring -G spring

WORKDIR /app

COPY target/*.jar app.jar

# Attribution permissions
RUN chown -R spring:spring /app

# Utilisateur sécurisé
USER spring

EXPOSE 8080

# Vérification santé container
HEALTHCHECK --interval=30s --timeout=10s --start-period=20s --retries=3 \
  CMD wget --spider -q http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]