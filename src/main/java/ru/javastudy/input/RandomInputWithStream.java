package ru.javastudy.input;

import ru.javastudy.collections.PersonList;
import ru.javastudy.models.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Stream;
import java.util.stream.Collectors;

public class RandomInputWithStream implements InputStrategy {
    private final int size;

    public RandomInputWithStream(int size) {
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
    public void load(PersonList personList ) {
        List<Person> persons = new ArrayList<>();

        //Поставил стрим вместо цикла for

        persons = Stream.generate(this::generateRandomPerson)
                .limit(size)  // Берем только size элементов
                .collect(Collectors.toList());
        personList = new PersonList(persons);
    }
    //Новый метод для генерации одного случайного Person
    private Person generateRandomPerson() {
            String firstName = generateFirstName();
            String lastName = generateLastName();
            int year = 1900 + random.nextInt(127);

            return Person.builder()
                    .firstName(capitalize(firstName))
                    .lastName(capitalize(lastName))
                    .year(year)
                    .build();
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
