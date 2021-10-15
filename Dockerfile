FROM ubuntu:focal as stage-backend

MAINTAINER Sebastian Syska (syska.seb@gmail.com)

ENV DEBIAN_FRONTEND=noninteractive

RUN apt update -y && apt install -y openjdk-11-jre-headless

COPY ./ /

RUN sh gradlew bootJar

FROM ubuntu:focal

MAINTAINER Sebastian Syska (syska.seb@gmail.com)

ENV DEBIAN_FRONTEND=noninteractive

RUN apt update -y && apt install -y openjdk-11-jre-headless

COPY --from=stage-backend /build/libs/aimachine-server-0.0.1-SNAPSHOT.jar /

CMD ["java","-XX:+UseSerialGC","-Xss512k","-XX:MaxRAM=72m","-jar","aimachine-server-0.0.1-SNAPSHOT.jar"]
