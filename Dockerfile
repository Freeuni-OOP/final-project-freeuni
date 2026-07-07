# ---- build stage: compile the .war with Maven (JDK 21) ----
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build
# copy pom first so dependencies are cached across rebuilds
COPY pom.xml .
RUN mvn -q dependency:go-offline
COPY src ./src
RUN mvn -q clean package

# ---- run stage: Tomcat serves the .war ----
FROM tomcat:10.1-jdk21-temurin
# clear Tomcat's default sample apps
RUN rm -rf /usr/local/tomcat/webapps/*
# deploy as ROOT so the app is served at http://localhost:8080/
COPY --from=build /build/target/oophotel.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]
