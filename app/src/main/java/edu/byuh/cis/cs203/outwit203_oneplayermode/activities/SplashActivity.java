package edu.byuh.cis.cs203.outwit203_oneplayermode.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;

import edu.byuh.cis.cs203.outwit203_oneplayermode.R;

public class SplashActivity extends Activity {

    private ImageView iv;

    /**
     * Create the title screen
     * @param b the Bundle object - we're not using it
     */
    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        iv = new ImageView(this);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        iv.setImageResource(R.drawable.splash);
        setContentView(iv);
    }

    /**
     * Respond to touch events; we divide the screen informally into three "buttons"
     * @param m contains encapsulated information about this touch event
     * @return true, always
     */
    @Override
    public boolean onTouchEvent(MotionEvent m) {
        if (m.getAction() == MotionEvent.ACTION_DOWN) {
            float w = iv.getWidth();
            float h = iv.getHeight();
            float x = m.getX();
            float y = m.getY();
            if (y > h / 2) {
                startGame();
            } else if (x < w / 2) {
                showAboutBox();
            } else {
                Intent i = new Intent(this, Prefs.class);
                startActivity(i);
            }
        }
            return true;
    }

    /**
     * Helper method. Just launches main activity.
     */
    private void startGame() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    /**
     * Helper method. Just launches the about box.
     */
    public void showAboutBox() {
        String message = """
            Welcome to Outwit!
            
            This app was programmed by the students of CS 203 at Brigham Young University--Hawaii in Fall 2023 semester.
            
            Gameplay was inspired by the (out of print) board game Outwit, published by Parker Brothers in 1978. If you enjoy this game, we encourage you to find a secondhand copy of the original game and play it!
            
            Instructions:
            Your goal is to move all your chips into your home corner.
            Each chip may only move horizontally or vertically.
            Each player has one power chip that can move diagonal too.
            Once a chip is in its home corner it can only move within that corner; it cannot leave.
            The first player to move all chips home wins!
            
        """;
        var ab = new AlertDialog.Builder(this);
        ab.setTitle("About Outwit")
                .setNeutralButton("OK", null)
                .setMessage(message);
        var box = ab.create();
        box.show();
    }

}
