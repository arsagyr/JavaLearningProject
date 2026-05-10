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
        
        while (isRunning) {
            System.out.println("Введите число:");
            System.out.println("1 - чтобы ввести данные вручную");
            System.out.println("2 - чтобы ввести данные случайно");
            System.out.println("3 - чтобы ввести данные по файлу");
            System.out.println("4 - чтобы вывести данные");

            System.out.println("0 - чтобы остановить программу");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    inputStrategy = new ConsoleInput();
                    persons = inputStrategy.load();
                    isFull = true;
                    System.out.print("Список заполнен\n");
                    break;
                case 2:
                    inputStrategy = new RandomInput();
                    persons = inputStrategy.load();
                    isFull = true;
                    System.out.print("Список заполнен\n");
                    break;
                case 3:
                    inputStrategy = new ReadFromFile();
                    persons = inputStrategy.load();                                        
                    isFull = true;
                    System.out.print("Список заполнен\n");
                    break;
                case 4:
                    if (isFull){
                        persons.stream().forEach(System.out::println);
                    } else {
                        System.out.print("Список не был заполнен");
                    }
                    break;
                case 0:
                    System.out.println("Программа завершена.");
                    isRunning = false;
                    break;
                default:
                    System.out.println("Некорректный ввод. Попробуйте снова.");
            }
        }
        scanner.close();
    }


}
