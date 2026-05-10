package ru.javastudy.app;

import ru.javastudy.models.Person;
import java.util.List;

import ru.javastudy.input.ConsoleInput;
import ru.javastudy.input.InputStrategy;
import ru.javastudy.input.PersonLoader;
import ru.javastudy.input.RandomInput;

public class Main {

    public static void main(String[] args) {
        List<Person> persons;

        InputStrategy inputStrategy = new ConsoleInput();
        persons =   inputStrategy.load();

        persons.stream()
        .forEach(System.out::println);
    }
}
