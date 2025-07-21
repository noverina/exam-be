# 1. Build stage
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

COPY pom.xml mvnw ./
COPY .mvn/ .mvn
COPY src/ src/
RUN ./mvnw clean package -DskipTests

# 2. Runtime stage
FROM eclipse-temurin:21-jdk-slim
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

# 3. Declare build-time args (to read Railway vars during build if needed)
ARG spring.application.name
ARG spring.mvc.dispatch-options-request
ARG secure.cookie
ARG frontend-address
ARG springdoc.swagger-ui.authorization-bearer-format
ARG springdoc.swagger-ui.request-redirect-enabled
ARG springdoc.swagger-ui.persist-authorize
ARG spring.datasource.url
ARG spring.datasource.driver-class-name
ARG spring.datasource.hikari.connection-test-query
ARG spring.jpa.properties.hibernate.dialect
ARG jwt.secret
ARG jwt.access.expiration
ARG jwt.access.expiration

# 4. Pass them into runtime
ENV spring.application.name=$spring.application.name \
    spring.mvc.dispatch-options-request=$spring.mvc.dispatch-options-request \
    secure.cookie=$secure.cookie \
    frontend-address=$frontend-address \
    springdoc.swagger-ui.authorization-bearer-format \
    springdoc.swagger-ui.request-redirect-enabled=$springdoc.swagger-ui.request-redirect-enabled \
    springdoc.swagger-ui.persist-authorize=$springdoc.swagger-ui.persist-authorize \
    spring.datasource.url=$spring.datasource.url \
    spring.datasource.driver-class-name=$spring.datasource.driver-class-name \
    spring.datasource.hikari.connection-test-query=$spring.datasource.hikari.connection-test-query \
    spring.jpa.properties.hibernate.dialect=$spring.jpa.properties.hibernate.dialect \
    jwt.secret=$jwt.secret \
    jwt.access.expiration=$jwt.access.expiration \
    jwt.refresh.expiration=$jwt.refresh.expiration \

# 5. Specify port
ENV PORT=8080
EXPOSE 8080

# 6. Launch using shell form (so $PORT expands)
ENTRYPOINT ["sh", "-c", "java -Dserver.port=$PORT -jar app.jar"]
