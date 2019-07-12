package com.wedowebapps.imagepicker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImagePikerActivity extends AppCompatActivity
{
    public static String ImagePickerType; //"Camera",Gallery
    public static String Camera="Camera";
    public static String Gallery="Gallery";

    public static String strProvider="com.wedowebapps.imagepicker.provider";

    public static int REQUEST_CAMERA = 0, SELECT_FILE = 1;

    File image;
    Bitmap thumbnail;
    private Uri fileUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getIntent().getStringExtra(ImagePickerType).equalsIgnoreCase(Camera)){
            cameraIntent();
        }
        else  if(getIntent().getStringExtra(ImagePickerType).equalsIgnoreCase(Gallery)){
            galleryIntent();
        }
    }
    private void cameraIntent() {

        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if(pictureIntent.resolveActivity(getPackageManager()) != null){
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            if (photoFile != null)
            {
                Uri photoURI = FileProvider.getUriForFile(this,strProvider, photoFile);


                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        photoURI);
                startActivityForResult(pictureIntent,
                        REQUEST_CAMERA);
            }
        }
    }
    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select file"), SELECT_FILE);
    }
    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return image;
    }
    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        if (data != null) {
            try {
                thumbnail = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                fileUri = data.getData();
                Log.e("fileUri", "fileUri" + fileUri);
                Intent datas = new Intent();
                datas.putExtra("ImageUrl",""+fileUri.toString());
                setResult(RESULT_OK,datas);
                finish();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);
            }
            else if (requestCode == REQUEST_CAMERA)
            {
                fileUri=Uri.fromFile(image);
                Intent datas = new Intent();
                datas.putExtra("ImageUrl",""+fileUri.toString());
                setResult(RESULT_OK,datas);
                finish();
            }





        }
    }
}
