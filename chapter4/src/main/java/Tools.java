import com.sun.javafx.application.PlatformImpl;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

class Tools {

    /**
     * Play an audio file located under directory "audios", wav/mp3/aiff extensions are supported
     */
    static void playAudio(final String audioFile) {
        PlatformImpl.startup(() -> {
            Media hit = new Media(new File("audios/" + audioFile).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(hit);
            mediaPlayer.play();
        });
    }

    static void sleepSilently(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            // -> Ignore
        }
    }

}
