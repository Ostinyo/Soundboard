package edu.ucsb.cs185.austintisor.soundboard;


import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Board {

    private Context context;
    private List<SoundButton> soundButtons = new ArrayList<>();
    private boolean editing = false;

    public Board (Context c) {
        context = c;
    }

    public void addSoundButton (SoundButton s) {
        soundButtons.add(s);
    }

    public int getSize () {
        return soundButtons.size();
    }

    public void saveBoard () {

    }

    public void loadBoard () {
        FileInputStream is;
        BufferedReader reader;
        final File file = new File("/sdcard/text.txt");

        if (file.exists()) {
            try {
                is = new FileInputStream(file);
                reader = new BufferedReader(new InputStreamReader(is));
                try { // SO MANY TRIES
                    String line = reader.readLine();
                    while (line != null) {
                        Log.d("Board file", line);
                        line = reader.readLine();
                    }
                } catch (IOException e) { e.printStackTrace(); }
            } catch (FileNotFoundException e) { e.printStackTrace(); }
        }
    }

    public void setEditing (boolean editing) {
        for (SoundButton s : soundButtons) {
            s.setEditing(editing);
        }
    }

    public boolean isEditing () {
        return editing;
    }
}
