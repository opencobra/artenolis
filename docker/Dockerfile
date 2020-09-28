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

# Install julia v1.2.0
RUN mkdir /opt; cd /opt; wget -q -O - https://julialang-s3.julialang.org/bin/linux/x64/1.2/julia-1.2.0-linux-x86_64.tar.gz | tar -xzv

# Symbolic link to old julia location so that existing test configs continue to work
RUN mkdir -p /home/vmhadmin; ln -s /opt/julia-1.2.0 /home/vmhadmin/julia-1.2.0

# IBM CPLEX
RUN mkdir /opt/ibm
ADD ibm.tar.gz /opt/
ENV PATH "$PATH:/opt/ibm/ILOG/CPLEX_Studio129/cplex/bin/x86-64_linux"

# Default run command
CMD java -jar /root/jenkins.war

