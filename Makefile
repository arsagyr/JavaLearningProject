all:
	mvn clean
	mvn compile
	mvn package
clean:
	mvn clean
run:
	mvn compile
	mvn exec:java -Dexec.mainClass="ru.javastudy.app.Main"