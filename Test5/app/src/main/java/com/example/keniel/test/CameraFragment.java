package com.example.keniel.test;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;


/**
 * A simple {@link Fragment} subclass.
 */
public class CameraFragment extends Fragment {
    Bitmap photo;
    Button button ;
    ImageView imageView;
    View view;
    private Uri fileuri;
    Uri selectedImage;
    private static final int ACTIVITY_START_CAMERA_APP = 0;
    private ImageView mPhotoCapturedImageView;
    private String mImageFileLocation = "";

    static final int CAM_REQUEST = 1;
    public CameraFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_camera, container, false);
        button = (Button) view.findViewById(R.id.picturebutton);
        imageView = (ImageView) view.findViewById(R.id.picture);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent callCameraApplicationIntent = new Intent();
                callCameraApplicationIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

                File photoFile = null;
                try {
                    photoFile = createImageFile();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                callCameraApplicationIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));

                startActivityForResult(callCameraApplicationIntent, ACTIVITY_START_CAMERA_APP);
            }
        });
        return view;
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == ACTIVITY_START_CAMERA_APP && resultCode == RESULT_OK) {
            // Toast.makeText(this, "Picture taken successfully", Toast.LENGTH_SHORT).show();
            // Bundle extras = data.getExtras();
            // Bitmap photoCapturedBitmap = (Bitmap) extras.get("data");
            // mPhotoCapturedImageView.setImageBitmap(photoCapturedBitmap);
            // Bitmap photoCapturedBitmap = BitmapFactory.decodeFile(mImageFileLocation);
            // mPhotoCapturedImageView.setImageBitmap(photoCapturedBitmap);
            setReducedImageSize();

        }



    }
    void setReducedImageSize() {
        int targetImageViewWidth = imageView.getWidth();
        int targetImageViewHeight = imageView.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        Log.i(mImageFileLocation.toString(),"hi");
        BitmapFactory.decodeFile(mImageFileLocation, bmOptions);
        int cameraImageWidth = bmOptions.outWidth;
        int cameraImageHeight = bmOptions.outHeight;

        int scaleFactor = Math.min(cameraImageWidth/targetImageViewWidth, cameraImageHeight/targetImageViewHeight);
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inJustDecodeBounds = false;

        Bitmap photoReducedSizeBitmp = BitmapFactory.decodeFile(mImageFileLocation, bmOptions);
        imageView.setImageBitmap(photoReducedSizeBitmp);


    }
    File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMAGE_" + timeStamp + "_";
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(imageFileName,".jpg", storageDirectory);
        mImageFileLocation = image.getAbsolutePath();

        return image;

    }




}
