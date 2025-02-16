#chmod +x buildAllImage.sh
BASE_IMAGE="localhost:8956"
# shellcheck disable=SC2034
API_GATEWAY_SERVICE=gateway-service-sandbox:1.0.2
AUTH_SERVICE=auth-service-sandbox:1.0.3
# shellcheck disable=SC2034
CART_VERSION=cart-service-sandbox:1.0.2
# shellcheck disable=SC2034
CUSTOMER_VERSION=customer-service-sandbox:1.0.3
# shellcheck disable=SC2034
NOTIFICATION_VERSION=noti-service-sandbox:1.0.3
# shellcheck disable=SC2034
ORDER_VERSION=order-service-sandbox:1.0.3
# shellcheck disable=SC2034
PAYMENT_VERSION=payment-service-sandbox:1.0.3
# shellcheck disable=SC2034
PRODUCT_VERSION=product-service-sandbox:1.0.3
# shellcheck disable=SC2034
RATING_VERSION=rating-service-sandbox:1.0.3
# shellcheck disable=SC2034
SEARCH_VERSION=search-service-sandbox:1.0.3
# shellcheck disable=SC2034
SEND_NOTIFICATION_VERSION=send-noti-service-sandbox:1.0.4
# shellcheck disable=SC2034
SETTING_VERSION=setting-service-sandbox:1.0.3
# shellcheck disable=SC2034
SYNC_VERSION=sync-service-sandbox:1.0.3
# shellcheck disable=SC2034
TRIGGER_VERSION=trigger-service-sandbox:1.0.3

#cd ../ && mvn clean install
cd ../api-gateway-service && mvn clean package && docker build -t $BASE_IMAGE/$API_GATEWAY_SERVICE . && docker push $BASE_IMAGE/$API_GATEWAY_SERVICE
cd ../auth-service && mvn clean package && docker build -t $BASE_IMAGE/$AUTH_SERVICE . && docker push $BASE_IMAGE/$AUTH_SERVICE
cd ../cart-service && mvn clean package && docker build -t $BASE_IMAGE/$CART_VERSION . && docker push $BASE_IMAGE/$CART_VERSION
cd ../customer-service && mvn clean package && docker build -t $BASE_IMAGE/$CUSTOMER_VERSION . && docker push $BASE_IMAGE/$CUSTOMER_VERSION
cd ../notification-service && mvn clean package && docker build -t $BASE_IMAGE/$NOTIFICATION_VERSION . && docker push $BASE_IMAGE/$NOTIFICATION_VERSION
cd ../order-service && mvn clean package && docker build -t $BASE_IMAGE/$ORDER_VERSION . && docker push $BASE_IMAGE/$ORDER_VERSION
cd ../payment-service && mvn clean package && docker build -t $BASE_IMAGE/$PAYMENT_VERSION . && docker push $BASE_IMAGE/$PAYMENT_VERSION
cd ../product-service && mvn clean package && docker build -t $BASE_IMAGE/$PRODUCT_VERSION . && docker push $BASE_IMAGE/$PRODUCT_VERSION
cd ../rating-service && mvn clean package && docker build -t $BASE_IMAGE/$RATING_VERSION . && docker push $BASE_IMAGE/$RATING_VERSION
cd ../search-service && mvn clean package && docker build -t $BASE_IMAGE/$SEARCH_VERSION . && docker push $BASE_IMAGE/$SEARCH_VERSION
cd ../send-notification-service && mvn clean package && docker build -t $BASE_IMAGE/$SEND_NOTIFICATION_VERSION . && docker push $BASE_IMAGE/$SEND_NOTIFICATION_VERSION
cd ../setting-service && mvn clean package && docker build -t $BASE_IMAGE/$SETTING_VERSION . && docker push $BASE_IMAGE/$SETTING_VERSION
cd ../sync-service && mvn clean package && docker build -t $BASE_IMAGE/$SYNC_VERSION . && docker push $BASE_IMAGE/$SYNC_VERSION
cd ../trigger-service && mvn clean package && docker build -t $BASE_IMAGE/$TRIGGER_VERSION . && docker push $BASE_IMAGE/$TRIGGER_VERSION
