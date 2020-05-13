package com.myapp.mediaplayer;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.zip.Inflater;

import static com.myapp.mediaplayer.MainActivity.player;
import static com.myapp.mediaplayer.MainActivity.seekBar;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongHolder> {

  ArrayList<SongInfo> arrayList = new ArrayList<SongInfo>();
  Context context;
  Activity activity;

    public SongAdapter(ArrayList<SongInfo> arrayList, Context context, Activity activity) {
        this.arrayList = arrayList;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public SongHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.media_item, parent, false);
        return new SongHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongHolder holder, final int position) {
        holder.name.setText(arrayList.get(position).Name);
        holder.artist.setText(arrayList.get(position).Artist);
        holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if ( MainActivity.player != null) {
                    if ( MainActivity.player.isPlaying()) {
                        MainActivity.player.stop();
                        seekBar.setProgress(0);
                    }
                }

                else {
                    MainActivity.player = new MediaPlayer();
                    try {
                        MainActivity.player.setDataSource(arrayList.get(position).Path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        MainActivity.player.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    MainActivity.player.start();

                    MyThread thread = new MyThread();
                    thread.start();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class SongHolder extends RecyclerView.ViewHolder {

        TextView name, artist;
        ImageView play;

        public SongHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.song_name);
            artist = itemView.findViewById(R.id.artist);
            play = itemView.findViewById(R.id.play_music);
        }
    }


    public class MyThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (MainActivity.player != null) {
                            MainActivity.seekBar.setProgress(MainActivity.player.getCurrentPosition());
                        }
                    }
                });
            }
        }
    }

}
