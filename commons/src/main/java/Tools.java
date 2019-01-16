import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.swing.*;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

abstract class Tools {
    /**
     * Switch flag for debug log output
     */
    static boolean DEBUG = false;

    /**
     * Play an audio file located under directory "audios"
     */
    static MediaPlayer playAudio(final String audioFile) {
        Media hit = new Media(new File("audios/" + audioFile).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(hit);
        mediaPlayer.play();
        return mediaPlayer;
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
