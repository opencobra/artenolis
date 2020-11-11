.. _building_server:

Building Artenolis Server
=========================

(work in progress 2020-11-10)

The following 'howto' explains how to setup a Artenolis server starting with a clean Ubuntu 20.04 distribution.

If running from a Virtual Machine, set RAM 4GB or higher, disk space 24GB or higher.

When installing Ubuntu 20.04, choose a minimal install with no 3rd party software.
Set the primary user to 'jenkins' with a strong password
and make note of the password. Set the hostname 'artenolis' (or your preferred host name).


.. code-block:: bash

        sudo apt update
        sudo apt install -y git openjdk-11-jre-headless

Download the LTS version of Jenkins from https://www.jenkins.io. Choose the Generic Java Package (war) download. This howto has been written for version 2.249.2 LTS which can be downloaded at
https://get.jenkins.io/war-stable/2.249.3/jenkins.war .  

.. code-block:: bash

	wget https://get.jenkins.io/war-stable/2.249.3/jenkins.war

To run Jenkins using the built in Jetty web server:

.. code-block:: bash

        java -jar jenkins.war

It takes about a minute to start. Logs are echoed to standard output. When Jenkins is finished starting, at the end of the logs you will see the random admin password. Make record of this. The admin password can be retrieve later from file /home/jenkins/.jenkins/secrets/initialAdminPassword. Example:

.. code-block:: bash

	Jenkins initial setup is required. An admin user has been created and a password generated.
	Please use the following password to proceed to installation:

	31032562bf8c4f96a0577790f79c17cc

	This may also be found at: /home/jenkins/.jenkins/secrets/initialAdminPassword


Initial setup of Jenkins
------------------------


Open browser at http://localhost:8080 and enter the admin password when prompted. Choose "Install Suggested Plugins". Allow a few minutes for the plugins to install. 

For first admin user chose username 'jenkins', full name 'jenkins' and use your own email address. 

For the 'Instance Configuration' leave at default http://localhost:8080 for the moment. Click 'Save and Finish'. You are now ready to setup Jenkins for the Artenolis configuration.


Setting up Jenkins for Artenolis
--------------------------------

Artenolis requires the 'Blue Ocean' plugin.  Go to Jenkins -> Manage Jenkins -> Manage Plugins -> Available tab. Search for "Blue Ocean". Install plugin "Blue Ocean" plugin by selecting it and clicking on "Install Now" button. "Blue Ocean" is an aggretate package which loads many sub-plugins. Check the "Restart Jenkins when installation is complete" checkbox to restart Jenkins which will enable the plugin.

After restart a new "Open Blue Ocean" menu option will be on the left column menu. Click on this. Then "New Pipeline" -> GitHub -> (create access token via link provided), paste access token. Select cobratoolbox project. Create pipeline.


Enabling HTTPS for public server
--------------------------------

A prerequisite to this step is that your server must be accessible from the internet in port 80 and 443 and that it has been given a hostname in DNS (example king.nuigalway.ie).

Jenkins defaults to using port 8080. However best practise is to make this available on the default HTTPS port (443). There are several ways of achiving this. The apporach taken here use the nginx web server as a reverse-proxy to the Jenkins server.

.. code-block:: bash

        sudo add-apt-repository ppa:certbot/certbot
        sudo apt install -y nginx python3-certbot-nginx


Replace file /etc/nginx/sites-available/default with:


.. code-block::

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

Replace 'artenolis.example.com' with the proper full host name of the Artenolis server, eg king.nuigalway.ie. Check config file and restart nginx:


.. code-block:: bash

        sudo nginx -t
        sudo systemctl restart nginx

Now run certbot to create a HTTPS certificate (again replace artenolis.example.com' with the correct full host name).

.. code-block:: bash

        sudo certbot --nginx -d artenolis.example.com

Certbot will ask a few questions (eg agreeing to terms and conditions etc). For the final question, choose redirect all traffic to HTTPS (option 2).

If all goes well Jenkins will be accessable from https://artenolis.example.com (again replace the host name)


