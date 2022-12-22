FROM node:16
RUN mkdir /app
COPY . /src
WORKDIR /src
EXPOSE 5000
# ENTRYPOINT ["java","-jar","*.jar"]
