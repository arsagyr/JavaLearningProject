package threads;

import ru.javastudy.collections.PersonList;
import ru.javastudy.models.Person;
import ru.javastudy.threads.NCounter;

public class TestNCounter {
    public static void main(String[] args) {
        try {
            // Подготовка: создаём коллекцию и целевой объект
            PersonList list = new PersonList(10);
            Person target = Person.builder().firstName("Иван").lastName("Иванов").year(1990).build();
            Person other = Person.builder().firstName("Петр").lastName("Петров").year(1985).build();

            list.add(target);
            list.add(other);
            list.add(target);
            list.add(target);
            list.add(other);
            list.add(other);
            list.add(target);

            int expected = 4;

            // Тест 1: 1 поток (однопоточный эквивалент)
            int found1 = NCounter.countAndPrintOccurrence(list, target, 1);
            System.out.println("Тест 1 (1 поток): ожидается=" + expected + ", найдено=" + found1);
            System.out.println(found1 == expected ? "Выполнен" : "Провален");

            // Тест 2: 2 потока
            int found2 = NCounter.countAndPrintOccurrence(list, target, 2);
            System.out.println("Тест 2 (2 потока): ожидается=" + expected + ", найдено=" + found2);
            System.out.println(found2 == expected ? "Выполнен" : "Провален");

            // Тест 3: больше потоков, чем элементов
            int found3 = NCounter.countAndPrintOccurrence(list, target, 20);
            System.out.println("Тест 3 (20 потоков): ожидается=" + expected + ", найдено=" + found3);
            System.out.println(found3 == expected ? "Выполнен" : "Провален");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Тест прерван");
        } catch (Exception e) {
            System.out.println("Ошибка в тесте: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
