# Variables
$dockerHubUser = "vcandel"
$image = "$dockerHubUser/gymbrosdb:latest"

# Pushear imágenes a Docker Hub
docker push $image