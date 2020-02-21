FROM sconecuratedimages/apps:8-jdk-alpine
RUN apk --upgrade add maven
ADD . /
RUN mvn package