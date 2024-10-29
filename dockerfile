FROM openjdk:21-oracle
COPY ./target/Book-Gateway-0.0.1-SNAPSHOT.jar Book-Gateway.jar
CMD ["java", "-jar", "Book-Gateway.jar"]