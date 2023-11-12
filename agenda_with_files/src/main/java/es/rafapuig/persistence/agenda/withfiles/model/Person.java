package es.rafapuig.persistence.agenda.withfiles.model;

public class Person {

    private final String fullName;
    private final int age;
    private final boolean isWorking;

    public Person(String fullName, int age, boolean isWorking) {
        this.fullName = fullName;
        this.age = age;
        this.isWorking = isWorking;
    }

    public String getFullName() {
        return fullName;
    }

    public int getAge() {
        return age;
    }

    public boolean isWorking() {
        return isWorking;
    }
}
