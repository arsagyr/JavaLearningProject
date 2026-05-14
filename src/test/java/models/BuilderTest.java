package models;

import org.junit.jupiter.api.Test;
import ru.javastudy.models.Person;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Набор стресс-тестов для проверки отказоустойчивости Builder.
 * Документирует работу механизмов нормализации данных и защиты от некорректного ввода.
 */
public class BuilderTest {
    /**
     * Проверка "магнитного" года.
     * Граничные условия 1900-2026. При выходе за пределы — принудительный сброс в 1989.
     */
    @Test
    void testYearRangeProtection() {
        // Тест верхней границы
        Person future = Person.builder().year(2027).build();
        assertEquals(1899, future.getYear());

        // Тест нижней границы
        Person ancient = Person.builder().year(1899).build();
        assertEquals(1899, ancient.getYear());

        // Валидный год должен пройти без изменений
        Person valid = Person.builder().year(2022).build();
        assertEquals(2022, valid.getYear());
    }

    /**
     * Тест механизма очистки строк.
     * Проверка удаления спецсимволов и цифр при сохранении допустимых разделителей.
     */
    @Test
    void testStringSanitization() {
        Person dirty = Person.builder()
                .lastName("  Ив@н0в!!  ")
                .firstName("П-е_т.р")
                .build();

        // Ожидаем удаление мусора и обрезку пробелов (trim)
        assertEquals("Ивнв", dirty.getLastName());
        assertEquals("П-етр", dirty.getFirstName());
    }

    /**
     * Защита от Null и пустых строк (NPE Protection).
     * Проверяет выживаемость до вызова .trim().
     */
    @Test
    void testNullAndEmptyInputHandling() {
        Person p = Person.builder()
                .lastName(null)
                .firstName("   ")
                .build();

        assertEquals("Фамилия_Unknown", p.getLastName());
        assertEquals("Имя_Unknown", p.getFirstName());
    }

    /**
     * Проверка защиты от пустых результатов после чистки.
     * Гарантирует подстановку Default Value при отсутствии букв в исходном вводе.
     */
    @Test
    void testEmptyResultHandling() {
        Person garbage = Person.builder()
                .lastName("12345")
                .firstName("?!@#")
                .build();

        assertEquals("Фамилия_Unknown", garbage.getLastName());
        assertEquals("Имя_Unknown", garbage.getFirstName());
    }

    /**
     * Проверка MLTI_SEPARATOR_FILTER:
     * куча пробелов или дефисов должна превращаться в одиночный пробел.
     */
    @Test
    void testMultiSeparatorCollapsing() {
        Person p = Person.builder()
                .lastName("Салтыков---Щедрин")
                .firstName("Иван   Иванович")
                .build();

        // Ожидаем принудительное приведение к единичному пробелу между словами
        assertEquals("Салтыков Щедрин", p.getLastName());
        assertEquals("Иван Иванович", p.getFirstName());
    }

    /**
     * Тест на изоляцию Unicode.
     * Гарантирует, что в систему попадет только кириллица или дефолтное значение.
     */
    @Test
    void testForeignLanguageIsolation() {
        Person p = Person.builder()
                .lastName("Ivanov")        // Латиница -> Фамилия_Unknown (букв а-я нет)
                .firstName("Иван😊")        // Смайлик -> Иван
                .build();

        assertEquals("Фамилия_Unknown", p.getLastName());
        assertEquals("Иван", p.getFirstName());
    }

    /**
     * Тест очистки краевых артефактов.
     * Проверка, что после удаления мусора и trim() по краям не остается дефисов или пробелов.
     */
    @Test
    void testEdgeGarbageCleaning() {
        Person p = Person.builder()
                .lastName("-Иванов-")
                .firstName("123Петр456")
                .build();

        // VALID_NAME_PATTERN блокирует дефис в начале/конце, чистка должна оставить только буквы
        assertEquals("Иванов", p.getLastName());
        assertEquals("Петр", p.getFirstName());
    }

    /**
     * Тест на Инвариантность (повторный вызов).
     * Гарантирует, что последний вызов перезаписывает предыдущий и валидируется заново.
     */
    @Test
    void testInvariance() {
        Person p = Person.builder()
                .lastName("Иванов")
                .lastName("Петров123") // Должен перезаписать и отвалидировать
                .build();

        assertEquals("Петров", p.getLastName());
    }
}
