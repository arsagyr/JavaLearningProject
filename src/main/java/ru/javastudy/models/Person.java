package ru.javastudy.models;

import java.util.regex.Pattern;

public class Person {
    private int year;      // год рождения
    private String lastName;    // фамилия
    private String firstName;   // имя

    private Person(Builder builder) {
        this.year = builder.year;
        this.lastName = builder.lastName;
        this.firstName = builder.firstName;
    }

    public Person() {
    }

    public Person(int year, String lastName, String firstName) {
        this.year = year;
        this.lastName = lastName;
        this.firstName = firstName;
    }

    public int getYear() {
        return year;
    }
    
    public void setBirthYear(int year) {
        this.year = year;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    // Статический метод для получения экземпляра Builder
    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(lastName).append(" ").append(firstName).append(" ");
        sb.append(year);
        return sb.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Person person = (Person) obj;
        return year == person.year &&
                lastName.equals(person.lastName) &&
                firstName.equals(person.firstName);
    }
    
    @Override
    public int hashCode() {
        int result = year;
        result = 31 * result + lastName.hashCode();
        result = 31 * result + firstName.hashCode();
        return result;
    }

    public static class Builder {
        private int year;
        private String lastName;
        private String firstName;

        // Строгая проверка: начинается с буквы, разделители (пробел/дефис) только внутри
        private static final Pattern VALID_NAME_PATTERN =
                Pattern.compile("^[а-яА-ЯёЁ]+(?:[\\s-][а-яА-ЯёЁ]+)*$");
        // Фильтр для удаления всего, кроме разрешенного
        private static final Pattern INVALID_CHAR_FILTER =
                Pattern.compile("[^а-яА-ЯёЁ\\s-]");
        // Удаление повторяющихся пробелов и дефисов
        private static final Pattern MULTI_SEPARATOR_FILTER =
                Pattern.compile("[\\s-]{2,}");

        /**
         * Установка года с проверкой диапазона 1900-2026.
         * При нарушении границ срабатывает "магнитный" год (1989) для сохранения стабильности системы.
         * и сбора невалидных данных по году в одном месте для возможного анализа
         */
        public Person.Builder year(int year) {
            if (year < 1900 || year > 2026) {
                System.err.println("\nГод " + year + " вне диапазона (1900 - 2026). Установлено: 1989.");
                this.year = 1989;
            } else {
                this.year = year;
            }
            return this;
        }

        public Person.Builder lastName(String lastName) {
            this.lastName = normalize(lastName, "Фамилия");
            return this;
        }

        public Person.Builder firstName(String firstName) {
            this.firstName = normalize(firstName, "Имя");
            return this;
        }

        /**
         * Метод нормализации входных данных.
         * Гарантирует получение валидных данных даже при подаче мусора на вход.
         * Если чистка не оставляет букв — возвращает дефолтное значение
         * для каждого поля (1989, Фамилия_Unknown, Имя_Unknown).
         */
        private String normalize(String value, String type) {
            if (value == null || value.trim().isEmpty()) {
                System.err.println("\n" + type + " пуста. Установлено: " + type + "_Unknown.");
                return type + "_Unknown";
            }

            String result = value.trim();

            // Если строка не проходит строгую проверку — чистим
            if (!VALID_NAME_PATTERN.matcher(result).matches()) {
                System.err.println("\nКорректировка поля " + type + " [" + value + "].");
                // Удаляем мусор
                result = INVALID_CHAR_FILTER.matcher(result).replaceAll("");
                // Удаляем двойные пробелы/дефисы
                result = MULTI_SEPARATOR_FILTER.matcher(result).replaceAll(" ").trim();
                // Удаляем дефисы и пробелы по краям, которые могли там остаться или появиться после чистки.
                        result = result.replaceAll("^[\\s-]+|[\\s-]+$", "");
                // Проверяем, осталась ли хотя бы одна буква
                if (result.isEmpty() || !result.matches(".*[а-яА-ЯёЁ].*")) {
                    System.err.println("\n" + type + " не содержит букв. Установлено: " + type + "_Unknown.");
                    return type + "_Unknown";
                }
            }
            return result;
        }

        // Метод для создания объекта Person
        public Person build() {
            return new Person(this);
        }
    }
}