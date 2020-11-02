# Building Artenolis Documentation

This procedure has been tested on Ubuntu 18.04. From the 'docs' subdirectory:

```
sudo apt install python3-pip
pip3 install -r requirements.txt
make html
```

Output HTML documentation will be in directory ./build/html

To publish the updated documentation on the Artenolis website at 
https://opencobra.github.io/artenolis/stable/
checkout the gh-pages branch of the https://github.com/opencobra/artenolis.git repository
and replace the ./stable or ./latest directory with the build output.


