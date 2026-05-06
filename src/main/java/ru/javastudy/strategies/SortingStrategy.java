package ru.javastudy.strategies;

import ru.javastudy.collections.PersonList;

public interface SortingStrategy {
    void sort(PersonList list);
}