# Artenolis docker

Project to encapsulate Artinolis system in a docker image at facilitate easy replication or
server redeployment.


## Using official Jenkins docker image

Documentation at 
https://github.com/jenkinsci/docker/blob/master/README.md


```
docker run -p 8080:8080 -p 50000:50000 jenkins/jenkins:lts
```


## Using custom docker image

To build docker image

```
docker build -t opencobra/artenolis .
```

(work in progress)



