FROM amazoncorretto:21 AS base
WORKDIR /app

RUN yum install -y tar gzip && yum clean all

COPY .mvn/ .mvn
COPY mvnw pom.xml ./

RUN ./mvnw dependency:go-offline

FROM base AS test
COPY src ./src
RUN ./mvnw test

FROM test AS package
COPY src ./src
RUN ./mvnw clean package -DskipTests

FROM amazoncorretto:21-alpine
WORKDIR /app

COPY --from=package /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
