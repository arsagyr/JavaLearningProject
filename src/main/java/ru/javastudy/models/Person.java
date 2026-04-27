package ru.javastudy.models;

public class Person {
    private int year;      // год рождения
    private String lastName;    // фамилия
    private String firstName;   // имя

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
    
    // Переопределение метода toString()
    @Override
    public String toString() {
        return "Person{" +
                "lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", year=" + year +
                '}';
    }
    
    // Переопределение метода equals()
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Person person = (Person) obj;
        return year == person.year &&
                lastName.equals(person.lastName) &&
                firstName.equals(person.firstName);
    }
    
    // Переопределение метода hashCode()
    @Override
    public int hashCode() {
        int result = year;
        result = 31 * result + lastName.hashCode();
        result = 31 * result + firstName.hashCode();
        return result;
    }
}