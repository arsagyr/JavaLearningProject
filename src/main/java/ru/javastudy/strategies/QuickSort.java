package ru.javastudy.strategies;

import ru.javastudy.collections.PersonList;
import ru.javastudy.models.Person;

public class QuickSort extends AbstractQuickSort {

    @Override
    public void sort(PersonList list) {
        if (list.size() > 1) {
            quickSort(list, 0, list.size() - 1);
        }
    }

    @Override
    protected int partition(PersonList list, int low, int high) {
        Person pivot = list.get(high);
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (compare(list.get(j), pivot) <= 0) {
                i++;
                swap(list, i, j);
            }
        }
        swap(list, i + 1, high);
        return i + 1;
    }
}