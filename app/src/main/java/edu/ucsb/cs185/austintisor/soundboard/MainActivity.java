package edu.ucsb.cs185.austintisor.soundboard;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
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
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ImageAdapter myAdapter;
    private static final int PERMISSIONS_REQUEST = 2;
    private static final int NEW_BUTTON_INTENT = 3;
    private static final int SELECT_BOARD = 4;
    public static final String FILENAME_EXTRA = "filename", NAME_EXTRA = "name", COLOR_EXTRA = "color";

    private boolean editing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        askPermissions();

        final GridView gridView = (GridView) findViewById(R.id.boardGrid);
        myAdapter = new ImageAdapter(this);
        gridView.setAdapter(myAdapter);
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
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.fragment_settings);
                dialog.setTitle("Settings");
                dialog.show();
                //showSettingsFragment(); // Will replace for easier access to sliders
                return true;
            case R.id.action_add_button:
                onNewButton();
                return true;
            case R.id.action_edit_buttons:
                editing = true;
                // We should tint the edit icon here
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showSettingsFragment () {
        final SettingsFragment settingsFragment = new SettingsFragment();
        settingsFragment.setSettingsSetListener(new SettingsFragment.OnSettingsSetListener() {
            @Override
            public void onSettingsSet(int volume, int size) {
                // Do stuff with the set values
            }
        });

        //Show the fragment
        //settingsFragment.show(getFragmentManager(), "settings_fragment"); // Not working?
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_new:
                // Create a new board (future possibility)
            case R.id.nav_clear:
                // Clear the board
                myAdapter.clear();
            case R.id.nav_load:
                // Launch load dialogue fragment/file system
                browseFileSystem();
            case R.id.nav_save:
                // Launch save dialogue fragment
                SaveBoardFragment saveBoardFragment = new SaveBoardFragment();
                saveBoardFragment.show(getFragmentManager(), "save_board_fragment");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void browseFileSystem () {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        startActivityForResult(intent, SELECT_BOARD);
    }

    public void onNewButton() {
        Intent intent = new Intent(this, NewButtonActivity.class);
        startActivityForResult(intent, NEW_BUTTON_INTENT);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == NEW_BUTTON_INTENT){
            if(resultCode == RESULT_OK){
                String filename = data.getStringExtra(FILENAME_EXTRA);
                int color = data.getIntExtra(COLOR_EXTRA, NewButtonActivity.DEFAULT_COLOR);
                String name = data.getStringExtra(NAME_EXTRA);
                myAdapter.addButton(filename, name, color);
                Log.d("Color", Integer.toString(color));
                Log.d("Filename", filename);
                Log.d("Name", name);
            }
        }
    }
}
