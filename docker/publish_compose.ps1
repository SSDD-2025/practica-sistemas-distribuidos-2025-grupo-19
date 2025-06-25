#!/bin/bash

# Ruta al archivo docker-compose
$COMPOSE_FILE="docker/docker-compose.prod.yml"
$IMAGE_NAME="vcandel/gymbrosdb:1.0.0"

# Login
echo "Logging into Docker Hub..."
docker login

echo "Publicando imagen $IMAGE_NAME usando $COMPOSE_FILE..."

docker compose -f $COMPOSE_FILE publish --with-env $IMAGE_NAME