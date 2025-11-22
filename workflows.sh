#!/bin/bash

WORKFLOW_TEMPLATE=$(cat .github/workflow-template.yaml)
SERVICES=(
  ezbuy-auth
  ezbuy-cart
  ezbuy-gateway
  ezbuy-noti
  ezbuy-order
  ezbuy-payment
  ezbuy-product
  ezbuy-rating
  ezbuy-search
  ezbuy-setting
  ezbuy-trigger
)

for SERVICE_NAME in "${SERVICES[@]}"; do
    echo "Generating workflow for ${SERVICE_NAME} service"
    WORKFLOW="${WORKFLOW_TEMPLATE//\{\{SERVICE_NAME\}\}/${SERVICE_NAME}}"
    echo "${WORKFLOW}" > ".github/workflows/${SERVICE_NAME}.yaml"
done
