FROM openjdk:11-jdk
ARG JAR_FILE=build/libs/*.jar

ADD ${JAR_FILE} app.jar
#ENTRYPOINT ["java", "-jar", "app.jar"]
CMD exec java -Xmx512m -Xss512k -XX:CICompilerCount=2 -XX:+UseContainerSupport -jar /app.jar --server.port=$PORT