package ru.javastudy.input;

import java.util.Scanner;
import ru.javastudy.models.Person;

public class PersonInput {

    private static final int MIN_YEAR = 1900;
    private static final int MAX_YEAR = 2026;
    
    private Scanner scanner;
    
    public PersonInput(Scanner scanner) {
        this.scanner = scanner;
    }
    
    public Person load() {
        System.out.println("\n--- Ввод данных человека ---");
        
        String lastName = getValidString("Введите фамилию (только русские буквы): ");
        String firstName = getValidString("Введите имя (только русские буквы): ");
        int year = getValidYear("Введите год рождения (" + MIN_YEAR + "-" + MAX_YEAR + "): ");
        
        return Person.builder()
                .lastName(lastName)
                .firstName(firstName)
                .year(year)
                .build();
    }
    
    private String getValidString(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine();
            if (input != null && input.matches("[а-яА-ЯёЁ\\s-]+")) {
                return input;
            }
            System.out.println("\nОшибка! Допустимы только русские буквы, пробел и дефис.");
        }
    }
    
    private int getValidYear(String message) {
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