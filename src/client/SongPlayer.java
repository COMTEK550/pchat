import java.io.InputStream;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import javax.sound.sampled.*;

public class SongPlayer {
    public SongPlayer(String fname) throws Exception {
        InputStream in = getClass().getResourceAsStream(fname);
        in = new BufferedInputStream(in);

        Clip clip = AudioSystem.getClip();
        AudioInputStream ais = AudioSystem.getAudioInputStream(in);
        clip.open(ais);
        clip.start();

    }
}
