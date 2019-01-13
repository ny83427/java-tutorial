import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Random;

@Data
@NoArgsConstructor
public class Audience {

    private int homeTeamFans;

    private int visitingTeamFans;

    public Audience(int homeTeamFans, int visitingTeamFans) {
        this.homeTeamFans = homeTeamFans;
        this.visitingTeamFans = visitingTeamFans;
    }

    private static final String[] AUDIO_FILES = {"cheer-home.mp3", "cheer-visiting.wav"};

    /**
     * Soccer fans would cry and cheer for their teams
     */
    void cheer() {
        Tools.playAudio(AUDIO_FILES[new Random().nextInt(AUDIO_FILES.length)]);
    }

    public static void main(String[] args) {
        new Audience().cheer();
    }

}
