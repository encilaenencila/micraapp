package com.example.micraapp;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.os.StrictMode;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Quota;

import de.hdodenhof.circleimageview.CircleImageView;

public class Resubmit extends AppCompatActivity {
    private TextInputEditText teusername, tepassword, teregnumber, teregemail, tefirstname, temiddlename, telastname, tedateofbirth, teaddressline;
    private TextInputLayout tlusername, tlpassword, tlregnumber, tlregemail, tlfirstname, tlmiddlename, tllastname, tlgender, tldateofbirth, tladdressline;

    private TextView tvusername, tvpassword, tvnumber, tvemail;

    private String getgender = "";
    private TextView tvidname, tvselfiename;
    private Button btnback, btnsubmit;
    ImageView ividphoto;
    CircleImageView ciselfie;
    RoundedImageView riidphoto;
    private String LOADINFO_URL = "http://www.micra.services/Registration/FetchResubmitInfo.php";
    private String LOADIMG_URL = "http://www.micra.services/Registration/";

    private RadioGroup radioGroup;
    private RadioButton radioMale, radioFemale, radioRNS;
    private int id;
    FloatingActionButton btnretakeidphoto, btnretakeselfie;
    public Uri IDphotoUri;
    public Uri SelfieUri;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    File IDphotofile, Selfiefile;
    boolean Idphoto = false;
    boolean Selfie = false;
    String username = "username";
    String password = "password";
    String number = "number";
    String email = "email";

    private boolean haserror, haserror2, haserror3, haserror4, haserror5, haserror6, haserror7, haserror8 = false;
    String firstname = "firstname";
    String midname = "midname";
    String lastname = "lastname";
    String gender = "gender";
    String dob = "dob";

    String address = "address";
    String idphoto = "idphoto";
    String selfie = "idselfie";


    FusedLocationProviderClient mFusedLocationClient;


    Double lat, lng;
    Geocoder geocoder;
    List<Address> Addresses;

    private LatLng latLng;
    String Getlat, Getlong;

    BroadcastReceiver broadcastReceiver;

    SharedPreferences Resubmitreference;
    SharedPreferences.Editor resubmiteditor;

    private DatePickerDialog.OnDateSetListener dateSetListener;
    private String selectedidphoto = "";
    private String selectedselfie = "";


    private String oldselfiename = "";
    private String oldidphotoname = "";

