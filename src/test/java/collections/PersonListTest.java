package collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.javastudy.collections.PersonList;
import ru.javastudy.models.Person;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Набор тестов для проверки отказоустойчивой коллекции PersonList.
 * Документирует поведение системы при некорректных индексах и операциях с памятью.
 */
public class PersonListTest {
    private PersonList list;
    private Person p1;
    private Person p2;

    @BeforeEach
    void setUp() {
        // Инициализируем список с минимальной вместимостью для явной проверки расширения массива
        list = new PersonList(2);
        p1 = Person.builder().year(1995).lastName("Иванов").firstName("Иван").build();
        p2 = Person.builder().year(2000).lastName("Петров").firstName("Петр").build();
    }

    /**
     * Проверка базового добавления элементов и корректного расчёта размера size.
     */
    @Test
    void testAddAndSize() {
        list.add(p1);
        list.add(p2);
        assertEquals(2, list.size());
        assertEquals(p1, list.get(0));
    }

    /**
     * Тест механизма динамического перевыделения памяти при превышении initialCapacity.
     */
    @Test
    void testDynamicExtension() {
        list.add(p1);
        list.add(p2);
        Person p3 = Person.builder().year(1998).lastName("Сидоров").firstName("Сидор").build();
        assertDoesNotThrow(() -> list.add(p3));
        assertEquals(3, list.size());
    }

     /**
     * Проверка стабильности get() на пустой коллекции (граничные условия).
     */
    @Test
    void testGetOnEmptyList() {
        PersonList emptyList = new PersonList();
        assertNull(emptyList.get(0));
        assertNull(emptyList.get(-5));
    }

    /**
     * Тестирование защитной коррекции при передаче отрицательных индексов для get() и set().
     */
    @Test
    void testNegativeIndexHandling() {
        list.add(p1);
        list.add(p2);

        // Отрицательные индексы принудительно сглаживаются до 0
        assertEquals(p1, list.get(-1));
        assertEquals(p1, list.get(-100));

        Person p3 = Person.builder().year(1980).lastName("Смирнов").firstName("С").build();
        list.set(-1, p3); // Должен перезаписать элемент на позиции 0
        assertEquals(p3, list.get(0));
    }

    /**
     * Тестирование защитной коррекции при выходе за правые границы массива для get() и set().
     */
    @Test
    void testIndexOutOfBoundsHandling() {
        list.add(p1);
        list.add(p2);

        // get должен вернуть последний элемент
        assertEquals(p2, list.get(2));
        assertEquals(p2, list.get(10));

        // set должен перезаписать последний элемент
        Person p3 = Person.builder().year(1980).lastName("Смирнов").firstName("С").build();
        list.set(5, p3);
        assertEquals(p3, list.get(1));
    }

    /**
     * Проверка полной очистки коллекции и сброса счётчика size.
     */
    @Test
    void testClear() {
        list.add(p1);
        list.add(p2);

        list.clear();

        assertEquals(0, list.size());
        assertNull(list.get(0));
    }

    /**
     * Проверка интеграции со Stream API и фильтрации данных.
     */
    @Test
    void testStreamIntegration() {
        list.add(p1);
        list.add(p2);

        long count = list.stream().count();
        assertEquals(2, count);

        boolean hasP2 = list.stream().anyMatch(p -> p.getLastName().equals("Петров"));
        assertTrue(hasP2);
    }

    /**
     * Проверка защитного механизма конструктора при передаче некорректной емкости.
     */
    @Test
    void testConstructorWithInvalidCapacity() {
        PersonList invalidList = new PersonList(-10);

        // Счетчик элементов должен быть равен 0, а вызовы get() должны возвращать null
        assertEquals(0, invalidList.size());
        assertNull(invalidList.get(0));
    }

    /**
     * Проверка поведения метода set() при попытке записи в пустую коллекцию.
     */
    @Test
    void testSetOnEmptyList() {
        PersonList emptyList = new PersonList();
        Person p3 = Person.builder().year(1980).lastName("Смирнов").firstName("С").build();

        // Вызов метода не должен выбрасывать исключений, а размер коллекции должен остаться нулевым
        assertDoesNotThrow(() -> emptyList.set(0, p3));
        assertEquals(0, emptyList.size());
    }

    /**
     * Тестирование корректности расширения массива ровно в момент достижения лимита.
     * Проверяет, что переход через границу (с 10 на 11 элемент) не приводит к потере данных.
     */
    @Test
    void testExactCapacityBoundary() {
        PersonList dynamicList = new PersonList(10);
        // Заполняем список до дефолтного предела
        for (int i = 0; i < 10; i++) {
            dynamicList.add(p1);
        }

        // Добавление 11-го элемента должно триггернуть System.arraycopy
        assertDoesNotThrow(() -> dynamicList.add(p2));

        assertEquals(11, dynamicList.size());
        assertEquals(p2, dynamicList.get(10));
    }

    /**
     * Проверка работоспособности коллекции после выполнения операции clear().
     * Гарантирует, что после сброса состояния список пригоден для повторного наполнения.
     */
    @Test
    void testClearAndReuse() {
        list.add(p1);
        list.clear();

        // После очистки добавляем новый элемент в "нулевую" ячейку пересозданного массива
        list.add(p2);

        assertEquals(1, list.size());
        assertEquals(p2, list.get(0));
        // Проверяем, что данные p1 больше не доступны
        assertNotEquals(p1, list.get(0));
    }

    /**
     * Проверка очистки коллекции с установкой кастомной вместимости.
     * Гарантирует сброс счетчика и корректное перевыделение внутреннего массива под новый размер.
     */
    @Test
    void testClearWithCustomCapacity() {
        list.add(p1);

        int newCapacity = 100;
        list.clear(newCapacity);

        assertEquals(0, list.size());

        // Заполняем список до новой отметки 100
        // Если clear(100) не сработал, мы узнаем об этом по скорости или ошибкам внутри
        for (int i = 0; i < newCapacity; i++) {
            list.add(p1);
        }

        // Проверяем, что все 100 элементов на месте
        assertEquals(newCapacity, list.size());
        assertEquals(p1, list.get(newCapacity - 1));
    }

    /**
     * Проверка массового копирования данных из другой кастомной коллекции.
     * Подтверждает работу системного копирования и синхронизацию размера size между списками.
     */
    @Test
    void testAddAllFromAnotherList() {
        PersonList sourceList = new PersonList();
        sourceList.add(p1);
        sourceList.add(p2);

        // Копируем всё содержимое из sourceList в текущий список
        list.addAll(sourceList);

        assertEquals(sourceList.size(), list.size());
        assertEquals(p1, list.get(0));
        assertEquals(p2, list.get(1));
    }
}
