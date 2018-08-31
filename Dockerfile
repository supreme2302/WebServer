FROM ubuntu:18.04

MAINTAINER Chuvashov Egor

RUN apt-get -y update

RUN apt-get install -y openjdk-11-jdk-headless

RUN apt-get install -y maven


ENV WORK /opt
ADD . $WORK/java/
RUN mkdir -p /var/www/html

WORKDIR $WORK/java
RUN mvn package

EXPOSE 80

CMD java -jar target/WebServer-1.0-SNAPSHOT.jar