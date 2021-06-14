# Building Artenolis Documentation

## Building documentation with Docker

From the ./docs directory build a docker image:

```
docker build -t opencobra/artenolis-docs .
```

Run run and build the docs (with output going into /var/tmp):

```
docker run --rm -it --volume /var/tmp:/output  opencobra/artenolis-docs
```


## Publishing updated documentation

To publish the updated documentation on the Artenolis website at 
https://opencobra.github.io/artenolis/stable/
checkout the gh-pages branch of the https://github.com/opencobra/artenolis.git repository
and replace the ./stable or ./latest directory with the build output.


