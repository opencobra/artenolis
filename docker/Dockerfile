FROM ubuntu:18.04

RUN apt update

# Essentials
RUN apt-get install -y git wget vim

# Java VM
RUN apt-get install -y openjdk-8-jre-headless

# Download LTS version of Jenkins
RUN wget -O /root/jenkins.war https://get.jenkins.io/war-stable/2.249.1/jenkins.war

# Restore jenkins backup taken with backup script located at https://github.com/sue445/jenkins-backup-script
COPY jenkins-backup.tar.gz /root/
RUN  cd /root; tar xvzf jenkins-backup.tar.gz; mv jenkins-backup .jenkins; rm jenkins-backup.tar.gz


CMD java -jar /root/jenkins.war

