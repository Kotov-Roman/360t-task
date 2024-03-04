#server.port should be the same for server and client
./mvnw clean package
java -Dserver.port=8085 -jar ./server/target/server-0.0.1-jar-with-dependencies.jar
