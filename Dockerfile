FROM ubuntu:18.04

MAINTAINER Chuvashov Egor

RUN apt-get -y update

RUN apt install -y openjdk-11-jre-headless

RUN apt install -y openjdk-11-jdk

RUN apt-get install -y maven


ADD . .
COPY ./httptest/ /var/www/html/
COPY ./httpd.conf /

EXPOSE 80

CMD java -jar target/WebServer-1.0-SNAPSHOT.jar
