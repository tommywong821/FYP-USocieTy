FROM openjdk:11
ARG JAR_FILE=build/libs/*.jar

ADD ${JAR_FILE} app.jar
#ENTRYPOINT ["java", "-jar", "app.jar"]
CMD exec java -jar /app.jar --server.port=$PORT