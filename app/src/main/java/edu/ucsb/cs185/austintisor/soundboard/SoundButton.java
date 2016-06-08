package edu.ucsb.cs185.austintisor.soundboard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
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
    private static final int EDIT_BUTTON_INTENT = 5;

    private String name;
    private int index, color, size = 250;

    private String soundFile = null;
    private int rawSound = R.raw.drum1;
    private Uri soundUri;
    private MediaPlayer mediaPlayer;
    private boolean editing = false;

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
        setDimens(size);
        //setBackgroundResource(R.drawable.button_style);
    }

    public void setSize (int s) {
        size = s;
        setDimens(size);
    }

    public void setDimens (int p) {
        this.setWidth(p);
        this.setHeight(p);
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
            intent.putExtra(MainActivity.FILENAME_EXTRA, soundFile);
            intent.putExtra(MainActivity.URI_EXTRA, soundUri);
            intent.putExtra(MainActivity.COLOR_EXTRA, color);
            intent.putExtra(MainActivity.NAME_EXTRA, name);
            intent.putExtra(MainActivity.INDEX_EXTRA, index);
            ((Activity)c).startActivityForResult(intent, EDIT_BUTTON_INTENT);
        }
    }

    public void setFile (String file) {
        soundFile = file;
        if (soundFile != null) {
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(soundFile);
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("File set to", soundFile);
        }
    }

    public void setUri (Uri uri) {
        soundUri = uri;
        if (soundUri != null) {
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(c, soundUri);
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("Uri set to", soundUri.toString());
        }
    }

    // Overload for string
    public void setUri (String uri) {
        if (uri != null) {
            soundUri = Uri.parse(uri);
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(c, soundUri);
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("Uri set to", uri);
        }
    }

    public void playSound () {
        mediaPlayer.release();
        mediaPlayer = new MediaPlayer();
        try {
            if (soundFile != null) {
                mediaPlayer.setDataSource(soundFile);
                mediaPlayer.prepare();
            }
            else if (soundUri != null) {
                //mediaPlayer.create(c, soundUri);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDataSource(c, soundUri);
                mediaPlayer.prepare();
            } else {
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

    public void setIndex (int i) {
        index = i;
    }

    public int getIndex () {
        return index;
    }

    public void setRawSound (int r) {
        rawSound = r;
    }

    public String toString () {
        return soundFile + "," + name + "," + color;
    }
}
