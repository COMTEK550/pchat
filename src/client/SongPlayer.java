import java.io.InputStream;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import javax.sound.sampled.*;

public class SongPlayer extends Thread {
    private String fname;
    public SongPlayer(String fname) throws Exception {
        this.fname = fname;
    }

    public void run() {
        try {
            InputStream in = getClass().getResourceAsStream(this.fname);
            in = new BufferedInputStream(in);

            Clip clip = AudioSystem.getClip();
            AudioInputStream ais = AudioSystem.getAudioInputStream(in);
            clip.open(ais);
            clip.start();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
