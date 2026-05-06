package ru.javastudy.collections;

import ru.javastudy.models.Person;
import java.util.Arrays;
import java.util.stream.Stream;

public class PersonList {
    private Person[] elements;
    private int size = 0;
    private static final int DEFAULT_CAPACITY = 10;

    public PersonList() {
        this.elements = new Person[DEFAULT_CAPACITY];
    }

    public PersonList(int initialCapacity) {
        if (initialCapacity <= 0) {
            System.out.println("Некорректный размер списка: " + initialCapacity + ". Установлен размер по умолчанию: " + DEFAULT_CAPACITY);
            this.elements = new Person[DEFAULT_CAPACITY];
        } else {
            this.elements = new Person[initialCapacity];
        }
    }

    public void add(Person person) {
        if (size == elements.length) {
            Person[] newElements = new Person[elements.length * 2];
            System.arraycopy(elements, 0, newElements, 0, elements.length);
            elements = newElements;
        }
        elements[size++] = person;
    }

    public Stream<Person> stream() {
        return Arrays.stream(elements, 0, size);
    }

    public void clear() {
        this.elements = new Person[DEFAULT_CAPACITY];
        this.size = 0;
    }

    public Person get(int index) {
        if (size == 0) {
            System.out.println("Список пуст. Возвращен null.");
            return null;
        }
        if (index < 0) {
            System.out.println("Индекс меньше нуля: " + index + ". Возвращен первый элемент.");
            return elements[0];
        }
        if (index >= size) {
            System.out.println("Индекс " + index + " за пределами размера (" + size + "). Возвращен последний элемент.");
            return elements[size - 1];
        }
        return elements[index];
    }

    public void set(int index, Person person) {
        if (size == 0) {
            System.out.println("Список пуст. Невозможно перезаписать элемент.");
            return;
        }
        if (index < 0) {
            System.out.println("Индекс меньше нуля: " + index + ". Перезаписан первый элемент.");
            elements[0] = person;
        } else if (index >= size) {
            System.out.println("Индекс " + index + " за пределами размера (" + size + "). Перезаписан последний элемент.");
            elements[size - 1] = person;
        } else {
            elements[index] = person;
        }
    }

    public int size() {
        return size;
    }
}
