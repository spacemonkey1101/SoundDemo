package com.example.sounddemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;

import java.io.IOException;
import java.util.jar.Attributes;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    SoundPool sp;

    int idFX1 = -1;
    int idFX2 = -1;
    int idFX3 = -1;
    int nowPlaying = -1;

    float volume = .1f;
    int repeats = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonfx1 = findViewById(R.id.btnFX1);
        Button buttonfx2 = findViewById(R.id.btnFX2);
        Button buttonfx3 = findViewById(R.id.btnFX3);

        buttonfx1.setOnClickListener(this);
        buttonfx2.setOnClickListener(this);
        buttonfx3.setOnClickListener(this);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            sp = new SoundPool.Builder()
                    .setMaxStreams(5)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            sp = new SoundPool(5 , AudioManager.STREAM_MUSIC,0);
        }

        try{
            AssetManager assetManager = getAssets();
            AssetFileDescriptor descriptor;

            descriptor = assetManager.openFd("fx1.ogg");
            idFX1 = sp.load(descriptor, 0);
            descriptor = assetManager.openFd("fx2.ogg");
            idFX2 = sp.load(descriptor, 0);
            descriptor = assetManager.openFd("fx3.ogg");
            idFX3 = sp.load(descriptor, 0);

        } catch (IOException e) {
            Log.e("msg", "failed to load sound file");
        }
        SeekBar seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                volume = i / 10f;
                sp.setVolume(nowPlaying, volume, volume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final Spinner spinner = findViewById(R.id.spinner);
        spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String temp = String.valueOf(spinner.getSelectedItem());
                repeats = Integer.valueOf(temp);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnStop:
                sp.stop(nowPlaying);
                break;
            case R.id.btnFX1:
                sp.stop(nowPlaying);
                nowPlaying = sp.play(idFX1,volume,volume,0,repeats,1 );
                break;
            case R.id.btnFX2:
                sp.stop(nowPlaying);
                nowPlaying = sp.play(idFX2,volume,volume,0,repeats,1 );
                break;
            case R.id.btnFX3:
                sp.stop(nowPlaying);
                nowPlaying = sp.play(idFX3,volume,volume,0,repeats,1 );
                break;
        }
    }
}