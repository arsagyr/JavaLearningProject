package ru.javastudy.input;

import ru.javastudy.models.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleInput   implements InputStrategy {
    @Override
    public List<Person> load() {

    List<Person> persons = new ArrayList<>();

        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите число людей: ");
        int n = scanner.nextInt();
        scanner.nextLine();

        String lastName, firstName;
        int year;
        for (int i=0; i < n;i++){
            System.out.print("Введите фамилию: ");
            lastName = scanner.nextLine();
            
            System.out.print("Введите имя: ");
            firstName = scanner.nextLine();
            
            System.out.print("Введите год рождения: ");
            year = scanner.nextInt();
            scanner.nextLine();

            persons.add(Person.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .year(year)
                    .build());
        }
        scanner.close();
        return persons;
    }
    // public List<Person> load(String size) {
    //     return List.of();
    // }
}
