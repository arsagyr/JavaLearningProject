package ru.javastudy.input;

import ru.javastudy.models.Person;
import ru.javastudy.collections.PersonList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ReadFromFile implements InputStrategy {
    private final Scanner scanner;

    public ReadFromFile(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void load(PersonList personList) {
        System.out.print("Введите название файла: ");
        String filename = scanner.next();
        scanner.nextLine();

        try (Scanner fileScanner = new Scanner(new File(filename))) {
            if (!fileScanner.hasNextLine()) {
                System.out.println("Предупреждение: файл пуст. Ничего не загружено.");
                return;
            }
            
            String firstLine = fileScanner.nextLine().trim();
            
            // Пропускаем пустые строки в начале файла
            while (firstLine.isEmpty() && fileScanner.hasNextLine()) {
                firstLine = fileScanner.nextLine().trim();
            }
            
            if (firstLine.isEmpty()) {
                System.out.println("Предупреждение: файл содержит только пустые строки. Ничего не загружено.");
                return;
            }
            
            try {
                Person firstPerson = parsePersonFromLine(firstLine);
                personList.add(firstPerson);
            } catch (RuntimeException e) {
                System.out.println("Ошибка при парсинге первой строки: " + e.getMessage());
                System.out.println("Строка будет пропущена.");
            }

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                try {
                    Person p = parsePersonFromLine(line);
                    personList.add(p);
                } catch (RuntimeException e) {
                    System.out.println("Ошибка при парсинге строки: " + e.getMessage());
                    System.out.println("Строка будет пропущена. Продолжаем загрузку...");
                }
            }
            
            System.out.println("Загрузка из файла завершена. Загружено записей: " + personList.size());
            
        } catch (FileNotFoundException e) {
            System.out.println("Ошибка: Файл \"" + filename + "\" не найден.");
            System.out.println("Пожалуйста, проверьте имя файла и убедитесь, что файл существует.");
            System.out.println("Загрузка из файла отменена. Коллекция осталась без изменений.");
        }
    }

    private Person parsePersonFromLine(String line) {
        String[] parts = line.split("\\s+");
        if (parts.length < 3) {
            throw new RuntimeException("Неверный формат строки: \"" + line +
                    "\". Ожидается: фамилия имя год");
        }
        String lastName = parts[0];
        String firstName = parts[1];
        int year;
        try {
            year = Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Неверный формат года в строке: \"" + line + 
                    "\". Год должен быть целым числом");
        }

        return Person.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .year(year)
                    .build();
    }
}