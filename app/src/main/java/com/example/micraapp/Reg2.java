package com.example.micraapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Reg2 extends AppCompatActivity {
    private TextInputLayout tlfirstname,tlmiddlename,tllastname,tldateofbirth;
    private TextInputEditText tefirstname,temiddlename,telastname,tedateofbirth;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private RadioGroup radioGroup;
    private RadioButton radioMale,radioFemale,radioRNS;

    String firstname = "KEY_FNAME";
    String midname = "KEY_MNAME";
    String lastname = "KEY_LNAME";
    String dob = "KEY_DOB";
    String gender = "KEY_GENDER";
    SharedPreferences sharedPreferences2;
    SharedPreferences.Editor editor;
    private Button btnback, btnproceed;
    private boolean haserror,haserror2,haserror3,haserror4 = false;


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
        setContentView(R.layout.activity_reg2);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        sharedPreferences2 = getSharedPreferences("Reg2", Context.MODE_PRIVATE);


        tefirstname = findViewById(R.id.tefirstname);
        temiddlename = findViewById(R.id.temiddlename);
        telastname= findViewById(R.id.telastname);
        tedateofbirth = findViewById(R.id.tedateofbirth);

        tlfirstname = findViewById(R.id.tlfirstname);
        tlmiddlename = findViewById(R.id.tlmiddlename);
        tllastname= findViewById(R.id.tllastname);
        tldateofbirth = findViewById(R.id.tldateofbirth);

        radioGroup = findViewById(R.id.radioSex);
        radioMale = findViewById(R.id.radioMale);
        radioFemale = findViewById(R.id.radioFemale);

        btnback = findViewById(R.id.btnback);
        btnproceed = findViewById(R.id.btnproceed);


        if(!sharedPreferences2.contains(firstname)){
            tefirstname.setFocusable(true);
        }else{
            tefirstname.setText(sharedPreferences2.getString(firstname,""));

        }

        if(!sharedPreferences2.contains(midname)){
            temiddlename.setFocusable(true);
        }else{
            temiddlename.setText(sharedPreferences2.getString(midname,""));

        }

        if(!sharedPreferences2.contains(lastname)){
            telastname.setFocusable(true);
        }else{
            telastname.setText(sharedPreferences2.getString(lastname,""));

        }

        if(!sharedPreferences2.contains(dob)){
            tedateofbirth.setFocusable(true);
        }else{
            tedateofbirth.setText(sharedPreferences2.getString(dob,""));

        }

        if(!sharedPreferences2.contains(gender)){
            radioMale.setChecked(true);
        }else{
            String savedgender = sharedPreferences2.getString(gender,"");
            if( savedgender.equals("Male")){
                radioMale.setChecked(true);
            }else if( savedgender.equals("Female")){
                radioFemale.setChecked(true);
            }
        }
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

      /*  if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {

            new AlertDialog.Builder(Reg2.this)
                    .setTitle("GPS Location")
                    .setMessage("To use this app please turn on your gps location!")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which)
                        {


                            getLastLocation();
                        }
                    })
                    .show();

        }*/
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editor = sharedPreferences2.edit();
                editor.putString(firstname,tefirstname.getText().toString());
                editor.putString(midname,temiddlename.getText().toString());
                editor.putString(lastname,telastname.getText().toString());
                editor.putString(dob,tedateofbirth.getText().toString());
                if(radioMale.isChecked()) {

                    editor.putString(gender,radioMale.getText().toString());
                }else if(radioFemale.isChecked()){

                    editor.putString(gender,radioFemale.getText().toString());
                }else if(radioRNS.isChecked()){

                    editor.putString(gender,radioRNS.getText().toString());
                }
                editor.apply();
                onBackPressed();
                finish();


            }
        });



        tedateofbirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog  =  new DatePickerDialog(
                        Reg2.this, R.style.MyTheme,dateSetListener,year,month,day
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff9c9c")));
                dialog.show();


            }
        });

        dateSetListener=  new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {

                month =  month +1;
                String date =  month + "/" + day + "/"+  year;
                tedateofbirth.setText(date);
            }
        };

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radioMale:

                        Toast.makeText(Reg2.this,"Male",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioFemale:
                        Toast.makeText(Reg2.this,"Female",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioRNS:
                        Toast.makeText(Reg2.this,"Rather not say",Toast.LENGTH_SHORT).show();
                        break;

                }

            }
        });

        btnproceed.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {



                if (!isValidFirstname(tefirstname.getText().toString())) {
                    haserror = true;
                    tlfirstname.setError("Firstname must be Alphabetical characters [a-Z]");
                }else{
                    haserror = false;
                }

                if(!TextUtils.isEmpty(temiddlename.getText().toString())){
                    if (!isValidMiddleName(temiddlename.getText().toString())) {
                        haserror2 = true;
                        tlmiddlename.setError("Middlename must be Alphabetical characters [a-Z]");
                    }else{
                        haserror2 = false;
                    }

                }else{
                    temiddlename.setText(null);
                }

                if(!isValidLastname( telastname.getText().toString())){
                    haserror3= true;
                    tllastname.setError("Lastname must be Alphabetical characters [a-Z]");
                }else{
                    haserror3= false;

                }
                String vdob =tedateofbirth.getText().toString();
                if(vdob.length() <= 0){
                    haserror4 = true;
                    tldateofbirth.setError("Date of birth must be selected");
                }else{
                    haserror4 = false;
                }


                if(haserror == false && haserror2 == false && haserror3 == false && haserror4 == false){

                    proceed();

                }

            }
        });

        tefirstname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    tlfirstname.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        temiddlename.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() <= 0) {
                    tlmiddlename.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        telastname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    tllastname.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tedateofbirth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    tldateofbirth.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void proceed(){
        editor = sharedPreferences2.edit();
        editor.clear();
        editor.putString(firstname,tefirstname.getText().toString());
        editor.putString(midname,temiddlename.getText().toString());
        editor.putString(lastname,telastname.getText().toString());
        editor.putString(dob,tedateofbirth.getText().toString());
        if(radioMale.isChecked()) {

            editor.putString(gender,radioMale.getText().toString());
        }else if(radioFemale.isChecked()){

            editor.putString(gender,radioFemale.getText().toString());
        }else if(radioRNS.isChecked()){

            editor.putString(gender,radioRNS.getText().toString());
        }
        editor.apply();
        editor.commit();
        Intent intent = new Intent(Reg2.this,Reg3.class);
        startActivity(intent);
    }
    public static boolean isValidFirstname(String vfirstname) {
        Matcher matcher = Pattern.compile("^[a-zA-Z ]+$").matcher(vfirstname);
        return matcher.matches();

    }
    public static boolean isValidMiddleName(String vmidname) {
        Matcher matcher = Pattern.compile("^[a-zA-Z ]+$").matcher(vmidname);
        return matcher.matches();

    }
    public static boolean isValidLastname(String vlastname) {
        Matcher matcher = Pattern.compile("^[a-zA-Z ]+$").matcher(vlastname);
        return matcher.matches();

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

                            geocoder = new Geocoder(Reg2.this, Locale.getDefault());

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
