package com.example.micraapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;


import android.app.Activity;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;

import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Reg4 extends AppCompatActivity {
    Button getIDphoto,getSelfie,btnback,btnproceed;
    ImageView ividphoto,ivselfie;
    TextView tvidname,tvselfiename,tvidentification;
    public Uri IDphotoUri;
    public Uri SelfieUri;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    File IDphotofile;
    File Selfiefile;
    boolean Idphoto = false;
    boolean Selfie = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg4);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);



        getIDphoto  = (Button) findViewById(R.id.btngetidphoto);
        getSelfie  = (Button) findViewById(R.id.btngetselfie);
        btnback  = (Button) findViewById(R.id.btnback);
        btnproceed  = (Button) findViewById(R.id.btnproceed);
        ividphoto =  (ImageView) findViewById(R.id.ividphoto);
        ivselfie =  (ImageView) findViewById(R.id.ivselfie);


        tvidname = (TextView)findViewById(R.id.tvidname);
        tvselfiename = (TextView)findViewById(R.id.tvselfiename);
        tvidentification = findViewById(R.id.tvidentification);
        openDialog();
        checkAndroidVersion();
        loadIdphoto();
        loadSelfie();



        getIDphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                deleteidphoto();
                CaptureIDphoto();
            }
        });


        getSelfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                deleteselfie();
                CaptureSelfie();
            }
        });

        btnproceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int ividphototoDP = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
                int ivselfietoDP = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());

                if(ividphoto.getLayoutParams().width != ividphototoDP && ivselfie.getLayoutParams().width != ivselfietoDP ){
                    openDialog();
                }else{
                    Intent intent = new Intent(Reg4.this,Reg5.class);
                    startActivity(intent);
                }
            }
        });


        tvidentification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });


    }



    private void CaptureIDphoto() {
        Idphoto = true;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(this.getPackageManager()) != null) {
            IDphotofile = null;
            try {

                IDphotofile = createImageFileIdphoto();
            } catch (IOException e) {
                e.printStackTrace();
            }
            IDphotoUri = null;
            if (IDphotofile != null) {
                IDphotoUri = Uri.fromFile(IDphotofile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, IDphotoUri);
                startActivityForResult(intent, REQUEST_ID_MULTIPLE_PERMISSIONS);
            }
        }else{
            Log.d("","Camera cancelled");
        }
    }
    //this method conrtradicts REQUEST PERMISSION.
    private File createImageFileIdphoto() throws IOException {

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        // File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile("idphoto", ".jpg", storageDir);
    }
    private void CaptureSelfie() {
        Selfie = true;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(this.getPackageManager()) != null) {
            Selfiefile = null;
            try {

                Selfiefile = createImageFileSelfie();
            } catch (IOException e) {
                e.printStackTrace();
            }
            SelfieUri = null;
            if (Selfiefile != null) {
                SelfieUri = Uri.fromFile(Selfiefile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, SelfieUri);
                startActivityForResult(intent, REQUEST_ID_MULTIPLE_PERMISSIONS);
            }
        }else{
            //  Log.d("","Camera cancelled");
        }
    }
    //this method conrtradicts REQUEST PERMISSION.
    private File createImageFileSelfie() throws IOException {

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        // File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        return File.createTempFile("selfie", ".jpg", storageDir);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(Idphoto == true) {

            if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {
                if (resultCode == RESULT_OK) {
                    loadIdphoto();
                }else{
                    IDphotofile.delete();

                    Log.d("","Camera cancelled");
                }

            }
        }



        if(Selfie == true) {
            if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {
                if (resultCode == RESULT_OK) {
                    loadSelfie();
                }else{
                    Selfiefile.delete();
                    Log.d("","Camera cancelled");
                }

            }
        }
    }



    public void loadIdphoto() {
        try{


            String path = Environment.getExternalStorageDirectory().toString()+"/Pictures";
            Log.d("Files", "Path: " + path);
            File directory = new File(path);
            File[] files = directory.listFiles();
            Log.d("Files", "Count : "+ files.length);
            for (int i = 0; i < files.length; i++) {
                Log.d("Files", "FileName:" + files[i].getName());
                String fname = files[i].getName();
                if (fname.startsWith("idphoto")) {
                    File loadfile = new File(Environment.getExternalStorageDirectory().toString() + "/Pictures", fname);
                    if (loadfile.exists()) {
                        Picasso.get().load(loadfile).into(ividphoto);
                        tvidname.setVisibility(View.VISIBLE);
                        tvidname.setText("idphoto.jpg");
                    }
                    Log.e("File", "loaded " + loadfile);
                } else {
                    Log.e("File", "not loaded ");
                }

            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public void loadSelfie() {
        try {


            String path = Environment.getExternalStorageDirectory().toString() + "/Pictures";
            Log.d("Files", "Path: " + path);
            File directory = new File(path);
            File[] files = directory.listFiles();
            Log.d("Files", "Count : " + files.length);
            for (int i = 0; i < files.length; i++) {
                Log.d("Files", "FileName:" + files[i].getName());
                String fname = files[i].getName();
                if (fname.startsWith("selfie")) {
                    File loadfile = new File(Environment.getExternalStorageDirectory().toString() + "/Pictures", fname);
                    if (loadfile.exists()) {
                        int ivselfietoDP = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
                        ivselfie.getLayoutParams().width = ivselfietoDP;
                        Picasso.get()
                                .load(loadfile)
                                .into(ivselfie);
                        //String text = file.getLastPathSegment();
                        tvselfiename.setVisibility(View.VISIBLE);
                        tvselfiename.setText("selfie.jpg");
                    }



                    Log.e("File", "loaded " + loadfile);
                } else {
                    Log.e("File", "not loaded ");
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void deleteidphoto(){

        try{
            String path = Environment.getExternalStorageDirectory().toString()+"/Pictures";
            Log.d("Files", "Path: " + path);
            File directory = new File(path);
            File[] files = directory.listFiles();
            Log.d("Files", "Count : "+ files.length);
            for (int i = 0; i < files.length; i++)
            {
                Log.d("Files", "FileName:" + files[i].getName());
                String fname = files[i].getName();
                if(fname.startsWith("idphoto")){
                    File newfile = new File(Environment.getExternalStorageDirectory().toString()+"/Pictures",fname);
                    if (newfile.exists()) {
                        if (newfile.delete()) {
                            if (newfile.exists()) {
                                newfile.getCanonicalFile().delete();
                                if (newfile.exists()) {
                                    getApplicationContext().deleteFile(newfile.getName());
                                }
                            }
                            Log.e("File", "Deleted " +newfile);
                        } else {
                            Log.e("File", "not Deleted " +newfile);
                        }}
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public void deleteselfie() {

        try {
            String path = Environment.getExternalStorageDirectory().toString() + "/Pictures";
            Log.d("Files", "Path: " + path);
            File directory = new File(path);
            File[] files = directory.listFiles();
            Log.d("Files", "Count : " + files.length);
            for (int i = 0; i < files.length; i++) {
                Log.d("Files", "FileName:" + files[i].getName());
                String fname = files[i].getName();
                if (fname.startsWith("selfie")) {
                    File newfile = new File(Environment.getExternalStorageDirectory().toString() + "/Pictures", fname);
                    if (newfile.exists()) {
                        if (newfile.delete()) {
                            if (newfile.exists()) {
                                newfile.getCanonicalFile().delete();
                                if (newfile.exists()) {
                                    getApplicationContext().deleteFile(newfile.getName());
                                }
                            }
                            Log.e("File", "Deleted " + newfile);
                        } else {
                            Log.e("File", "not Deleted " + newfile);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    private void checkAndroidVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkAndRequestPermissions();

        } else {
            checkAndRequestPermissions();
        }

    }


    private boolean checkAndRequestPermissions() {
        int camera = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        int wtite = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (wtite != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (read != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("Request", "Permission callback");
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Log.d("Request", "CAMERA & WRITE_EXTERNAL_STORAGE READ_EXTERNAL_STORAGE permission granted");
                    } else {
                        Log.d("Request", "Permission not granted.. ");
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            showDialogOK("Access permission for Camera and Storage is required for this App!",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    break;
                                            }
                                        }
                                    });
                        }
                        else {
                            Toast.makeText(this, "Go to settings and enable permissions manually", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                }
            }




        }




    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    public void openDialog() {
        final Dialog dialog = new Dialog(this); // Context, this, etc.
        dialog.setContentView(R.layout.dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);

        dialog.setCancelable(false);

        TextView btnOkay  = (TextView) dialog.findViewById(R.id.btnok);
        btnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (btnback.isPressed()) {
            super.onBackPressed();
        } else {

        }
    }
}