all:
	make run
clean:
	mvn clean
run:
	mvn clean
	mvn compile
	mvn exec:java -Dexec.mainClass="ru.javastudy.app.Main"