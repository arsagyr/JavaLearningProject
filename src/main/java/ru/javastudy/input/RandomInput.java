package ru.javastudy.input;

import ru.javastudy.models.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class RandomInput implements InputStrategy {
    private final Scanner scanner;
    private final int size;

    public RandomInput(int size,  Scanner scanner) {
        this.scanner = scanner;
        this.size = size;
    }


    private static final String[] SYLLABLES = {
            "мир", "бор", "дар", "вол", "гор",
            "род", "лад", "вод", "сол", "све",
            "зор", "зар", "яр",  "вла",
            "мор", "мол", "жар", "кар",
            "мак", "рак", "лак", "вал",
            "сар", "тан", "сан", "хор",
            "пер", "тер", "дер", "пор"
    };

    private static final String[] LASTNAME_ENDINGS = {"ов", "ев", "ин"};


    private final Random random = new Random();

    @Override
    public List<Person> load() {
        List<Person> persons = new ArrayList<>();

        for (int i = 0; i < size; i++) {

            String firstName = generateFirstName();
            String lastName = generateLastName();
            int year = 1900 + random.nextInt(127);

            persons.add(Person.builder()
                    .firstName(capitalize(firstName))
                    .lastName(capitalize(lastName))
                    .year(year)
                    .build());
        }
        
        return persons;
    }

    private String generateFirstName() {
        return generateWorld(2);
    }

    private String generateLastName() {
        return generateWorld(2) + LASTNAME_ENDINGS[random.nextInt(LASTNAME_ENDINGS.length)];
    }

    private String generateWorld(int syllablesCount) {
        StringBuilder word = new StringBuilder();
        for (int i = 0; i < syllablesCount; i++) {
            word.append(SYLLABLES[random.nextInt(SYLLABLES.length)]);
        }
        return word.toString();
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

}
