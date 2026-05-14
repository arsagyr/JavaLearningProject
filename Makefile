all:
	make run
clean:
	mvn clean
run:
	mvn clean compile exec:java -Dexec.mainClass=ru.javastudy.app.Main
# 	mvn clean
# 	mvn compile
# 	mvn exec:java -Dexec.mainClass="ru.javastudy.app.Main"
# 	mvn exec:java -Dexec.mainClass=ru.javastudy.app.Main

test:
	mvn test

# Запуск программы
	

