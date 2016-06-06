package edu.ucsb.cs185.austintisor.soundboard;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;
import android.media.MediaPlayer;
import android.view.View;
import android.view.View.OnClickListener;
import android.net.Uri;

import java.io.IOException;

public class SoundButton extends Button implements OnClickListener {

    private Context c;

    private String name;
    private int color;

    private String soundFile;
    private Uri soundUri;
    private MediaPlayer mediaPlayer;

    public SoundButton (Context context) {
        super(context);
        init(context);
    }

    public SoundButton (Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init (Context context) {
        c = context;
        mediaPlayer = MediaPlayer.create(context, R.raw.drum1); // Temporarily set to drum1
    }

    @Override
    public void onClick (View v) {
        // Play the sound (if already playing start it again)
        playSound();
    }

    public void setFile (String file) {
        soundFile = file;
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(file);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("File set to", file);
    }

    /*
    public void setUri (Uri uri) {
        soundUri = uri;
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(c, soundUri);
            mediaPlayer.prepare();
        } catch (IOException e) { }
    }*/

    public void playSound () {
        // Fix the scenario when the button is pressed while playing sound
        /*if (mediaPlayer.isPlaying()) {
            stopSound();
            mediaPlayer.reset();
            try {
                mediaPlayer.setDataSource(c, soundURI);
                mediaPlayer.prepare();
            } catch (IOException e) { }
        }*/
        mediaPlayer.start();
    }

    public void stopSound () {
        mediaPlayer.stop();
    }

    public void setColor (int c) {
        color = c;
        // Actually set the color
        setBackgroundColor(color);
        Log.d("Color set to", Integer.toString(color));
    }

    public void setName (String n) {
        name = n;
        setText(name);
        Log.d("Name set to", name);
    }

    public String toString () {
        return soundFile + "," + name + "," + color;
    }
}
