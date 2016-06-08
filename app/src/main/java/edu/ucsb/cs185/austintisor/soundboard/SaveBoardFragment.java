package edu.ucsb.cs185.austintisor.soundboard;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class SaveBoardFragment extends DialogFragment {

    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View contentView = getActivity().getLayoutInflater().inflate(R.layout.fragment_save, null);

        //Get any Views you need (Any local variables accessed from inside an anonymous inner class must be final)
        final EditText nameEdit = (EditText) contentView.findViewById(R.id.boardNamePrompt);

        builder.setView(contentView).setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Save board
                String boardData = "Test stub";
                save(nameEdit.getText().toString(), boardData);
                System.out.println("Click Saved");
            }
        });
        return builder.create();
    }

    private void save(String filename, String data) {
        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + MainActivity.FOLDER;
            //create directory if not there
            File saveFolder = new File(path);
            if (!saveFolder.exists()) {
                saveFolder.mkdirs();
            }

            File boardFile = null;
            boardFile = new File(saveFolder, filename + ".sbs");
            boardFile.createNewFile();
            FileOutputStream stream = new FileOutputStream(boardFile);
            try {
                stream.write(data.getBytes());
            } finally {
                stream.close();
            }
            Log.d("Absolute board path",boardFile.getAbsolutePath());

            // Send a Toast message to notify the user that the file has been saved
            Toast.makeText(getActivity(), "Board saved!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
