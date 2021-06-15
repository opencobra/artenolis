FROM ubuntu:18.04
RUN apt update; apt install -y git vim python-pip
RUN mkdir /workspace
WORKDIR /workspace
RUN git clone https://github.com/opencobra/artenolis
RUN pip install -r ./artenolis/docs/requirements.txt
CMD	echo "Checking out lastest from opencobra/artenolis master branch..."; \
	cd ./artenolis; git pull origin master; \
	cd ./docs; \
	echo "Building docs..."; \
	make html; \
	output_tar=artenolis_doc_`date +%Y%m%d-%H%M`.tar.gz; \
	echo "Copying build docs to output folder as file ${output_tar} ..."; \
	tar czf /output/${output_tar} ./build
