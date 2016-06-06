package edu.ucsb.cs185.austintisor.soundboard;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.os.Bundle;
import android.app.DialogFragment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;


public class SettingsFragment extends DialogFragment {

    private SeekBar volumeSlider, sizeSlider;
    private OnSettingsSetListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Context context = getActivity().getApplicationContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Inflate (turn xml into code) your layout
        View contentView = getActivity().getLayoutInflater().inflate(R.layout.fragment_settings, null);

        //Get any Views you need (Any local variables accessed from inside an anonymous inner class must be final)
        volumeSlider = (SeekBar) contentView.findViewById(R.id.volume_slider);
        sizeSlider = (SeekBar) contentView.findViewById(R.id.size_slider);
        AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        final int maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        final int curVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        volumeSlider.setMax(maxVolume);
        volumeSlider.setProgress(curVolume);

        //Pass your data to the builder (these can be chained)
        builder.setView(contentView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setListeners();
                        AudioManager bm = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
                        bm.setStreamVolume(AudioManager.STREAM_MUSIC, volumeSlider.getProgress(),0);
                    }
                });

        //Build the dialog and return it
        return builder.create();
    }

    public void setSettingsSetListener(OnSettingsSetListener listener) {
        this.listener = listener;
    }

    public interface OnSettingsSetListener {
        void onSettingsSet(int volume, int size);
    }

    public void setListeners() {
        volumeSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Toast.makeText(getActivity(), "Volume Changed", Toast.LENGTH_SHORT).show();
                volumeSlider.setProgress(progress);
                
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // Do nothing
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                // Do nothing
            }
        });

        sizeSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //Implement later
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Do nothing
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Do nothing
            }
        });
    }
}
