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
    List<Integer> buttons = new ArrayList<>();

    public ImageAdapter(Context c){
        context = c;
        for(int i=0; i<9; i++) {
            buttons.add(1); // Replace with board loading
        }
    }

    @Override
    public int getCount() {
        return buttons.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SoundButton soundButton;
        if (convertView == null)
            soundButton = createButton();
        else
            soundButton = (SoundButton) convertView;
        //soundButton.setImageResource(buttons.get(position));
        return soundButton;
    }

    public SoundButton createButton () {
        final SoundButton soundButton = new SoundButton(context);
        soundButton.setLayoutParams(new GridView.LayoutParams(250,250));
        //soundButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
        soundButton.setPadding(5, 5, 5, 5);
        soundButton.setBackgroundColor(context.getResources().getColor(R.color.default1));

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
        return buttons.get(position);
    }

}