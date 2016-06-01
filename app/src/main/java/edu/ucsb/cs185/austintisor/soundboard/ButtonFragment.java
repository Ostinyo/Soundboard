package edu.ucsb.cs185.austintisor.soundboard;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.DialogFragment;
import android.view.View;
import android.widget.EditText;

public class ButtonFragment extends DialogFragment {

    private OnButtonSetListener listener;

    public void setButtonSetListener(OnButtonSetListener listener) {
        this.listener = listener;
    }

    public interface OnButtonSetListener {
        void onButtonSet(String name, String filename, int color);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Create a builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Inflate (turn xml into code) your layout
        View contentView = getActivity().getLayoutInflater().inflate(R.layout.fragment_button, null);

        //Get any Views you need (Any local variables accessed from inside an anonymous inner class must be final)
        final EditText nameEdit = (EditText) contentView.findViewById(R.id.button_name_edit);

        //Pass your data to the builder (these can be chained)
        builder.setView(contentView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = nameEdit.getText().toString();
                        String file = "sound.wav"; //temp
                        int color = 0; //temp
                        listener.onButtonSet(name, file, color);
                    }
                });

        //Build the dialog and return it
        return builder.create();
    }
}
