# ngok3fyp-backend

## Deploy Flow

GitHub receive `git:push` from master branch -> Trigger TravisCi build -> Execute CI Testing -> Dockerize kotlin
springboot -> Push docker image to heroku registry -> Deploy to heroku