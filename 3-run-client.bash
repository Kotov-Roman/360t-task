#run when server is already running, otherwise connection will be refused
#server.port should be the same for server and client
./mvnw clean package
java -Dserver.port=8085 -jar ./client/target/client-0.0.1-jar-with-dependencies.jar
