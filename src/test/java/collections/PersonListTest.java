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
     * Проверка защитного механизма get() при сильном превышении верхней границы индекса.
     */
    @Test
    void testSafeGetOutOfBounds() {
        list.add(p1);
        assertEquals(p1, list.get(5));
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

    /***
     * Доп. задание 3: заполнение кастомной коллекции посредством стрима
     */
    @Test
    void testPopulateCollectionUsingStreams() {
        Stream<Person> personStream = Stream.of(p1, p2);

        PersonList destinationList = new PersonList();

        personStream.forEach(destinationList::add);

        assertEquals(2, destinationList.size());
        assertEquals(p1, destinationList.get(0));
        assertEquals(p2, destinationList.get(1));
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
}
