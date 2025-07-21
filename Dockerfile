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
ARG spring_application_name
ARG spring_mvc_dispatch_options_request
ARG secure_cookie
ARG frontend_address
ARG springdoc_swagger_ui_authorization_bearer_format
ARG springdoc_swagger_ui_request_redirect_enabled
ARG springdoc_swagger_ui_persist_authorize
ARG spring_datasource_url
ARG spring_datasource_driver_class_name
ARG spring_datasource_hikari_connection_test_query
ARG spring_jpa_properties_hibernate_dialect
ARG jwt_secret
ARG jwt_access_expiration
ARG jwt_access_expiration

# 4. Pass them into runtime
ENV spring.application.name=$spring_application_name \
    spring.mvc.dispatch-options-request=$spring_mvc_dispatch_options_request \
    secure.cookie=$secure_cookie \
    frontend-address=$frontend_address \
    springdoc.swagger-ui.authorization-bearer-format=$springdoc_swagger_ui_authorization_bearer_format \
    springdoc.swagger-ui.request-redirect-enabled=$springdoc_swagger_ui_request_redirect_enabled \
    springdoc.swagger-ui.persist-authorize=$springdoc_swagger_ui_persist_authorize \
    spring.datasource.url=$spring_datasource_url \
    spring.datasource.driver-class-name=$spring_datasource_driver_class_name \
    spring.datasource.hikari.connection-test-query=$spring_datasource_hikari_connection_test_query \
    spring.jpa.properties.hibernate.dialect=$spring_jpa_properties_hibernate_dialect \
    jwt.secret=$jwt_secret \
    jwt.access.expiration=$jwt_access_expiration \
    jwt.refresh.expiration=$jwt_refresh_expiration \

# 5. Specify port
ENV PORT=8080
EXPOSE 8080

# 6. Launch using shell form (so $PORT expands)
ENTRYPOINT ["sh", "-c", "java -Dserver.port=$PORT -jar app.jar"]
