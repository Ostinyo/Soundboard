package edu.ucsb.cs185.austintisor.soundboard;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class NewButtonActivity extends AppCompatActivity {
    private static final int ACTIVITY_RECORD_SOUND = 1;
    Uri savedUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_button);
        ImageButton b = (ImageButton)findViewById(R.id.imageButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Button!");
                //Toast.makeText(NewButtonActivity.this,"Hello World!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(NewButtonActivity.this, AudioRecordActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == ACTIVITY_RECORD_SOUND){
            savedUri = data.getData();
            Toast.makeText(this,
                    "Saved: " + savedUri.getPath(),
                    Toast.LENGTH_LONG).show();
        }
    }
}


