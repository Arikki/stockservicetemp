FROM openjdk:8
EXPOSE 8082
ADD /target/stockService.jar stockService.jar
ENTRYPOINT ["java","-jar","/stockService.jar"]