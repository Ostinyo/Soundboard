package edu.ucsb.cs185.austintisor.soundboard;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    MediaPlayer sound;
    List<Integer> boardSounds = new ArrayList<>();
    ImageAdapter myAdapter = new ImageAdapter(this);
    private static final int PERMISSIONS_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initializeDefaultBoard();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //final GridView gridView = (GridView) findViewById(R.id.boardGrid);
        //gridView.setAdapter(myAdapter);
        askPermissions();

        /*gridView.setOnTouchListener(new AdapterView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getActionMasked();
                float currentX = event.getX();
                float currentY = event.getY();
                int position = gridView.pointToPosition((int) currentX, (int) currentY);
                switch (action) {
                    case (MotionEvent.ACTION_DOWN):
                        sound = MediaPlayer.create(MainActivity.this, boardSounds.get(position));
                        sound.start();
                        return true;
                    case (MotionEvent.ACTION_MOVE):
                        return true;
                    case (MotionEvent.ACTION_UP):
                        sound.stop();
                        return true;
                    case (MotionEvent.ACTION_CANCEL):
                        return true;
                }
                return true;
            }
        });*/
    }

    public void initializeDefaultBoard(){
        boardSounds.add(R.raw.drum1);
        boardSounds.add(R.raw.drum2);
        boardSounds.add(R.raw.applause);
        boardSounds.add(R.raw.bird);
        boardSounds.add(R.raw.guitar1);
        boardSounds.add(R.raw.piano_melody1);
        boardSounds.add(R.raw.drum3);
        boardSounds.add(R.raw.violin1);
        boardSounds.add(R.raw.whistle);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void askPermissions(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED  ) {
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_add_button:
                onNewButton();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_load) {
            // Launch load dialogue fragment/file system
        } else if (id == R.id.nav_save) {
            // Launch save dialogue fragment
            SaveBoardFragment saveBoardFragment = new SaveBoardFragment();
            saveBoardFragment.show(getFragmentManager(), "save_board_fragment");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onNewButton() {
        Intent intent = new Intent(this, NewButtonActivity.class);
        startActivity(intent);
    }

    public void onEditButton(View view) {
        final ButtonFragment buttonFragment = new ButtonFragment();
        buttonFragment.setButtonSetListener(new ButtonFragment.OnButtonSetListener() {
            @Override
            public void onButtonSet(String name, String filename, int color) {
                // Do stuff with the set values
            }
        });

        //Show the fragment
        buttonFragment.show(getFragmentManager(), "edit_button");
    }
}
