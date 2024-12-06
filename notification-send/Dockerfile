FROM amazoncorretto:21.0.3 as builder
WORKDIR application
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM  amazoncorretto:21.0.4
LABEL com.ezbuy.ecom.product.version="1.0.0"
LABEL version="1.0.0"
LABEL description="notification send service"
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
