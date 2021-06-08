MAINTAINER Sebastian Syska (syska.seb@gmail.com)

FROM ubuntu:focal

ENV DEBIAN_FRONTEND=noninteractive

RUN apt update -y && apt install -y openjdk-11-jre-headless

COPY build/libs/aimachine-server-0.0.1-SNAPSHOT.jar /

ENTRYPOINT ["java","-jar","aimachine-server-0.0.1-SNAPSHOT.jar"]