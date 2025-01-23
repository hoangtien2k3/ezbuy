FROM amazoncorretto:21.0.6 as builder
WORKDIR application
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract
FROM  openjdk:11.0.12-oracle
LABEL com.ezbuy.ecom.auth.version="1.0.0"
LABEL version="1.0.0"
LABEL description="order service"
LABEL author="Ezbuy Platform"
WORKDIR application

# additional timezone +7
ENV TZ=Asia/Ho_Chi_Minh

COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./
EXPOSE 8080
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]


