package ru.javastudy.input;

import ru.javastudy.collections.PersonList;

public interface InputStrategy {
    void load(PersonList persons);
}
