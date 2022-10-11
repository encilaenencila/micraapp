package com.example.micraapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private Button btnlogin,btncreate;
    private TextInputEditText teusername,tepassword;
    private TextInputLayout tlusername,tlpassword;
    private String nullorempty = "Username cannot be empty";
    private String nullorempty2 = "Password cannot be empty";
    private TextView tvcreate,tvforgetpassword;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;


    BroadcastReceiver broadcastReceiver;


    FusedLocationProviderClient mFusedLocationClient;
    Double lat, lng;
    Geocoder geocoder;
    List<Address>Addresses;
    private LatLng latLng;
    String Getlat, Getlong;

    //shared reference to save userinfo and fetch anywhere the project

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        broadcastReceiver = new NetworkListener();
        restorecon();

        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        btnlogin =  findViewById(R.id.btnlogin);


        teusername =  findViewById(R.id.teusername);
        tepassword =  findViewById(R.id.tepassword);
        tlusername =findViewById(R.id.tlusername);
        tlpassword = findViewById(R.id.tlpassword);

        tvcreate = findViewById(R.id.tvcreate);
        checkAndroidVersion();

        String result = getIntent().getStringExtra("Register");
        if(result != null){


            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Account Verification")
                    .setMessage("Thank You!\nPlease give us maximum of 3 working days to verify your application")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }

        String changepass = getIntent().getStringExtra("changepass");
        if(changepass != null){


            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Reset Password")
                    .setMessage("Password successfully changed!")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }

        String resubmit = getIntent().getStringExtra("Resubmit");
        if(changepass != null){


            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Account Resubmission")
                    .setMessage("Thank You!\nPlease give us maxmimum of 3 working days to verify your application")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }

        tvcreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegister1();
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean emptyusername = false;
                boolean emptypassword = false;
                String strUserName = teusername.getText().toString();
                String strPassName = tepassword.getText().toString();
                if(strUserName.length() > 0) {
                    tlusername.setError(null);
                } else {
                    emptyusername =true;
                    tlusername.setError(nullorempty);
                }

                if(strPassName.length() > 0) {
                    tlpassword.setError(null);
                } else {
                    emptypassword =true;
                    tlpassword.setError(nullorempty2);
                }

                if(emptyusername == false && emptypassword == false){


                    String type = "login";
                    LoginWorker loginWorker = new LoginWorker(MainActivity.this);
                    loginWorker.execute(type, teusername.getText().toString().trim(), tepassword.getText().toString().trim());
                   tepassword.setText("");
                   teusername.setText("");
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
    }



    public void openRegister1() {
        Intent intent = new Intent(MainActivity.this, Reg1.class);
        startActivity(intent);

    }
    public void Changepass(View v){
        Intent intent = new Intent(MainActivity.this,SendOTP.class);
        startActivity(intent);
    }

    protected void restorecon() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(broadcastReceiver, new IntentFilter((ConnectivityManager.CONNECTIVITY_ACTION)));
        }
    }

    protected  void unrestorecon(){
        try {
            unregisterReceiver(broadcastReceiver);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unrestorecon();
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


    private void checkAndroidVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkAndRequestPermissions();

        } else {
            checkAndRequestPermissions();
        }

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
                    getLastLocation();
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

                            geocoder = new Geocoder(MainActivity.this, Locale.getDefault());

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
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }



    @Override
    public void onBackPressed() {
        if (true) {
            finish();
        } else {

        }
    }


}
