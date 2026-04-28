package ru.javastudy.models;

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
        return "Person{" +
                "lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", year=" + year +
                '}';
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
        
        public Builder year(int year) {
            this.year = year;
            return this;
        }
        
        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }
        
        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }
        
        // Метод для валидации и создания объекта Person
        public Person build() {
            if (lastName == null || lastName.trim().isEmpty()) {
                throw new IllegalStateException("LastName cannot be null or empty");
            }
            if (firstName == null || firstName.trim().isEmpty()) {
                throw new IllegalStateException("FirstName cannot be null or empty");
            }
            if (year < 1900 || year > 2026) {
                throw new IllegalStateException("Year must be between 1900 and 2026");
            }
            
            return new Person(this);
        }
    }
}