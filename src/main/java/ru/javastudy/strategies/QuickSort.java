package ru.javastudy.strategies;

import ru.javastudy.collections.PersonList;
import ru.javastudy.models.Person;

/**
 * Стратегия быстрой сортировки (QuickSort) с использованием схемы разделения Ломуто.
 * Метод разделения скрыт на уровне пакета для предотвращения внешних ошибок вызова.
 */
public class QuickSort extends AbstractQuickSort {

    @Override
    public void sort(PersonList list) {
        // Запуск алгоритма производится только если в коллекции более одного элемента
        if (list != null && list.size() > 1) {
            quickSort(list, 0, list.size() - 1);
        }
    }

    /**
     * Разделение подмассива по схеме Ломуто.
     * Модификатор доступа package-private.
     * Метод скрыт от внешних пакетов (включая класс Main), но доступен родительскому классу.
     */
    @Override
     int partition(PersonList list, int low, int high) {
        // По схеме Ломуто опорным элементом (pivot) всегда выступает последний элемент диапазона
        Person pivot = list.get(high);

        // Индекс 'i' указывает на границу элементов, которые меньше или равны pivot
        int i = low - 1;

        // Итератор 'j' сканирует подмассив от начального элемента до опорного
        for (int j = low; j < high; j++) {
            // Если текущий элемент меньше или равен опорному, расширяем левую зону
            if (compare(list.get(j), pivot) <= 0) {
                i++;
                swap(list, i, j); // Перемещаем меньший элемент в левую часть
            }
        }

        // Ставим сам опорный элемент на его законное место — сразу после меньших элементов
        swap(list, i + 1, high);

        // Возвращаем итоговый индекс опорного элемента для дальнейшего рекурсивного деления
        return i + 1;
    }
}