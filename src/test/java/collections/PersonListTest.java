package collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.javastudy.collections.PersonList;
import ru.javastudy.models.Person;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class PersonListTest {
    private PersonList list;
    private Person p1;
    private Person p2;

    @BeforeEach
    void setUp() {
        list = new PersonList(2);
        p1 = Person.builder().year(1995).lastName("Иванов").firstName("Иван").build();
        p2 = Person.builder().year(2000).lastName("Петров").firstName("Петр").build();
    }

    @Test
    void testAddAndSize() {
        list.add(p1);
        list.add(p2);
        assertEquals(2, list.size());
        assertEquals(p1, list.get(0));
    }

    @Test
    void testDynamicExtension() {
        list.add(p1);
        list.add(p2);
        Person p3 = Person.builder().year(1998).lastName("Сидоров").firstName("Сидор").build();
        assertDoesNotThrow(() -> list.add(p3));
        assertEquals(3, list.size());
    }

    @Test
    void testSafeGetOutOfBounds() {
        list.add(p1);
        assertEquals(p1, list.get(5));
    }

    @Test
    void testGetOnEmptyList() {
        PersonList emptyList = new PersonList();
        assertNull(emptyList.get(0));
        assertNull(emptyList.get(-5));
    }

    @Test
    void testNegativeIndexHandling() {
        list.add(p1);
        list.add(p2);

        assertEquals(p1, list.get(-1));
        assertEquals(p1, list.get(-100));

        Person p3 = Person.builder().year(1980).lastName("Смирнов").firstName("С").build();
        list.set(-1, p3);
        assertEquals(p3, list.get(0));
    }

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

    @Test
    void testClear() {
        list.add(p1);
        list.add(p2);

        list.clear();

        assertEquals(0, list.size());
        assertNull(list.get(0));
    }

    @Test
    void testStreamIntegration() {
        list.add(p1);
        list.add(p2);

        long count = list.stream().count();
        assertEquals(2, count);

        boolean hasP2 = list.stream().anyMatch(p -> p.getLastName().equals("Петров"));
        assertTrue(hasP2);
    }

    @Test
    void testPopulateCollectionUsingStreams() {
        Stream<Person> personStream = Stream.of(p1, p2);

        PersonList destinationList = new PersonList();

        // Доп. задание 3: заполнение кастомной коллекции посредством стрима
        personStream.forEach(destinationList::add);

        assertEquals(2, destinationList.size());
        assertEquals(p1, destinationList.get(0));
        assertEquals(p2, destinationList.get(1));
    }
}
