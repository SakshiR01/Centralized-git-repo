FROM node:16
RUN mkdir /src
COPY . /src
WORKDIR /src
EXPOSE 5000
# ENTRYPOINT ["java","-jar","*.jar"]
