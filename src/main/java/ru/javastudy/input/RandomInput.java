package ru.javastudy.input;

import ru.javastudy.models.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class RandomInput implements InputStrategy {

    private static final String[] SYLLABLES = {
            "мир", "бор", "дар", "вол", "гор",
            "род", "лад", "вод", "сол", "све",
            "зор", "зар", "яр", "вла",
            "мор", "мол", "жар", "кар",
            "мак", "рак", "лак", "вал",
            "сар", "тан", "сан", "хор",
            "пер", "тер", "дер", "пор"
    };

    private static final String[] LASTNAME_ENDINGS = {
            "ов", "ев", "ин"
    };

    private final Random random = new Random();

    private final String[] FIRST_NAMES = generateFirstNames();
    private final String[] LAST_NAMES = generateLastNames();

    @Override
    public List<Person> load() {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите кол-во человек: ");
        int size = scanner.nextInt();

        if (size <= 0) {
            throw new IllegalArgumentException("Size must be positive");
        }

        List<Person> persons = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {

            String firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
            String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];

            int year = 1900 + random.nextInt(127);

            persons.add(new Person(
                    year,
                    lastName,
                    firstName
            ));
        }

        return persons;
    }

    private String[] generateFirstNames() {

        List<String> names = new ArrayList<>();

        for (String s1 : SYLLABLES) {
            for (String s2 : SYLLABLES) {

                names.add(capitalize(s1 + s2).intern());
            }
        }

        return names.toArray(new String[0]);
    }

    private String[] generateLastNames() {

        List<String> names = new ArrayList<>();

        for (String s1 : SYLLABLES) {
            for (String s2 : SYLLABLES) {
                for (String ending : LASTNAME_ENDINGS) {

                    names.add(
                            capitalize(s1 + s2 + ending).intern()
                    );
                }
            }
        }

        return names.toArray(new String[0]);
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        return Character.toUpperCase(str.charAt(0))
                + str.substring(1).toLowerCase();
    }
}