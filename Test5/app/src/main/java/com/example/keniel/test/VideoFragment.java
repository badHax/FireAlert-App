package com.example.keniel.test;


import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFragment extends Fragment {

    Button button;
    View view;
    private final int VIDEO_REQUEST_CODE = 1;

    public VideoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_video, container, false);
        button = (Button) view.findViewById(R.id.recordVideo);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                File video_file = getFilePath();
                Uri video_uri = Uri.fromFile(video_file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,video_uri);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,1);
                startActivityForResult(intent,VIDEO_REQUEST_CODE);

            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VIDEO_REQUEST_CODE){
            if (resultCode == RESULT_OK){

                Uri u = data.getData();
                Map<String, String> params = new HashMap();
                params.put("content_type", "video");
                params.put("description", u.toString());
                params.put("lat",MainActivity.getX().toString());
                params.put("long",MainActivity.getY().toString());
                JSONObject object = new JSONObject(params);

                NetworkManager.getInstance().somePostRequestReturningString(object, new NetworkManager.SomeCustomListener<String>()
                {
                    @Override
                    public void getResult(String result)
                    {
                        if (!result.isEmpty())
                        {

                            //do what you need with the result...
                        }
                    }
                });

            }
            else {
                Toast.makeText(getActivity().getApplicationContext(),"Failed",Toast.LENGTH_LONG).show();
            }
        }
    }

    public File getFilePath(){
        File folder = new File("sdcard/video_app");
        if (!folder.exists()){

            folder.mkdir();
        }

        File video_file = new File(folder,"video.mp4");
        return video_file;
    }

}
