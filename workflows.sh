#!/bin/bash

WORKFLOW_TEMPLATE=$(cat .github/workflow-template.yaml)
SERVICES=(auth-service cart-service notification-send notification-service order-service payment-service product-service rating-service search-service setting-service trigger-service)

for SERVICE_NAME in "${SERVICES[@]}"; do
    echo "Generating workflow for ${SERVICE_NAME} service"

    WORKFLOW="${WORKFLOW_TEMPLATE//\{\{SERVICE_NAME\}\}/${SERVICE_NAME}}"
    echo "${WORKFLOW}" > ".github/workflows/${SERVICE_NAME}.yaml"
done