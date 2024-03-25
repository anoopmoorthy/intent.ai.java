FROM openjdk:11
#FROM openjdk:8-jdk-alpine
#FROM nginx:alpine

#RUN apk add --no-cache libssl1.1
#RUN apt-get update && apt-get install libssl1.0.0 libssl-dev
#RUN apt-get update && apt-get -y upgrade openssl


RUN echo  find / -name "libssl.so.3"
RUN echo  openssl version

RUN apt-get update && \
    apt-get -y upgrade openssl && \
    apt-get -y install libssl-dev

RUN echo  openssl version
RUN echo  find / -name "libssl.so.3"

ARG JAR_FILE=target/service-0.0.1-SNAPSHOT.jar
ENV SPRING_REDIS_HOST=127.0.0.1
ENV SPRING_REDIS_PORT=6379

COPY ${JAR_FILE} service.jar

EXPOSE 1818

ENTRYPOINT ["java","-jar","/service.jar"]