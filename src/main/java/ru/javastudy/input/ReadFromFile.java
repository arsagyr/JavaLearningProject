package ru.javastudy.input;

import ru.javastudy.models.Person;
import ru.javastudy.collections.PersonList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReadFromFile implements InputStrategy {
    private final Scanner scanner;

    public ReadFromFile( Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void load(PersonList personList) {
        System.out.print("Введите название файла: ");
        String filename = scanner.next();
        scanner.nextLine();


        try (Scanner scanner = new Scanner(new File(filename))) {
            if (!scanner.hasNextLine()) {
                // personList; // пустой файл
            }
            String firstLine = scanner.nextLine().trim();

                Person firstPerson = parsePersonFromLine(firstLine);
                personList.add(firstPerson);

                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim();
                    if (line.isEmpty()) {
                        continue;
                    }
                    Person p = parsePersonFromLine(line);
                    personList.add(p);
                }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Файл не найден: " + filename, e);
        }
    }

    private Person parsePersonFromLine(String line) {
        String[] parts = line.split("\\s+");
        if (parts.length < 3) {
            throw new RuntimeException("Неверный формат строки: " + line +
                    ". Ожидается: фамилия имя год");
        }
        String lastName = parts[0];
        String firstName = parts[1];
        int year;
        try {
            year = Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Неверный формат года в строке: " + line, e);
        }


        return Person.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .year(year)
                    .build();
    }
}