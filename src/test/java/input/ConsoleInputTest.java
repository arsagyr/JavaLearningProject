package input;

import org.junit.jupiter.api.Test;
import ru.javastudy.collections.PersonList;
import ru.javastudy.input.ConsoleInput;
import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Набор тестов для автономного модуля ConsoleInput.
 * Имитирует поведение пользователя (включая ввод мусора) для проверки отказоустойчивости цикла.
 */
public class ConsoleInputTest {
    /**
     * Тест прохождения "сквозь мусор".
     * Имитируем ситуацию:
     * 1. Ввод фамилии: сначала цифры (ошибка), потом "Иванов" (ок).
     * 2. Ввод имени: "Иван" (ок).
     * 3. Ввод года: сначала буквы (ошибка), потом 1995 (ок).
     */
    @Test
    void testResilienceThroughGarbage() {
        String simulatedInput = "Ив@н0в\nИванов\nИван\nтыща-девятьсот\n1995\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        Scanner scanner = new Scanner(System.in);
        ConsoleInput input = new ConsoleInput(1, scanner);

        // Запуск цикла опроса
        PersonList result = input.read();

        // Проверка: система должна была проигнорировать ошибки и собрать 1 валидный объект
        assertEquals(1, result.size());
        assertEquals("Иванов", result.get(0).getLastName());
        assertEquals(1995, result.get(0).getYear());
    }

    /**
     * Тест динамического расширения кастомной коллекции (ТЗ 3*).
     * Проверка корректности работы внутреннего метода расширения массива при заполнении через консоль.
     */
    @Test
    void testCustomListExpansion() {
        // Вводим 3 человек. Проверка: выдержит ли PersonList превышение начальной емкости.
        String simulatedInput = "А\nА\n2000\nБ\nБ\n2001\nВ\nВ\n2002\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        ConsoleInput input = new ConsoleInput(3, new Scanner(System.in));
        PersonList result = input.read();

        // Проверка структурной целостности после расширения
        assertEquals(3, result.size());
        assertEquals("В", result.get(2).getLastName());
    }

    /**
     * Тест связки "Консоль-Билдер".
     * Проверка отсечения краевых артефактов, которые пропустил фильтр ConsoleInput, но должен срезать Билдер.
     */
    @Test
    void testEdgeCasesSanitization() {
        // Ввод с дефисом на конце (проходит через matches, но является мусором)
        String simulatedInput = "Иванов-\nИван\n1990\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        ConsoleInput input = new ConsoleInput(1, new Scanner(System.in));
        PersonList result = input.read();

        // Ожидаем: чистка Билдером до эталонного состояния
        assertEquals("Иванов", result.get(0).getLastName());
    }

    /**
     * Тест на аварийное прерывание входного потока.
     * Проверка стабильности системы, если ввод закончился раньше, чем достигнут заданный size.
     */
    @Test
    void testUnexpectedEndOfStream() {
        // Ожидаем 2-х людей, но даем данные только на одного
        String simulatedInput = "Смирнов\nИлья\n2005\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        ConsoleInput input = new ConsoleInput(2, new Scanner(System.in));

        // Система должна поймать ошибку отсутствия данных и не падать
        assertThrows(java.util.NoSuchElementException.class, input::read,
                "Должно быть выброшено исключение при внезапном окончании потока");
    }

    /**
     * Тест изоляции данных.
     * Гарантирует предотвращение утечки данных в поля следующего объекта.
     */

    @Test
    void testDataIsolation() {
        // Первый - ок. Второй - спровоцированный дефолт через "---" (пройдет консоль, срежется Билдером)
        String simulatedInput = "Иванов\nИван\n1990\n---\n---\n2000\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        ConsoleInput input = new ConsoleInput(2, new Scanner(System.in));
        PersonList result = input.read();

        assertEquals("Иванов", result.get(0).getLastName());
        assertEquals("Фамилия_Unknown", result.get(1).getLastName()); // Данные не протекли!
    }




}
