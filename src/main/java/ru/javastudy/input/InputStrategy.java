package ru.javastudy.input;

import ru.javastudy.models.Person;

import java.util.List;

public interface InputStrategy {

    List<Person> load(int size);

}
