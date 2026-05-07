package ru.javastudy.input;

import ru.javastudy.models.Person;

import java.util.List;

public class PersonLoader {
    private final InputStrategy strategy;

    public PersonLoader(InputStrategy strategy) {
        this.strategy = strategy;
    }

    public List<Person> load() {
        return strategy.load();
    }
}