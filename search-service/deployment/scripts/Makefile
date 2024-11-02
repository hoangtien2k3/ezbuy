# change version before deploying
IMAGE_VERSION= 1.0.15
# set correctly env. ENV includes: dev, staging
ENV= dev
IMAGE_NAME= 10.207.252.224:9901/ezbuy-search-service

# Run this cmd if your source code changes more than 1 current module
install_fw:
	cd ../../../ && mvn clean install
build_service:
	cd ../../ && mvn clean package
docker_build:
	cd ../../ && docker build -t $(IMAGE_NAME):$(IMAGE_VERSION) .
docker_push:
	docker push $(IMAGE_NAME):$(IMAGE_VERSION)
#Run this cmd if your secret.yml file has changed
apply_secret:
	cd ../$(ENV) && kubectl apply -f secret.yml
#Run this cmd if your deployment.yml file has changed
apply_deployment:
	cd ../$(ENV) && kubectl apply -f deployment.yml
#Run this cmd if your configMap.yml file has changed
apply_configMap:
	cd ../$(ENV) && kubectl apply -f configMap.yml
#Run this cmd if your service.yml file has changed
apply_service:
	cd ../$(ENV) && kubectl apply -f service.yml
build_and_push_image: install_fw build_service docker_build docker_push

first_deploy_service: apply_secret apply_configMap apply_deployment apply_service

#for the first service deploying
#Step 1: set IMAGE_VERSION, ENV, IMAGE_NAME
#Step 2: run: build_and_push_image
#Step 3: make sure to set the correct image version that matches IMAGE_VERSION
#Step 4: run: first_deploy_service
#Goodluck !!!


