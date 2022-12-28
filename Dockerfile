FROM IMAGE
RUN mkdir /src
COPY . /src
WORKDIR /src
EXPOSE PORT
# Installing npm for DOCKER
# RUN npm install
# RUN npm install pm2 -g

# # RUN the build.
# RUN npm run build

# ENTRYPOINT ["java","-jar","*.jar"]
