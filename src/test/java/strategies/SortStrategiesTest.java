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

    /**
     * Проверка стандартной сортировки: сначала год, при равенстве — фамилия, затем имя.
     */
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

    /**
     * Проверка хитрой сортировки: фиксации нечетных элементов на месте и сортировки четных.
     */
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

    /**
     * Проверка устойчивости к пустой коллекции и коллекции из одного элемента.
     */
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

    /**
     * Проверка стабильности на отсортированной и реверсивной коллекции.
     */
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

    /**
     * Проверка стабильности: корректная перестановка четных элементов с одинаковыми годами.
     * Сортировка перестраивает элементы по фамилии, не нарушая общую структуру индексов коллекции.
     */
    @Test
    void testEvenQuickSortWithIdenticalEvenYears() {
        PersonList list = new PersonList();
        Person odd1 = Person.builder().year(2001).lastName("Я").firstName("А").build();
        Person even1 = Person.builder().year(2002).lastName("Б").firstName("Б").build(); // Индекс 1
        Person odd2 = Person.builder().year(2003).lastName("В").firstName("В").build();
        Person even2 = Person.builder().year(2002).lastName("А").firstName("Г").build(); // Индекс 3

        list.add(odd1);
        list.add(even1);
        list.add(odd2);
        list.add(even2);

        SortingStrategy sorter = new EvenQuickSort();
        sorter.sort(list);

        // Нечетные жестко зафиксированы на позициях 0 и 2
        assertEquals(odd1, list.get(0));
        assertEquals(odd2, list.get(2));

        // Из-за приоритета фамилии элементы 2002 "А" и 2002 "Б" обязаны поменяться индексами
        assertEquals(even2, list.get(1));
        assertEquals(even1, list.get(3));
    }

    /**
     * Проверка защиты от бесконечной рекурсии на коллекции дубликатов.
     */
    @Test
    void testSortWithIdenticalElements() {
        PersonList list = new PersonList();
        Person identical1 = Person.builder().year(1995).lastName("Смирнов").firstName("Игорь").build();
        Person identical2 = Person.builder().year(1995).lastName("Смирнов").firstName("Игорь").build();
        Person identical3 = Person.builder().year(1995).lastName("Смирнов").firstName("Игорь").build();

        list.add(identical1);
        list.add(identical2);
        list.add(identical3);

        SortingStrategy sorter = new QuickSort();

        assertDoesNotThrow(() -> sorter.sort(list));
        assertEquals(3, list.size());
    }

    /**
     * Экстремальный стресс-тест: 1 000_000 элементов.
     * Проверка стабильности архитектуры и производительности на критических объемах данных.
     */
    @Test
    void testEvenQuickSortUltraStress() {
        int count = 1_000_000;
        PersonList list = new PersonList(count);

        // Буфер для эталонной проверки позиций
        int[] originalYears = new int[count];
        java.util.Random rnd = new java.util.Random(42); // Фиксированный сид для стабильности тестов

        for (int i = 0; i < count; i++) {
            int year;
            if (i % 2 == 0) {
                // Генерируем случайные ЧЕТНЫЕ года в диапазоне 1900-2026
                year = 1900 + rnd.nextInt(63) * 2;
            } else {
                // Генерируем случайные НЕЧЕТНЫЕ года
                year = 1901 + rnd.nextInt(63) * 2;
            }

            originalYears[i] = year;
            list.add(Person.builder()
                    .year(year)
                    .lastName("Фамилия")
                    .firstName("Имя")
                    .build());
        }

        SortingStrategy sorter = new EvenQuickSort();

        long start = System.currentTimeMillis();
        // Проверяем, что миллион элементов не роняет стек рекурсии
        assertDoesNotThrow(() -> sorter.sort(list));
        long end = System.currentTimeMillis();

        System.out.println("Обработка 1 000 000 элементов заняла: " + (end - start) + " мс");

        // ПРОВЕРКА 1: Общий размер не изменился
        assertEquals(count, list.size());

        // ПРОВЕРКА 2: Проверяем целостность структуры и правильность сортировки
        int lastEvenYear = -1;
        for (int i = 0; i < count; i++) {
            int currentYear = list.getFast(i).getYear();

            if (i % 2 != 0) {
                // Нечетные элементы обязаны остаться строго на своих исходных позициях!
                assertEquals(originalYears[i], currentYear, "Нечетный элемент на индексе " + i + " сместился!");
            } else {
                // Четные элементы обязаны идти строго по возрастанию!
                assertTrue(currentYear >= lastEvenYear, "Нарушен порядок сортировки четных элементов на индексе " + i);
                lastEvenYear = currentYear;
            }
        }
    }
}