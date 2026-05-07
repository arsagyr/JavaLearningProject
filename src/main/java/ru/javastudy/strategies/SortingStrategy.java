package ru.javastudy.strategies;

import ru.javastudy.collections.PersonList;

/**
 * Интерфейс, определяющий контракт для стратегий сортировки.
 * Реализует паттерн проектирования "Стратегия" (Strategy), позволяя
 * динамически подменять алгоритмы сортировки коллекции PersonList в рантайме.
 */
public interface SortingStrategy {

    /**
     * Выполняет сортировку переданной кастомной коллекции.
     * Конкретная реализация (например, QuickSort) сама определяет алгоритм упорядочивания.
     *
     * @param list кастомный список объектов Person, подлежащий сортировке
     */
    void sort(PersonList list);
}