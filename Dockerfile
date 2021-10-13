FROM ubuntu:focal

MAINTAINER Sebastian Syska (syska.seb@gmail.com)

ENV DEBIAN_FRONTEND=noninteractive

RUN apt update -y && apt install -y openjdk-11-jre-headless

COPY build/libs/aimachine-server-0.0.1-SNAPSHOT.jar /

CMD ["java","-jar","aimachine-server-0.0.1-SNAPSHOT.jar"]