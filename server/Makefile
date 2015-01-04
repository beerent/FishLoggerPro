all: clean build run

kill:
	kill %1

clean:
	rm *.class

build:
	javac -cp ".:mysql-connector-java-5.1.33-bin.jar" FishLoggerServer.java

run:
	java -cp ".:mysql-connector-java-5.1.33-bin.jar" FishLoggerServer
