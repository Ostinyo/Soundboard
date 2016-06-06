package edu.ucsb.cs185.austintisor.soundboard;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.os.Environment;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;

public class NewButtonActivity extends AppCompatActivity {
    private static final int ACTIVITY_RECORD_SOUND = 1, SELECT_SOUND = 2;
    public static final String STRING_EXTRA = "filename";
    Uri savedUri;

    private final String FOLDER = "Soundboard";
    private static String mFilename = null;

    private ImageButton mRecordButton = null;
    private MediaRecorder mRecorder = null;

    private ImageButton   mPlayButton = null;
    private MediaPlayer   mPlayer = null;

    private static final String LOG_TAG = "NewButtonActivity";
    private static final int TINT_COLOR = Color.argb(120, 0, 0, 255);
    private static final int NO_COLOR = Color.argb(0, 0, 0, 0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_button);
        ImageButton b = (ImageButton)findViewById(R.id.imageButton);

        mRecordButton = (ImageButton) findViewById(R.id.imageButton);
        mRecordButton.setOnTouchListener(new RecordListener());

        mPlayButton = (ImageButton) findViewById(R.id.new_button_play);
        mPlayButton.setOnClickListener(new PlayListener());
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

    class RecordListener implements View.OnTouchListener {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                    NewButtonActivity.this.setNextFilename();
                    mRecordButton.setColorFilter(TINT_COLOR);
                    onRecord(true);
                } else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                    mPlayButton.setEnabled(true);
                    mRecordButton.setColorFilter(NO_COLOR);
                    onRecord(false);
                }
                return true;
            }

    }

    private void setNextFilename() {
        mFilename = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + FOLDER;

        //if folder does not exist
        File folder = new File(mFilename);
        folder.mkdir();

        Timestamp time = new Timestamp(System.currentTimeMillis());
        mFilename += File.separator + "sound_" + time.toString().replaceAll("\\W", "_") + ".3gp";
    }

    class PlayListener implements View.OnClickListener {
            public void onClick(View v) {
                onPlay(true);
            }
    }


    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        mPlayButton.setEnabled(false);
        mPlayer = new MediaPlayer();
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mPlayButton.setEnabled(true);
            }
        });

        try {
            mPlayer.setDataSource(mFilename);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFilename);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        //if this is called too soon after startRecording(), this may create an error: "stop failed"
        //I assume that this is happening because it is telling the recorder to stop before it is finished starting
        try {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }catch(Exception ex){
            //No audio to play, so disable the play button
            mPlayButton.setEnabled(false);
        }
    }

}


