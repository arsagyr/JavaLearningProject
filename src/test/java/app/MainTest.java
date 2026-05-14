package app;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.javastudy.collections.PersonList;
import ru.javastudy.models.Person;
import ru.javastudy.app.Main;


import java.io.*;
import java.nio.file.Path;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    private ByteArrayOutputStream outContent;
    private ByteArrayOutputStream errContent;
    private PrintStream originalOut;
    private PrintStream originalErr;

    @BeforeEach
    void setUp() {
        // Перенаправляем вывод для проверки
        outContent = new ByteArrayOutputStream();
        errContent = new ByteArrayOutputStream();
        originalOut = System.out;
        originalErr = System.err;
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    void tearDown() {
        // Восстанавливаем стандартный вывод
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    void testInputInt_ValidInput() {
        // Подготовка
        String input = "5\n";
        Scanner scanner = new Scanner(input);
        
        // Действие
        int result = Main.inputInt(scanner);
        
        // Проверка
        assertEquals(5, result);
    }

    @Test
    void testInputInt_InvalidThenValidInput() {
        // Подготовка: сначала некорректный ввод, затем корректный
        String input = "-3\n0\n10\n";
        Scanner scanner = new Scanner(input);
        
        // Действие
        int result = Main.inputInt(scanner);
        
        // Проверка
        assertEquals(10, result);
        String output = outContent.toString();
        assertTrue(output.contains("Ошибка! Число должно быть натуральным"));
    }

    @Test
    void testInputInt_NonNumericThenValid() {
        // Подготовка: сначала буквы, затем число
        String input = "abc\n-5\n7\n";
        Scanner scanner = new Scanner(input);
        
        // Действие
        int result = Main.inputInt(scanner);
        
        // Проверка
        assertEquals(7, result);
        assertTrue(errContent.toString().contains("Ошибка! Введите целое натуральное число"));
    }

    @Test
    void testChooseInput_ConsoleInput() {
        // Подготовка: выбор консольного ввода
        String input = "1\n2\nИван\nИванов\n1990\nМария\nПетрова\n1995\n";
        Scanner scanner = new Scanner(input);
        PersonList personList = new PersonList();
        
        // Действие
        Main.chooseInput(scanner, personList);
        
        // Проверка
        assertEquals(2, personList.size());
        assertEquals("Иванов", personList.get(0).getFirstName());
        assertEquals("Иван", personList.get(0).getLastName());
        assertEquals("Петрова", personList.get(1).getFirstName());
        assertEquals("Мария", personList.get(1).getLastName());
    }

    @Test
    void testChooseInput_RandomInput() {
        // Подготовка: выбор случайного ввода
        String input = "2\n3\n";
        Scanner scanner = new Scanner(input);
        PersonList personList = new PersonList();
        
        // Действие
        Main.chooseInput(scanner, personList);
        
        // Проверка: список должен содержать 3 случайных Person
        assertEquals(3, personList.size());
    }

    @Test
    void testChooseInput_InvalidChoice() {
        // Подготовка: неверный выбор
        String input = "99\n";
        Scanner scanner = new Scanner(input);
        PersonList personList = new PersonList();
        personList.add(new Person(2000, "Тест", "Тестов"));
        int originalSize = personList.size();
        
        // Действие
        Main.chooseInput(scanner, personList);
        
        // Проверка: список не изменился
        assertEquals(originalSize, personList.size());
    }

    @Test
    void testChooseInput_ReadFromFile(@TempDir Path tempDir) throws IOException {
        // Подготовка: создаем временный файл с данными
        Path testFile = tempDir.resolve("test_persons.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile.toFile()))) {
            writer.write("Алексей Смирнов 1985");
            writer.newLine();
            writer.write("Елена Козлова 1990");
            writer.newLine();
            writer.write("Дмитрий Соколов 1995");
            writer.newLine();
        }
        
        String input = "3\n" + testFile.toString() + "\n";
        Scanner scanner = new Scanner(input);
        PersonList personList = new PersonList();
        
        // Действие
        Main.chooseInput(scanner, personList);
        
        // Проверка
        assertEquals(3, personList.size());
        assertEquals("Смирнов", personList.get(0).getFirstName());
        assertEquals("Алексей", personList.get(0).getLastName());
    }

    @Test
    void testChooseSort_QuickSort() {
        // Подготовка
        PersonList personList = new PersonList();
        personList.add(new Person(2000, "Волков", "Сергей"));
        personList.add(new Person(1990, "Морозова", "Анна"));
        personList.add(new Person(1995, "Новикова", "Ольга"));
        
        String input = "1\n";
        Scanner scanner = new Scanner(input);
        
        // Действие
        Main.chooseSort(scanner, personList);
        
        // Проверка: сортировка по фамилии, затем по имени, затем по году
        assertEquals("Морозова", personList.get(0).getLastName());
        assertEquals("Новикова", personList.get(1).getLastName());
        assertEquals("Волков", personList.get(2).getLastName());
        
        String output = outContent.toString();
        assertTrue(output.contains("Запуск QuickSort"));
    }

    @Test
    void testChooseSort_EvenQuickSort() {
        // Подготовка
        PersonList personList = new PersonList();
        personList.add(new Person(2001, "Федоров", "Павел")); // нечетный
        personList.add(new Person(2000, "Егорова", "Татьяна")); // четный
        personList.add(new Person(1998, "Павлов", "Николай")); // четный
        personList.add(new Person(2003, "Михайлова", "Светлана")); // нечетный
        
        String input = "2\n";
        Scanner scanner = new Scanner(input);
        
        // Действие
        Main.chooseSort(scanner, personList);
        
        // Проверка
        String output = outContent.toString();
        assertTrue(output.contains("Запуск EvenQuickSort"));
        assertTrue(output.contains("Готово"));
    }

    @Test
    void testChooseSort_EmptyList() {
        // Подготовка
        PersonList emptyList = new PersonList();
        String input = "1\n";
        Scanner scanner = new Scanner(input);
        
        // Действие
        Main.chooseSort(scanner, emptyList);
        
        // Проверка: сообщение об ошибке
        String output = outContent.toString();
        assertTrue(output.contains("Нечего сортировать"));
    }

    @Test
    void testIsSure_YesConfirmation() {
        // Подготовка
        String input = "yes\n";
        Scanner scanner = new Scanner(input);
        
        // Действие
        boolean result = Main.isSure(scanner);
        
        // Проверка
        assertTrue(result);
        String output = outContent.toString();
        assertTrue(output.contains("Операция подтверждена"));
    }

    @Test
    void testIsSure_NoConfirmation() {
        // Подготовка
        String input = "no\n";
        Scanner scanner = new Scanner(input);
        
        // Действие
        boolean result = Main.isSure(scanner);
        
        // Проверка
        assertFalse(result);
        String output = outContent.toString();
        assertTrue(output.contains("Операция отменена"));
    }

    @Test
    void testIsSure_YesVariants() {
        // Проверка разных вариантов "да"
        assertTrue(Main.isSure(new Scanner("y\n")));
        assertTrue(Main.isSure(new Scanner("да\n")));
        assertTrue(Main.isSure(new Scanner("д\n")));
        assertTrue(Main.isSure(new Scanner("YES\n")));
        assertTrue(Main.isSure(new Scanner("ДА\n")));
    }

    @Test
    void testIsSure_NoVariants() {
        // Проверка разных вариантов "нет"
        assertFalse(Main.isSure(new Scanner("n\n")));
        assertFalse(Main.isSure(new Scanner("нет\n")));
        assertFalse(Main.isSure(new Scanner("н\n")));
        assertFalse(Main.isSure(new Scanner("NO\n")));
        assertFalse(Main.isSure(new Scanner("НЕТ\n")));
    }

    @Test
    void testIsSure_InvalidThenValid() {
        // Подготовка: сначала неверный ввод, затем "yes"
        String input = "maybe\nyes\n";
        Scanner scanner = new Scanner(input);
        
        // Действие
        boolean result = Main.isSure(scanner);
        
        // Проверка
        assertTrue(result);
        String output = outContent.toString();
        assertTrue(output.contains("Неверный ввод"));
        assertTrue(output.contains("Операция подтверждена"));
    }

    @Test
    void testMainFlow_AddPersonsAndDisplay() {
        // Подготовка: полный сценарий работы
        String input = "1\n1\n2\nИван\nИванов\n1990\nМария\nПетрова\n1995\n3\n0\n";
        InputStream stdin = System.in;
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        // Действие
        Main.main(new String[]{});
        
        // Проверка: вывод содержит данные о пользователях
        String output = outContent.toString();
        assertTrue(output.contains("Иван"));
        assertTrue(output.contains("Иванов"));
        assertTrue(output.contains("Мария"));
        assertTrue(output.contains("Петрова"));
        assertTrue(output.contains("1990"));
        assertTrue(output.contains("1995"));
        
        // Восстановление
        System.setIn(stdin);
    }

    @Test
    void testMainFlow_SaveToFile(@TempDir Path tempDir) {
        // Подготовка: сохраняем в файл во временной директории
        String filename = tempDir.resolve("test_output.txt").toString();
        String input = "1\n2\n2\n4\n" + filename + "\n0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        // Действие
        Main.main(new String[]{});
        
        // Проверка: файл создан и содержит данные
        File file = new File(filename);
        assertTrue(file.exists());
        assertTrue(file.length() > 0);
        
        // Восстановление
        System.setIn(System.in);
    }

    @Test
    void testMainFlow_ClearListConfirmation() {
        // Подготовка: добавляем элементы, затем сбрасываем с подтверждением
        String input = "1\n2\n2\n5\nyes\n3\n0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        // Действие
        Main.main(new String[]{});
        
        // Проверка: после сброса список пуст
        String output = outContent.toString();
        assertTrue(output.contains("Операция подтверждена"));
        assertTrue(output.contains("Список успешно заполнен") || output.contains("Заполните список"));
        
        // Восстановление
        System.setIn(System.in);
    }

    @Test
    void testMainFlow_InvalidMenuChoice() {
        // Подготовка: неверный выбор в меню
        String input = "99\n0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        // Действие
        Main.main(new String[]{});
        
        // Проверка: сообщение о некорректном вводе
        String output = outContent.toString();
        assertTrue(output.contains("Некорректный ввод"));
        
        // Восстановление
        System.setIn(System.in);
    }

    @Test
    void testMainFlow_AddMultiplePersons() {
        // Подготовка: добавляем 3 человека
        String input = "1\n1\n3\nАлексей\nСмирнов\n1985\nЕлена\nКозлова\n1990\nДмитрий\nСоколов\n1995\n3\n0\n";
        InputStream stdin = System.in;
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        // Действие
        Main.main(new String[]{});
        
        // Проверка
        String output = outContent.toString();
        assertTrue(output.contains("Алексей"));
        assertTrue(output.contains("Смирнов"));
        assertTrue(output.contains("Елена"));
        assertTrue(output.contains("Козлова"));
        assertTrue(output.contains("Дмитрий"));
        assertTrue(output.contains("Соколов"));
        
        // Восстановление
        System.setIn(stdin);
    }
}