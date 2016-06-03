package edu.ucsb.cs185.austintisor.soundboard;

//credit: https://developer.android.com/guide/topics/media/audio-capture.html

import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.os.Bundle;
import android.os.Environment;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Context;
import android.util.Log;
import android.media.MediaRecorder;
import android.media.MediaPlayer;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;


public class AudioRecordActivity extends Activity
{
    private static final String LOG_TAG = "AudioRecordActivity";
    private final String FOLDER = "Soundboard";
    private static String mFileName = null;

    private RecordButton mRecordButton = null;
    private MediaRecorder mRecorder = null;

    private PlayButton   mPlayButton = null;
    private MediaPlayer   mPlayer = null;

    private DoneButton mDoneButton = null;

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
            mPlayer.setDataSource(mFileName);
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
        mRecorder.setOutputFile(mFileName);
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

    class RecordButton extends Button {

        OnTouchListener clicker = new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                    setNextFilename();
                    setText("RECORDING!!");
                    onRecord(true);
                } else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                    setText("Hold to Record");
                    mPlayButton.setEnabled(true);
                    onRecord(false);
                }
                return true;
            }

        };

        public RecordButton(Context ctx) {
            super(ctx);
            setText("Hold to Record");
            setOnTouchListener(clicker);
        }
    }

    class PlayButton extends Button {
        boolean mStartPlaying = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onPlay(true);
            }
        };

        public PlayButton(Context ctx) {
            super(ctx);
            setText("Start playing");
            setOnClickListener(clicker);
        }
    }

    class DoneButton extends Button{

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(NewButtonActivity.STRING_EXTRA, mFileName);
                setResult(RESULT_OK, intent);
                finish();
            }
        };

        public DoneButton(Context ctx){
            super(ctx);
            setText("Done");
            setOnClickListener(clicker);
        }
    }

    public AudioRecordActivity() {
        setNextFilename();
    }

    private void setNextFilename(){
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + FOLDER;

        //if folder does not exist
        File folder = new File(mFileName);
        folder.mkdir();

        Timestamp time = new Timestamp(System.currentTimeMillis());
        mFileName += File.separator + "sound_" + time.toString().replaceAll("\\W", "_") + ".3gp";
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        mRecorder = new MediaRecorder();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                0);
        params.gravity = Gravity.CENTER;

        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        mRecordButton = new RecordButton(this);
        ll.addView(mRecordButton, params);
        mPlayButton = new PlayButton(this);
        mPlayButton.setEnabled(false);

        ll.addView(mPlayButton, params);

        //this button does nothing at the moment
        mDoneButton = new DoneButton(this);
        ll.addView(mDoneButton, params);

        setContentView(ll);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }
}