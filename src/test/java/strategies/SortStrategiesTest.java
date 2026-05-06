package strategies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.javastudy.collections.PersonList;
import ru.javastudy.models.Person;
import ru.javastudy.strategies.EvenQuickSort;
import ru.javastudy.strategies.QuickSort;
import ru.javastudy.strategies.SortingStrategy;

import static org.junit.jupiter.api.Assertions.*;

public class SortStrategiesTest {
    private Person p1;
    private Person p2;
    private Person p3;
    private Person p4;

    @BeforeEach
    void setUp() {
        p1 = Person.builder().year(2005).lastName("Алексеев").firstName("Иван").build();
        p2 = Person.builder().year(1990).lastName("Борисов").firstName("Петр").build();
        p3 = Person.builder().year(1990).lastName("Антонов").firstName("Сергей").build();
        p4 = Person.builder().year(1990).lastName("Антонов").firstName("Андрей").build();
    }

    @Test
    void testStandardQuickSort() {
        PersonList list = new PersonList();
        list.add(p1);
        list.add(p2);
        list.add(p3);
        list.add(p4);

        SortingStrategy sorter = new QuickSort();
        sorter.sort(list);

        // Проверка каскада: 1990 < 2005. При равенстве годов: Антонов < Борисов.
        // При равенстве фамилий: Андрей < Сергей.
        assertEquals(p4, list.get(0));
        assertEquals(p3, list.get(1));
        assertEquals(p2, list.get(2));
        assertEquals(p1, list.get(3));
    }

    @Test
    void testEvenQuickSort() {
        PersonList list = new PersonList();

        // Создаем тестовую выборку с четными и нечетными годами
        Person odd1 = Person.builder().year(2001).lastName("Яковлев").firstName("А").build();  // Нечетный (0)
        Person even1 = Person.builder().year(2006).lastName("Яковлев").firstName("Б").build(); // Четный (1)
        Person odd2 = Person.builder().year(2003).lastName("Алексеев").firstName("В").build(); // Нечетный (2)
        Person even2 = Person.builder().year(2002).lastName("Аваков").firstName("Г").build();   // Четный (3)

        list.add(odd1);
        list.add(even1);
        list.add(odd2);
        list.add(even2);

        SortingStrategy sorter = new EvenQuickSort();
        sorter.sort(list);

        // Нечетные объекты остались ЖЕСТКО на своих исходных позициях (0 и 2)
        assertEquals(odd1, list.get(0));
        assertEquals(odd2, list.get(2));

        // Четные объекты отсортировались между собой (2002 стал на индекс 1, а 2006 ушел на индекс 3)
        assertEquals(even2, list.get(1));
        assertEquals(even1, list.get(3));
    }

    @Test
    void testSortEmptyAndSingleElementList() {
        PersonList emptyList = new PersonList();
        PersonList singleList = new PersonList();
        singleList.add(p1);

        SortingStrategy sorter = new QuickSort();

        // Проверяем, что не падает на пустой коллекции и коллекции из 1 элемента
        assertDoesNotThrow(() -> sorter.sort(emptyList));
        assertDoesNotThrow(() -> sorter.sort(singleList));

        assertEquals(0, emptyList.size());
        assertEquals(1, singleList.size());
        assertEquals(p1, singleList.get(0));
    }

    @Test
    void testSortAlreadySortedAndReversedList() {
        PersonList sortedList = new PersonList();
        sortedList.add(p3); // 1990
        sortedList.add(p1); // 2005

        SortingStrategy sorter = new QuickSort();
        sorter.sort(sortedList);

        // Проверяем, что уже отсортированный список остается корректным
        assertEquals(p3, sortedList.get(0));
        assertEquals(p1, sortedList.get(1));

        PersonList reversedList = new PersonList();
        reversedList.add(p1); // 2005
        reversedList.add(p3); // 1990

        sorter.sort(reversedList);

        // Проверяем разворот обратного списка
        assertEquals(p3, reversedList.get(0));
        assertEquals(p1, reversedList.get(1));
    }

    @Test
    void testSortWithIdenticalElements() {
        PersonList list = new PersonList();
        // Создаем три абсолютно одинаковых объекта
        Person identical1 = Person.builder().year(1995).lastName("Смирнов").firstName("Игорь").build();
        Person identical2 = Person.builder().year(1995).lastName("Смирнов").firstName("Игорь").build();
        Person identical3 = Person.builder().year(1995).lastName("Смирнов").firstName("Игорь").build();

        list.add(identical1);
        list.add(identical2);
        list.add(identical3);

        SortingStrategy sorter = new QuickSort();

        // Должен успешно завершиться, не зациклившись
        assertDoesNotThrow(() -> sorter.sort(list));
        assertEquals(3, list.size());
    }
}
