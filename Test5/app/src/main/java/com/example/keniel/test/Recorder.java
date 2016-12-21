package com.example.keniel.test;

import android.*;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import cafe.adriel.androidaudiorecorder.model.AudioChannel;
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate;
import cafe.adriel.androidaudiorecorder.model.AudioSource;

public class Recorder extends AppCompatActivity {

    private static final int REQUEST_RECORD_AUDIO = 0;
    private static final String AUDIO_FILE_PATH =
            Environment.getExternalStorageDirectory().getPath() + "/recorded_audio.wav";
    String filePath;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(
                    new ColorDrawable(ContextCompat.getColor(this, R.color.colorPrimaryDark)));
        }
        progressDialog = new ProgressDialog(this);
        Util.requestPermission(this, android.Manifest.permission.RECORD_AUDIO);
        Util.requestPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        this.filePath = Environment.getExternalStorageDirectory() + "/recorded_audio.wav";
        int color = getResources().getColor(R.color.colorPrimaryDark);
        int requestCode = 0;
        AndroidAudioRecorder.with(this)
                // Required
                .setFilePath(filePath)
                .setColor(color)
                .setRequestCode(requestCode)

                // Optional
                .setSource(AudioSource.MIC)
                .setChannel(AudioChannel.STEREO)
                .setSampleRate(AudioSampleRate.HZ_48000)
                .setAutoStart(true)
                .setKeepDisplayOn(true)

                // Start recording
                .record();
    }

    private void moveToNewActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_RECORD_AUDIO) {
            if (resultCode == RESULT_OK) {
                progressDialog.setMessage("Uploading Audio");
                progressDialog.show();
                Map<String, String> params = new HashMap();
                params.put("content_type", "audio");
                params.put("description", this.filePath);
                params.put("lat", MainActivity.getX().toString());
                params.put("long",MainActivity.getY().toString());
                JSONObject object = new JSONObject(params);

                NetworkManager.getInstance().somePostRequestReturningString(object, new NetworkManager.SomeCustomListener<String>()
                {
                    @Override
                    public void getResult(String result)
                    {
                        if (!result.isEmpty())
                        {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    progressDialog.dismiss();
                                }}, 30000);
                            //do what you need with the result...
                        }
                    }
                });
                Toast.makeText(this, "Audio Sent and Received by FireStation", Toast.LENGTH_SHORT).show();
                moveToNewActivity();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Audio was not recorded", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
