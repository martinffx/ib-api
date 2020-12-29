FROM ubuntu:20.04
MAINTAINER Martin C. Richards <martinffx@gmail.com>

ENV DEBIAN_FRONTEND noninteractive
ENV DISPLAY :0
ENV USER "docker"
ENV VNC_PASSWORD "password"
ENV PROFILE ""
ENV TWS_VERSION "latest"
ENV IBC_VERSION 3.8.2

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
RUN wget -q https://download2.interactivebrokers.com/installers/tws/$TWS_VERSION-standalone/tws-$TWS_VERSION-standalone-linux-x64.sh
RUN chmod +x tws-$TWS_VERSION-standalone-linux-x64.sh && ./tws-$TWS_VERSION-standalone-linux-x64.sh -q && rm -f tws-$TWS_VERSION-standalone-linux-x64.sh

# IBC
USER root
RUN wget -q https://github.com/IbcAlpha/IBC/releases/download/$IBC_VERSION/IBCLinux-$IBC_VERSION.zip && \
    unzip  IBCLinux-$IBC_VERSION.zip -d /opt/ibc && \
    rm IBCLinux-$IBC_VERSION.zip
RUN sed -i "s|TWS_MAJOR_VRSN=972|TWS_MAJOR_VRSN=979|g" /opt/ibc/twsstart.sh && \
    sed -i "s|TWS_MAJOR_VRSN=972|TWS_MAJOR_VRSN=979|g" /opt/ibc/gatewaystart.sh
RUN cat /opt/ibc/twsstart.sh
RUN cat /opt/ibc/gatewaystart.sh

# Copy profile settings
COPY ./Jts/jts.ini /home/$USER/Jts/
RUN mkdir -p /home/$USER/Jts/$PROFILE
RUN mkdir -p /home/$USER/ibc
COPY ./Jts/tws.xml /home/$USER/Jts/$PROFILE/
COPY start.sh /home/$USER/
COPY ./ibc/config.ini.tpl /home/$USER/ibc
RUN chown $USER:$USER /home/$USER/Jts/jts.ini
RUN chown -R $USER:$USER /home/$USER/Jts/$PROFILE
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
