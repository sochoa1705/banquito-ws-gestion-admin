FROM eclipse-temurin:17-jdk-focal

COPY target/banquito-ws-gestion-admin-0.0.1-SNAPSHOT.jar banquito-ws-gestion-admin-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/banquito-ws-gestion-admin-0.0.1-SNAPSHOT.jar"]