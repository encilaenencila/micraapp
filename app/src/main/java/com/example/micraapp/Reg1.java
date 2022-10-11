package com.example.micraapp;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Region;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Reg1 extends AppCompatActivity {

    TextInputEditText teusername, tepassword,teregnumber,teregemail;
    TextInputLayout tlusername, tlpassword,tlregnumber,tlregemail;
    Button btnproceed,btnback,btnremove;
    String username = "KEY_USER";
    String password = "KEY_PASS";
    String number = "KEY_NUM";
    String email = "KEY_EMAIL";
    private boolean haserror,haserror2,haserror3,haserror4 = false;


    SharedPreferences sharedPreferences, sharedPreferences2, sharedPreferences3;
    SharedPreferences.Editor editor, editor2, editor3;


    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    FusedLocationProviderClient mFusedLocationClient;
    Double lat, lng;
    Geocoder geocoder;
    List<Address> Addresses;
    private LatLng latLng;
    String Getlat, Getlong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg1);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        teusername = findViewById(R.id.teusername);
        tepassword = findViewById(R.id.tepassword);
        teregnumber = findViewById(R.id.teregnumber);
        teregemail = findViewById(R.id.teregemail);

        tlusername = findViewById(R.id.tlusername);
        tlpassword = findViewById(R.id.tlpassword);
        tlregnumber = findViewById(R.id.tlregnumber);
        tlregemail  = findViewById(R.id.tlregemail);

        btnback = findViewById(R.id.btnback);
        btnproceed = findViewById(R.id.btnproceed);


        sharedPreferences = getSharedPreferences("Reg1",Context.MODE_PRIVATE);
        sharedPreferences2 = getSharedPreferences("Reg2", Context.MODE_PRIVATE);
        sharedPreferences3 = getSharedPreferences("Reg3", Context.MODE_PRIVATE);


        if(!sharedPreferences.contains(username)){
            teusername.setFocusable(true);
        }else{
            teusername.setText(sharedPreferences.getString(username,""));

        }

        if(!sharedPreferences.contains(password)){
            tepassword.setFocusable(true);
        }else{
            tepassword.setText(sharedPreferences.getString(password,""));
        }
        if(!sharedPreferences.contains(number)){
            teregnumber.setFocusable(true);
        }else{
            teregnumber.setText(sharedPreferences.getString(number,""));
        }

        if(!sharedPreferences.contains(email)){
            teregemail.setFocusable(true);
        }else{
            teregemail.setText(sharedPreferences.getString(email,""));
        }

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteidphoto();
                deleteselfie();
                clearSharedPref();
                onBackPressed();
                finish();
            }
        });

        btnproceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (!isValidUsername(teusername.getText().toString()) ) {
                    haserror = true;
                    tlusername.setError("Username must be Letters. Numbers. Length (8-30) characters");
                }else{
                    haserror = false;
                }
                if (!isValidPassword(tepassword.getText().toString())) {
                    haserror2 = true;
                    tlpassword.setError("Password must be Upper & Lowercase Letters. Numbers. Special characters. Length (8-30)");
                }else{
                    haserror2 = false;
                }
                if(!isValidPhoneNumber(tlregnumber.getPrefixText().toString() + teregnumber.getText().toString())){
                    haserror3 = true;
                    tlregnumber.setError("Phone number must be (+639*********)");
                }else{
                    haserror3 = false;
                }
                if(!isValidEmail(teregemail.getText().toString())){
                    haserror4 = true;
                    tlregemail.setError("Invalid Email Address");
                }else{
                    haserror4  =false;
                }
                if(haserror == false  && haserror2 == false  && haserror3 == false  &&  haserror4 == false ){
                    editor = sharedPreferences.edit();
                    editor.clear();
                    editor.putString(username,teusername.getText().toString());
                    editor.putString(password,tepassword.getText().toString());
                    editor.putString(number, teregnumber.getText().toString());
                    editor.putString(email,teregemail.getText().toString());
                    editor.commit();

                    Intent intent = new Intent(Reg1.this, Reg2.class);
                    startActivity(intent);
                }
            }
        });


        teusername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    tlusername.setError(null);
                    teusername.setFocusable(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tepassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    tlpassword.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        teregnumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    tlregnumber.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        teregemail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    tlregemail.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public static boolean isValidUsername(String username) {
        Matcher matcher = Pattern.compile("^[a-zA-Z0-9]{8,30}$").matcher(username);
        return matcher.matches();

    }

    public static boolean isValidPassword(String password) {
        // Matcher matcher = Pattern.compile("((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,30})").matcher(password);
        Matcher matcher = Pattern.compile("((?=.*[a-z])(?=.*[0-9])(?=.*[A-Z])(?=.*[\\\\!\\\\\\\"\\\\#\\\\$\\\\%\\\\&\\\\'\\\\(\\\\)\\\\*\\\\+\\\\,\\\\-\\\\.\\\\/\\\\:\\\\;\\\\<\\\\>\\\\=\\\\?\\\\@\\\\[\\\\]\\\\{\\\\}\\\\\\\\\\\\^\\\\_\\\\`\\\\~]).{8,30})").matcher(password);
        return matcher.matches();
    }
    private boolean isValidPhoneNumber(CharSequence phoneNumber) {

        String regex1= "(^(09|\\+639)\\d{9}$)";
        String regex2 = "((\\+63)|0)\\d{10}";
        Matcher matcher = Pattern.compile("(^(09|\\+639)\\d{9}$)").matcher(phoneNumber);
        return matcher.matches();
    }
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
        Matcher matcher = Pattern.compile(EMAIL_PATTERN).matcher(email);
        return matcher.matches();
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

    public void clearSharedPref(){

        editor = sharedPreferences.edit();
        editor2 = sharedPreferences2.edit();
        editor3 = sharedPreferences3.edit();
        editor.clear().commit();
        editor2.clear().commit();
        editor3.clear().commit();
    }



    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions()) {

            if (isLocationEnabled()) {


                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {

                            //this is string getlat,getlong
                            Getlat =String.valueOf(location.getLatitude()) ;
                            Getlong =String.valueOf(location.getLongitude());
                           /* latitudeTextView.setText(location.getLatitude() + "");
                            longitTextView.setText(location.getLongitude() + "");*/

                            DecimalFormat form = new DecimalFormat();
                            form.setMaximumFractionDigits(3);

                            //this string getlat,getlong to double lat,long
                            lat = Double.parseDouble(Getlat);
                            lng = Double.parseDouble(Getlong);

                            Double latlat = Double.parseDouble(form.format(lat));
                            Double longlong = Double.parseDouble(form.format(lng));;
                          /*
                            String latlat = form.format(lat);
                            String longlong = form.format(lng);*/

                            /*   latLng = new LatLng(latlat, longlong);*/

                            geocoder = new Geocoder(Reg1.this, Locale.getDefault());

                            String completeaddress,city,state,country,postalCode, KnownName = null;



                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods

        LocationRequest mLocationRequest = LocationRequest.create()
                .setInterval(6)
                .setFastestInterval(3000)
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            Getlat =String.valueOf(mLastLocation.getLatitude()) ;
            Getlong =String.valueOf(mLastLocation.getLongitude());
/*
            Getlat = "" + mLastLocation.getLatitude();
            Getlong = "" + mLastLocation.getLongitude();*/

          /*  latitudeTextView.setText("Latitude: " + mLastLocation.getLatitude() + "");
            longitTextView.setText("Longitude: " + mLastLocation.getLongitude() + "");
*/

        }
    };

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_ID_MULTIPLE_PERMISSIONS);
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }

    @Override
    public void onBackPressed() {
        if (btnback.isPressed()) {
            super.onBackPressed();
        } else {

        }
    }

}