package com.example.micraapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Reg5 extends AppCompatActivity {
    private TextView tvusername, tvpassword, tvnumber, tvemail, tvfirstname, tvmidname, tvlastname, tvgender, tvdateofbirth, tvaddressline, tvidname, tvselfiename;
    private Button btnback, btnsubmit;
    ImageView ividphoto, ivselfie;
    String username = "KEY_USER";
    String password = "KEY_PASS";
    String number = "KEY_NUM";
    String email = "KEY_EMAIL";
    String firstname = "KEY_FNAME";
    String midname = "KEY_MNAME";
    String lastname = "KEY_LNAME";
    String gender = "KEY_GENDER";
    String dob = "KEY_DOB";
    String address = "KEY_ADDRESS";

    private static final String TAG = "PushNotification";
    private static final String CHANNEL_ID = "101";
    private String devicetoken;
    private String selectedidphoto;
    private String selectedselfie;
    SharedPreferences sharedPreferences, sharedPreferences2, sharedPreferences3;
    SharedPreferences.Editor editor, editor2, editor3;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg5);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        sharedPreferences = getSharedPreferences("Reg1", Context.MODE_PRIVATE);
        sharedPreferences2 = getSharedPreferences("Reg2", Context.MODE_PRIVATE);
        sharedPreferences3 = getSharedPreferences("Reg3", Context.MODE_PRIVATE);
        //ACCOUNT
        tvusername = findViewById(R.id.tvusername);
        tvpassword = findViewById(R.id.tvpassword);
        tvnumber = findViewById(R.id.tvnumber);
        tvemail = findViewById(R.id.tvemail);

        //PROFILE
        tvfirstname = findViewById(R.id.tvfirstname);
        tvmidname = findViewById(R.id.tvmidname);
        tvlastname = findViewById(R.id.tvlastname);
        tvgender = findViewById(R.id.tvgender);
        tvdateofbirth = findViewById(R.id.tvdateofbirth);
        // Images
        tvselfiename = findViewById(R.id.tvselfiename);
        tvidname = findViewById(R.id.tvidname);

        //ADDRESLINE
        tvaddressline = findViewById(R.id.tvaddressline);


        // Identification
        ividphoto = findViewById(R.id.ividphoto);
        ivselfie = findViewById(R.id.ivselfie);
        //BUTTONS
        btnback = findViewById(R.id.btnback);
        btnsubmit = findViewById(R.id.btnsubmit);
        //progressbar
        // progressBar = findViewById(R.id.progressBar);
        //load captured images from imagepath

        if (sharedPreferences.contains(username)) {

            tvusername.setText(sharedPreferences.getString(username, ""));
        }
        if (sharedPreferences.contains(password)) {
            tvpassword.setText(sharedPreferences.getString(password, ""));
        }

        if (sharedPreferences.contains(number)) {
            String phonenumber = "0"+sharedPreferences.getString(number, "");
            tvnumber.setText(phonenumber);
        }
        if (sharedPreferences.contains(email)) {
            tvemail.setText(sharedPreferences.getString(email, ""));
        }


        if (sharedPreferences2.contains(firstname)) {
            String fn = sharedPreferences2.getString(firstname, "");

            fn = fn.substring(0, 1).toUpperCase() + fn.substring(1).toLowerCase();
            tvfirstname.setText(fn);
        }
        if (sharedPreferences2.contains(midname)) {
            String mn = sharedPreferences2.getString(midname, "");
            if (mn.length() <= 0) {

                tvmidname.setText("NA");
            } else {
                mn = mn.substring(0, 1).toUpperCase() + mn.substring(1).toLowerCase();
                tvmidname.setText(mn);

            }

        }

        if (sharedPreferences2.contains(lastname)) {
            String ln = sharedPreferences2.getString(lastname, "");

            ln = ln.substring(0, 1).toUpperCase() + ln.substring(1).toLowerCase();

            tvlastname.setText(ln);
        }
        if (sharedPreferences2.contains(gender)) {
            tvgender.setText(sharedPreferences2.getString(gender, ""));
        }
        if (sharedPreferences2.contains(dob)) {
            tvdateofbirth.setText(sharedPreferences2.getString(dob, ""));
        }

        if (sharedPreferences3.contains(address)) {
            tvaddressline.setText(sharedPreferences3.getString(address, ""));
        }

        loadIdphoto();
        loadSelfie();
        UploadIDphoto();
        UploadSelfie();
        getToken();
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Log.d("Connectivity: ", "Mobile Data");
                new AlertDialog.Builder(Reg5.this)

                        .setTitle("Confirm Submission")
                        .setMessage("Do you want to submit this application?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Registration();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Perform Your Task Here--When No is pressed
                                dialog.cancel();

                            }
                        }).show();


            }

        });


    }

    private void getToken() {


        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        devicetoken = task.getResult();

                        // Log and toast

                        Log.d(TAG, "onComplete: " + devicetoken);
                    }
                });
    }


    public void Registration() {
        progressDialog = progressDialog.show(Reg5
                .this, "Please wait", "submitting your application..");


        String url = "http://www.micra.services/Registration/Registration.php";
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        if (response.equals("success")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Reg5.this);
                            builder.setMessage("Application Submitted.")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            clearSharedPref();
                                            deleteidphoto();
                                            deleteselfie();
                                            Intent intent = new Intent(Reg5.this, MainActivity.class);
                                            intent.putExtra("Register", "Done");
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();

                        } else {
                            Toast.makeText(getApplicationContext(), "Something went wrong?", Toast.LENGTH_LONG).show();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //error.printStackTrace();
                progressDialog.dismiss();
                Log.d("Error in connection", error.toString());
                if (error instanceof NoConnectionError) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(Reg5.this);
                    builder.setMessage("Unable to connect to the server! Please ensure your internet is stable!")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    clearSharedPref();
                                    deleteidphoto();
                                    deleteselfie();
                                    Intent intent = new Intent(Reg5.this, MainActivity.class);
                                    intent.putExtra("Register", "Done");
                                    startActivity(intent);
                                    finish();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", tvusername.getText().toString());
                params.put("password", tvpassword.getText().toString());
                params.put("number", tvnumber.getText().toString());
                params.put("email", tvemail.getText().toString());
                params.put("firstname", tvfirstname.getText().toString());
                params.put("middlename", tvmidname.getText().toString());
                params.put("lastname", tvlastname.getText().toString());
                params.put("birthdate", tvdateofbirth.getText().toString());
                params.put("gender", tvgender.getText().toString());
                params.put("address", tvaddressline.getText().toString());
                params.put("idphoto", selectedidphoto);
                params.put("selfie", selectedselfie);
                params.put("isVerified", "No");
                params.put("devicetoken", devicetoken);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                /* params.put("Content-Type", "application/x-www-form-urlencoded");*/
                return params;
            }

            @Override
            public Request.Priority getPriority() {
                return Request.Priority.HIGH;
            }
        };

        // To prevent timeout error
        //  stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,-1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);

    }

    public Bitmap ReduceSize(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float ratio = (float) width / (float) height;
        if (ratio > 1) {
            width = maxSize;
            height = (int) (width / ratio);
        } else {
            height = maxSize;
            width = (int) (height * ratio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


    public void UploadIDphoto() {
        try {
            String path = Environment.getExternalStorageDirectory().toString() + "/Pictures";
            Log.d("Files", "Path: " + path);
            File directory = new File(path);
            File[] files = directory.listFiles();
            Log.d("Files", "Count : " + files.length);
            for (int i = 0; i < files.length; i++) {
                Log.d("Files", "FileName:" + files[i].getName());
                String fname = files[i].getName();
                if (fname.startsWith("idphoto")) {
                    File loadfile = new File(Environment.getExternalStorageDirectory().toString() + "/Pictures", fname);
                    if (loadfile.exists()) {
                        String idphotopath = loadfile.getPath().toString();
                        Log.d("", "idphotopath" + idphotopath);


                        Bitmap bitmap = BitmapFactory.decodeFile(idphotopath);
                        try {
                            // Determine Orientation
                            ExifInterface exifInterface = new ExifInterface(idphotopath);
                            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

                            // Determine Rotation
                            int rotation = 0;
                            if (orientation == 6) rotation = 90;
                            else if (orientation == 3) rotation = 180;
                            else if (orientation == 8) rotation = 270;

                            // Rotate Image if Necessary
                            if (rotation != 0) {
                                // Create Matrix
                                Matrix matrix = new Matrix();
                                matrix.postRotate(rotation);

                                // Rotate Bitmap
                                Bitmap rotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                                Bitmap resize = ReduceSize(rotated, 500);


                                bitmap.recycle();
                                bitmap = resize;
                                resize = null;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] imageBytes = baos.toByteArray();
                        selectedidphoto = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                        Log.d("THIS SIZE 20", selectedidphoto);
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


    public void UploadSelfie() {
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
                        String selfiepath = loadfile.getPath().toString();

                        Log.d("", "idphotopath" + selfiepath);


                        Bitmap bitmap = BitmapFactory.decodeFile(selfiepath);
                        try {
                            // Determine Orientation
                            ExifInterface exif = new ExifInterface(selfiepath);
                            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

                            // Determine Rotation
                            int rotation = 0;
                            if (orientation == 6) rotation = 90;
                            else if (orientation == 3) rotation = 180;
                            else if (orientation == 8) rotation = 270;

                            // Rotate Image if Necessary
                            if (rotation != 0) {
                                // Create Matrix
                                Matrix matrix = new Matrix();
                                matrix.postRotate(rotation);

                                Bitmap rotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                                Bitmap resize = ReduceSize(rotated, 500);


                                bitmap.recycle();
                                bitmap = resize;
                                resize = null;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] imageBytes = baos.toByteArray();
                        selectedselfie = Base64.encodeToString(imageBytes, Base64.DEFAULT);


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


    public void loadIdphoto() {
        try {
            String path = Environment.getExternalStorageDirectory().toString() + "/Pictures";
            Log.d("Files", "Path: " + path);
            File directory = new File(path);
            File[] files = directory.listFiles();
            Log.d("Files", "Count : " + files.length);
            for (int i = 0; i < files.length; i++) {
                Log.d("Files", "FileName:" + files[i].getName());
                String fname = files[i].getName();
                if (fname.startsWith("idphoto")) {
                    File loadfile = new File(Environment.getExternalStorageDirectory().toString() + "/Pictures", fname);
                    if (loadfile.exists()) {
                        int ividphototoDP = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
                        ividphoto.getLayoutParams().width = ividphototoDP;

                         Picasso.get()
                                .load(loadfile)
                                .into(ividphoto);
                        // String text = file.getLastPathSegment();
                        tvidname.setVisibility(View.VISIBLE);
                        tvidname.setText("idphoto.jpg");

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

    public void deleteidphoto() {

        try {
            String path = Environment.getExternalStorageDirectory().toString() + "/Pictures";
            Log.d("Files", "Path: " + path);
            File directory = new File(path);
            File[] files = directory.listFiles();
            Log.d("Files", "Count : " + files.length);
            for (int i = 0; i < files.length; i++) {
                Log.d("Files", "FileName:" + files[i].getName());
                String fname = files[i].getName();
                if (fname.startsWith("idphoto")) {
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

    public void clearSharedPref() {

        editor = sharedPreferences.edit();
        editor2 = sharedPreferences2.edit();
        editor3 = sharedPreferences3.edit();
        editor.clear().commit();
        editor2.clear().commit();
        editor3.clear().commit();
    }

    @Override
    public void onBackPressed() {
        if (true) {

        } else {

        }

    }


}