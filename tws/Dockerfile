FROM ubuntu:20.04
MAINTAINER Martin C. Richards <martinffx@gmail.com>

ENV DEBIAN_FRONTEND noninteractive
ENV USER "docker"

ARG VNC_PASSWORD "password"
ARG TWS_VERSION "adsasdas"
ARG IBC_VERSION 3.8.2

USER root

# Create docker user
RUN useradd -ms /bin/bash docker
WORKDIR /home/docker

# Update
RUN apt update && apt upgrade -qy
RUN apt install wget unzip ca-certificates tightvncserver fluxbox xterm xautomation openjdk-11-jdk openjfx -qy
RUN apt clean
RUN rm -rf /var/lib/apt/lists/*

# Trader Workstation
USER docker
RUN wget -q https://download2.interactivebrokers.com/installers/tws/stable-standalone/tws-stable-standalone-linux-x64.sh
RUN chmod +x tws-stable-standalone-linux-x64.sh && ./tws-stable-standalone-linux-x64.sh -q && rm -f tws-stable-standalone-linux-x64.sh
RUN rm -rf tws-stable-standalone-linux-x64.sh

# IBC
USER root
RUN wget -q https://github.com/IbcAlpha/IBC/releases/download/$IBC_VERSION/IBCLinux-$IBC_VERSION.zip && \
    unzip  IBCLinux-$IBC_VERSION.zip -d /opt/ibc && \
    rm IBCLinux-$IBC_VERSION.zip
RUN sed -i "s|TWS_MAJOR_VRSN=972|TWS_MAJOR_VRSN=$TWS_VERSION|g" /opt/ibc/twsstart.sh && \
    sed -i "s|TWS_MAJOR_VRSN=972|TWS_MAJOR_VRSN=$TWS_VERSION|g" /opt/ibc/gatewaystart.sh


# Must be set after install of IBGateway
ENV DISPLAY :0

# Copy profile settings
COPY ./Jts/jts.ini /home/$USER/Jts/
RUN mkdir -p /home/$USER/Jts/ibgateway/$TWS_VERSION
COPY ./Jts/ibgateway.vmoptions /home/$USER/Jts/ibgateway/$TWS_VERSION/ibgateway.vmoptions

COPY start.sh /home/$USER/

RUN mkdir -p /home/$USER/ibc
COPY ./ibc/config.ini.tpl /home/$USER/ibc

RUN chown $USER:$USER /home/$USER/Jts/jts.ini
RUN chown -R $USER:$USER /home/$USER/ibc
RUN chmod o+x /opt/ibc/*.sh /opt/ibc/*/*.sh
RUN chmod o+x ./start.sh

# VNC
USER docker
RUN mkdir -p /home/$USER/.vnc
RUN set -xe && (echo $VNC_PASSWORD | vncpasswd -f > /home/docker/.vnc/passwd)
RUN chmod 0600 /home/$USER/.vnc/passwd

EXPOSE 7496
EXPOSE 7497
EXPOSE 5900

CMD tightvncserver $DISPLAY -geometry 1920x1080; ./start.sh
