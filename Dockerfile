FROM sconecuratedimages/apps:8-jdk-alpine
RUN apk --upgrade add maven
ADD . /
RUN mvn package
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar","arg1","arg2"]

