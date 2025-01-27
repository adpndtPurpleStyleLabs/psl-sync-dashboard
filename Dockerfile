FROM amazoncorretto:17-alpine-jdk
MAINTAINER baeldung.com
COPY target/psl-sync-dashboard.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]