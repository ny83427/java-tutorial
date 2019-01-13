import lombok.Data;

import java.awt.*;

@Data
public class Player {
    enum Role {
        FORWARD, MIDFIELDER, DEFENDER, GOALKEEPER
    }

    enum Speed {
        NORMAL, FAST, SUPERFAST
    }

    private Name name;

    private int number;

    private Role role;

    private int age;

    private int height;

    private Speed speed = Speed.NORMAL;

    private Location location;

    public Player(Name name, int number, Role role, int age, int height) {
        this.name = name;
        this.number = number;
        this.role = role;
        this.age = age;
        this.height = height;
    }

    void shoot(Ball ball) {
        Tools.playAudio("shoot-2.wav");
    }

    void pass(Ball ball) {
        Tools.playAudio("shoot-1.wav");
    }

    void dribble(Ball ball) {

    }

    void run() {
        // Run to 8 directions randomly with random selected speed
    }

    public static void main(String[] args) {
        Player ronaldo = new Player(new Name("Cristiano", "Ronaldo "), 7, Role.FORWARD, 33, 185);
        Ball ball = new Ball(5, Color.WHITE, new Location(53, 34));
        ronaldo.shoot(ball);
        Tools.sleepSilently(2000);
        ronaldo.pass(ball);
    }
}
