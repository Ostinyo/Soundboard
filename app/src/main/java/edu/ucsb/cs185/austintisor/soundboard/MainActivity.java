package edu.ucsb.cs185.austintisor.soundboard;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int PERMISSIONS_REQUEST = 2;
    private static final int NEW_BUTTON_INTENT = 3, EDIT_BUTTON_INTENT = 5;
    private static final int SELECT_BOARD = 4;
    public static final String FILENAME_EXTRA = "filename", URI_EXTRA = "uri", NAME_EXTRA = "name", COLOR_EXTRA = "color", INDEX_EXTRA = "index";
    public static final String SETTINGS_SIZE = "size";
    public static final String FOLDER = "Soundboard";
    public static final int MIN_SIZE = 150;

    private ButtonAdapter mAdapter;
    private boolean editing = false;
    private int size = 250;

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
        mAdapter = new ButtonAdapter(this);
        gridView.setAdapter(mAdapter);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void askPermissions(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST);
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
                SettingsFragment settingsFragment = new SettingsFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(SETTINGS_SIZE, size);
                settingsFragment.setArguments(bundle);
                settingsFragment.show(getFragmentManager(), "settings_fragment");
                return true;
            case R.id.action_add_button:
                onNewButton();
                return true;
            case R.id.action_edit_buttons:
                onEditButtons(item);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_new) {
            // Create a new board (future possibility)
            Toast.makeText(this, "This feature is not yet implemented!", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_clear) {
            // Clear the board
            mAdapter.clear();
        } else if (id == R.id.nav_load) {
            // Launch load dialogue fragment/file system
            browseFileSystem();
            Toast.makeText(this, "This feature is not yet implemented!", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_save) {
            // Launch save dialogue fragment
            SaveBoardFragment saveBoardFragment = new SaveBoardFragment();
            saveBoardFragment.show(getFragmentManager(), "save_board_fragment");
            Toast.makeText(this, "This feature is not yet implemented!", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == NEW_BUTTON_INTENT){
            if(resultCode == RESULT_OK){
                String filename = data.getStringExtra(FILENAME_EXTRA);
                Uri uri = data.getParcelableExtra(URI_EXTRA);
                int color = data.getIntExtra(COLOR_EXTRA, NewButtonActivity.DEFAULT_COLOR);
                String name = data.getStringExtra(NAME_EXTRA);

                mAdapter.addButton(filename, uri, name, color);

                // Logging
                Log.d("Color", Integer.toString(color));
                if(filename != null) Log.d("Filename", filename);
                if (uri != null) Log.d("Uri", uri.toString());
                Log.d("Name", name);
            }
        } else if (requestCode == EDIT_BUTTON_INTENT) {
            if (resultCode == RESULT_OK) {
                // Change the button data
                String filename = data.getStringExtra(FILENAME_EXTRA);
                Uri uri = data.getParcelableExtra(URI_EXTRA);
                int color = data.getIntExtra(COLOR_EXTRA, NewButtonActivity.DEFAULT_COLOR);
                String name = data.getStringExtra(NAME_EXTRA);
                int index = data.getIntExtra(INDEX_EXTRA, 0);

                mAdapter.editButton(index, filename, uri, name, color); // We'll need to save the index for this

                // Logging
                Log.d("Edit Button", "Requested");
                Log.d("Color", Integer.toString(color));
                if(filename != null) Log.d("Filename", filename);
                if (uri != null) Log.d("Uri", uri.toString());
                Log.d("Name", name);
            }
        }
    }

    public void browseFileSystem () {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/" + FOLDER + "/");
        intent.setDataAndType(uri, "text/sbs");
        startActivityForResult(intent, SELECT_BOARD);
    }

    public void onNewButton () {
        Intent intent = new Intent(this, NewButtonActivity.class);
        startActivityForResult(intent, NEW_BUTTON_INTENT);
    }

    public void onEditButtons (MenuItem item) {
        TextView textView;
        textView = (TextView) findViewById(R.id.text_mode);
        if (editing) {
            item.setIcon(R.drawable.ic_menu_play_clip);
            textView.setText(R.string.play_mode);
            editing = false;
        }
        else {
            item.setIcon(R.drawable.ic_menu_edit);
            textView.setText(R.string.edit_mode);
            editing = true;
        }
        mAdapter.setEditing(editing);
    }

    public void setSize (int s) {
        size = s;
        mAdapter.setSize(size);
    }
}
