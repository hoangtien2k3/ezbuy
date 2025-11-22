#chmod +x buildAllImage.sh
BASE_IMAGE="hoangtien2k3"
EZBUY_AUTH=ezbuy-auth:lastest
EZBUY_CART=ezbuy-cart:lastest
EZBUY_GATEWAY=ezbuy-gateway:lastest
EZBUY_NOTI=ezbuy-noti:lastest
EZBUY_ORDER=ezbuy-order:lastest
EZBUY_PAYMENT=ezbuy-payment:lastest
EZBUY_PRODUCT=ezbuy-product:lastest
EZBUY_RATING=ezbuy-rating:lastest
EZBUY_SEARCH=ezbuy-search:lastest
EZBUY_SETTING=ezbuy-setting:lastest
EZBUY_TRIGGER=ezbuy-trigger:lastest

#cd ../ && mvn clean install
cd ../ezbuy-auth && mvn clean package && docker build -t $BASE_IMAGE/$EZBUY_AUTH . && docker push $BASE_IMAGE/$EZBUY_AUTH
cd ../ezbuy-cart && mvn clean package && docker build -t $BASE_IMAGE/$EZBUY_CART . && docker push $BASE_IMAGE/$EZBUY_CART
cd ../ezbuy-gateway && mvn clean package && docker build -t $BASE_IMAGE/$EZBUY_GATEWAY . && docker push $BASE_IMAGE/$EZBUY_GATEWAY
cd ../ezbuy-noti && mvn clean package && docker build -t $BASE_IMAGE/$EZBUY_NOTI . && docker push $BASE_IMAGE/$EZBUY_NOTI
cd ../ezbuy-order && mvn clean package && docker build -t $BASE_IMAGE/$EZBUY_ORDER . && docker push $BASE_IMAGE/$EZBUY_ORDER
cd ../ezbuy-payment && mvn clean package && docker build -t $BASE_IMAGE/$EZBUY_PAYMENT . && docker push $BASE_IMAGE/$EZBUY_PAYMENT
cd ../ezbuy-product && mvn clean package && docker build -t $BASE_IMAGE/$EZBUY_PRODUCT . && docker push $BASE_IMAGE/$EZBUY_PRODUCT
cd ../ezbuy-rating && mvn clean package && docker build -t $BASE_IMAGE/$EZBUY_RATING . && docker push $BASE_IMAGE/$EZBUY_RATING
cd ../ezbuy-search && mvn clean package && docker build -t $BASE_IMAGE/$EZBUY_SEARCH . && docker push $BASE_IMAGE/$EZBUY_SEARCH
cd ../ezbuy-setting && mvn clean package && docker build -t $BASE_IMAGE/$EZBUY_SETTING . && docker push $BASE_IMAGE/$EZBUY_SETTING
cd ../ezbuy-trigger && mvn clean package && docker build -t $BASE_IMAGE/$EZBUY_TRIGGER . && docker push $BASE_IMAGE/$EZBUY_TRIGGER