    ProgressDialog progressDialog;
    private String getintentusername, getintentpassword;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resubmit);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        Resubmitreference = getSharedPreferences("Resubmit", Context.MODE_PRIVATE);
        broadcastReceiver = new NetworkListener();
        restorecon();

        //ACCOUNT
        teusername = findViewById(R.id.teusername);
        tepassword = findViewById(R.id.tepassword);
        teregnumber = findViewById(R.id.teregnumber);
        teregemail = findViewById(R.id.teregemail);


        tlusername = findViewById(R.id.tlusername);
        // tlpassword = findViewById(R.id.tlpassword);
        tlregnumber = findViewById(R.id.tlregnumber);
        tlregemail = findViewById(R.id.tlregemail);

        //PROFILE
        tefirstname = findViewById(R.id.tefirstname);
        temiddlename = findViewById(R.id.temiddlename);
        telastname = findViewById(R.id.telastname);
        tedateofbirth = findViewById(R.id.tedateofbirth);


        tlfirstname = findViewById(R.id.tlfirstname);
        tlmiddlename = findViewById(R.id.tlmiddlename);
        tllastname = findViewById(R.id.tllastname);
        tldateofbirth = findViewById(R.id.tldateofbirth);

        // Images
        tvselfiename = findViewById(R.id.tvselfiename);
        tvidname = findViewById(R.id.tvidname);

        //ADDRESLINE
        teaddressline = findViewById(R.id.teaddressline);

        //Radiogroup
        radioGroup = findViewById(R.id.radioSex);
        radioMale = findViewById(R.id.radioMale);
        radioFemale = findViewById(R.id.radioFemale);
        radioRNS = findViewById(R.id.radioRNS);

        // Identification
        ividphoto = findViewById(R.id.ividphoto);


        ciselfie = findViewById(R.id.ciselfie);

        //BUTTONS
        btnback = findViewById(R.id.btnback);
        btnsubmit = findViewById(R.id.btnsubmit);
        btnretakeidphoto = findViewById(R.id.btnretakeidphoto);
        btnretakeselfie = findViewById(R.id.btnretakeselfie);
        //image button

        //TEXTVIEW
        tvusername = findViewById(R.id.tvusername);
        tvpassword = findViewById(R.id.tvpassword);
        tvnumber = findViewById(R.id.tvnumber);
        tvemail = findViewById(R.id.tvemail);

        try {

            Intent intent = getIntent();
            getintentusername = intent.getStringExtra("username");
            getintentpassword = intent.getStringExtra("password");


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }


        //METHODS

        checkAndroidVersion();
        deleteAppCache(this);
        LoadInfo();


        if (radioMale.isChecked()) {

            getgender = "Male";
        } else if (radioFemale.isChecked()) {

            getgender = "Female";
        } else if (radioRNS.isChecked()) {

            getgender = "Rather not say";
        }
        //  openDialog();

        btnretakeidphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                deleteidphoto();
                CaptureIDphoto();
            }
        });


        btnretakeselfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                deleteselfie();
                CaptureSelfie();
            }
        });

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());

                if (!isValidFirstname(tefirstname.getText().toString())) {
                    haserror5 = true;
                    tlfirstname.setError("Firstname must be Alphabetical characters [a-Z]");
                } else {
                    haserror5 = false;
                }
                if (!isValidMiddleName(temiddlename.getText().toString())) {
                    haserror6 = true;
                    tlmiddlename.setError("Middlename must be Alphabetical characters [a-Z]");
                } else {
                    haserror6 = false;
                }
                if (!isValidLastname(telastname.getText().toString())) {
                    haserror7 = true;
                    tllastname.setError("Lastname must be Alphabetical characters [a-Z]");
                } else {
                    haserror7 = false;

                }
                String vdob = tedateofbirth.getText().toString();
                if (vdob.length() <= 0) {
                    haserror8 = true;
                    tldateofbirth.setError("Date of birth must be selected");
                } else {
                    haserror8 = false;
                }
                if (haserror == false && haserror2 == false && haserror3 == false && haserror4 == false && haserror5 == false && haserror6 == false && haserror7 == false && haserror8 == false) {


                    new AlertDialog.Builder(Resubmit.this)

                            .setTitle("Confirm Updates")
                            .setMessage("Do you want to Update this application?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Resubmit();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Perform Your Task Here--When No is pressed
                                    dialog.cancel();
                                }
                            }).show();

                }
            }
        });


        tedateofbirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        Resubmit.this, R.style.MyTheme, dateSetListener, year, month, day
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff9c9c")));
                dialog.show();
            }

        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {

                month = month + 1;
                String date = month + "/" + day + "/" + year;
                tedateofbirth.setText(date);
            }
        };

