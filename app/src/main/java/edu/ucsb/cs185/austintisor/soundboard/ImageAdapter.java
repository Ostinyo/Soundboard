package edu.ucsb.cs185.austintisor.soundboard;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends BaseAdapter {

    private Context context;
    List<String> buttonFiles = new ArrayList<>();
    List<String> buttonNames = new ArrayList<>();
    List<Integer> buttonColors = new ArrayList<>();
    List<SoundButton> soundButtons = new ArrayList<>();

    public ImageAdapter(Context c){
        context = c;
        for(int i = 0; i < 9; i++) {
            buttonFiles.add("filename");
            buttonNames.add(""); // Replace with board loading
            switch (i) {
                case 0:
                    buttonColors.add(Color.BLUE); //(context.getResources().getColor(R.color.default1));
                case 1:
                    buttonColors.add(Color.DKGRAY);
                case 2:
                    buttonColors.add(Color.YELLOW);
                case 3:
                    buttonColors.add(Color.GREEN);
                case 4:
                    buttonColors.add(Color.MAGENTA);
                case 5:
                    buttonColors.add(Color.BLACK);
                case 6:
                    buttonColors.add(Color.RED);
                case 7:
                    buttonColors.add(Color.CYAN);
                case 8:
                    buttonColors.add(Color.WHITE);
                case 9:
                    buttonColors.add(Color.GREEN);
            }
        }
    }

    @Override
    public int getCount() {
        return buttonFiles.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("getView", "Called!");
        SoundButton soundButton;
        if (convertView == null)
            soundButton = createButton(position);
        else
            soundButton = (SoundButton) convertView;
        soundButton.setColor(buttonColors.get(position));
        soundButton.setName(buttonNames.get(position));
        soundButton.setFile(buttonFiles.get(position));

        // Sketchily make the default board work, this will overwrite a new board
        if (position == 0)
            soundButton.setRawSound(R.raw.drum1);
        else if (position == 1)
            soundButton.setRawSound(R.raw.drum2);
        else if (position == 2)
            soundButton.setRawSound(R.raw.whistle);
        else if (position == 3)
            soundButton.setRawSound(R.raw.guitar1);
        else if (position == 4)
            soundButton.setRawSound(R.raw.drum1);
        else if (position == 5)
            soundButton.setRawSound(R.raw.violin1);
        else if (position == 6)
            soundButton.setRawSound(R.raw.whistle);
        else if (position == 7)
            soundButton.setRawSound(R.raw.applause);
        else if (position == 8)
            soundButton.setRawSound(R.raw.bird);
        Log.d("Button position", Integer.toString(position));

        soundButtons.add(soundButton);
        return soundButton;
    }

    public SoundButton createButton (int position) {
        Log.d("CreateButton", "Called!");
        final SoundButton soundButton = new SoundButton(context);
        soundButton.setLayoutParams(new GridView.LayoutParams(250,250)); // Change to size preference
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

    public void addButton (String filename, String name, int color) {
        buttonFiles.add(filename);
        buttonNames.add(name);
        buttonColors.add(color);
        notifyDataSetChanged();
    }

    public void clear () {
        buttonFiles.clear();
        buttonNames.clear();
        buttonColors.clear();
        notifyDataSetChanged();
    }

    public void setEditing (boolean editing) {
        for (SoundButton s : soundButtons) {
            s.setEditing(editing);
        }
    }

    public void editButton (int position, String filename, String name, int color) {
        // Replace the button in this position
    }
}