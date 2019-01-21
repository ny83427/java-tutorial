import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.swing.*;
import java.io.File;
import java.net.URISyntaxException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

class Tools {
    private static Random RANDOM = new Random();

    static boolean nextBoolean() {
        return RANDOM.nextBoolean();
    }

    /**
     * generate a random number less than giving number
     * @param endExclusive  exclusive maximum number
     */
    static int nextInt(final int endExclusive) {
        return RANDOM.nextInt(endExclusive);
    }

    /**
     * Play an audio file located under directory "audios" or under resources
     */
    static MediaPlayer playAudio(final String audioFile) {
        File file = new File("audios/" + audioFile);
        try {
            Media hit = file.exists() && file.isFile() ? new Media(file.toURI().toString())
                : new Media(Tools.class.getResource("/" + audioFile).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(hit);
            mediaPlayer.play();
            return mediaPlayer;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    static void sleepSilently(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            // -> Ignore
        }
    }

    /**
     * Set Swing Theme: Windows or Nimbus
     */
    static void setTheme() {
        String theme = System.getProperty("os.name").startsWith("Windows") ? "Windows" : "Nimbus";
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if (theme.equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Tools.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
