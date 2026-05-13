package input;

import org.junit.jupiter.api.Test;
import ru.javastudy.collections.PersonList;
import ru.javastudy.input.RandomInput;
import ru.javastudy.models.Person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RandomInputTest {

    @Test
    void shouldGeneratePersons() {

        int size = 5;

        RandomInput randomInput = new RandomInput(size);
        PersonList persons = new PersonList();

        randomInput.load(persons);

        assertEquals(size, persons.size());
    }

    @Test
    void shouldGenerateValidPersons() {
        int size = 20;
        PersonList list = new PersonList();

        new RandomInput(size).load(list);

        for (int i = 0; i < list.size(); i++) {
            Person p = list.get(i);

            assertNotNull(p.getFirstName());
            assertNotNull(p.getLastName());

            assertFalse(p.getFirstName().isEmpty());
            assertFalse(p.getLastName().isEmpty());

            assertEquals(
                    Character.toUpperCase(p.getFirstName().charAt(0)),
                    p.getFirstName().charAt(0)
            );

            assertEquals(
                    Character.toUpperCase(p.getLastName().charAt(0)),
                    p.getLastName().charAt(0)
            );

            assertTrue(p.getYear() >= 1900 && p.getYear() <= 2026);
        }
    }
}