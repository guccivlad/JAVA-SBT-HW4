package Test;

import java.io.Serializable;

public class Person implements Serializable {
    private final String name;
    private final String surname;
    private final int salary;

    public Person(String name, String surname, int salary) {
        this.name = name;
        this.surname = surname;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public int getSalary() {
        return salary;
    }
}
