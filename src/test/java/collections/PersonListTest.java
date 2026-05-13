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

    /**
     * Комплексный тест на одиночное и пакетное удаление элементов.
     * Покрывает граничные случаи, сдвиг памяти, защиту от null и сжатие массива (trimCapacity).
     */
    @Test
    void testComprehensiveRemovalAndCapacitySqueezing() {
        // Убеждаемся, что на пустой коллекции методы возвращают безопасный дефолт
        assertNull(list.remove(0));
        assertNull(list.remove(-10));

        // Наполняем список элементами (размер станет 6, емкость увеличится до 8)
        PersonList heavyList = new PersonList(2);
        Person extra1 = Person.builder().year(1990).lastName("А").firstName("А").build();
        Person extra2 = Person.builder().year(1991).lastName("Б").firstName("Б").build();
        Person extra3 = Person.builder().year(1992).lastName("В").firstName("В").build();
        Person extra4 = Person.builder().year(1993).lastName("Г").firstName("Г").build();
        heavyList.add(p1);      // 0
        heavyList.add(p2);      // 1
        heavyList.add(extra1);  // 2
        heavyList.add(extra2);  // 3
        heavyList.add(extra3);  // 4
        heavyList.add(extra4);  // 5

        // Тестируем сглаживание некорректных индексов при удалении
        assertEquals(p1, heavyList.remove(-5));        // Сгладит в 0, удалит p1
        assertEquals(extra4, heavyList.remove(100));    // Сгладит в последний, удалит extra4

        // Проверяем, что массив сдвинулся корректно и дыр нет
        assertEquals(p2, heavyList.get(0));
        assertEquals(extra3, heavyList.get(heavyList.size() - 1));

        // Защита от null и отсутствующих элементов
        assertFalse(heavyList.remove((Object) null));

        Person stranger = Person.builder().year(1900).lastName("Чужой").firstName("Ч").build();
        assertFalse(heavyList.remove(stranger));

        // Пакетное удаление removeAll
        java.util.List<Person> toRemove = java.util.List.of(p2, extra2);
        assertTrue(heavyList.removeAll(toRemove));

        // Проверяем остаток и автоматическую отмену при null-коллекции
        assertFalse(heavyList.removeAll(null));
        assertEquals(2, heavyList.size());
        assertEquals(extra1, heavyList.get(0));
        assertEquals(extra3, heavyList.get(1));
    }

    /**
     * Комплексный тест на метод фильтрации retainAll (работа в логике rewrite).
     * Проверяет фильтрацию за один проход, защиту от null и полное пересоздание хранилища.
     */
    @Test
    void testRetainAllRewriteLogic() {
        list.add(p1);
        list.add(p2);
        Person p3 = Person.builder().year(1988).lastName("Попов").firstName("П").build();
        list.add(p3);

        // Передача null-коллекции должна заблокировать изменения
        assertFalse(list.retainAll(null));
        assertEquals(3, list.size());

        // Оставляем p1 и p3, удаляя p2
        java.util.List<Person> target = java.util.List.of(p1, p3);
        assertTrue(list.retainAll(target));

        // Проверяем, что массив полностью перезаписался, а хвост занулен
        assertEquals(2, list.size());
        assertEquals(p1, list.get(0));
        assertEquals(p3, list.get(1));

        // Если совпадений нет, коллекция не должна мутировать
        assertFalse(list.retainAll(target));
    }

    /**
     * Комплексный тест на извлечение подсписка subList.
     * Проверяет сглаживание кривых/перепутанных индексов и копирование без двойного жора памяти.
     */
    @Test
    void testSubListBoundaryHandlingAndCopying() {
        list.add(p1);
        list.add(p2);

        // Тест 13: Перепутанные или вылетевшие за край индексы
        java.util.List<Person> sub1 = list.subList(-10, 100); // Отрежет строго от 0 до size
        assertEquals(2, sub1.size());
        assertEquals(p1, sub1.get(0));
        assertEquals(p2, sub1.get(1));

        java.util.List<Person> sub2 = list.subList(2, 0); // Конечный меньше начального -> сбросит end в start
        assertEquals(0, sub2.size());
        assertTrue(sub2.isEmpty());
    }

    /**
     * Комплексный тест на выгрузку в массивы через оба метода toArray.
     * Проверяет работу без рефлексии, обработку null-ссылок и автоматическое выделение Person[].
     */
    @Test
    void testToArray() {
        list.add(p1);
        list.add(p2);

        // Базовый toArray()
        Object[] objects = list.toArray();
        assertEquals(2, objects.length);
        assertEquals(p1, objects[0]);

        // Передача null массива в параметризованный toArray
        Person[] nullHandled = list.toArray((Person[]) null);
        assertNotNull(nullHandled);
        assertEquals(2, nullHandled.length);

        // Передача массива меньшего размера (должен выделиться новый Person[] без ClassCastException)
        Person[] smallArray = new Person[0];
        Person[] result = list.toArray(smallArray);

        assertEquals(2, result.length);
        assertEquals(p1, result[0]);
        assertEquals(p2, result[1]);

        // Передача массива большего размера (проверка зануления элемента за хвостом)
        Person[] bigArray = new Person[5];
        list.toArray(bigArray);
        assertNull(bigArray[2]);
    }

    /**
     * Глобальный тест на пуленепробиваемый ListIterator и шаблон EMPTY_PERSON.
     * Проверяет полный проход итератора, маркер ошибки, модификации через set/add/remove и блокировку двойного удаления.
     */
    @Test
    void testListIteratorWithNullObjectPattern() {
        list.add(p1);
        list.add(p2);

        java.util.ListIterator<Person> it = list.listIterator();

        // Проверка сглаживания при инициализации с кривым индексом
        java.util.ListIterator<Person> badIt = list.listIterator(-5);
        assertTrue(badIt.hasNext());

        // Проход до упора
        assertEquals(p1, it.next());
        assertEquals(p2, it.next());

        // Лишний вызов next() после окончания данных (должен выдать EMPTY_PERSON)
        Person errorPerson = it.next();
        assertNotNull(errorPerson);
        assertEquals("Выход", errorPerson.getLastName());
        assertEquals("ЗаПределы", errorPerson.getFirstName());
        assertEquals(1899, errorPerson.getYear());

        // Модификации через итератор
        java.util.ListIterator<Person> modifyIt = list.listIterator();
        assertEquals(p1, modifyIt.next());

        // Тестируем iterator.set()
        Person p3 = Person.builder().year(1990).lastName("Тестовый").firstName("Т").build();
        modifyIt.set(p3);
        assertEquals(p3, list.get(0));

        // Тестируем двойной remove() подряд без промежуточного next() (должен заблокировать операцию)
        modifyIt.remove(); // Удалит первый элемент (p3), размер станет 1
        assertEquals(1, list.size());

        // Повторный вызов без next() вызовет ошибку в лог, но не уронит программу
        assertDoesNotThrow(modifyIt::remove);
        assertEquals(1, list.size()); // Размер не изменился, данные не повредились
    }

}
