package edu.byuh.cis.cs203.outwit203_oneplayermode.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Bundle;

import edu.byuh.cis.cs203.outwit203_oneplayermode.GameView;
import edu.byuh.cis.cs203.outwit203_oneplayermode.R;

public class MainActivity extends AppCompatActivity {

    private GameView gv;
    private MediaPlayer soundtrack;

    /**
     * The onCreate method is one of the first methods that gets called
     * when an Activity starts. We override this method to do any one-time
     * initialization stuff
     * @param b not used in this program. In general, the Bundle object is used
     *          to preserve data from one instance of the program to the next;
     *          for example, after a device rotation event.
     */
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        gv = new GameView(this);
        if (Prefs.getMusicPrefs(this)) {
            soundtrack = MediaPlayer.create(this, R.raw.sneaky_snitch);
            soundtrack.setLooping(true);
        }
        setContentView(gv);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Prefs.getMusicPrefs(this)) {
            soundtrack.start();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Prefs.getMusicPrefs(this)) {
            soundtrack.pause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (Prefs.getMusicPrefs(this)) {
            soundtrack.release();
        }
    }

    /**
     * Don't take this method too seriously. I wrote it a few years ago to
     * compensate for the scarcity of documentation on Android font sizes.
     * I don't guarantee that it is 100% debugged.
     * @param lowerThreshold the approximate pixel size of how big you want the font to be
     * @return a number you can pass to Paint.setTextSize
     */
    public static float findThePerfectFontSize(float lowerThreshold) {
        float fontSize = 1;
        Paint p = new Paint();
        p.setTextSize(fontSize);
        while (true) {
            float asc = -p.getFontMetrics().ascent;
            if (asc > lowerThreshold) {
                break;
            }
            fontSize++;
            p.setTextSize(fontSize);
        }
        return fontSize;
    }
}