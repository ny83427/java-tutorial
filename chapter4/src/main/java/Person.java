abstract class Person implements Drawable {

    private Name name;

    private int age;

    private Location location;

    Person(Name name, int age) {
        this.name = name;
        this.age = age;
    }

    Name getName() {
        return this.name;
    }

    int getAge() {
        return this.age;
    }

    Location getLocation() {
        return location;
    }

    void setLocation(Location location) {
        this.location = location;
    }

}
