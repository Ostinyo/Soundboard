package edu.ucsb.cs185.austintisor.soundboard;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Environment;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import android.util.Log;

public class NewButtonActivity extends AppCompatActivity {
    private static final int ACTIVITY_RECORD_SOUND = 1, SELECT_SOUND = 2;
    public static final String STRING_EXTRA = "filename";
    private String mFilename;
    Uri savedUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_button);
        ImageButton b = (ImageButton)findViewById(R.id.imageButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(NewButtonActivity.this,"Hello World!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(NewButtonActivity.this, AudioRecordActivity.class);
                intent.putExtra(STRING_EXTRA, mFilename);
                startActivityForResult(intent, ACTIVITY_RECORD_SOUND);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ACTIVITY_RECORD_SOUND:
                if (resultCode == RESULT_OK)
                    mFilename = data.getStringExtra(STRING_EXTRA);



            case SELECT_SOUND:
                if (resultCode == RESULT_OK) {
                    mFilename = data.getData().getPath();
                    Log.d("Select sound", "SELECTED");
                    Log.d("Filename result", mFilename);
                }
                else if (resultCode == RESULT_CANCELED)
                    Log.d("Select sound", "CANCELED");
        }
    }

    public void browseFileSystem (View v) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        startActivityForResult(intent, SELECT_SOUND);
    }
}


