package com.myapp.mediaplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static SeekBar seekBar;
    public static RecyclerView list;
    public static MediaPlayer player;
    ArrayList<SongInfo> songInfos = new ArrayList<SongInfo>();
    public static int seekValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seekBar = findViewById(R.id.seek_bar);
        list = findViewById(R.id.recycler_view);

        checkPermission();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekValue = progress;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                player.seekTo(seekValue);

            }
        });

    }

    public void  loadSongs() {
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null, null);
        while (cursor.moveToNext()) {
            SongInfo info = new SongInfo();
            info.Name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
            info.Path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            info.Album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
            info.Artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            songInfos.add(info);
        }
        SongAdapter adapter = new SongAdapter(songInfos, getApplicationContext(),this);
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL,false));
    }

    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                loadSongs();
            }
            else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(getApplicationContext(),"Please Accept Permission",Toast.LENGTH_LONG).show();
                }
                requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 123) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadSongs();
            }
        }
    }

    @Override
    public void onBackPressed() {
        player.stop();
        super.onBackPressed();
    }

    public void play(View view) {
        player.seekTo(seekValue);
        seekBar.setProgress(seekValue);
        player.start();
    }

    public void pause(View view) {
        player.pause();
        seekBar.setProgress(seekValue);
    }

    public void stop(View view) {
        player.stop();
    }
}