/*

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
*/
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
                if (s.length() != 0) {
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


    public void LoadInfo() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOADINFO_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject(response);
                            JSONArray jsonArray = object.getJSONArray("reports");
                            JSONObject reportdata = jsonArray.getJSONObject(0);


                            String a, b, c, d, e, f, g, h, i, j, k, l;

                            id = reportdata.getInt("id");
                            a = reportdata.getString("username");
                            b = reportdata.getString("password");
                            c = reportdata.getString("number");
                            d = reportdata.getString("email");
                            e = reportdata.getString("firstname");
                            f = reportdata.getString("middlename");
                            g = reportdata.getString("lastname");
                            getgender = reportdata.getString("gender");
                            i = reportdata.getString("birthdate");
                            j = reportdata.getString("address");
                            oldidphotoname = reportdata.getString("idphoto");
                            oldselfiename = reportdata.getString("selfie");

                            //Account


                            tvusername.setText(a);
                            //  tvpassword.setText(b);
                            tvnumber.setText(c);
                            tvemail.setText(d);

                            //PROFILE
                            tefirstname.setText(e);
                            temiddlename.setText(f);
                            telastname.setText(g);

                            if (getgender.equals("Male")) {
                                radioMale.setChecked(true);
                            } else if (getgender.equals("Female")) {
                                radioFemale.setChecked(true);

                            } else if (getgender.equals("Rather not say")) {
                                radioRNS.setChecked(true);

                            }


                            tedateofbirth.setText(i);

                            //ADDRESLINE
                            teaddressline.setText(j);
                            // Images
                            //  tvidname.setText(oldidphotoname);
                            //  tvselfiename.setText(oldselfiename);


                            Glide.with(Resubmit.this)
                                    .load(LOADIMG_URL + oldidphotoname.trim())
                                    .into(ividphoto);


                            Glide.with(Resubmit.this)
                                    .load(LOADIMG_URL + oldselfiename.trim())
                                    .into(ciselfie);
                            //ACoount
                            resubmiteditor = Resubmitreference.edit();
                            resubmiteditor.clear();
                            resubmiteditor.putString(username, a);
                            resubmiteditor.putString(password, b);
                            resubmiteditor.putString(number, c);
                            resubmiteditor.putString(email, d);
                            //Profile
                            resubmiteditor.putString(firstname, e);
                            resubmiteditor.putString(midname, f);
                            resubmiteditor.putString(lastname, g);
                            resubmiteditor.putString(gender, getgender);
                            resubmiteditor.putString(dob, i);

                            resubmiteditor.putString(address, j);
                            //images
                            //  Toast.makeText(ResubmitModule.this, oldselfiename, Toast.LENGTH_SHORT).show();
                            resubmiteditor.putString(idphoto, oldidphotoname.trim());
                            resubmiteditor.putString(selfie, oldselfiename.trim());

                            resubmiteditor.apply();
                            resubmiteditor.commit();

                        } catch (JSONException e) {
                            Toast.makeText(Resubmit.this, e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Resubmit.this, "Something went wrong", Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("username", getintentusername.trim());
                parameters.put("password", getintentpassword.trim());

                return parameters;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
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
        } else {
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

        if (Idphoto == true) {

            if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {
                if (resultCode == RESULT_OK) {
                    loadIdphoto();
                    UploadIDphoto();
                } else {
                    IDphotofile.delete();

                    Log.d("", "Camera cancelled");
                }

            }
        }


        if (Selfie == true) {
            if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {
                if (resultCode == RESULT_OK) {
                    loadSelfie();
                    UploadSelfie();
                } else {
                    Selfiefile.delete();
                    Log.d("", "Camera cancelled");
                }

            }
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
                        /*   Picasso.get().load(loadfile).into(ividphoto);*/

                        Glide.with(Resubmit.this)
                                .load(loadfile)
                                .into(ividphoto);

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


                        Glide.with(Resubmit.this)
                                .load(loadfile)
                                .into(ciselfie);
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
        int write = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (write != PackageManager.PERMISSION_GRANTED) {
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
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {

                            //this is string getlat,getlong
                            Getlat = String.valueOf(location.getLatitude());
                            Getlong = String.valueOf(location.getLongitude());
                           /* latitudeTextView.setText(location.getLatitude() + "");
                            longitTextView.setText(location.getLongitude() + "");*/

                            DecimalFormat form = new DecimalFormat();
                            form.setMaximumFractionDigits(3);

                            //this string getlat,getlong to double lat,long
                            lat = Double.parseDouble(Getlat);
                            lng = Double.parseDouble(Getlong);

                            Double latlat = Double.parseDouble(form.format(lat));
                            Double longlong = Double.parseDouble(form.format(lng));
                            ;
                          /*
                            String latlat = form.format(lat);
                            String longlong = form.format(lng);*/

                            /*   latLng = new LatLng(latlat, longlong);*/

                            geocoder = new Geocoder(Resubmit.this, Locale.getDefault());

                            String completeaddress, city, state, country, postalCode, KnownName = null;

                            try {
                                Addresses = geocoder.getFromLocation(latlat, longlong, 1);
                                if (Addresses.size() > 0) {
                                    completeaddress = Addresses.get(0).getAddressLine(0);
                                    Log.d("add", completeaddress);
                                    teaddressline.setText(String.format(completeaddress));
                                } else {
                                    completeaddress = Addresses.get(0).getAddressLine(0);
                                    Log.d("add", completeaddress);
                                    teaddressline.setText(String.format(completeaddress));
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
            Getlat = String.valueOf(mLastLocation.getLatitude());
            Getlong = String.valueOf(mLastLocation.getLongitude());


        }
    };

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
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
                                Bitmap resize = ReduceSize(rotated, 300);


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

                        Log.d("THIS SIZE 300", selectedidphoto);
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
                                Bitmap resize = ReduceSize(rotated, 300);


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


    public void clearSharedPref() {

        resubmiteditor = Resubmitreference.edit();
        resubmiteditor.clear().commit();
    }


    public void Resubmit() {

        progressDialog = progressDialog.show(Resubmit
                .this, "Please wait", "Resubmitting your application..");


/*
        UploadIDphoto();
        UploadSelfie();*/


        String url = "http://www.micra.services/Registration/Resubmit.php";
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equals("success1")) {
                            progressDialog.dismiss();


                            AlertDialog.Builder builder = new AlertDialog.Builder(Resubmit.this);
                            builder.setMessage("Resubmission Successful!")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            clearSharedPref();
                                            deleteidphoto();
                                            deleteselfie();
                                            LoadInfo();
                                            /*Intent intent = new Intent(Resubmit.this, MainActivity.class);
                                            intent.putExtra("Resubmit", "Done");
                                            startActivity(intent);
                                            finish();*/
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();


                            // Toast.makeText(ResubmitModule.this, "success1", Toast.LENGTH_SHORT).show();
                        } else if (response.equals("success2")) {
                            progressDialog.dismiss();


                            AlertDialog.Builder builder = new AlertDialog.Builder(Resubmit.this);
                            builder.setMessage("Resubmission Successful!")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            clearSharedPref();
                                            deleteidphoto();
                                            deleteselfie();
                                            LoadInfo();
                                          /* Intent intent = new Intent(Resubmit.this, MainActivity.class);
                                            intent.putExtra("Resubmit", "Done");
                                            startActivity(intent);
                                            finish();*/
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                            //  Toast.makeText(ResubmitModule.this, "success2", Toast.LENGTH_SHORT).show();
                        } else if (response.equals("success3")) {
                            progressDialog.dismiss();


                            AlertDialog.Builder builder = new AlertDialog.Builder(Resubmit.this);
                            builder.setMessage("Resubmission Successful!")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            clearSharedPref();
                                            deleteidphoto();
                                            deleteselfie();
                                            LoadInfo();
                                            /*Intent intent = new Intent(Resubmit.this, MainActivity.class);
                                            intent.putExtra("Resubmit", "Done");
                                            startActivity(intent);
                                            finish();*/
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                            //  Toast.makeText(ResubmitModule.this, "success3", Toast.LENGTH_SHORT).show();
                        } else if (response.equals("success4")) {
                            progressDialog.dismiss();

                            AlertDialog.Builder builder = new AlertDialog.Builder(Resubmit.this);
                            builder.setMessage("Resubmission Successful!")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            clearSharedPref();
                                            deleteidphoto();
                                            deleteselfie();
                                            LoadInfo();
                                        /*    Intent intent = new Intent(Resubmit.this, MainActivity.class);
                                            intent.putExtra("Resubmit", "Done");
                                            startActivity(intent);
                                            finish();*/
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();

                            // Toast.makeText(ResubmitModule.this, "success4", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Resubmit.this, "Something went wrong?", Toast.LENGTH_SHORT).show();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                String updnumber ="0"+tvnumber.getText().toString().trim();
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(id));
                params.put("username", tvusername.getText().toString().trim());
                params.put("number", updnumber);
                params.put("email", tvemail.getText().toString().trim());
                params.put("firstname", tefirstname.getText().toString().trim());
                params.put("middlename", temiddlename.getText().toString().trim());
                params.put("lastname", telastname.getText().toString().trim());
                params.put("birthdate", tedateofbirth.getText().toString().trim());
                params.put("gender", getgender.trim());
                params.put("address", teaddressline.getText().toString().trim());
                params.put("idphoto", selectedidphoto);
                params.put("selfie", selectedselfie);
                params.put("oldidphotoname", oldidphotoname.trim());
                params.put("oldselfiename", oldselfiename.trim());


                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                /* params.put("Content-Type", "application/x-www-form-urlencoded");*/
                return params;
            }
        };

        // To prevent timeout error
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Add the request to the RequestQueue.
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);

    }

    // your code

//

    public void deleteAppCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir.list() != null) {
                deleteCacheChild(dir);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean deleteCacheChild(File dir) {
        if (dir.isDirectory()) {
            for (File children : dir.listFiles()) {
                boolean success = deleteCacheChild(children);
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }


    public void resubmitusername(View view) {
        final Dialog dialog = new Dialog(this); // Context, this, etc.
        dialog.setContentView(R.layout.resubmitusername);


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);

        dialog.setCancelable(false);


        Button btnOkay = (Button) dialog.findViewById(R.id.btnok);
        Button btncancel = (Button) dialog.findViewById(R.id.btncancel);

        TextInputLayout tlusername1 = dialog.findViewById(R.id.tlusername1);
        TextInputLayout tlusername2 = dialog.findViewById(R.id.tlusername2);
        TextInputEditText teusername1 = dialog.findViewById(R.id.teusername1);
        TextInputEditText teusername2 = dialog.findViewById(R.id.teusername2);


        teusername1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    tlusername1.setError(null);
                    teusername1.setFocusable(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        teusername2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    tlusername2.setError(null);
                    teusername2.setFocusable(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();


            }
        });


        btnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean haserror1 = false;
                boolean haserror2 = false;
                boolean haserror3 = false;
                if (!isValidUsername(teusername1.getText().toString())) {
                    haserror1 = true;
                    tlusername1.setError("Username must be Letters. Numbers. Length (8-30) characters");
                } else if (!isValidUsername(teusername2.getText().toString())) {
                    haserror2 = true;
                    tlusername2.setError("Username must be Letters. Numbers. Length (8-30) characters");
                } else if (!teusername1.getText().toString().equals(teusername2.getText().toString())) {
                    haserror3 = true;
                    tlusername1.setError("Username does not match");
                    tlusername2.setError("Username does not match");
                }
                if (haserror1 == false && haserror2 == false && haserror3 == false) {
                    String username = teusername2.getText().toString();
                    tvusername.setText(username);
                    dialog.dismiss();
                }

            }
        });


    }


    public void resubmitnumber(View view) {
        final Dialog dialog = new Dialog(this); // Context, this, etc.
        dialog.setContentView(R.layout.resubmitnumber);


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);

        dialog.setCancelable(false);


        Button btnOkay = (Button) dialog.findViewById(R.id.btnok);
        Button btncancel = (Button) dialog.findViewById(R.id.btncancel);

        TextInputLayout tlregnumber1 = dialog.findViewById(R.id.tlregnumber1);

        TextInputEditText teregnumber1 = dialog.findViewById(R.id.teregnumber1);


        teregnumber1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    tlregnumber1.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();



            }
        });


        btnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean haserror=false;
                if(!isValidPhoneNumber(tlregnumber1.getPrefixText().toString() + teregnumber1.getText().toString())){
                    haserror = true;
                    tlregnumber1.setError("Phone number must be (+639*********)");
                }

                if(haserror == false) {
                    tvnumber.setText(teregnumber1.getText().toString());
                    dialog.dismiss();
                }


            }
        });
    }



    public void resubmitemail(View view) {
        final Dialog dialog = new Dialog(this); // Context, this, etc.
        dialog.setContentView(R.layout.resubmitemail);


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);

        dialog.setCancelable(false);


        Button btnOkay = (Button) dialog.findViewById(R.id.btnok);
        Button btncancel = (Button) dialog.findViewById(R.id.btncancel);

        TextInputLayout tlemail1 = dialog.findViewById(R.id.tlemail1);
        TextInputLayout tlemail2 = dialog.findViewById(R.id.tlemail2);

        TextInputEditText teemail1 = dialog.findViewById(R.id.teemail1);
        TextInputEditText teemail2 = dialog.findViewById(R.id.teemail2);

        teemail1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    tlemail1.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        teemail2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    tlemail2.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();


            }
        });


        btnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean haserror1 = false;
                boolean haserror2 = false;
                boolean haserror3 = false;
                if (!isValidEmail(teemail1.getText().toString())) {
                    haserror1 = true;
                    tlemail1.setError("Invalid email");
                } else if (!isValidEmail(teemail2.getText().toString())) {
                    haserror2 = true;
                    tlemail2.setError("Invalid email");
                } else if (!teemail1.getText().toString().equals(teemail2.getText().toString())) {
                    haserror3 = true;
                    tlemail1.setError("Email does not match");
                    tlemail2.setError("Email does not match");
                }
                if (haserror1 == false && haserror2 == false && haserror3 == false) {
                    String email = teemail2.getText().toString();
                    tvemail.setText(email);
                    dialog.dismiss();
                }

            }
        });


    }






    public static boolean isValidUsername(String username) {
        Matcher matcher = Pattern.compile("^[a-zA-Z0-9]{8,30}$").matcher(username);
        return matcher.matches();

    }



    public static boolean isValidPassword(String password) {
        Matcher matcher = Pattern.compile("((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,30})").matcher(password);
        return matcher.matches();
    }

    private boolean isValidPhoneNumber(CharSequence phoneNumber) {

        String regex1 = "(^(09|\\+639)\\d{9}$)";
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

}
