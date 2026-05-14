package ru.javastudy.strategies;

import ru.javastudy.collections.PersonList;

/**
 * Стратегия частичной сортировки.
 * Находит в исходной коллекции людей с четным годом рождения,
 * сортирует только их с помощью QuickSort и возвращает обратно на свои позиции.
 * Исключает баги перезаписи данных за счет использования временного буфера результата.
 */
public class EvenQuickSort implements SortingStrategy {

    @Override
    public void sort(PersonList list) {
        // Запуск алгоритма производится только если в коллекции более одного элемента
        if (list != null && list.size() > 1) {
            // Создаем временный кастомный список для сбора элементов с четным годом рождения
            PersonList evenList = new PersonList(list.size());
            for (int i = 0; i < list.size(); i++) {
                // Если год рождения четный, копируем ссылку на объект во временный список
                if (list.get(i).getYear() % 2 == 0) {
                    evenList.add(list.getFast(i));
                }
            }

            // Если найдено более одного "четного" элемента, сортируем их
            if (evenList.size() > 1) {
                QuickSort myQuickSort = new QuickSort();
                myQuickSort.sort(evenList); // Вызов основной стратегии QuickSort Ломуто
            }

            // Сборка результата в промежуточный список для защиты от искажения данных по ссылкам
            PersonList result = new PersonList(list.size());
            int evenIndex = 0;

            for (int i = 0; i < list.size(); i++) {
                // Опираемся на структуру еще не измененного исходного списка
                if (list.getFast(i).getYear() % 2 == 0) {
                    // На четное значение подставляем гарантированно отсортированный элемент
                    result.add(evenList.get(evenIndex++));
                } else {
                    // Нечетные элементы переносим без изменений структуры
                    result.add(list.getFast(i));
                }
            }

            // Сбрасываем старое состояние коллекции и наполняем её валидными данными из промежуточного списка
            list.clear(result.size());
            list.rewrite(result);
        }
    }
}