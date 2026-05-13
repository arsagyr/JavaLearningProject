package ru.javastudy.output;

import ru.javastudy.collections.PersonList;
import ru.javastudy.models.Person;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class WriteToFile {

    private static final String DEFAULT_FILE_NAME = "persons.txt";

    public void save(PersonList persons) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DEFAULT_FILE_NAME))) {

            for (Person person : persons) {
                writer.write(person.toString());
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Ошибка записи в файл");
            e.printStackTrace();
        }
    }
}