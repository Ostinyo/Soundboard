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
    List<Integer> imageIDs = new ArrayList<>();

    public ImageAdapter(Context c){
        context=c;
        for(int i=0; i<9; i++) {
            imageIDs.add(R.drawable.grid_button);
        }
    }

    @Override
    public int getCount() {
        return imageIDs.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if(convertView==null){
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(200,200));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(5, 5, 5, 5);
        }
        else{
            imageView = (ImageView) convertView;
        }
        imageView.setImageResource(imageIDs.get(position));
        return imageView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return imageIDs.get(position);
    }

}