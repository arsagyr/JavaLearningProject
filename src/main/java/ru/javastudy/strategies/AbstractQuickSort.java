package ru.javastudy.strategies;

import ru.javastudy.collections.PersonList;
import ru.javastudy.models.Person;

/**
 * Базовый абстрактный класс для алгоритмов быстрой сортировки (QuickSort).
 * Реализует общую рекурсивную структуру алгоритма и кастомную логику сравнения,
 * оставляя реализацию фазы разделения (partition) для конкретных подклассов.
 */
public abstract class AbstractQuickSort implements SortingStrategy {
    // Принудительное переопределение метода интерфейса SortingStrategy подклассами
    @Override
    public abstract void sort(PersonList list);

    /**
     * Рекурсивный каркас алгоритма быстрой сортировки.
     * Разделяет коллекцию на подмассивы относительно опорного элемента и сортирует их.
     *
     * @param list сортируемая кастомная коллекция
     * @param low  начальный индекс текущего подмассива
     * @param high конечный индекс текущего подмассива
     */
    protected void quickSort(PersonList list, int low, int high) {
        // Базовое условие остановки рекурсии: подмассив должен содержать минимум 2 элемента
        if (low < high) {
            // Вычисляем индекс опорного элемента после разделения
            int pivotIndex = partition(list, low, high);
            // Рекурсивно сортируем левую часть (элементы, которые меньше или равны опорному)
            quickSort(list, low, pivotIndex - 1);
            // Рекурсивно сортируем правую часть (элементы, которые больше опорного)
            quickSort(list, pivotIndex + 1, high);
        }
    }

    /**
     * Абстрактный метод для разделения подмассива.
     * Конкретная стратегия (схема Ломуто) должна определить этот шаг самостоятельно.
     */
     abstract int partition(PersonList list, int low, int high);

    /**
     * Компаратор для последовательного сравнения объектов Person по трем полям:
     * 1. Год рождения (int) — приоритет 1
     * 2. Фамилия (String) — приоритет 2 (если года рождения совпадают)
     * 3. Имя (String) — приоритет 3 (если года рождения и фамилии совпадают)
     *
     * @return отрицательное число, если p1 < p2; 0, если p1 == p2; положительное число, если p1 > p2
     */
    protected int compare(Person p1, Person p2) {
        if (p1 == p2) {
            return 0;
        }

        // Первичное сравнение по годам рождения
        if (p1.getYear() != p2.getYear()) {
            return Integer.compare(p1.getYear(), p2.getYear());
        }

        // Быстрая проверка на равенство строк по ссылкам (String Pool) перед тяжелым compareTo
        if (p1.getLastName() == p2.getLastName() && p1.getFirstName() == p2.getFirstName()) {
            return 0;
        }

        // Вторичное сравнение по фамилии в алфавитном порядке
        int lastNameRes = p1.getLastName().compareTo(p2.getLastName());
        if (lastNameRes != 0) {
            return lastNameRes;
        }

        // Третичное сравнение по имени в алфавитном порядке
        return p1.getFirstName().compareTo(p2.getFirstName());
    }

    /**
     * Взаимный обмен двух элементов местами внутри коллекции PersonList.
     * Сортировка выполняется на месте (in-place) без выделения дополнительной памяти под массивы.
     */
    protected void swap(PersonList list, int i, int j) {
        Person temp = list.getFast(i);
        list.setFast(i, list.getFast(j));
        list.setFast(j, temp);
    }
}