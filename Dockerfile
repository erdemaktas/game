# Start with a base image containing Java runtime
FROM maven:3.8-eclipse-temurin-17-focal AS build
COPY . /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean install  -Dmaven.test.skip

FROM eclipse-temurin:17.0.6_10-jre-focal
COPY --from=build /usr/src/app/target/*.jar /usr/src/app/backend.jar
#COPY --from=build /usr/src/app/config /usr/src/app/config/
COPY --from=build /usr/src/app/src/main/resources/application.properties /usr/src/app/config/application.properties

# Run the jar file
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/usr/src/app/backend.jar"]