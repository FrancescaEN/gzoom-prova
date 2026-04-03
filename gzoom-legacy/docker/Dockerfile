#     ______________________
#    / Build Image Stage \
#  _/________________________\_______________________________________
# /   
#FROM amazoncorretto:11-al2-jdk
FROM debian:12-slim

RUN apt-get update
RUN apt-get install java-common
RUN apt-get install -y wget
RUN wget https://corretto.aws/downloads/latest/amazon-corretto-11-x64-linux-jdk.deb 
RUN dpkg --install amazon-corretto-11-x64-linux-jdk.deb
RUN echo java -version
RUN apt-get install -y fontconfig
RUN apt-get install -y ttf-dejavu-*
RUN apt-get install -y fonts-dejavu

RUN groupadd -r gzoom && useradd -r -g gzoom gzoom
RUN mkdir -p /home/gzoom/legacy && chown -R gzoom:gzoom /home/gzoom/legacy
RUN mkdir -p /etc/gzoom && chown -R gzoom:gzoom /etc/gzoom

RUN wget https://archive.apache.org/dist/ant/binaries/apache-ant-1.10.8-bin.tar.gz
RUN tar -xzf apache-ant-1.10.8-bin.tar.gz -C /opt
RUN mkdir /home/gzoom/legacy/applications
RUN mkdir /home/gzoom/legacy/framework
RUN mkdir /home/gzoom/legacy/hot-deploy
RUN mkdir -p /home/gzoom/legacy/runtime/uploads
RUN mkdir /home/gzoom/legacy/specialpurpose
RUN mkdir /home/gzoom/legacy/themes
RUN mkdir /home/gzoom/legacy/tools
RUN mkdir /home/gzoom/legacy/certs
RUN mkdir -p /home/gzoom/legacy/lib/build/javacc
RUN mkdir /tmp/gzoom/
RUN mkdir /tmp/gzoom/themes
RUN mkdir /tmp/gzoom/resources
RUN mkdir /tmp/gzoom/images

RUN mkdir -p /home/gzoom/legacy/hot-deploy/custom && chown -R gzoom:gzoom /home/gzoom/legacy/hot-deploy/custom
RUN chown -R gzoom:gzoom /home/gzoom/legacy
RUN chown -R gzoom:gzoom /tmp/gzoom

ADD ./applications/ /home/gzoom/legacy/applications/
ADD ./lib/build/javacc/ /home/gzoom/legacy/lib/build/javacc/
ADD ./framework/ /home/gzoom/legacy/framework/
ADD ./hot-deploy/ /home/gzoom/legacy/hot-deploy/
ADD ./runtime/ /home/gzoom/legacy/runtime/
ADD ./specialpurpose/ /home/gzoom/legacy/specialpurpose/
ADD ./themes/ /tmp/gzoom/themes/
ADD ./tools/ /home/gzoom/legacy/tools/
ADD ./hot-deploy/base/webapp/resources/ /tmp/gzoom/resources/
ADD ./framework/images/webapp/images/ /tmp/gzoom/images

COPY ./ant /home/gzoom/legacy/
COPY ./APACHE2_HEADER /home/gzoom/legacy/
COPY ./build.xml ./common.xml ./macros.xml /home/gzoom/legacy/
COPY ./en.json ./it.json /home/gzoom/legacy/
COPY ./LICENSE ./LICENSE-GZoom ./NOTICE ./README /home/gzoom/legacy/
COPY ./docker/start-gzoom.sh /home/gzoom/legacy/
COPY ./docker/patch-gzoom.sh /home/gzoom/legacy/

RUN chmod +x /home/gzoom/legacy/start-gzoom.sh /home/gzoom/legacy/tools/apache-tomcat-9.0.37/bin/catalina.sh /home/gzoom/legacy/patch-gzoom.sh
#replace windows lfcr with lf
RUN find /home/gzoom/legacy/tools/apache-tomcat-9.0.37/bin/*.sh -exec sed -i -e 's/\r$//' {} \;
RUN sed -i -e 's/\r$//' /home/gzoom/legacy/start-gzoom.sh
RUN sed -i -e 's/\r$//' /home/gzoom/legacy/patch-gzoom.sh
RUN chown -R gzoom:gzoom /home/gzoom/legacy

EXPOSE 8080/tcp

USER gzoom
WORKDIR /home/gzoom/legacy
## Build of the JAR in the init script
RUN /opt/apache-ant-1.10.8/bin/ant build

CMD ["./start-gzoom.sh" ]
# \                                                                  /
#  ------------------------------------------------------------------