chmod +x gradlew
./gradlew bootJar
docker login --username $HEROKU_DOCKER_USERNAME --password $HEROKU_API_KEY registry.heroku.com
heroku container:push -a $HEROKU_APP_NAME web
heroku container:release -a $HEROKU_APP_NAME web