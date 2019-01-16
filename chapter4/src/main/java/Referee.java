import java.awt.*;
import java.util.Random;

class Referee extends Person {

    enum Role {
        MAJOR, LINESMAN
    }

    private Role role;

    Referee(Role role, Name name, int age) {
        super(name, age);
        this.role = role;
    }

    private static final String[] AUDIO_FILES = {"whistle-1.wav", "whistle-2.aiff", "whistle-3.aiff"};

    void actRandomly() {
        if (new Random().nextInt(33) == 3) {
            whistle();
        } else {
            run();
        }
    }

    void whistle() {
        Tools.playAudio(AUDIO_FILES[new Random().nextInt(AUDIO_FILES.length)]);
    }

    private void run() {
        this.getLocation().move(-30 + new Random().nextInt(61), -20 + new Random().nextInt(41));
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        Location location = this.getLocation();
        g.fill3DRect(location.x, location.y, Constants.ROLE_SIZE, Constants.ROLE_SIZE, true);
        g.setColor(Color.BLACK);
        g.drawString("R", location.x + Constants.ROLE_SIZE / 2, location.y + Constants.ROLE_SIZE / 2);
    }

    @Override
    public String toString() {
        return String.format("%s(%s, %d years old)", this.getName(), this.role, this.getAge());
    }

}
