FROM node:16
#FROM 829324643700.dkr.ecr.ap-south-1.amazonaws.com/spa-node:12.13.1-slim
 
# WORKDIR specifies the application directory
WORKDIR /src
 
# Copying package.json file to the app directory
#COPY package.json /src
 
#RUN apt-get update || : && apt-get install build-essential python -y
# RUN apt-get update

# Copying rest of the application to app directory
COPY . /src

# Installing npm for DOCKER
RUN npm install
RUN npm install pm2 -g

# RUN the build.
RUN npm run build

ARG ENVIRONMENT
ENV environment=$TYPE
RUN echo "Type: $type"
 
# Starting the application using npm start
CMD ["sh", "-c", "pm2-runtime --env $type /src/ecosystem.config.js"]

# pm2-logrotate setup
RUN pm2 install pm2-logrotate@2.6.0
#RUN pm2 set pm2-logrotate:compress true
RUN pm2 set pm2-logrotate:retain 2

# Expose port 
EXPOSE 3000
