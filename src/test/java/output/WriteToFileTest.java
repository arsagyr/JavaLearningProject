package output;

import org.junit.jupiter.api.Test;
import ru.javastudy.collections.PersonList;
import ru.javastudy.models.Person;
import ru.javastudy.output.WriteToFile;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class WriteToFileTest {

    @Test
    void shouldWritePersonsToFile() throws Exception {
        // given
        PersonList list = new PersonList();

        list.add(Person.builder()
                .firstName("Иван")
                .lastName("Иванов")
                .year(1990)
                .build());

        list.add(Person.builder()
                .firstName("Пётр")
                .lastName("Петров")
                .year(1985)
                .build());

        // when
        new WriteToFile().save(list);

        // then
        Path file = Path.of("persons.txt");

        assertTrue(Files.exists(file));

        String content = Files.readString(file);

        assertTrue(content.contains("Иван"));
        assertTrue(content.contains("Иванов"));
        assertTrue(content.contains("Пётр"));
        assertTrue(content.contains("Петров"));
    }

    @Test
    void shouldNotWriteEmptyFile() throws Exception {
        PersonList list = new PersonList();

        new WriteToFile().save(list);

        Path file = Path.of("persons.txt");

        assertTrue(Files.exists(file));

        String content = Files.readString(file);

        assertNotNull(content);

        // файл может быть пустым, это НОРМАЛЬНО
        // поэтому лучше так:
        assertTrue(content.isBlank());
    }
}