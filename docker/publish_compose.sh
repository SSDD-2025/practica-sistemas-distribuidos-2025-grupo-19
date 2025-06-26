#!/bin/bash
docker compose -f ./docker/docker-compose.prod.yml publish --with-env  vcandel/gymbrosdb-compose:1.0.0
sleep 2