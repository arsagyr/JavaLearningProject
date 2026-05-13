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
        InputStrategy inputStrategy;
        int size;

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
                    chooseInput(scanner, myPersons);
                    isFull = true;
                    System.out.print("Список заполнен\n");
                    break;
                case "2":
                chooseSort(scanner, myPersons);
                    break;
                case "3":
                    System.out.println("\n--- Вывод данных ---");
                    if ((persons != null)){
                        persons.stream().forEach(System.out::println);
                        System.out.println("\n--- Вывод данных заввершен ---");
                    } else if (myPersons != null){
                        myPersons.stream().forEach(System.out::println);
                        System.out.println("\n--- Вывод данных заввершен ---");
                    } else {
                        System.out.println("Список не был заполнен\n");
                    }
                    break;
                case "4":
                    //Здесь должна быть функция вывода в файл
                    break;
                case "5":
                    persons = null;
                    myPersons = new PersonList();
                     System.out.println("Список сброшен");
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

    public static void chooseInput(Scanner scanner, PersonList personList){ 
        System.out.println("Введите число:");
        System.out.println("1 - чтобы ввести данные вручную");
        System.out.println("2 - чтобы ввести данные случайно");
        System.out.println("3 - чтобы ввести данные по файлу");
        System.out.println("4 - чтобы ввести данные случайно через поток");

        System.out.println("0 - чтобы остановить программу");
        InputStrategy inputStrategy;
        String choice = scanner.next();
        scanner.nextLine();  
        int size;
        switch (choice) {
            case "1":
                size = inputInt(scanner);
                inputStrategy = new ConsoleInput(size, scanner);
                inputStrategy.load(personList);
                if (personList.size() > 0) System.out.println("Список успешно заполнен.");
                break;
            case "2":
                size = inputInt(scanner);
                inputStrategy = new RandomInput(size);
                inputStrategy.load(personList);
                break;
            case "3":
                inputStrategy = new ReadFromFile(scanner);
                inputStrategy.load(personList);                                        
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

        System.out.println("0 - чтобы остановить программу");
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
}
