# FROM maven:3.8.5-openjdk-17 # for Java 17
FROM maven:3.8.3-openjdk-17

WORKDIR /ndfraisback
COPY . .
RUN mvn clean install -DskipTests

# Copier les ressources nécessaires dans un emplacement approprié
RUN mkdir -p /app/resources
COPY src/main/resources/images/logo.png /ndfrais/images/resources/images/logo.png
COPY src/main/resources/email-support-to-admin.html /ndfrais/images/resources/email-support-to-admin.html
COPY src/main/resources/email-inscription.html /ndfrais/images/resources/email-inscription.html
COPY src/main/resources/email-support-to-user.html /ndfrais/images/resources/email-support-to-user.html
COPY src/main/resources/email-empty.html /ndfrais/images/resources/email-empty.html


LABEL name="ndfraisback"

CMD mvn spring-boot:run

