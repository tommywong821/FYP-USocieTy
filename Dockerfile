FROM openjdk:11-jdk
ARG JAR_FILE=build/libs/*.jar

ADD ${JAR_FILE} app.jar
#ENTRYPOINT ["java", "-jar", "app.jar"]
CMD exec java -Xmx300m -Xss512k -XX:CICompilerCount=2 -Dfile.encoding=UTF-8 -XX:+UseContainerSupport -XX:+UseG1GC -jar /app.jar --server.port=$PORT