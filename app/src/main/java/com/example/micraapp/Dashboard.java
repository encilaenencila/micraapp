package com.example.micraapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.micraapp.databinding.ActivityDashboardBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Dashboard extends drawerbase implements View.OnClickListener {
    ActivityDashboardBinding activityDashboardBinding;

    List<SliderData> imagelist;
    SliderView sliderView;
    private String getTitle;
    private String getDescription;

    SharedPreferences userinfo;
    private SliderAdapter adapter;
    SharedPreferences.Editor userinfoeditor;

    public CardView Cprofile, Creport, Ccall, cardViewRecordReport, Cannouncement;
    private String LOADINFO_URL = "http://www.micra.services/Micra/resources/files/announcement/FetchAnnouncement.php";
    BroadcastReceiver broadcastReceiver;

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private static final String TAG = "PushNotification";
    private static final String CHANNEL_ID = "101";

    private String Curloc;
    FusedLocationProviderClient mFusedLocationClient;
    Double lat, lng;
    Geocoder geocoder;
    List<Address> Addresses;
    private LatLng latLng;
    String Getlat, Getlong;

    SharedPreferences C1, C2, C3;
    SharedPreferences.Editor C1editor;
    SharedPreferences.Editor C2editor;
    SharedPreferences.Editor C3editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        activityDashboardBinding = ActivityDashboardBinding.inflate(getLayoutInflater());
        allocateActivitytitle("Dashboard");
        setContentView(activityDashboardBinding.getRoot());
        Ccall = (CardView) findViewById(R.id.cardViewCall);
        Creport = (CardView) findViewById(R.id.cardViewReport);
        Cannouncement = (CardView) findViewById(R.id.cardViewAnnouncement);
        cardViewRecordReport = (CardView) findViewById(R.id.cardViewRecordReport);
        sliderView = findViewById(R.id.imageSlider);
        broadcastReceiver = new NetworkListener();
        restorecon();
        imagelist = new ArrayList<>();
        loadAnnouncement();
        checkAndroidVersion();

        String result = getIntent().getStringExtra("Report");
        if(result != null){


            new androidx.appcompat.app.AlertDialog.Builder(Dashboard.this)
                    .setIcon(R.drawable.micralogo)
                    .setTitle("MICRA")
                    .setMessage("Please be advised that your report status is  currently PENDING. Our barangay team is already on top of it to resolve the report as soon as possible. Thank you for bearing with us. Be safe.")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }


        String result2 = getIntent().getStringExtra("Report2");
        if(result2 != null){


            new androidx.appcompat.app.AlertDialog.Builder(Dashboard.this)
                    .setIcon(R.drawable.micralogo)
                    .setTitle("MICRA")
                    .setMessage("Within 24 hours you need to go to the Barangay Hall to settle your report.")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }




        sliderView.setIndicatorAnimation(IndicatorAnimationType.FILL); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.parseColor("#d7011d"));
        sliderView.setIndicatorUnselectedColor(Color.parseColor("#d7011d"));

        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(2); //set scroll delay in seconds :
        sliderView.startAutoCycle();
        sliderView.setCurrentPageListener(new SliderView.OnSliderPageListener() {
            @Override
            public void onSliderPageChanged(int position) {
                SliderData sliderData = imagelist.get(position);

                getTitle = sliderData.getTitle();
                getDescription = sliderData.getDescription();

            }
        });


        userinfo = getSharedPreferences("info", Context.MODE_PRIVATE);

        Ccall.setOnClickListener(this);

        Creport.setOnClickListener(this);
        Cannouncement.setOnClickListener(this);
        cardViewRecordReport.setOnClickListener(this);
        //SHARED REF
        C1 = getSharedPreferences("SPC1", Context.MODE_PRIVATE);
        C2 = getSharedPreferences("SPC2", Context.MODE_PRIVATE);
        C3 = getSharedPreferences("SPC3", Context.MODE_PRIVATE);


        C2editor = C2.edit();
        C3editor = C3.edit();

        C2editor.clear().commit();
        C3editor.clear().commit();

    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {

            case R.id.cardViewReport:
                intent = new Intent(this, category.class);
                startActivity(intent);
                break;
            case R.id.cardViewCall:
                intent = new Intent(this, callmodule.class);
                startActivity(intent);
                break;
            case R.id.cardViewRecordReport:
                intent = new Intent(this, recordreports.class);
                startActivity(intent);
                break;
            case R.id.cardViewAnnouncement:
                intent = new Intent(this, Announcement.class);
                startActivity(intent);
                break;

        }
        CreatFileInternalStorage();
    }


    private void loadAnnouncement() {


        StringRequest stringRequest = new StringRequest(Request.Method.GET, LOADINFO_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {


                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {

                                JSONObject report = array.getJSONObject(i);

                                imagelist.add(new SliderData(

                                        report.getString("title"),
                                        report.getString("image"),
                                        report.getString("description")


                                ));
                            }


                            adapter = new SliderAdapter(Dashboard.this, imagelist);
                            sliderView.setSliderAdapter(adapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        Volley.newRequestQueue(this).add(stringRequest);
    }

    public void ClickDetails(View view) {
        final Dialog dialog = new Dialog(this); // Context, this, etc.
        dialog.setContentView(R.layout.detailsdialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);
        dialog.setCancelable(false);
        // Button btnOkay = (Button) dialog.findViewById(R.id.btnok);
        TextView tvdetails = (TextView) dialog.findViewById(R.id.tvdetails);
        TextInputEditText tetitle = (TextInputEditText) dialog.findViewById(R.id.tetitle);
        TextInputEditText tegetannounce = (TextInputEditText) dialog.findViewById(R.id.tegetannounce);
        tetitle.setText(getTitle.trim());
        tegetannounce.setText(getDescription.trim());


        tvdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

    }


    protected void restorecon() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(broadcastReceiver, new IntentFilter((ConnectivityManager.CONNECTIVITY_ACTION)));
        }
    }

    protected void unrestorecon() {
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unrestorecon();
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
                    getLastLocation();
                    if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        //CreatFileInternalStorage();

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
                        } else {
                            Toast.makeText(this, "Go to settings and enable permissions manually", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                }
            }


        }


    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
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


                            geocoder = new Geocoder(Dashboard.this, Locale.getDefault());

                            String completeaddress, city, state, country, postalCode, KnownName = null;


                            try {
                                Addresses = geocoder.getFromLocation(latlat, longlong, 1);
                                Log.d("Addresses geocoder:", "" + Addresses);
                                List<Address> addresslist = Addresses;
                                if (addresslist.size() > 0) {
                                    completeaddress = addresslist.get(0).getAddressLine(0);
                                    Log.d("address", completeaddress);

                                    Curloc = String.format(completeaddress).replaceAll("[,]", "");

                                    //  teaddressline.setText(String.format(comadd));
                                } else {
                                    completeaddress = addresslist.get(0).getAddressLine(0);
                                    Log.d("address", completeaddress);

                                    Curloc = String.format(completeaddress).replaceAll("[,]", "");

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
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }


    public void CreatFileInternalStorage() {
        String Filename = "Savenum.txt";
        String path = "/data/data/" + getApplicationContext().getPackageName() + "/files/" + Filename;
        FileOutputStream fileOutputStream = null;
        File file = new File(path);
        if (!file.exists()) {
            try {
                fileOutputStream = openFileOutput("Savenum.txt", MODE_PRIVATE);
                OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream);
                writer.flush();
                writer.close();
                Log.d("Success", "Sucess");


            } catch (Exception e) {
                Log.d("ERROR", "ERROR");
            }
        } else {
            Log.d("EXIST", "EXIST");
        }
    }


    @Override
    public void onBackPressed() {
        if (true) {

            new AlertDialog.Builder(Dashboard.this)

                    .setTitle("Exit")

                    .setMessage("Do you want to exit this Application?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            finishAffinity();
                            System.exit(0);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Perform Your Task Here--When No is pressed
                            dialog.cancel();

                        }
                    }).show();
        } else {

        }
    }


}
