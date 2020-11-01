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

## Building Artenolis documentation using Sphinx

Require:
  * https://github.com/martamatos/sphinx_cobra_theme.git (build with python3 setup.py install)
  * pip3 install sphinx_rtd_theme
  * pip3 install sphinxcontrib-fulltoc
  
```
cd docs
Make html
```

Output in build/html. Checkout gh-pages branch and copy to ./latest or ./stable documenation tree.

Remark: current output template differs from HTML currently in the repository. TODO: see if there is 
more recent theme/template.

# Artenolis inventory of accounts

 * cobratoolboxbot at github
 
 * cobratoolboxbot at gmail (linked to github ac) TODO: get telephone number linked to account
 
# Building Artenolis from scratch
 
Using a clean minimal Ubuntu 20.04 distribution as base. Recommended minimum for Jenkins is 4GB RAM, 24GB disk space, however in the Artenolis configuration, due to the need to run resourse intensive Matlab jobs, a minimum of 16GB and 500GB disk space is recommended. 

During Ubuntu install select minimal install, no third part apps. Set primary user 'jenkins', host 'artenolis'. Select a strong password and keep record of it.
 
```
apt update
apt install openjdk-11-jre-headless
```

Download the LTS version of Jenkins from https://www.jenkins.io/  (this howto has been written for version 2.249.2 LTS).

To run Jenkins using the built in Jetty web server:

```
java -jar jenkins.war
```

It takes about a minute to start. At the end of the logs you will see the randmin admin password. Make record of this. The admin password can be retrieve later from file /home/jenkins/.jenkins/secrets/initialAdminPassword

Open browser at http://localhost:8080   and enter the admin password when prompted.  Choose "Install Suggested Plugins". Allow a few minutes for the plugins to install.  For first admin user chose 'jenkins' and use your own email address. For instance configuration leave at default http://localhost:8080

Go to Jenkins -> Manage Jenkins -> Manage Plugins -> Avaiable tab. Search for  "Blue Ocean".   Install plugin "Blue Ocean" (this is an aggretate package which loads many sub-plugins).

A new  "Open Blue Ocean" menu option will be on the left column menu. Click on this. Then "New Pipeline" -> GitHub -> (create access token via link provided), paste access token. Select cobratoolbox project. Create pipeline.

Jenkins defaults to using port 8080. However best practise is to make this available on the default HTTPS port (443). There are several ways of achiving this. The apporach taken here use the nginx web server as a reverse-proxy to the Jenkins server.

```
sudo add-apt-repository ppa:certbot/certbot
sudo apt install -y nginx python3-certbot-nginx
```

Replace file /etc/nginx/sites-available/default  with:

```
upstream jenkins {
  server 127.0.0.1:8080 fail_timeout=0;
}

server {
        listen 80 default_server;
        listen [::]:80 default_server;

        root /var/www/html;
        index index.html index.htm index.nginx-debian.html;

        server_name artenolis.example.com;

        location / {
                proxy_set_header        Host $host:$server_port;
                proxy_set_header        X-Real-IP $remote_addr;
                proxy_set_header        X-Forwarded-For $proxy_add_x_forwarded_for;
                proxy_set_header        X-Forwarded-Proto $scheme; 
                proxy_set_header        Upgrade $http_upgrade;
                proxy_set_header        Connection "upgrade";
                proxy_pass              http://jenkins;
        }
}

```

Replace 'artenolis.example.com' with the proper full host name of the Artenolis server, eg king.nuigalway.ie. Check config file and restart nginx:

```
sudo nginx -t
sudo systemctl restart nginx
```

Now run certbot to create a HTTPS certificate (again replace artenolis.example.com' with the correct full host name).

```
sudo certbot --nginx -d artenolis.example.com
```

Certbot will ask a few questions (eg agreeing to terms and conditions etc). For the final question, choose redirect all traffic to HTTPS (option 2).

If all goes well Jenkins will be accessable from https://artenolis.example.com  (again replace the host name)


## References

  * "Set up Jenkins on Ubuntu 18.04 with LetsEncrypt (HTTPS) using Nginx" https://medium.com/@kerren_12230/set-up-jenkins-on-ubuntu-18-04-with-letsencrypt-https-using-nginx-7046baa276d9



 
