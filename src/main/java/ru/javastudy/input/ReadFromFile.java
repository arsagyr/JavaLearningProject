package ru.javastudy.input;

import ru.javastudy.models.Person;

import java.util.List;

public class ReadFromFile implements InputStrategy {

    @Override
    public List<Person> load(int size) {
        return List.of();
    }
}
