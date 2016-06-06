package edu.ucsb.cs185.austintisor.soundboard;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;


public class SettingsFragment extends DialogFragment {

    private SeekBar volumeSlider, sizeSlider;
    private OnSettingsSetListener listener;

    public void setSettingsSetListener(OnSettingsSetListener listener) {
        this.listener = listener;
    }

    public interface OnSettingsSetListener {
        void onSettingsSet(int volume, int size);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Create a builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Inflate (turn xml into code) your layout
        View contentView = getActivity().getLayoutInflater().inflate(R.layout.fragment_settings, null);

        //Get any Views you need (Any local variables accessed from inside an anonymous inner class must be final)
        volumeSlider = (SeekBar) contentView.findViewById(R.id.volume_slider);
        sizeSlider = (SeekBar) contentView.findViewById(R.id.size_slider);

        //Pass your data to the builder (these can be chained)
        builder.setView(contentView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int volume = volumeSlider.getProgress();
                        int size = sizeSlider.getProgress();
                        listener.onSettingsSet(volume, size);
                    }
                });

        //Build the dialog and return it
        return builder.create();
    }

    public void setListeners() {
        volumeSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChanged = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // Do nothing
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(MainActivity.this, "seek bar progress:" + progressChanged, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
