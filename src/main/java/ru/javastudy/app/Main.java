package ru.javastudy.app;

import ru.javastudy.models.Person;
import java.util.List;
import java.util.Scanner;

import ru.javastudy.input.InputStrategy;
import ru.javastudy.input.PersonLoader;
import ru.javastudy.input.RandomInput;
import ru.javastudy.input.ConsoleInput;

import ru.javastudy.input.ReadFromFile;



public class Main {

    public static void main(String[] args) {
        boolean isRunning = true, isFull = false;
        System.out.println("Приветствую! Это программа ввода данных пользователей по фамилии, имени и году рождения.");

        List<Person> persons = null;
        Scanner scanner = new Scanner(System.in);
        InputStrategy inputStrategy;
        int size;

        while (isRunning) {
            System.out.println("Введите число:");
            System.out.println("1 - чтобы ввести данные вручную");
            System.out.println("2 - чтобы ввести данные случайно");
            System.out.println("3 - чтобы ввести данные по файлу");
            System.out.println("4 - чтобы вывести данные");
            System.out.println("5 - чтобы отсортировать данные");

            System.out.println("0 - чтобы остановить программу");

            String choice = scanner.next();
            scanner.nextLine();  

            switch (choice) {
                case "1":
                    size = inputInt(scanner);
                    inputStrategy = new ConsoleInput(size, scanner);
                    persons = inputStrategy.load();
                    isFull = true;
                    System.out.print("Список заполнен\n");
                    break;
                case "2":
                    size = inputInt(scanner);
                    inputStrategy = new RandomInput(size, scanner);
                    persons = inputStrategy.load();
                    isFull = true;
                    System.out.print("Список заполнен\n");
                    break;
                case "3":
                    inputStrategy = new ReadFromFile(scanner);
                    persons = inputStrategy.load();                                        
                    isFull = true;
                    System.out.print("Список заполнен\n");
                    break;
                case "4":
                    if (isFull){
                        persons.stream().forEach(System.out::println);
                    } else {
                        System.out.print("Список не был заполнен\n");
                    }
                    break;
                case "5":
                    if (isFull){
                        //Функция сортировки
                    } else {
                        System.out.print("Список не был заполнен\n");
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

}
