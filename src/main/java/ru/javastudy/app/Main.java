package ru.javastudy.app;

import ru.javastudy.models.Person;
import java.util.List;
import java.util.Scanner;

import ru.javastudy.input.InputStrategy;
import ru.javastudy.input.PersonInput;
import ru.javastudy.input.PersonLoader;
import ru.javastudy.input.RandomInput;
import ru.javastudy.input.ConsoleInput;
import ru.javastudy.output.WriteToFile;
import ru.javastudy.strategies.EvenQuickSort;
import ru.javastudy.strategies.QuickSort;
import ru.javastudy.collections.PersonList;
import ru.javastudy.threads.NCounter;

import ru.javastudy.input.ReadFromFile;



public class Main {

    public static void main(String[] args) {
        boolean isRunning = true;
        System.out.println("Приветствую! Это программа ввода данных пользователей по фамилии, имени и году рождения.");

        PersonList myPersons = new PersonList();
        Scanner scanner = new Scanner(System.in);

        while (isRunning) {
            System.out.println("Введите число:");
            System.out.println("1 - чтобы выбрать ввод данных");
            System.out.println("2 - чтобы выбрать способ сортировки данных");
            System.out.println("3 - чтобы вывести данные");
            System.out.println("4 - чтобы сохранить данные в файл");
            System.out.println("5 - чтобы сбросить список");
            System.out.println("6 - чтобы искать вхождения в списке");
            System.out.println("0 - чтобы остановить программу");

            String choice = scanner.next();
            scanner.nextLine();  

            switch (choice) {
                case "1":
                    chooseInput(scanner, myPersons);
                    break;
                case "2":
                    if (!myPersons.isEmpty()) chooseSort(scanner, myPersons);
                    else System.out.print("Заполните список\n");
                    break;
                case "3":
                    if (!myPersons.isEmpty()){
                        myPersons.stream().forEach(System.out::println);
                        System.out.println("\n" + myPersons.getTypicalPersonaDescription());
                        System.out.println("\n--- Вывод данных завершен ---");
                    }  
                    else System.out.print("Заполните список\n");
                    break;
                case "4":
                    if (!myPersons.isEmpty()) {
                        System.out.print("Введите имя файла для сохранения (по дефолту - 'persons.txt'): ");
                        String filename = scanner.nextLine().trim();
                        WriteToFile writer = new WriteToFile();
                        writer.save(myPersons, filename);  
                    }
                    else System.out.print("Заполните список\n");
                    break;
                case "5":
                    if (isSure(scanner)) {
                        myPersons = new PersonList();
                    }
                    break;
                case "6":
                    if (!myPersons.isEmpty()){
                        Person target;
                        int upToThreads = 1;
                        PersonInput personInput = new PersonInput(scanner);
                        target = personInput.load();
                        try { 
                            NCounter.countAndPrintOccurrence(myPersons, target, 1);
                        } 
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else System.out.print("Заполните список\n");
                    break;
                case "0":
                    System.out.println("Программа завершена.");
                    isRunning = false;
                    break;
                default:
                    System.out.println("Некорректный ввод. Попробуйте снова.");
            }
        }
        scanner.close();
    }

    public static int inputInt(Scanner scanner) { 
        System.out.print("Введите число людей: ");
        int size;
        
        while (true) {
            // Проверяем, что введено именно целое число
            while (!scanner.hasNextInt()) {
                System.err.print("Ошибка! Введите целое натуральное число: ");
                scanner.next(); // пропускаем некорректный ввод
            }
            
            size = scanner.nextInt();
            
            if (size  > 0) {
                if (size< 1000000) break;  
                else System.out.print("Ошибка! Число должно быть меньше миллиона): "); 
                } else {
                System.out.print("Ошибка! Число должно быть натуральным (больше 0): ");
            }
        }
        scanner.nextLine();
        return size;
    }

    public static void chooseInput(Scanner scanner, PersonList personList){ 
        System.out.println("Введите число:");
        System.out.println("1 - чтобы ввести данные вручную");
        System.out.println("2 - чтобы ввести данные случайно");
        System.out.println("3 - чтобы ввести данные по файлу");
        System.out.println("4 - чтобы ввести данные случайно через поток");

        System.out.println("Любой другой ввод - чтобы вернуться в меню");
        InputStrategy inputStrategy;
        String choice = scanner.next();
        scanner.nextLine();  
        int size;
        switch (choice) {
            case "1":
                size = inputInt(scanner);
                inputStrategy = new ConsoleInput(size, scanner);
                inputStrategy.load(personList);
                if (personList.size() > 0) {
                    System.out.println("Список успешно заполнен");
                }
                break;
            case "2":
                size = inputInt(scanner);
                inputStrategy = new RandomInput(size);
                inputStrategy.load(personList);
                break;
            case "3":
                inputStrategy = new ReadFromFile(scanner);
                inputStrategy.load(personList);     
                if (personList.size() > 0) {
                    System.out.println("Список успешно заполнен");    
                }                       
                break;
            case "4":
                size = inputInt(scanner);
                inputStrategy = new RandomInput(size);
                inputStrategy.load(personList);                                     
                break;
            default:
                break;
        }
    }

        public static void chooseSort(Scanner scanner, PersonList personList){ 
        System.out.println("Введите число:");
        System.out.println("1 - чтобы отсортировать данные");
        System.out.println("2 - чтобы отсортировать данные особым способом");

        System.out.println("Любой другой ввод - чтобы вернуться в меню");
        String choice = scanner.next();
        scanner.nextLine();  
        switch (choice) {
            case "1":
                if (personList != null && personList.size() > 0) {
                    System.out.println("\nЗапуск QuickSort (3 поля)...");
                    new QuickSort().sort(personList);
                    System.out.println("Готово.\n");
                } else {
                    System.out.println("Нечего сортировать.\n");
                }
                break;
            case "2":
                if (personList != null && personList.size() > 0) {
                    System.out.println("Запуск EvenQuickSort (четные года)...");
                    new EvenQuickSort().sort(personList);
                    System.out.println("Готово.");
                } else {
                    System.err.println("Нечего сортировать.");
                }
                break;
            default:
                break;
        }
    }

    public static boolean isSure(Scanner scanner) { 
        System.out.print("Вы уверены в сбросе? (yes/no): ");
        String answer = scanner.nextLine().trim().toLowerCase();
        
        // Fingerprint: проверка только конкретных вариантов подтверждения
        if (answer.equals("yes") || answer.equals("y") || answer.equals("да") || answer.equals("д")) {
            System.out.println("Операция подтверждена. Выполняется сброс...");
            return true;
        } else if (answer.equals("no") || answer.equals("n") || answer.equals("нет") || answer.equals("н")) {
            System.out.println("Операция отменена.");
            return false;
        } else {
            System.out.println("Неверный ввод. Пожалуйста, введите 'yes' или 'no'.");
            // Рекурсивный вызов для повторного запроса (fingerprint - защита от случайного нажатия)
            return isSure(scanner);
        }
    }
}
