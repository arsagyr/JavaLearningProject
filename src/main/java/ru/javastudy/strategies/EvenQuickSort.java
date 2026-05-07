package ru.javastudy.strategies;

import ru.javastudy.collections.PersonList;
import ru.javastudy.models.Person;

/**
 * Стратегия частичной сортировки.
 * Находит в исходной коллекции людей с четным годом рождения,
 * сортирует только их с помощью QuickSort и возвращает обратно на свои позиции.
 * Люди с нечетным годом рождения остаются на своих исходных местах.
 */
public class EvenQuickSort implements SortingStrategy {

    @Override
    public void sort(PersonList list) {
        // Запуск алгоритма производится только если в коллекции более одного элемента
        if (list != null && list.size() > 1) {
            //1. Создаем временный кастомный список для сбора элементов с четным годом рождения
            PersonList evenList = new PersonList(list.size());
            for (int i = 0; i < list.size(); i++) {
                // Если год рождения четный, копируем ссылку на объект во временный список
                if (list.get(i).getYear() % 2 == 0) {
                    evenList.add(list.get(i));
                }
            }

            // 2. Если найдено более одного "четного" элемента, сортируем их
            if (evenList.size() > 1) {
                QuickSort myQuickSort = new QuickSort();
                myQuickSort.sort(evenList); // Вызов основной стратегии QuickSort Ломуто
            }

            // 3. Возвращаем отсортированные четные элементы обратно в исходный список
            int evenIndex = 0;
            for (int i = 0; i < list.size(); i++) {
                // Находим те же самые позиции с четным годом рождения
                if (list.get(i).getYear() % 2 == 0) {
                    // Перезаписываем старый элемент отсортированным из evenList
                    list.set(i, evenList.get(evenIndex++));
                }
            }
        }
    }
}