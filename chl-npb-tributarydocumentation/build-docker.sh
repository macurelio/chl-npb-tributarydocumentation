#!/usr/bin/env bash
set -e

###Para construir la imagen de manera más rápida y simular lo que hace el CI
PUSH=${1:-"false"}
JAR_FILE=$(grep <pom.properties "jar.file" | cut -d"=" -f2)
DOCKER_IMAGE_NAME=$(grep <pom.properties "docker.image.name" | cut -d"=" -f2)
DEFAULT_DOCKER_TAG=$(grep <pom.properties "docker.image.tag" | cut -d"=" -f2)

FINAL_DOCKER_TAG=${DOCKER_TAG:-$DEFAULT_DOCKER_TAG}

FINAL_DOCKER_COORDINATES="$DOCKER_IMAGE_NAME:$FINAL_DOCKER_TAG"

echo "Construyendo y taggeando imagen: $FINAL_DOCKER_COORDINATES"
echo "Para reemplazar tag, utilice variable de entorno DOCKER_TAG"

###Realiza build utilizando la imagen docker de maven, utilizando el usuario actual para generar los artefactos
docker run -v "$PWD":/usr/src/mymaven -v "$HOME/.m2":/var/maven/.m2 -w /usr/src/mymaven -ti --rm -u $UID \
  -e MAVEN_CONFIG=/var/maven/.m2 \
  maven:3.8.6-eclipse-temurin-17 \
  mvn -Duser.home=/var/maven -DskipTests -V -U -e --batch-mode clean package

docker build -f Dockerfile -t \
  "${FINAL_DOCKER_COORDINATES}" \
  --build-arg JAR_FILE="${JAR_FILE}" .

if [[ $PUSH -eq "true" ]]; then
  docker push ${FINAL_DOCKER_COORDINATES}
fi
