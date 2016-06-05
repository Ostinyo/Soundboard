package edu.ucsb.cs185.austintisor.soundboard;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.media.MediaPlayer;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;

import java.io.IOException;

public class SoundButton extends Button implements OnClickListener {

    private String name;
    private int color;

    private String soundFile;
    private MediaPlayer mediaPlayer;

    public SoundButton (Context context) {
        super(context);
        init();
    }

    public SoundButton (Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init () {
        mediaPlayer = new MediaPlayer();
    }

    @Override
    public void onClick (View v) {
        // Create a button dialogue fragment with this button's values
    }

    public void setFile (String file) {
        soundFile = file;
        try {
            mediaPlayer.setDataSource(file);
        } catch (IOException e) {

        }
    }

    public void playSound () {
        mediaPlayer.start();
    }

    public void stopSound () {
        mediaPlayer.stop();
    }

    public void setColor (int c) {
        color = c;
        // Actually set the color
        setBackgroundColor(c);
    }

    public String toString () {
        return soundFile + "," + name + "," + color;
    }
}
