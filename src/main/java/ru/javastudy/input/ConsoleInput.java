package ru.javastudy.input;

import ru.javastudy.models.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleInput   implements InputStrategy {
    private final Scanner scanner;
    private final int size;

    public ConsoleInput(int size, Scanner scanner) {
        this.scanner = scanner;
        this.size = size;
    }

    @Override
    public List<Person> load() {

    List<Person> persons = new ArrayList<>();


        String lastName, firstName;
        int year;
        for (int i = 0; i < this.size; i++){
            System.out.print("Введите фамилию: ");
            lastName = this.scanner.nextLine();
            
            System.out.print("Введите имя: ");
            firstName = this.scanner.nextLine();
            
            System.out.print("Введите год рождения: ");
            year = this.scanner.nextInt();
            this.scanner.nextLine();

            persons.add(Person.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .year(year)
                    .build());
        }
        return persons;
    }
}
