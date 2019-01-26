/*
* This is a personal academic project. Dear PVS-Studio, please check it.
* PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
*/
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
