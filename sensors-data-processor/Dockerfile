FROM azul/zulu-openjdk:17-latest

COPY target/sensors-data-processor-0.0.1-SNAPSHOT.jar sensor-data-processor.jar

ENTRYPOINT ["java","-jar","sensor-data-processor.jar"]