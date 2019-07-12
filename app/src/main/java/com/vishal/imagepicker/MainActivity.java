package com.vishal.imagepicker;



import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.wedowebapps.imagepicker.ImagePikerActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ImageView ivProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivProfile=(ImageView)findViewById(R.id.ivProfile);
        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dexter.withActivity(MainActivity.this)
                        .withPermissions(
                                Manifest.permission.CAMERA,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        try
                        {
                            selectimage();
                        }
                        catch (Exception e)
                        {

                        }
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
                }).check();


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Glide.with(MainActivity.this).load(Uri.parse(data.getStringExtra("ImageUrl"))).into(ivProfile);

    }
    private void selectimage(){


        final CharSequence[] items = {getResources().getString(R.string.take_photo), getResources().getString(R.string.choose_from_library),
                getResources().getString(R.string.cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getResources().getString(R.string.app_name));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                // boolean result = Utility.checkPermission(this);

                if (items[item].equals(getResources().getString(R.string.take_photo)))
                {
                    Intent intent=new Intent(MainActivity.this,ImagePikerActivity.class);
                    intent.putExtra(ImagePikerActivity.ImagePickerType,ImagePikerActivity.Camera);
                    startActivityForResult(intent,ImagePikerActivity.REQUEST_CAMERA);
                }
                else if (items[item].equals(getResources().getString(R.string.choose_from_library)))
                {
                    Intent intent=new Intent(MainActivity.this,ImagePikerActivity.class);
                    intent.putExtra(ImagePikerActivity.ImagePickerType,ImagePikerActivity.Gallery);
                    startActivityForResult(intent,ImagePikerActivity.SELECT_FILE);

                } else if (items[item].equals(getResources().getString(R.string.cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

}
