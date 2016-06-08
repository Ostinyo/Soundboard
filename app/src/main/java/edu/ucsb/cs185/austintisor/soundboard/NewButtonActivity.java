package edu.ucsb.cs185.austintisor.soundboard;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.os.Environment;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.util.Log;

import org.xdty.preference.colorpicker.ColorPickerDialog;
import org.xdty.preference.colorpicker.ColorPickerSwatch;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;

public class NewButtonActivity extends AppCompatActivity {
    private static final int SELECT_SOUND = 2;
    public static final String STRING_EXTRA = "filename";

    private static String mFilename = null, mName = "";
    private Uri mFileUri;
    private int mIndex = 0;

    private ImageButton mRecordButton = null;
    private MediaRecorder mRecorder = null;

    private ImageButton   mPlayButton = null;
    private MediaPlayer   mPlayer = null;
    private final int PLAY_TINT = Color.GREEN;

    private ImageButton mDoneButton;
    private Button mColorSelectButton;

    private EditText mFilenameText, mNameText;

    private static final String LOG_TAG = "NewButtonActivity";
    private static final int TINT_COLOR = Color.argb(120, 0, 0, 255);
    private static final int NO_COLOR = Color.argb(0, 0, 0, 0);
    public static final int DEFAULT_COLOR = Color.RED;

    private int mColor = DEFAULT_COLOR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_button);

        getData();

        mRecordButton = (ImageButton) findViewById(R.id.imageButton);
        mRecordButton.setOnTouchListener(new RecordListener());

        mPlayButton = (ImageButton) findViewById(R.id.new_button_play);
        mPlayButton.setOnClickListener(new PlayListener());
        mPlayButton.setEnabled(false);

        mDoneButton = (ImageButton) findViewById(R.id.new_button_done);
        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDone();
            }
        });

        mColorSelectButton = (Button)findViewById(R.id.button_color_select);
        mColor = getResources().getColor(R.color.default2);
        mColorSelectButton.setBackgroundColor(mColor);

        mFilenameText = (EditText)findViewById(R.id.filename_text);
        mNameText = (EditText)findViewById(R.id.name_text);

        // Apply values for editing
        setNameText(mName);
        if(mFilename != null || mFileUri != null) activatePlay();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_SOUND) {
            if (resultCode == RESULT_OK && data != null) {
                mFileUri = data.getData();
                mFilename = mFileUri.getPath();
                setFilenameText(""); // Clear any recorded file name
                activatePlay();
                Log.d("Select sound", "SELECTED");
                Log.d("Uri result", mFileUri.toString());
            }
            else if (resultCode == RESULT_CANCELED)
                Log.d("Select sound", "CANCELED");
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        stopPlaying();
        super.onBackPressed();
    }

    // Pass data to this activity when editing
    public void getData () {
        mFilename = this.getIntent().getStringExtra(MainActivity.FILENAME_EXTRA);
        mFileUri = this.getIntent().getParcelableExtra(MainActivity.URI_EXTRA);
        mColor = this.getIntent().getIntExtra(MainActivity.COLOR_EXTRA, DEFAULT_COLOR);
        mName = this.getIntent().getStringExtra(MainActivity.NAME_EXTRA);
        mIndex = this.getIntent().getIntExtra(MainActivity.INDEX_EXTRA, 0);
    }

    public void browseFileSystem (View v) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/*");
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
                activatePlay();
                mRecordButton.setColorFilter(NO_COLOR);
                onRecord(false);
            }
            return true;
        }

    }

    private void setNextFilename() {
        mFilename = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + MainActivity.FOLDER;

        //if folder does not exist
        File folder = new File(mFilename);
        folder.mkdir();

        Timestamp time = new Timestamp(System.currentTimeMillis());
        mFilename += File.separator + "sound_" + time.toString().replaceAll("\\W", "_") + ".3gp";
        setFilenameText(mFilename);
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
        deactivatePlay();
        mPlayer = new MediaPlayer();
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                activatePlay();
            }
        });

        try {
            if(mFileUri != null){
                mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mPlayer.setDataSource(this, mFileUri);
            }else {
                mPlayer.setDataSource(mFilename);
            }
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        if (mPlayer != null && mPlayer.isPlaying())
            mPlayer.release();
        mPlayer = null;
    }

    private void startRecording() {
        mFileUri = null;

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
            activatePlay();
        }
    }

    private void setFilenameText(String path) {
        mFilenameText.setText(path.substring(path.lastIndexOf(File.separator) + 1));
    }

    private void setNameText(String name) {
        mNameText.setText(name);
    }

    private void activatePlay() {
        mPlayButton.setEnabled(true);
        mPlayButton.setColorFilter(PLAY_TINT, PorterDuff.Mode.MULTIPLY);
    }

    private void deactivatePlay() {
        mPlayButton.setEnabled(false);
        mPlayButton.setColorFilter(getResources().getColor(R.color.colorBackground), PorterDuff.Mode.MULTIPLY);
    }

    private void onDone() {
        stopPlaying();
        if (mFilename != null || mFileUri != null) {
            Intent intent = new Intent();
            if(mFileUri != null){
                intent.putExtra(MainActivity.URI_EXTRA, mFileUri);
                //intent.setData(mFileUri);
            }else{
                saveFile();
                intent.putExtra(MainActivity.FILENAME_EXTRA, mFilename);
            }
            mName = ((EditText) findViewById(R.id.name_text)).getText().toString();
            intent.putExtra(MainActivity.NAME_EXTRA, mName);
            intent.putExtra(MainActivity.COLOR_EXTRA, mColor);
            intent.putExtra(MainActivity.INDEX_EXTRA, mIndex);
            setResult(RESULT_OK, intent);
            finish();
        } else
            Toast.makeText(this, "You must select a sound!", Toast.LENGTH_SHORT).show();
    }

    //Credit: https://github.com/xdtianyu/ColorPicker
    public void selectColor(View view) {
        int[] colorArray = {getResources().getColor(R.color.default1), getResources().getColor(R.color.default2), getResources().getColor(R.color.default3), getResources().getColor(R.color.default4), getResources().getColor(R.color.default5), getResources().getColor(R.color.default6), getResources().getColor(R.color.default7), getResources().getColor(R.color.default8), getResources().getColor(R.color.default9), getResources().getColor(R.color.default10), getResources().getColor(R.color.default11), getResources().getColor(R.color.default12), getResources().getColor(R.color.default13), getResources().getColor(R.color.default14), getResources().getColor(R.color.default15), getResources().getColor(R.color.default16)};
        ColorPickerDialog colors = ColorPickerDialog.newInstance(R.string.color_picker,
                colorArray,
                mColor,
                4,
                ColorPickerDialog.SIZE_LARGE);
        colors.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                mColor = color;
                mColorSelectButton.setBackgroundColor(mColor);
            }
        });
        colors.show(getFragmentManager(), "color picker");
    }

    private void saveFile(){
        File old = new File(mFilename);
        String newFileString = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + MainActivity.FOLDER + File.separator + mFilenameText.getText();
        File newF = new File(newFileString);
        old.renameTo(newF);
    }

}


