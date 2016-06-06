package edu.ucsb.cs185.austintisor.soundboard;

import android.content.Context;
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

    public ImageAdapter(Context c){
        context = c;
        for(int i = 0; i < 9; i++) {
            buttonFiles.add("filename");
            buttonNames.add(""); // Replace with board loading
            buttonColors.add(context.getResources().getColor(R.color.default1));
        }
    }

    @Override
    public int getCount() {
        return buttonNames.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SoundButton soundButton;
        if (convertView == null)
            soundButton = createButton(position);
        else
            soundButton = (SoundButton) convertView;
        return soundButton;
    }

    public SoundButton createButton (int position) {
        final SoundButton soundButton = new SoundButton(context);
        soundButton.setLayoutParams(new GridView.LayoutParams(250,250)); // Change to size preference
        soundButton.setPadding(5, 5, 5, 5);
        soundButton.setColor(buttonColors.get(position)); //(context.getResources().getColor(R.color.default1));
        soundButton.setName(buttonNames.get(position));
        soundButton.setFile(buttonFiles.get(position));

        soundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundButton.playSound();
            }
        });

        return soundButton;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return buttonNames.get(position);
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
}