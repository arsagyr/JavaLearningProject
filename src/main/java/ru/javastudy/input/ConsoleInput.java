
package ru.javastudy.input;

import ru.javastudy.collections.PersonList;
import ru.javastudy.models.Person;
import java.util.Scanner;
import java.util.stream.IntStream;

/**
 * Автономный модуль консольного ввода.
 * Работает напрямую с кастомной коллекцией PersonList.
 * Реализует заполнение кастомной коллекции PersonList через Stream API (ТЗ п.3).
 */
public class ConsoleInput {
    private final Scanner scanner;
    private final int size;

    public ConsoleInput(int size, Scanner scanner) {
        this.scanner = scanner;
        this.size = size;
    }

    /**
     * Запускает процесс ввода данных.
     * Использует IntStream для итерации и формирования коллекции.
     */
    public PersonList read() {
        // Создаем кастомную коллекцию сразу нужного размера
        PersonList persons = new PersonList(this.size);

        // Cборка объектов через Stream API
        IntStream.range(0, this.size)
                .peek(i -> System.out.println("\n--- Ввод данных человека [" + (i + 1) + "/" + size + "] ---"))
                .mapToObj(i -> Person.builder()
                        .lastName(validString("Введите фамилию (только русские буквы): "))
                        .firstName(validString("Введите имя (только русские буквы): "))
                        .year(validYear("Введите год рождения (1900-2026): "))
                        .build())
                .forEach(persons::add);

        return persons;
    }

    /**
     * Валидация строк
     */
    private String validString(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine();
            if (input != null && input.matches("[а-яА-ЯёЁ\\s-]+")) {
                return input;
            }
            System.out.println("\nОшибка! Допустимы только русские буквы, пробел и дефис.");
        }
    }

    /**
     * Валидация даты (диапазон 1900-2026).
     */
    private int validYear(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine();
            try {
                // Пытаемся привести к целочисленному типу
                int year = Integer.parseInt(input.trim());
                if (year >= 1900 && year <= 2026) {
                    return year;
                }
            } catch (NumberFormatException ignored) {
                // Игнорируем некорректный формат, сохраняя цикл
            }
            System.out.println("\nОшибка! Введите целое число от 1900 до 2026.");
        }
    }
}
