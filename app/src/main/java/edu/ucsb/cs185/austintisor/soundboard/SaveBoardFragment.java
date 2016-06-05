package edu.ucsb.cs185.austintisor.soundboard;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Brenda on 6/5/2016.
 */
public class SaveBoardFragment extends DialogFragment {
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View contentView = getActivity().getLayoutInflater().inflate(R.layout.save_board_layout, null);

        builder.setView(contentView).setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Save board
                System.out.println("Click Saved");
            }
        });
        return builder.create();
    }
}
