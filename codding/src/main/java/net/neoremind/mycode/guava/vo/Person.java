package net.neoremind.mycode.guava.vo;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class Person {

    private int age;

    private String name;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("age", age).add("name", name).toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(age, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Person) {
            Person that = (Person) obj;
            return Objects.equal(age, that.age) && Objects.equal(name, that.name);
        }
        return false;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
