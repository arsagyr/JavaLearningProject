package ru.javastudy.collections;

import ru.javastudy.models.Person;
import java.util.*;
import java.util.stream.Stream;

public class PersonList implements List<Person> {
    // Внутренний массив для хранения элементовq
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
    // Конструктор создающий из спика кастомный класс
    public PersonList(List<Person> persons) {
        this.elements = persons.toArray(new Person[0]);

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
    @Override
    public boolean add(Person person) {
        if (size == elements.length) {
            Person[] newElements = new Person[elements.length * 2];
            System.arraycopy(elements, 0, newElements, 0, elements.length);
            elements = newElements;
        }
        elements[size++] = person;
        return true;
    }

    /**
     * Быстрое заполнение коллекции данными из другого массива.
     * Использует системное копирование памяти для максимальной производительности.
     */
    public void addAll(PersonList newElements) {
        if (newElements == null || newElements.size() == 0) return;

        // Если новый список больше нашего текущего массива — расширяемся
        if (newElements.size() > this.elements.length){
            this.elements = new Person[newElements.size()];
        }

        // Копируем ИЗ массива другого объекта В наш массив
        System.arraycopy(newElements.elements, 0, this.elements, 0, newElements.size());
        this.size = newElements.size();
    }

    @Override
    public boolean addAll(Collection<? extends Person> c) {
        if (c == null || c.isEmpty()) return false;
        
        int newSize = size + c.size();
        ensureCapacity(newSize);
        
        for (Person person : c) {
            elements[size++] = person;
        }
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends Person> c) {
        if (c == null || c.isEmpty()) return false;
        
        checkIndexForAdd(index);
        
        int numNew = c.size();
        ensureCapacity(size + numNew);
        
        // Сдвигаем элементы вправо
        System.arraycopy(elements, index, elements, index + numNew, size - index);
        
        // Вставляем новые элементы
        int i = index;
        for (Person person : c) {
            elements[i++] = person;
        }
        
        size += numNew;
        return true;
    }

    @Override
    public void add(int index, Person element) {
        checkIndexForAdd(index);
        
        ensureCapacity(size + 1);
        
        // Сдвигаем элементы вправо
        System.arraycopy(elements, index, elements, index + 1, size - index);
        
        elements[index] = element;
        size++;
    }

    /**
     * Возвращает поток данных Stream.
     * Ограничивает обработку только заполненными элементами (игнорирует null-хвост массива).
     */
    @Override
    public Stream<Person> stream() {
        return Arrays.stream(elements, 0, size);
    }

    /**
     * Сбрасывает состояние коллекции к первоначальному.
     */
    @Override
    public void clear() {
        Arrays.fill(elements, 0, size, null);
        this.size = 0;
    }

    /**
     * Сбрасывает состояние коллекции с установкой новой вместимости.
     * Позволяет избежать многократного расширения массива при заполнении данными
     * известного объема (например, после промежуточной обработки).
     */
    public void clear(int capacity) {
        this.elements = new Person[capacity];
        this.size = 0;
    }

    /**
     * Безопасное получение элемента по индексу.
     * Корректирует любые ошибочные индексы под границы массива для предотвращения падения программы.
     */
    @Override
    public Person get(int index) {
        Objects.checkIndex(index, size);
        return elements[index];
    }

    /**
     * Безопасная перезапись элемента по индексу.
     * Предотвращает IndexOutOfBoundsException, принудительно записывая данные в валидные крайние ячейки.
     */
    @Override
    public Person set(int index, Person person) {
        Objects.checkIndex(index, size);
        Person oldValue = elements[index];
        elements[index] = person;
        return oldValue;
    }

    /**
     * Возвращает текущее количество элементов в списке.
     */
    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public Iterator<Person> iterator() {
        return new Iterator<Person>() {
            private int cursor = 0;
            
            @Override
            public boolean hasNext() {
                return cursor < size;
            }
            
            @Override
            public Person next() {
                if (!hasNext()) throw new NoSuchElementException();
                return elements[cursor++];
            }
        };
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(elements, size);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            return (T[]) Arrays.copyOf(elements, size, a.getClass());
        }
        System.arraycopy(elements, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index >= 0) {
            remove(index);
            return true;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object item : c) {
            if (!contains(item)) return false;
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (int i = 0; i < size; ) {
            if (c.contains(elements[i])) {
                remove(i);
                modified = true;
            } else {
                i++;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        for (int i = 0; i < size; ) {
            if (!c.contains(elements[i])) {
                remove(i);
                modified = true;
            } else {
                i++;
            }
        }
        return modified;
    }

    @Override
    public Person remove(int index) {
        Objects.checkIndex(index, size);
        Person oldValue = elements[index];
        
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elements, index + 1, elements, index, numMoved);
        }
        
        elements[--size] = null;
        return oldValue;
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(elements[i], o)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            if (Objects.equals(elements[i], o)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public ListIterator<Person> listIterator() {
        return listIterator(0);
    }

    @Override
    public ListIterator<Person> listIterator(int index) {
        Objects.checkIndex(index, size + 1);
        return new ListIterator<Person>() {
            private int cursor = index;
            
            @Override
            public boolean hasNext() {
                return cursor < size;
            }
            
            @Override
            public Person next() {
                if (!hasNext()) throw new NoSuchElementException();
                return elements[cursor++];
            }
            
            @Override
            public boolean hasPrevious() {
                return cursor > 0;
            }
            
            @Override
            public Person previous() {
                if (!hasPrevious()) throw new NoSuchElementException();
                return elements[--cursor];
            }
            
            @Override
            public int nextIndex() {
                return cursor;
            }
            
            @Override
            public int previousIndex() {
                return cursor - 1;
            }
            
            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public void set(Person person) {
                PersonList.this.set(cursor - 1, person);
            }
            
            @Override
            public void add(Person person) {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public List<Person> subList(int fromIndex, int toIndex) {
        Objects.checkFromToIndex(fromIndex, toIndex, size);
        
        return new AbstractList<Person>() {
            @Override
            public Person get(int index) {
                Objects.checkIndex(index, toIndex - fromIndex);
                return PersonList.this.get(fromIndex + index);
            }
            
            @Override
            public int size() {
                return toIndex - fromIndex;
            }
        };
    }

    // Вспомогательные методы
    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            int newCapacity = Math.max(elements.length * 2, minCapacity);
            elements = Arrays.copyOf(elements, newCapacity);
        }
    }
    
    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }
}