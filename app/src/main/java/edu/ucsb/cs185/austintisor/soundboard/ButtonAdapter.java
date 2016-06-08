package edu.ucsb.cs185.austintisor.soundboard;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

public class ButtonAdapter extends BaseAdapter {

    private Context context;
    List<String> buttonFiles = new ArrayList<>();
    List<Uri> buttonUris = new ArrayList<>();
    List<String> buttonNames = new ArrayList<>();
    List<Integer> buttonColors = new ArrayList<>();
    List<SoundButton> soundButtons = new ArrayList<>();

    public ButtonAdapter(Context c){
        context = c;
        for(int i = 0; i < 9; i++) {
            buttonFiles.add(null);
            buttonUris.add(null);

            // Add default names and colors
            switch (i) {
                case 0:
                    buttonNames.add("Drum 1");
                    buttonColors.add(c.getResources().getColor(R.color.default1));
                    break;
                case 1:
                    buttonNames.add("Drum 2");
                    buttonColors.add(c.getResources().getColor(R.color.default2));
                    break;
                case 2:
                    buttonNames.add("Drum 3");
                    buttonColors.add(c.getResources().getColor(R.color.default3));
                    break;
                case 3:
                    buttonNames.add("Guitar");
                    buttonColors.add(c.getResources().getColor(R.color.default4));
                    break;
                case 4:
                    buttonNames.add("Piano");
                    buttonColors.add(c.getResources().getColor(R.color.default5));
                    break;
                case 5:
                    buttonNames.add("Violin");
                    buttonColors.add(c.getResources().getColor(R.color.default6));
                    break;
                case 6:
                    buttonNames.add("Whistle");
                    buttonColors.add(c.getResources().getColor(R.color.default7));
                    break;
                case 7:
                    buttonNames.add("Applause");
                    buttonColors.add(c.getResources().getColor(R.color.default8));
                    break;
                case 8:
                    buttonNames.add("Bird Chirp");
                    buttonColors.add(c.getResources().getColor(R.color.default9));
                    break;
            }
        }
    }

    @Override
    public int getCount() {
        return buttonFiles.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SoundButton soundButton;
        if (convertView == null)
            soundButton = createButton(position);
        else
            soundButton = (SoundButton) convertView;
        soundButton.setColor(buttonColors.get(position));
        soundButton.setName(buttonNames.get(position));
        soundButton.setFile(buttonFiles.get(position));
        soundButton.setUri(buttonUris.get(position));
        soundButton.setIndex(position);

        // Set default board sounds
        if (position == 0)
            soundButton.setRawSound(R.raw.drum1);
        else if (position == 1)
            soundButton.setRawSound(R.raw.drum2);
        else if (position == 2)
            soundButton.setRawSound(R.raw.drum3);
        else if (position == 3)
            soundButton.setRawSound(R.raw.guitar1);
        else if (position == 4)
            soundButton.setRawSound(R.raw.piano_melody1);
        else if (position == 5)
            soundButton.setRawSound(R.raw.violin1);
        else if (position == 6)
            soundButton.setRawSound(R.raw.whistle);
        else if (position == 7)
            soundButton.setRawSound(R.raw.applause);
        else if (position == 8)
            soundButton.setRawSound(R.raw.bird);

        soundButtons.add(position, soundButton);
        return soundButton;
    }

    public SoundButton createButton (int position) {
        final SoundButton soundButton = new SoundButton(context);
        soundButton.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.WRAP_CONTENT,GridView.LayoutParams.WRAP_CONTENT));
        soundButton.setPadding(5, 5, 5, 5);

        return soundButton;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return buttonFiles.get(position);
    }

    public void addButton (String filename, Uri uri, String name, int color) {
        buttonFiles.add(filename);
        buttonUris.add(uri);
        buttonNames.add(name);
        buttonColors.add(color);
        notifyDataSetChanged();
    }

    public void clear () {
        buttonFiles.clear();
        buttonNames.clear();
        buttonColors.clear();
        stopPlaying();
        notifyDataSetChanged();
    }

    public void setEditing (boolean editing) {
        for (SoundButton s : soundButtons) {
            s.setEditing(editing);
        }
    }

    public void setSize (int size) {
        for (SoundButton s : soundButtons) {
            s.setSize(size);
        }
    }

    public void stopPlaying () {
        for (SoundButton s : soundButtons) {
            s.stopSound();
        }
    }

    public void editButton (int position, String filename, Uri uri, String name, int color) {
        // Replace the button in this position
        buttonFiles.set(position, filename);
        buttonUris.set(position, uri);
        buttonNames.set(position, name);
        buttonColors.set(position, color);
        notifyDataSetChanged();
    }
}