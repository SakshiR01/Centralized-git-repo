FROM IMAGE
MAINTAINER MAINTAINER
COPY target/*.?ar /home/
WORKDIR WORKDIR_CMD
EXPOSE PORT
RUN export executor=`ls /home/SERVICE-SNAPSHOT.?ar`
ENTRYPOINT ["java","-jar","$executor"]
