package ru.javastudy.strategies;

import ru.javastudy.collections.PersonList;
import ru.javastudy.models.Person;

public abstract class AbstractQuickSort implements SortingStrategy {

    @Override
    public abstract void sort(PersonList list);

    protected void quickSort(PersonList list, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(list, low, high);
            quickSort(list, low, pivotIndex - 1);
            quickSort(list, pivotIndex + 1, high);
        }
    }

    protected abstract int partition(PersonList list, int low, int high);

    /**
     * Сравнение по приоритетам:
     * 1. Год рождения (int)
     * 2. Фамилия (String)
     * 3. Имя (String)
     */
    protected int compare(Person p1, Person p2) {
        if (p1.getYear() != p2.getYear()) {
            return Integer.compare(p1.getYear(), p2.getYear());
        }

        int lastNameRes = p1.getLastName().compareTo(p2.getLastName());
        if (lastNameRes != 0) {
            return lastNameRes;
        }

        return p1.getFirstName().compareTo(p2.getFirstName());
    }

    protected void swap(PersonList list, int i, int j) {
        Person temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }
}