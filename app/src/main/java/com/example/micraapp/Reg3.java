package com.example.micraapp;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.gms.maps.model.LatLng;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

public class Reg3 extends AppCompatActivity {
    String address = "KEY_ADDRESS";
    String autoaddress = "KEY_AUTOADDRESS";

    SharedPreferences sharedPreferences3;
    SharedPreferences.Editor editor;
    // initializing
    // FusedLocationProviderClient
    // object
    FusedLocationProviderClient mFusedLocationClient;
    TextInputEditText teaddressline, tehousenumber, tebarangay;
    TextView tvidentification;

    //Dropdown
    TextInputLayout tlstreet, tlhousenumber, tladdressline;
    AutoCompleteTextView atstreet;
    String[] items = {"Amaloi", "Amuslan", "Bahawan", "Bakil", "Cagna", "Cadang", "Corumi", "Eskinita", "Gabo", "Gasan", "Ilihan", "Inaman", "Lamila", "Mabituan", "Malac", "Malasimbo", "Masola", "Monong", "Payte", "Posooy", "Toctokan", "Wayan"};

    String[] streetlist = {"amaloi", "amuslan", "bahawan", "bakil", "cadang", "cagna", "capoas", "corumi", "eskinita", "gabo", "gasan", "ilihan", "inaman", "lamila", "mabitoan", "mabituan", "malac", "malasimbo", "masola", "monong", "payte", "posooy", "tinaduan", "toctocan", "toctokan", "wayan"
    };

    ArrayAdapter<String> adapterItems;


    private String StrSetaddress = "aweaweawe";
    private String getAddresses = "";
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    Double lat, lng;
    Geocoder geocoder;
    List<Address> Addresses;
    Button getCurrentLocation, btnproc3;
    private LatLng latLng;
    String Getlat, Getlong;

    private Button btngetcurrentloc, btnproceed, btnback;
    private boolean haserror, haserror2 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg3);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        sharedPreferences3 = getSharedPreferences("Reg3", Context.MODE_PRIVATE);

        btnback = findViewById(R.id.btnback);
        btngetcurrentloc = findViewById(R.id.btngetcurrentloc);
        btnproceed = findViewById(R.id.btnproceed);

        teaddressline = findViewById(R.id.teaddressline);
        tvidentification = findViewById(R.id.tvidentification);
        tladdressline = findViewById(R.id.tladdressline);


        getAddresses = sharedPreferences3.getString(address, "");





        if (getAddresses != null && !getAddresses.equals("")) {
            teaddressline.setText(getAddresses);
            // Toast.makeText(this, "not null" + getAddresses, Toast.LENGTH_SHORT).show();
        } else {
            teaddressline.setText(sharedPreferences3.getString(autoaddress, ""));
            //Toast.makeText(this, " null ", Toast.LENGTH_SHORT).show();
        }




        btngetcurrentloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teaddressline.setText(sharedPreferences3.getString(autoaddress, ""));
            }
        });

        btnproceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                openRegister4();

            }
        });
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor = sharedPreferences3.edit();
                editor.putString(address, teaddressline.getText().toString());
                editor.apply();
                onBackPressed();
                finish();
            }
        });

        tvidentification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
        teaddressline.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    tladdressline.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions()) {

            if (isLocationEnabled()) {


                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(Task<Location> task) {
                        Location location = task.getResult();


                        if (location == null) {
                            requestNewLocationData();
                        } else {

                            //Toast.makeText(Reg3.this, ""+ location, Toast.LENGTH_SHORT).show();

                            //this is string getlat,getlong
                            Getlat = String.valueOf(location.getLatitude());
                            Getlong = String.valueOf(location.getLongitude());


                            DecimalFormat form = new DecimalFormat();
                            form.setMaximumFractionDigits(3);

                            //this string getlat,getlong to double lat,long
                            lat = Double.parseDouble(Getlat);
                            lng = Double.parseDouble(Getlong);

                            Double latlat = Double.parseDouble(form.format(lat));
                            Double longlong = Double.parseDouble(form.format(lng));


                            geocoder = new Geocoder(Reg3.this, Locale.getDefault());

                            String completeaddress, city, state, country, postalCode, KnownName = null;


                            try {
                                Addresses = geocoder.getFromLocation(latlat, longlong, 1);
                                Log.d("Addresses geocoder:", "" + Addresses);
                                if (Addresses.size() > 0) {
                                    completeaddress = Addresses.get(0).getAddressLine(0);
                                    Log.d("address", completeaddress);

                                    String comadd = String.format(completeaddress).replaceAll("[,]", "");

                                    editor = sharedPreferences3.edit();
                                    editor.clear();
                                    editor.putString(autoaddress, comadd);
                                    //editor.apply();
                                    editor.commit();
                                    //  teaddressline.setText(String.format(comadd));
                                } else {
                                    completeaddress = Addresses.get(0).getAddressLine(0);
                                    Log.d("address", completeaddress);
                                    String comadd = String.format(completeaddress).replaceAll("[,]", "");

                                    editor = sharedPreferences3.edit();
                                    editor.clear();
                                    editor.putString(autoaddress, comadd);
                                    //editor.apply();
                                    editor.commit();
                                    // teaddressline.setText(String.format(comadd));
                                }

                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }


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
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
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
            Getlat = String.valueOf(mLastLocation.getLatitude());
            Getlong = String.valueOf(mLastLocation.getLongitude());


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

    // If everything is alright then
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

    public void addAddress() {
        teaddressline.setText(StrSetaddress);
    }

    public void openRegister4() {
        String straddress = teaddressline.getText().toString();

        if (straddress.length() <= 0) {
            haserror = true;
            tladdressline.setError("Please fill!");
        } else {
            haserror = false;
        }
        if (!CheckingStreet(teaddressline.getText().toString(), streetlist)) {
            haserror2 = true;
            tladdressline.setError("Address not found!");
        } else {
            haserror2 = false;

        }


        if (haserror == false && haserror2 == false) {

            editor = sharedPreferences3.edit();
            editor.clear();
            editor.putString(address, teaddressline.getText().toString());
            //editor.apply();
            editor.commit();

            Intent intent = new Intent(this, Reg4.class);
            startActivity(intent);

        }

    }

    public static boolean CheckingStreet(String address, String[] streetlist) {
        StringTokenizer test = new StringTokenizer(address);
        String element = "Check if exist";
        while (!element.equals("")) {
            try {
                element = test.nextElement().toString().toLowerCase();
                for (int index = 0; index < streetlist.length; index++) {
                    if (element.equals(streetlist[index])) {
                        return true;
                    }
                }
            } catch (Exception e) {

                System.out.println("Error");
                element = "";
            }
        }
        return false;
    }

    public void openDialog() {
        final Dialog dialog = new Dialog(this); // Context, this, etc.
        dialog.setContentView(R.layout.dialog2);
        dialog.show();
        dialog.setCancelable(false);
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.50);

      dialog.getWindow().setLayout(width, height);
        TextView btnOkay = (TextView) dialog.findViewById(R.id.btnok);

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
