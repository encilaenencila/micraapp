package com.example.micraapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.micraapp.databinding.ActivityCrimeBinding;
import com.example.micraapp.databinding.ActivityDashboardBinding;
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
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrimeActivity extends drawerbase {

    ActivityCrimeBinding activityCrimeBinding;

    TextInputLayout tlfirstname, tllastname, tlcontact, tlhouseno, tlstreet, tlage, tlemail;
    TextInputEditText tefirstname, telastname, tecontact, tehouseno, teage, teemail;
    Button btnproceed, btnuploadphotoincident;
    LinearLayout LLcomplainant;
    TextView tvcomplainant;
    AutoCompleteTextView testreet;
    ArrayAdapter<String> arrayAdapter;
    String[] street = {"Amaloi", "Amuslan", "Bahawan", "Bakil", "Cagna", "Cadang", "Corumi", "Eskinita", "Gabo", "Gasan", "Ilihan", "Inaman", "Lamila", "Mabituan", "Malac", "Malasimbo", "Masola", "Monong", "Payte", "Posooy", "Toctokan", "Wayan"};


    private DatePickerDialog.OnDateSetListener dateSetListener;

    private final int IMG_REQUEST = 1;
    Bitmap bitmap;
    String ComEvidence;
    ImageView imageViewinci, imageViewinci2;
    public Uri IDphotoUri;
    public Uri SelfieUri;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    File IDphotofile;
    ProgressDialog progressDialog;
    boolean Idphoto = false;
    boolean gallery = false;
    boolean haserror4 = false;
    boolean haserror = false;
    boolean haserror2 = false;
    boolean haserror3 = false;

    boolean haserror5 = false;
    boolean haserror6 = false;
    boolean haserror7 = false;

    SharedPreferences C1, C2, C3;
    SharedPreferences.Editor C1editor;
    SharedPreferences.Editor C2editor;
    SharedPreferences.Editor C3editor;


    private String Curloc;
    private String c1_comFname = "KEY_C1COMFNAME";
    private String c1_comLame = "KEY_C1COMLNAME";
    private String c1_comAge = "KEY_C1COMAGE";
    private String c1_comNumber = "KEY_C1COMNUMBER";
    private String c1_comEmail = "KEY_C1COMEMAIL";
    private String c1_comHouseno = "KEY_C1COMHOUSENO";
    private String c1_comStreet = "KEY_C1COMSTREET";
    private String c1_comCurloc = "KEY_C1COMCURLOC";
    FusedLocationProviderClient mFusedLocationClient;
    Double lat, lng;
    Geocoder geocoder;
    List<Address> Addresses;
    private LatLng latLng;
    String Getlat, Getlong;

    BroadcastReceiver broadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        activityCrimeBinding = activityCrimeBinding.inflate(getLayoutInflater());
        allocateActivitytitle("CrimeActivity");

        setContentView(activityCrimeBinding.getRoot());
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        //BTN
        btnproceed = findViewById(R.id.btnproceed);
        btnuploadphotoincident = findViewById(R.id.btnuploadphotoincident);
        //TE
        teemail = findViewById(R.id.teemail);
        testreet = findViewById(R.id.testreet);
        teage = findViewById(R.id.teage);
        tefirstname = findViewById(R.id.tefirstname);
        telastname = findViewById(R.id.telastname);
        tecontact = findViewById(R.id.tecontact);
        tehouseno = findViewById(R.id.tehouseno);


        //TL
        tlemail = findViewById(R.id.tlemail);
        tlage = findViewById(R.id.tlage);
        tlfirstname = findViewById(R.id.tlfirstname);
        tllastname = findViewById(R.id.tllastname);
        tlcontact = findViewById(R.id.tlcontact);
        tlhouseno = findViewById(R.id.tlhouseno);
        tlstreet = findViewById(R.id.tlstreet);

        imageViewinci = findViewById(R.id.imageViewinci);

        tvcomplainant = findViewById(R.id.tvcomplainant);


        broadcastReceiver = new NetworkListener();
        restorecon();
        checkAndroidVersion();
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.listitems, street);
        testreet.setAdapter(arrayAdapter);


        //SHARED REF
        C1 = getSharedPreferences("SPC1", Context.MODE_PRIVATE);
        C2 = getSharedPreferences("SPC2", Context.MODE_PRIVATE);
        C3 = getSharedPreferences("SPC3", Context.MODE_PRIVATE);


        if (!C1.contains(c1_comFname)) {
            tefirstname.setFocusable(true);
        } else {
            tefirstname.setText(C1.getString(c1_comFname, ""));

        }
        if (!C1.contains(c1_comLame)) {
            telastname.setFocusable(true);
        } else {
            telastname.setText(C1.getString(c1_comLame, ""));
        }
        if (!C1.contains(c1_comAge)) {
            teage.setFocusable(true);
        } else {
            teage.setText(C1.getString(c1_comAge, ""));
        }

        if (!C1.contains(c1_comNumber)) {
            tecontact.setFocusable(true);
        } else {
            tecontact.setText(C1.getString(c1_comNumber, ""));
        }
        if (!C1.contains(c1_comEmail)) {
            teemail.setFocusable(true);
        } else {
            teemail.setText(C1.getString(c1_comEmail, ""));
        }

        if (!C1.contains(c1_comHouseno)) {
            tehouseno.setFocusable(true);
        } else {
            tehouseno.setText(C1.getString(c1_comHouseno, ""));
        }
        if (!C1.contains(c1_comStreet)) {
            testreet.setFocusable(true);
        } else {

            testreet.setText(C1.getString(c1_comStreet, ""));
            arrayAdapter = new ArrayAdapter<String>(this, R.layout.listitems, street);
            testreet.setAdapter(arrayAdapter);


        }


        testreet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });


        //filtering


        tefirstname.setFilters(new InputFilter[]{filter2, new InputFilter.LengthFilter(16)});
        telastname.setFilters(new InputFilter[]{filter2, new InputFilter.LengthFilter(16)});
        tehouseno.setFilters(new InputFilter[]{filter3, new InputFilter.LengthFilter(4)});

        tefirstname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    if (!TextUtils.isEmpty(tefirstname.getText().toString())) {
                        if (!isValidNameFormat(tefirstname.getText().toString())) {
                            haserror = true;
                            tlfirstname.setError("Alphabetical characters only [a-Z]");
                        } else {

                            tlfirstname.setError(null);
                            tlfirstname.setErrorEnabled(false);
                            haserror = false;
                        }
                    }
                } else {
                    tlfirstname.setError(null);
                    tlfirstname.setErrorEnabled(false);
                    haserror = false;
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
                    if (!TextUtils.isEmpty(telastname.getText().toString())) {
                        if (!isValidNameFormat(telastname.getText().toString())) {
                            haserror2 = true;
                            tllastname.setError("Alphabetical characters only [a-Z]");
                        } else {
                            tllastname.setError(null);
                            tllastname.setErrorEnabled(false);
                            haserror2 = false;
                        }
                    }
                } else {
                    tllastname.setError(null);
                    tllastname.setErrorEnabled(false);
                    haserror2 = false;
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tecontact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    if (!TextUtils.isEmpty(tecontact.getText().toString())) {
                        if (!isValidPhoneNumber(tlcontact.getPrefixText().toString() + tecontact.getText().toString())) {
                            haserror3 = true;
                            tlcontact.setError("Number must be (+639*********)");
                        } else {
                            tlcontact.setError(null);
                            tlcontact.setErrorEnabled(false);
                            haserror3 = false;
                        }
                    }
                } else {
                    tlcontact.setError(null);
                    tlcontact.setErrorEnabled(false);
                    haserror3 = false;
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        teage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    if (!TextUtils.isEmpty(teage.getText().toString())) {
                        if (!isValidAge(teage.getText().toString())) {
                            haserror4 = true;
                            tlage.setError("Invalid Age");
                        } else {
                            tlage.setError(null);
                            tlage.setErrorEnabled(false);
                            haserror4 = false;
                        }
                    }
                } else {
                    tlage.setError(null);
                    tlage.setErrorEnabled(false);
                    haserror4 = false;
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tehouseno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    if (!TextUtils.isEmpty(tehouseno.getText().toString())) {
                        if (!isValidHouseNo(tehouseno.getText().toString())) {
                            haserror5 = true;
                            tlhouseno.setError("Invalid House No.");
                        } else {
                            tlhouseno.setError(null);
                            tlhouseno.setErrorEnabled(false);
                            haserror5 = false;
                        }
                    }
                } else {
                    tlhouseno.setError(null);
                    tlhouseno.setErrorEnabled(false);
                    haserror5 = false;
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        testreet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                testreet.setAdapter(arrayAdapter);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {


                    tlstreet.setError(null);
                    tlstreet.setErrorEnabled(false);
                    haserror6 = false;
                } else {
                    tlstreet.setError("Please select street");
                    haserror6 = true;
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        teemail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    if (!TextUtils.isEmpty(teemail.getText().toString())) {
                        if (!isValidEmail(teemail.getText().toString())) {
                            haserror7 = true;
                            tlemail.setError("Invalid Email");
                        } else {
                            tlemail.setError(null);
                            tlemail.setErrorEnabled(false);
                            haserror7 = false;
                        }
                    }
                } else {
                    tlemail.setError(null);
                    tlemail.setErrorEnabled(false);
                    haserror7 = false;
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnproceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isValidNameFormat(tefirstname.getText().toString())) {
                    haserror = true;
                    tlfirstname.setError("Alphabetical characters only [a-Z]");
                } else {
                    tlfirstname.setError(null);
                    tlfirstname.setErrorEnabled(false);
                    haserror = false;
                }


                if (!isValidNameFormat(telastname.getText().toString())) {
                    haserror2 = true;
                    tllastname.setError("Alphabetical characters only [a-Z]");
                } else {
                    tllastname.setError(null);
                    tllastname.setErrorEnabled(false);
                    haserror2 = false;
                }

                if (!isValidPhoneNumber(tlcontact.getPrefixText().toString() + tecontact.getText().toString())) {
                    haserror3 = true;
                    tlcontact.setError("Number must be (+639*********)");
                } else {
                    tlcontact.setError(null);
                    tlcontact.setErrorEnabled(false);
                    haserror3 = false;
                }


                if (!isValidHouseNo(tehouseno.getText().toString())) {
                    haserror4 = true;
                    tlhouseno.setError("Invalid House No.");
                } else {
                    tlhouseno.setError(null);
                    tlhouseno.setErrorEnabled(false);
                    haserror4 = false;
                }


                if (!isValidAge(teage.getText().toString())) {
                    haserror5 = true;
                    tlage.setError("Invalid Age");
                } else {
                    tlage.setError(null);
                    tlage.setErrorEnabled(false);
                    haserror5 = false;
                }

                if (TextUtils.isEmpty(testreet.getText().toString())) {
                    tlstreet.setError("Please select street");
                    haserror6 = true;
                } else {
                    tlstreet.setError(null);
                    tlstreet.setErrorEnabled(false);
                    haserror6 = false;

                }

                if (!isValidEmail(teemail.getText().toString())) {
                    haserror7 = true;
                    tlemail.setError("Invalid Email");
                } else {
                    tlemail.setError(null);
                    tlemail.setErrorEnabled(false);
                    haserror7 = false;
                }

                if (haserror == false && haserror2 == false && haserror3 == false && haserror4 == false && haserror5 == false && haserror6 == false && haserror7 == false) {

                    Intent intent = new Intent(CrimeActivity.this, CrimeActivity2.class);
                    if (bitmap != null) {
                        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream);
                        byte[] byteArray = bStream.toByteArray();
                        Bundle bundle = new Bundle();
                        bundle.putByteArray("photo", byteArray);
                        intent.putExtra("bundle", bundle);
                    }


                    C1editor = C1.edit();
                    C1editor.clear();
                    C1editor.putString(c1_comFname, tefirstname.getText().toString().trim());
                    C1editor.putString(c1_comLame, telastname.getText().toString().trim());
                    C1editor.putString(c1_comAge, teage.getText().toString().trim());
                    C1editor.putString(c1_comNumber, tecontact.getText().toString().trim());
                    C1editor.putString(c1_comEmail, teemail.getText().toString().trim());
                    C1editor.putString(c1_comHouseno, tehouseno.getText().toString().trim());
                    C1editor.putString(c1_comStreet, testreet.getText().toString().trim());
                    C1editor.putString(c1_comCurloc, Curloc);
                    C1editor.commit();


                    startActivity(intent);
                }
            }
        });


        btnuploadphotoincident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
                //openDialog();

            }
        });
    }


    public void openDialog() {
        final Dialog dialog = new Dialog(this); // Context, this, etc.
        dialog.setContentView(R.layout.galleryorcamera);
        dialog.show();
        dialog.setCancelable(false);
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.99);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.30);

        dialog.getWindow().setLayout(width, height);
        TextView btnOkay = (TextView) dialog.findViewById(R.id.btnok);
        ImageView choosegallery = (ImageView) dialog.findViewById(R.id.choosegallery);
        ImageView choosecamera = (ImageView) dialog.findViewById(R.id.choosecamera);
        btnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        choosegallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
                dialog.dismiss();
            }
        });


        choosecamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());

                CaptureIDphoto();
                dialog.dismiss();
            }
        });


    }
    //END OF METHOD

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
        } else {
            Log.d("", "Camera cancelled");
        }
    }

    //this method conrtradicts REQUEST PERMISSION.
    private File createImageFileIdphoto() throws IOException {

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        // File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile("photoevidence" + timeStamp, ".jpg", storageDir);
    }

    //////////////////// SELECT IMAGE FROM DIRECTORY
    //////////////////// SELECT IMAGE FROM DIRECTORY
    //////////////////// SELECT IMAGE FROM DIRECTORY
    //////////////////// SELECT IMAGE FROM DIRECTORY
    //////////////////// SELECT IMAGE FROM DIRECTORY

    public void selectImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_ID_MULTIPLE_PERMISSIONS);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (Idphoto == true) {

            if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {
                if (resultCode == RESULT_OK) {
                    bitmap = null;
                    ComEvidence = null;

                } else {

                    bitmap = null;
                    ComEvidence = null;
                    IDphotofile.delete();

                    Log.d("", "Camera cancelled");
                }

            }
        }


        if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS && resultCode == RESULT_OK && data != null) {


            Uri path = data.getData();
            try {

                bitmap = MediaStore.Images.Media.getBitmap(CrimeActivity.this.getContentResolver(), path);


                imageViewinci.getLayoutParams().height = 500;
                imageViewinci.getLayoutParams().width = 500;
                imageViewinci.setImageBitmap(bitmap);
                bitmap = ReduceSize(bitmap, 400);
                Log.d("Bitmap", "" + bitmap);


                //Assign image into bitmap


            } catch (IOException e) {
                e.printStackTrace();
            }

        }


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

    public class ImageFileFilter implements FileFilter {

        private final String[] okFileExtensions = new String[]{
                "jpg",
                "png",
                "gif",
                "jpeg"
        };


        public boolean accept(File file) {
            for (String extension : okFileExtensions) {
                if (file.getName().toLowerCase().endsWith(extension)) {
                    return true;
                }
            }
            return false;
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


                            geocoder = new Geocoder(CrimeActivity.this, Locale.getDefault());

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
    public void TextOnly(TextInputEditText textInputEditText) {


        textInputEditText.setFilters(new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence cs, int start,
                                               int end, Spanned spanned, int dStart, int dEnd) {
                        // TODO Auto-generated method stub
                        if (cs.equals("")) {  // for backspace
                            return cs;
                        }
                        if (cs.toString().matches("(?!.*[ ]{2})[a-zA-Z ]+")) {
                            return cs;
                        }


                        return "";
                    }
                }
        });
    }

    InputFilter filter2 = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; ++i) {
                if (!Pattern.compile("^(?!.*[ ]{2})[abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ ]*").matcher(String.valueOf(source.charAt(i))).matches()) {
                    return "";
                }
            }

            return null;
        }
    };


    InputFilter filter3 = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; ++i) {
                if (!Pattern.compile("[a-zA-Z0-9]*").matcher(String.valueOf(source.charAt(i))).matches()) {
                    return "" + tehouseno.getText().toString();
                }
            }

            return null;
        }
    };


    private boolean isValidPhoneNumber(CharSequence phoneNumber) {

        String regex1 = "(^(09|\\+639)\\d{9}$)";
        String regex2 = "((\\+63)|0)\\d{10}";
        Matcher matcher = Pattern.compile("(^(09|\\+639)\\d{9}$)").matcher(phoneNumber);
        return matcher.matches();
    }

    private boolean isValidHouseNo(CharSequence phoneNumber) {
        String finaldecision = "(^(?![0]|[00]|[000][a-zA-Z]|[00]+[1-3]+[a-zA-Z]*$)\\d{1,3}([a-zA-Z])?$)";
        String onehundrednotallow = "(^(?![0][00][000][a-zA-Z]|[00]+[1-3]+[a-zA-Z]*$)\\d{1,2}([a-zA-Z])?$)";
        String regex = "(^(?![0][00][000][a-zA-Z]*$)\\d{1,3}([a-zA-Z])?$)";
        String regex2 = "(^(?![0][00][000][a-zA-Z]|[00]+[1-9]+[a-zA-Z]*$)\\d{1,3}([a-zA-Z])?$)";
        Matcher matcher = Pattern.compile(finaldecision).matcher(phoneNumber);
        return matcher.matches();
    }

    private boolean isValidAge(CharSequence phoneNumber) {


        Matcher matcher = Pattern.compile("(^(1[8-9]|[2-9]\\d|\\d{3,})$)").matcher(phoneNumber);
        return matcher.matches();
    }


    public static boolean isValidNameFormat(String Nameformat) {


        String oldregex = "^(?!.*[ ]{2})[abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ ]*";
        Matcher matcher = Pattern.compile(oldregex).matcher(Nameformat);
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


    @Override
    public void onBackPressed() {
        if (true) {

            new android.app.AlertDialog.Builder(CrimeActivity.this)

                    .setMessage("Choose another report?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            C2editor = C2.edit();
                            C3editor = C3.edit();
                            C2editor.clear().commit();
                            C3editor.clear().commit();
                           finish();
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