import lombok.Data;

import java.util.Random;

@Data
public class Referee {

    enum Role {
        MAJOR, LINESMAN
    }

    private Name name;

    private int age;

    private Location location;

    private static final String[] AUDIO_FILES = {"whistle-1.wav", "whistle-2.aiff", "whistle-3.aiff"};

    void whistle() {
        Tools.playAudio(AUDIO_FILES[new Random().nextInt(AUDIO_FILES.length)]);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 7; i++) {
            new Referee().whistle();
            Tools.sleepSilently(2000);
        }
    }

}
