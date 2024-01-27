FROM amazoncorretto:17-alpine-jdk

COPY ./target/*.jar fast-food-producao.jar

EXPOSE 8090

ENTRYPOINT exec java $JAVA_OPTS -jar ./fast-food-producao.jar