package ru.javastudy.strategies;

import ru.javastudy.collections.PersonList;
import ru.javastudy.models.Person;

public class EvenQuickSort implements SortingStrategy {

    @Override
    public void sort(PersonList list) {
        if (list.size() <= 1) return;

        PersonList evenList = new PersonList(list.size());
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getYear() % 2 == 0) {
                evenList.add(list.get(i));
            }
        }

        if (evenList.size() > 1) {
            QuickSort myQuickSort = new QuickSort();
            myQuickSort.sort(evenList);
        }

        int evenIndex = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getYear() % 2 == 0) {
                list.set(i, evenList.get(evenIndex++));
            }
        }
    }
}