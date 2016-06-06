package edu.ucsb.cs185.austintisor.soundboard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;
import android.media.MediaPlayer;
import android.view.View;
import android.view.View.OnClickListener;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;

public class SoundButton extends Button implements OnClickListener {

    private Context c;
    private static final int EDIT_BUTTON_INTENT = 5;

    private String name;
    private int index, color;

    private String soundFile;
    private int rawSound = R.raw.drum1;
    private Uri soundUri;
    private MediaPlayer mediaPlayer;
    private boolean editing = false, raw = false;

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
        setOnClickListener(this);
        mediaPlayer = MediaPlayer.create(context, rawSound); // Temporarily set to drum1
    }

    @Override
    public void onClick (View v) {
        if (!editing) {
            // Play the sound (if already playing start it again)
            Log.d("Sound Button", "Playing!");
            playSound();
        } else {
            // Launch the new button activity to modify data
            Log.d("Sound Button", "Editing!");
            Intent intent = new Intent(c, NewButtonActivity.class);
            //intent.putExtra(MainActivity.EDIT_INDEX, index);
            intent.putExtra(MainActivity.EDIT_SOUND, soundFile);
            //intent.putExtra(MainActivity.EDIT_COLOR, color);
            //intent.putExtra(MainActivity.EDIT_NAME, name);
            ((Activity)c).startActivityForResult(intent, EDIT_BUTTON_INTENT);
        }
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

    public void playSound () {
        mediaPlayer.release();
        mediaPlayer = new MediaPlayer();
        try {
            if (!raw) {
                mediaPlayer.setDataSource(soundFile);
                mediaPlayer.prepare();
            }
            else {
                //InputStream ins = getResources().openRawResource(rawSound);
                mediaPlayer = MediaPlayer.create(c, rawSound);
                //ins.close();
            }
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopSound () {
        if(mediaPlayer.isPlaying())
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

    public void setEditing (boolean e) {
        editing = e;
        Log.d("Editing set to", Boolean.toString(e));
    }

    public boolean getEditing () {
        return editing;
    }

    public void setRawSound (int r) {
        rawSound = r;
        raw = true;
    }

    public String toString () {
        return soundFile + "," + name + "," + color;
    }
}
