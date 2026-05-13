package ru.javastudy.collections;

import ru.javastudy.models.Person;

import java.util.*;
import java.util.stream.Stream;


public class PersonList implements List<Person> {
    // Внутренний массив для хранения элементов
    private Person[] elements;
    // Количество фактически добавленных элементов
    private int size = 0;
    // Вместимость массива по умолчанию
    private static final int DEFAULT_CAPACITY = 10;
    // Маркер ошибки
    private static final Person EMPTY_PERSON = Person.builder()
            .lastName("Выход")
            .firstName("ЗаПределы")
            .year(1899) // Наш маркерный год для битых данных
            .build();


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
            System.err.println("Некорректный размер списка: " + initialCapacity + ". Установлен размер по умолчанию: " + DEFAULT_CAPACITY);
            this.elements = new Person[DEFAULT_CAPACITY];
        } else {
            this.elements = new Person[initialCapacity];
        }
    }

    /**
     * Внутренний метод автоматического контроля емкости хранилища.
     * Защищает систему от переполнения, удваивая физический размер массива при достижении лимита.
     */
    private void ensureCapacity() {
        if (size == elements.length) {
            Person[] newElements = new Person[elements.length * 2];
            System.arraycopy(elements, 0, newElements, 0, elements.length);
            elements = newElements;
        }
    }

    /**
     * Добавляет элемент в конец списка.
     * Использует централизованный метод контроля емкости и возвращает true для подтверждения успешного изменения коллекции.
     */
    @Override
    public boolean add(Person person) {
        ensureCapacity();
        elements[size++] = person;
        return true;
    }

    /**
     * Безопасное добавление элемента по указанному индексу.
     * Автоматически расширяет массив при необходимости и сглаживает некорректные индексы под границы списка.
     */
    @Override
    public void add(int index, Person element) {
        ensureCapacity();

        int targetIndex = index;
        if (size == 0) {
            System.err.println("Список пуст. Элемент добавлен на позицию 0.");
            targetIndex = 0;
        } else if (index < 0) {
            System.err.println("Индекс вставки меньше нуля: " + index + ". Элемент вставлен в начало списка.");
            targetIndex = 0;
        } else if (index > size) {
            System.err.println("Индекс вставки " + index + " превышает размер (" + size + "). Элемент добавлен в конец списка.");
            targetIndex = size;
        }

        // Выполняем сдвиг элементов вправо для освобождения целевой ячейки
        System.arraycopy(elements, targetIndex, elements, targetIndex + 1, size - targetIndex);
        elements[targetIndex] = element;
        size++;
    }

    /**
     * Внутренний метод автоматического контроля емкости хранилища.
     * Проверяет наличие свободных ячеек и принудительно расширяет массив в 2 раза
     * (или под точный размер большой пачки данных), защищая систему от переполнения.
     */
    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            int newCapacity = Math.max(elements.length * 2, minCapacity);
            Person[] newElements = new Person[newCapacity];
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;
        }
    }

    /**
     * Безопасное добавление коллекции элементов в конец списка.
     */
    @Override
    public boolean addAll(java.util.Collection<? extends Person> c) {
        return addAll(size, c);
    }

    /**
     * Безопасное добавление коллекции элементов начиная с указанного индекса.
     * Cглаживает некорректные индексы под границы массива,
     * упреждающе готовит память и переносит всю пачку данных в один миг.
     */
    @Override
    public boolean addAll(int index, java.util.Collection<? extends Person> c) {
        if (c == null || c.isEmpty()) {
            System.err.println("Переданная коллекция для addAll пуста или равна null. Изменений нет.");
            return false;
        }

        int targetIndex = index;
        if (size == 0) {
            System.err.println("Список пуст. Коллекция addAll добавлена с индекса 0.");
            targetIndex = 0;
        } else if (index < 0) {
            System.err.println("Индекс вставки коллекции меньше нуля: " + index + ". Вставлено в начало.");
            targetIndex = 0;
        } else if (index > size) {
            System.err.println("Индекс вставки " + index + " превышает размер (" + size + "). Вставлено в конец.");
            targetIndex = size;
        }

        int numNew = c.size();
        ensureCapacity(size + numNew);

        // Сдвигаем существующие элементы вправо
        int numMoved = size - targetIndex;
        if (numMoved > 0) {
            System.arraycopy(elements, targetIndex, elements, targetIndex + numNew, numMoved);
        }

        // Копируем всю коллекцию в один шаг
        Object[] a = c.toArray();
        System.arraycopy(a, 0, elements, targetIndex, numNew);

        // Корректно увеличиваем размер списка
        size += numNew;
        return true;
    }

    /**
     * Быстрое заполнение коллекции данными из другого массива.
     * Использует системное копирование памяти для максимальной производительности.
     */
    public void rewrite(PersonList newElements) {
        if (newElements == null || newElements.size() == 0) return;

        // Если новый список больше нашего текущего массива — расширяемся
        ensureCapacity(newElements.size());

        // Копируем ИЗ массива другого объекта В наш массив
        System.arraycopy(newElements.elements, 0, this.elements, 0, newElements.size());
        this.size = newElements.size();
    }

    /**
     * Внутренний метод оптимизации памяти коллекции.
     * Защищает систему от удержания лишней памяти, автоматически сжимая массив,
     * если количество элементов стало меньше половины текущей емкости.
     */
    private void trimCapacity() {
        if (elements.length > DEFAULT_CAPACITY && size < elements.length / 2) {
            int newCapacity = Math.max(DEFAULT_CAPACITY, size);
            Person[] trimmedElements = new Person[newCapacity];
            System.arraycopy(elements, 0, trimmedElements, 0, size);
            elements = trimmedElements;
        }
    }

    /**
     * Безопасный поиск первого индекса вхождения элемента.
     * Защищает от передачи null с выводом предупреждения в консоль и находит точную позицию объекта.
     */
    @Override
    public int indexOf(Object o) {
        if (o == null) {
            System.err.println("Элемент в коллекции не найден.");
            return -1;
        }
        for (int i = 0; i < size; i++) {
            if (elements[i].equals(o)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Безопасный поиск последнего индекса вхождения элемента.
     * Сканирует массив с конца.
     */
    @Override
    public int lastIndexOf(Object o) {
        if (o == null) {
            System.err.println("Элемент в коллекции не найден");
            return -1;
        }
        for (int i = size - 1; i >= 0; i--) {
            if (elements[i].equals(o)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Безопасное удаление конкретного объекта из списка.
     * Защищает от передачи null, проверяет наличие объекта через indexOf
     * и отменяет операцию с выводом ошибки в консоль, если объект не найден.
     */
    @Override
    public boolean remove(Object o) {
        if (o == null) {
            System.err.println("Попытка удаления null объекта. Операция отменена.");
            return false;
        }

        int index = indexOf(o);
        if (index == -1) {
            System.err.println("Объект не найден в списке для удаления: " + o);
            return false;
        }

        remove(index);
        return true;
    }

    /**
     * Безопасное удаление элемента по индексу со сдвигом массива.
     * Корректирует неверные индексы под крайние границы, исключая падение.
     */
    @Override
    public Person remove(int index) {
        if (size == 0) {
            System.err.println("Удаление невозможно: список пуст.");
            return null;
        }

        int targetIndex = index;
        if (index < 0) {
            System.err.println("Индекс меньше нуля: " + index + ". Удален первый элемент.");
            targetIndex = 0;
        } else if (index >= size) {
            System.err.println("Индекс " + index + " за пределами размера (" + size + "). Удален последний элемент.");
            targetIndex = size - 1;
        }

        Person removed = elements[targetIndex];
        int numMoved = size - targetIndex - 1;
        if (numMoved > 0) {
            System.arraycopy(elements, targetIndex + 1, elements, targetIndex, numMoved);
        }
        elements[--size] = null;

        trimCapacity();
        return removed;
    }

    /**
     * Безопасное пакетное удаление группы элементов из списка.
     * Защищает от передачи null-коллекции и последовательно вычищает совпадения.
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) {
            System.err.println("Переданная коллекция для removeAll равна null. Операция отменена.");
            return false;
        }
        boolean modified = false;
        for (int i = 0; i < size; i++) {
            if (c.contains(elements[i])) {
                remove(i);
                i--; // Корректируем указатель назад из-за сдвига элементов
                modified = true;
            }
        }
        return modified;
    }

    /**
     * Сбрасывает состояние коллекции к первоначальному.
     */
    public void clear() {
        this.elements = new Person[DEFAULT_CAPACITY];
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
     * Безопасное сохранение в списке только тех элементов, которые содержатся в переданной коллекции.
     * Защищает от null коллекции, фильтрует за один проход через буфер и оптимизирует память через trimCapacity.
     */
    @Override
    public boolean retainAll(java.util.Collection<?> c) {
        if (c == null) {
            System.err.println("Переданная коллекция равна null. Очистка списка заблокирована.");
            return false;
        }

        Person[] retained = new Person[elements.length];
        int newSize = 0;

        // В один проход копируем только то, что нужно оставить
        for (int i = 0; i < size; i++) {
            if (elements[i] != null && c.contains(elements[i])) {
                retained[newSize++] = elements[i];
            }
        }

        if (newSize == size) {
            return false;
        }

        // Перезаписываем внутреннее хранилище отфильтрованными данными.
        this.elements = retained;
        this.size = newSize;
        trimCapacity();

        return true;
    }

    /**
     * Безопасная проверка наличия элемента в списке.
     * Возвращает false при передаче null и информирует об этом, предотвращая NullPointerException.
     */
    @Override
    public boolean contains(Object o) {
        if (o == null) {
            System.err.println("Попытка поиска null элемента. Возвращено false.");
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (elements[i] != null && elements[i].equals(o)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Безопасная проверка наличия коллекции элементов.
     * Предотвращает падение при передаче null-коллекции или null-элементов, возвращая false.
     */
    @Override
    public boolean containsAll(java.util.Collection<?> c) {
        if (c == null) {
            System.err.println("Переданная коллекция для проверки containsAll равна null. Возвращено false.");
            return false;
        }
        for (Object e : c) {
            if (e == null) {
                System.err.println("В проверяемой коллекции обнаружен null элемент. Возвращено false.");
                return false;
            }
            if (!contains(e)) {
                return false;
            }
        }
        return true;
    }
    /**
     * Возвращает безопасный итератор для последовательного обхода элементов списка.
     * Предотвращает падение программы, возвращая null при выходе за пределы заполненных данных.
     */
    @Override
    public Iterator<Person> iterator() {
        return listIterator(0);
    }

    /**
     * Возвращает двунаправленный списочный итератор с начальной позиции 0.
     */
    @Override
    public ListIterator<Person> listIterator() {
        return listIterator(0);
    }

    /**
     * Возвращает безопасный двунаправленный списочный итератор с указанной позиции.
     * Корректирует ошибочный начальный индекс под реальные границы заполненного массива
     * и переиспользует безопасные методы удаления и перезаписи класса.
     */
    @Override
    public ListIterator<Person> listIterator(int index) {
        int targetIndex;
        if (index < 0) {
            System.err.println("Начальный индекс меньше нуля. Сброшено на 0.");
            targetIndex = 0;
        } else if (index > size) {
            System.err.println("Начальный индекс превышает размер (" + size + "). Установлено в конец списка.");
            targetIndex = size;
        } else {
            targetIndex = index;
        }

        return new ListIterator<Person>() {
            private int cursor = targetIndex;
            private int lastRet = -1;

            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            @Override
            public Person next() {
                if (!hasNext()) {
                    System.err.println("Достигнут конец списка. Возвращен маркер об ошибки.");
                    return EMPTY_PERSON;
                }
                lastRet = cursor;
                return elements[cursor++];
            }

            @Override
            public boolean hasPrevious() {
                return cursor > 0;
            }

            @Override
            public Person previous() {
                if (!hasPrevious()) {
                    System.err.println("Достигнуто начало списка.  Возвращен маркер об ошибки.");
                    return EMPTY_PERSON;
                }
                lastRet = --cursor;
                return elements[cursor];
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
                if (lastRet < 0) {
                    System.err.println("Удаление невозможно, не было вызова next() или previous().");
                    return;
                }
                PersonList.this.remove(lastRet);
                cursor = lastRet;
                lastRet = -1;
            }

            @Override
            public void set(Person person) {
                if (lastRet < 0) {
                    System.err.println("Замена невозможна, не было вызова next() или previous().");
                    return;
                }
                PersonList.this.set(lastRet, person);
            }

            @Override
            public void add(Person person) {
                PersonList.this.add(cursor++, person);
                lastRet = -1;
            }
        };
    }

    /**
     * Возвращает поток данных Stream.
     * Ограничивает обработку только заполненными элементами (игнорирует null-хвост массива).
     */
    public Stream<Person> stream() {
        return Arrays.stream(elements, 0, size);
    }

    /**
     * Безопасное извлечение subList в границах от fromIndex до toIndex.
     * Выравнивает ошибочные индексы под реальный размер коллекции для защиты от падений.
     */
    @Override
    public List<Person> subList(int fromIndex, int toIndex) {
        int start;
        if (fromIndex < 0) {
            System.err.println("Начальный индекс меньше нуля (" + fromIndex + "). Индекс сброшен на 0.");
            start = 0;
        } else if (fromIndex > size) {
            System.err.println("Начальный индекс превышает размер (" + fromIndex + "). Индекс установлено в " + size + ".");
            start = size;
        } else {
            start = fromIndex;
        }

        int end;
        if (toIndex < start) {
            System.err.println("Конечный индекс (" + toIndex + ") меньше начального (" + start + "). Индекс сброшен на " + start + ".");
            end = start;
        } else if (toIndex > size) {
            System.err.println("Конечный индекс превышает размер (" + toIndex + "). Индекс установлено в " + size + ".");
            end = size;
        } else {
            end = toIndex;
        }

        int subSize = end - start;
        PersonList sub = new PersonList(subSize);

        if (subSize > 0) {
            System.arraycopy(this.elements, start, sub.elements, 0, subSize);
            sub.size = subSize;
        }

        return sub;
    }

    /**
     * Безопасное получение элемента по индексу.
     * Корректирует любые ошибочные индексы под границы массива для предотвращения падения программы.
     */
    public Person get(int index) {
        // Защита от работы с пустой коллекцией
        if (size == 0) {
            System.err.println("Список пуст. Возвращен null.");
            return null;
        }
        // Коррекция отрицательного индекса — возвращается первый элемент
        if (index < 0) {
            System.err.println("Индекс меньше нуля: " + index + ". Возвращен первый элемент.");
            return elements[0];
        }
        // Коррекция индекса за пределами размера — возвращается последний добавленный элемент
        if (index >= size) {
            System.err.println("Индекс " + index + " за пределами размера (" + size + "). Возвращен последний элемент.");
            return elements[size - 1];
        }
        return elements[index];
    }

    /**
     * Безопасное получение элемента по индексу.
     * Корректирует любые ошибочные индексы под границы массива для предотвращения падения программы.
     * Необходим для обхода виртуальной машины для ускорения сортировки
     */
    public Person getFast(int index) {
        // Защита от работы с пустой коллекцией
        if (size == 0) {
            System.err.println("Список пуст. Возвращен null.");
            return null;
        }
        // Коррекция отрицательного индекса — возвращается первый элемент
        if (index < 0) {
            System.err.println("Индекс меньше нуля: " + index + ". Возвращен первый элемент.");
            return elements[0];
        }
        // Коррекция индекса за пределами размера — возвращается последний добавленный элемент
        if (index >= size) {
            System.err.println("Индекс " + index + " за пределами размера (" + size + "). Возвращен последний элемент.");
            return elements[size - 1];
        }
        return elements[index];
    }

    /**
     * Безопасная перезапись элемента по индексу.
     * Предотвращает IndexOutOfBoundsException, принудительно записывая данные в валидные крайние ячейки.
     */
    public Person set(int index, Person person) {
        // Защита от записи в пустую коллекцию
        if (size == 0) {
            System.err.println("Список пуст. Невозможно перезаписать элемент.");
            return null;
        }
        // Коррекция отрицательного индекса — перезаписывается первый элемент
        if (index < 0) {
            System.err.println("Индекс меньше нуля: " + index + ". Перезаписан первый элемент.");
            elements[0] = person;
        } else if (index >= size) {
            // Коррекция индекса за пределами размера — перезаписывается последний добавленный элемент
            System.err.println("Индекс " + index + " за пределами размера (" + size + "). Перезаписан последний элемент.");
            elements[size - 1] = person;
        } else {
            // Штатная запись при валидном индексе
            elements[index] = person;
        }
        return null;
    }

    /**
     * Безопасная перезапись элемента по индексу.
     * Предотвращает IndexOutOfBoundsException, принудительно записывая данные в валидные крайние ячейки.
     * Необходим для обхода виртуальной машины для ускорения сортировки
     */
    public Person setFast(int index, Person person) {
        // Защита от записи в пустую коллекцию
        if (size == 0) {
            System.err.println("Список пуст. Невозможно перезаписать элемент.");
            return null;
        }
        // Коррекция отрицательного индекса — перезаписывается первый элемент
        if (index < 0) {
            System.err.println("Индекс меньше нуля: " + index + ". Перезаписан первый элемент.");
            elements[0] = person;
        } else if (index >= size) {
            // Коррекция индекса за пределами размера — перезаписывается последний добавленный элемент
            System.err.println("Индекс " + index + " за пределами размера (" + size + "). Перезаписан последний элемент.");
            elements[size - 1] = person;
        } else {
            // Штатная запись при валидном индексе
            elements[index] = person;
        }
        return null;
    }

    /**
     * Преобразует коллекцию в стандартный массив объектов.
     * Возвращает чистый массив строго фактической длины, используя быстрое системное копирование памяти.
     */
    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        System.arraycopy(elements, 0, result, 0, size);
        return result;
    }

    /**
     * Заполнение переданного массива данными из списка.
     * Защищает программу от падения при передаче null-ссылки со своевременным информированием.
     */
    @Override
    public <T> T[] toArray(T[] a) {
        if (a == null || a.length < size) {
            if (a == null) {
                System.err.println("Передан null массив для выгрузки. Возвращен новый массив Person[].");
            } else {
                System.err.println("Длина переданного массива меньше размера списка. Создан новый массив Person[].");
            }
            Person[] newArray = new Person[size];
            System.arraycopy(elements, 0, newArray, 0, size);
            return (T[]) newArray;
        }

        System.arraycopy(elements, 0, a, 0, size);

        if (a.length > size) {
            a[size] = null;
        }

        return a;
    }

    /**
     * Возвращает текущее фактическое количество элементов в списке.
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Проверяет, пуст ли список.
     */
    @Override
    public boolean isEmpty() {
        return (size == 0);
    }
}