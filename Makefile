all:
	mvn clean
	mvn compile
	mvn package
clean:
	mvn clean
test:
	mvn clean
	mvn compile
	mvn exec:java -Dexec.mainClass="ru.javastudy.app.Main"