package ru.javastudy.app;

import ru.javastudy.models.Person;
import java.util.List;
import java.util.Scanner;

import ru.javastudy.input.InputStrategy;
import ru.javastudy.input.PersonLoader;
import ru.javastudy.input.RandomInput;
import ru.javastudy.input.ConsoleInput;
import ru.javastudy.strategies.EvenQuickSort;
import ru.javastudy.strategies.QuickSort;
import ru.javastudy.collections.PersonList;

import ru.javastudy.input.ReadFromFile;



public class Main {

    public static void main(String[] args) {
        boolean isRunning = true, isFull = false;
        System.out.println("Приветствую! Это программа ввода данных пользователей по фамилии, имени и году рождения.");

        List<Person> persons = null;
        PersonList myPersons = new PersonList();
        Scanner scanner = new Scanner(System.in);

        while (isRunning) {
            System.out.println("Введите число:");
            System.out.println("1 - чтобы выбрать ввод данных");
            System.out.println("2 - чтобы выбрать способ сортировки данных");
            System.out.println("3 - чтобы вывести данные");
            System.out.println("4 - чтобы сохранить данные в файл");
            System.out.println("5 - чтобы сбросить список");
            System.out.println("0 - чтобы остановить программу");

            String choice = scanner.next();
            scanner.nextLine();  

            switch (choice) {
                case "1":
                    chooseInput(scanner, myPersons, isFull);
                    break;
                case "2":
                    if (isFull) chooseSort(scanner, myPersons);
                    else System.out.print("Заполните список\n");
                    break;
                case "3":
                    System.out.println("\n--- Вывод данных ---");
                    if ((persons != null)){
                        persons.stream().forEach(System.out::println);
                        System.out.println("\n--- Вывод данных завершен ---");
                    } else if (myPersons != null){
                        myPersons.stream().forEach(System.out::println);
                        System.out.println("\n--- Вывод данных завершен ---");
                    } else {
                        System.out.println("Список не был заполнен\n");
                    }
                    break;
                case "4":
                    if (isFull) {
                        //Здесь должна быть функция вывода в файл
                    }
                    else System.out.print("Заполните список\n");
                    break;
                case "5":
                    if (isSure(scanner)) {
                        persons = null;
                        myPersons = new PersonList();
                    }
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

    public static int inputInt(Scanner scanner){ 
        System.out.print("Введите число людей: ");
        int size = scanner.nextInt();
        if (size <= 0) {
            throw new IllegalArgumentException("Введите натуральное число");
        }
        scanner.nextLine();
        return size;
    }

    public static void chooseInput(Scanner scanner, PersonList personList, Boolean isFull){ 
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
                    isFull = true;
                }
                break;
            case "2":
                size = inputInt(scanner);
                inputStrategy = new RandomInput(size);
                inputStrategy.load(personList);
                isFull = true;
                break;
            case "3":
                inputStrategy = new ReadFromFile(scanner);
                inputStrategy.load(personList);     
                if (personList.size() > 0) {
                    System.out.println("Список успешно заполнен");    
                    isFull = true; 
                }                              
                break;
            case "4":
                size = inputInt(scanner);
                inputStrategy = new RandomInput(size);
                inputStrategy.load(personList);  
                isFull = true;                                      
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
