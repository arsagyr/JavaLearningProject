package ru.javastudy.collections;

import ru.javastudy.models.Person;
import java.util.Arrays;
import java.util.stream.Stream;

public class PersonList {
    // Внутренний массив для хранения элементов
    private Person[] elements;
    // Количество фактически добавленных элементов
    private int size = 0;
    // Вместимость массива по умолчанию
    private static final int DEFAULT_CAPACITY = 10;

    /**
     * Создает список с дефолтной вместимостью.
     */
    public PersonList() {
        this.elements = new Person[DEFAULT_CAPACITY];
    }

    /**
     * Создает список с заданной вместимостью.
     * Защищает от передачи отрицательного или нулевого размера.
     */
    public PersonList(int initialCapacity) {
        if (initialCapacity <= 0) {
            System.out.println("Некорректный размер списка: " + initialCapacity + ". Установлен размер по умолчанию: " + DEFAULT_CAPACITY);
            this.elements = new Person[DEFAULT_CAPACITY];
        } else {
            this.elements = new Person[initialCapacity];
        }
    }

    /**
     * Добавляет элемент в конец списка.
     * При нехватке места динамически расширяет внутренний массив в 2 раза.
     */
    public void add(Person person) {
        if (size == elements.length) {
            Person[] newElements = new Person[elements.length * 2];
            System.arraycopy(elements, 0, newElements, 0, elements.length);
            elements = newElements;
        }
        elements[size++] = person;
    }

    /**
     * Возвращает поток данных Stream.
     * Ограничивает обработку только заполненными элементами (игнорирует null-хвост массива).
     */
    public Stream<Person> stream() {
        return Arrays.stream(elements, 0, size);
    }


    /**
     * Сбрасывает состояние коллекции к первоначальному.
     */
    public void clear() {
        this.elements = new Person[DEFAULT_CAPACITY];
        this.size = 0;
    }

    /**
     * Безопасное получение элемента по индексу.
     * Корректирует любые ошибочные индексы под границы массива для предотвращения падения программы.
     */
    public Person get(int index) {
        // Защита от работы с пустой коллекцией
        if (size == 0) {
            System.out.println("Список пуст. Возвращен null.");
            return null;
        }
        // Коррекция отрицательного индекса — возвращается первый элемент
        if (index < 0) {
            System.out.println("Индекс меньше нуля: " + index + ". Возвращен первый элемент.");
            return elements[0];
        }
        // Коррекция индекса за пределами размера — возвращается последний добавленный элемент
        if (index >= size) {
            System.out.println("Индекс " + index + " за пределами размера (" + size + "). Возвращен последний элемент.");
            return elements[size - 1];
        }
        return elements[index];
    }

    /**
     * Безопасная перезапись элемента по индексу.
     * Предотвращает IndexOutOfBoundsException, принудительно записывая данные в валидные крайние ячейки.
     */
    public void set(int index, Person person) {
        // Защита от записи в пустую коллекцию
        if (size == 0) {
            System.out.println("Список пуст. Невозможно перезаписать элемент.");
            return;
        }
        // Коррекция отрицательного индекса — перезаписывается первый элемент
        if (index < 0) {
            System.out.println("Индекс меньше нуля: " + index + ". Перезаписан первый элемент.");
            elements[0] = person;
        } else if (index >= size) {
            // Коррекция индекса за пределами размера — перезаписывается последний добавленный элемент
            System.out.println("Индекс " + index + " за пределами размера (" + size + "). Перезаписан последний элемент.");
            elements[size - 1] = person;
        } else {
            // Штатная запись при валидном индексе
            elements[index] = person;
        }
    }

    /**
     * Возвращает текущее количество элементов в списке.
     */
    public int size() {
        return size;
    }
}
