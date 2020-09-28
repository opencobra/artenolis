# Artenolis docker

Project to encapsulate Artinolis system in a docker image to facilitate easy replication or
server redeployment.


## Building and executing Artenolis Jenkins installation

The build process requires a backup of the Jenkins CI installation. This can be 
done with the script at https://github.com/sue445/jenkins-backup-script 
which backups all the essential files from the .jenkins home directory.
Copy this backup file to jenkins-backup.tar.gz in the build directory.

Prior to building copy the following files into the build directory:

 * jenkins-backup.tar.gz (Artenolis Jenkins configuration backup)
 * ibm.tar.gz (ILOG CPLEX library)

To build:

```
sudo docker build -t opencobra/artenolis .
```

To execute: 

```
sudo docker run -p 8080:8080 --rm -it opencobra/artenolis
```

The -p flag maps the Jenkins server runing in port 8080 in the docker container to 
port 8080 on the host (-p host-port:docker-port). If port 8080 is already in use
substitute the host-port for another port number.


## TODO

 * The Jenkins backup contains some private login credentials, so need to find a 
good way to separate out the public and private parts of the Jenkins 
configuration.

 * A bulk of the current Artenolis Jenkins configuration backup (about 100MB) is 
made of up the  plugin executable code. Perhaps this can be downloaded as part of 
the docker build process (?). The rest of the configuration compresses to to a 20kB 
file.

 * How to run matlab inside docker? https://github.com/mathworks-ref-arch/matlab-dockerfile


## Automating backup of Jenkins configuration

```
git clone https://github.com/sue445/jenkins-backup-script.git
```

Backup script

```
#!/bin/bash
# Backup Artenolis Jenkins configuration
# Uses backup script at https://github.com/sue445/jenkins-backup-script
JENKINS_BACKUP=/home/vmhadmin/jenkins-backup-script
JENKINS_HOME=/home/vmhadmin/.jenkins
TS=`date +%Y%m%d-%H%M`

${JENKINS_BACKUP}/jenkins-backup.sh ${JENKINS_HOME} ${JENKINS_BACKUP}/jenkins-backup-${TS}.tar.gz >& ${JENKINS_BACKUP}/backup.log
```

Add to crontab:

```
@daily /home/vmhadmin/scripts/backup-jenkins-config.sh 
```

Remark: the bulk of the configuration backup are the optional Jenkins plugins. Without these the 
configuration is about 20kB. 
