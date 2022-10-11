package com.example.micraapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.micraapp.databinding.ActivityCrimeActivity2bBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrimeActivity2b extends drawerbase implements
        RecognitionListener {

    ActivityCrimeActivity2bBinding activityCrimeActivity2bBinding;
    ProgressDialog progressDialog;
    TextInputLayout tlnarrative;
    TextInputEditText tenarrative;
    Button btnproceed, btnsave;
    private boolean haserror = false;

    SharedPreferences C1, C2, C3;


    SharedPreferences.Editor C1editor;
    SharedPreferences.Editor C2editor;
    SharedPreferences.Editor C3editor;

    private String c3_comNarrative = "KEY_C3COMNARRATIVE";
    private String c1_comFname = "KEY_C1COMFNAME";
    private String c1_comLame = "KEY_C1COMLNAME";
    private String c1_comAge = "KEY_C1COMAGE";
    private String c1_comNumber = "KEY_C1COMNUMBER";
    private String c1_comEmail = "KEY_C1COMEMAIL";
    private String c1_comHouseno = "KEY_C1COMHOUSENO";
    private String c1_comStreet = "KEY_C1COMSTREET";
    private String c1_comCurloc = "KEY_C1COMCURLOC";


    private String c2_resFname = "KEY_C2RESFNAME";
    private String c2_resLame = "KEY_C2RESLNAME";
    private String c2_resCategory = "KEY_C2RESCATEGORY";
    private String c2_resOther = "KEY_C2RESOTHER";
    private String c2_resDate = "KEY_C2RESDATE";
    private String c2_resTime = "KEY_C2RESTIME";
    private String c2_resLocation = "KEY_C2RESLOCATION";
    private String c2_resLandmark = "KEY_C2RESLANDMARK";

    private String c2_resHouseno = "KEY_C2RESHOUSENO";
    private String c2_resStreet = "KEY_C2RESSTREET";
    private String c2_resContact = "KEY_C2RESCONTACT";
    private String comFname, comLname, comAge, comNumber, comEmail, comHouseno, comStreet, comCurloc, comAddress;
    private String resFname, resLname, resCategory, resOther, resDate, resTime, resLocation, resLandmark, resHouseno, resStreet,resContact,resAddress;

    private String Local = "Masambong Q.C.";


    private static final int REQUEST_RECORD_PERMISSION = 100;

    private ToggleButton toggleButton;
    private ProgressBar progressBar;
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;

    TextView tvtalk;
    ImageButton ibinstruction, ibread, ibread2;
    private String LOG_TAG = "Speechtotext";
    TextToSpeech textToSpeech;


    BroadcastReceiver broadcastReceiver;

    private String selectedphoto;

    ImageButton ib_instruction;

    //for saving number
    private static final String FILE_NAME = "Input.txt";


    private String selectedselfie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //SETTING LANGUAGE HERE
        String languageToLoad = "fil";
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        setContentView(R.layout.activity_crime_activity2b);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        activityCrimeActivity2bBinding = activityCrimeActivity2bBinding.inflate(getLayoutInflater());
        allocateActivitytitle("CrimeActivity2b");
        setContentView(activityCrimeActivity2bBinding.getRoot());
        tlnarrative = findViewById(R.id.tlnarrative);
        tenarrative = findViewById(R.id.tenarrative);
        btnproceed = findViewById(R.id.btnproceed);
        ib_instruction = findViewById(R.id.ib_instruction);
        broadcastReceiver = new NetworkListener();
        restorecon();
        UploadSelfie();

        try {

            Bundle b3 = getIntent().getBundleExtra("bundle");

            if (b3 != null) {
                byte[] bytrarray = b3.getByteArray("photo");
                Bitmap intentBitmap = BitmapFactory.decodeByteArray(bytrarray, 0, bytrarray.length);
                selectedphoto = imagetoString(intentBitmap);
            } else {
                selectedphoto = "";
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        C1 = getSharedPreferences("SPC1", Context.MODE_PRIVATE);
        C2 = getSharedPreferences("SPC2", Context.MODE_PRIVATE);
        C3 = getSharedPreferences("SPC3", Context.MODE_PRIVATE);

        //CRIME 1

        if (!C1.contains(c1_comFname)) {
            comFname = null;
        } else {
            comFname = C1.getString(c1_comFname, "");

        }
        if (!C1.contains(c1_comLame)) {
            comLname = null;
        } else {
            comLname = C1.getString(c1_comLame, "");
        }
        if (!C1.contains(c1_comAge)) {
            comAge = null;
        } else {
            comAge = C1.getString(c1_comAge, "");
        }

        if (!C1.contains(c1_comNumber)) {
            comNumber = null;
        } else {
            comNumber = C1.getString(c1_comNumber, "");
        }
        if (!C1.contains(c1_comEmail)) {
            comEmail = null;
        } else {
            comEmail = C1.getString(c1_comEmail, "");
        }

        if (!C1.contains(c1_comHouseno)) {
            comHouseno = null;
        } else {
            comHouseno = C1.getString(c1_comHouseno, "");
        }
        if (!C1.contains(c1_comStreet)) {
            comStreet = null;
        } else {
            comStreet = C1.getString(c1_comStreet, "");
        }

        if (!C1.contains(c1_comCurloc)) {
            comCurloc = null;
        } else {
            comCurloc = C1.getString(c1_comCurloc, "");
        }


        //CRIME2
        if (!C2.contains(c2_resFname)) {
            resFname = null;
        } else {
            resFname = C2.getString(c2_resFname, "");

        }
        if (!C2.contains(c2_resLame)) {
            resLname = null;
        } else {
            resLname = C2.getString(c2_resLame, "");
        }
        if (!C2.contains(c2_resCategory)) {
            resCategory = null;
        } else {
            resCategory = C2.getString(c2_resCategory, "");
        }
        if (!C2.contains(c2_resOther)) {
            resOther = null;
        } else {

            resOther = C2.getString(c2_resOther, "");

        }

        if (!C2.contains(c2_resDate)) {
            resDate = null;
        } else {
            resDate = C2.getString(c2_resDate, "");
        }
        if (!C2.contains(c2_resTime)) {
            resTime = null;
        } else {
            resTime = C2.getString(c2_resTime, "");
        }

        if (!C2.contains(c2_resLocation)) {
            resLocation = null;
        } else {
            resLocation = C2.getString(c2_resLocation, "");
        }
        if (!C2.contains(c2_resLandmark)) {
            resLandmark = null;
        } else {
            resLandmark = C2.getString(c2_resLandmark, "");
        }




        if (!C2.contains(c2_resHouseno)) {
           resHouseno = null;
        } else {
           resHouseno =C2.getString(c2_resHouseno, "");
        }
        if (!C2.contains(c2_resStreet)) {
           resStreet = null;
        } else {
            resStreet = C2.getString(c2_resStreet, "");

        }
        if (!C2.contains(c2_resContact)) {
            resContact = null;
        } else {
            resContact = C2.getString(c2_resContact, "");
        }





        if (!C3.contains(c3_comNarrative)) {
            tenarrative.setFocusable(true);
        } else {
            tenarrative.setText(C3.getString(c3_comNarrative, ""));
        }


        // CONVERTIONS




        comFname = comFname.substring(0, 1).toUpperCase() + comFname.substring(1).toLowerCase();
        comLname = comLname.substring(0, 1).toUpperCase() + comLname.substring(1).toLowerCase();

        if(!TextUtils.isEmpty(resFname)){
            resFname = resFname.substring(0, 1).toUpperCase() + resFname.substring(1).toLowerCase();
        }else{
            resFname = "N/A";
        }
        if(!TextUtils.isEmpty(resLname)){
            resLname = resLname.substring(0, 1).toUpperCase() + resLname.substring(1).toLowerCase();
        }else{
            resLname ="N/A";
        }


        if(!TextUtils.isEmpty(resContact)){
            resContact = "0"+ C2.getString(c2_resContact, "");
        }else{
            resContact = "N/A";
        }
        if(!TextUtils.isEmpty(resHouseno) && !TextUtils.isEmpty(resStreet)){
            resAddress = resHouseno +" "+ resStreet +" " + Local;
        }else{
            resAddress = "N/A";
        }


        progressBar = findViewById(R.id.progressBar1);
        toggleButton = findViewById(R.id.toggleButton1);
        tenarrative = findViewById(R.id.tenarrative);
        tvtalk = findViewById(R.id.tvtalk);

        ibinstruction = findViewById(R.id.ib_instruction);
        ibread = findViewById(R.id.ibread);
        ibread2 = findViewById(R.id.ibread2);


        progressBar.setVisibility(View.INVISIBLE);
        progressBar.getProgressDrawable().setColorFilter(
                Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
        speech = SpeechRecognizer.createSpeechRecognizer(this);
        Log.i(LOG_TAG, "isRecognitionAvailable: " + SpeechRecognizer.isRecognitionAvailable(this));
        speech.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        recognizerIntent.putExtra("android.speech.extra.EXTRA_ADDITIONAL_LANGUAGES", new String[]{"fil"});
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, new Long(2000));
        recognizerIntent.putExtra("android.speech.extra.DICTATION_MODE", true);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, false);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {

                    progressBar.setVisibility(View.VISIBLE);
                    tenarrative.requestFocus();
                    tvtalk.setTextColor(Color.RED);
                    tvtalk.setText("Listening please speak..");
                    toggleButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_mic_24, 0);
                    ActivityCompat.requestPermissions
                            (CrimeActivity2b.this,
                                    new String[]{Manifest.permission.RECORD_AUDIO},
                                    REQUEST_RECORD_PERMISSION);
                } else {
                    tvtalk.setTextColor(Color.parseColor("#757575"));
                    tvtalk.setText("Stopped Listening.");
                    tenarrative.requestFocus();
                    toggleButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_mic_off_24, 0);

                    progressBar.setIndeterminate(false);
                    progressBar.setVisibility(View.INVISIBLE);
                    speech.stopListening();
                }
            }
        });

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

                // if No error is found then only it will run
                if (i != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.getDefault());
                }
            }
        });


        // Adding OnClickListener
        ibread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tenarrative.getText().toString().length() <= 0) {
                    Toast.makeText(CrimeActivity2b.this, "Field is empty", Toast.LENGTH_SHORT).show();
                } else {
                    textToSpeech.speak(tenarrative.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);

                }

            }
        });
        ibread2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                textToSpeech.stop();


            }
        });

        tenarrative.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() != 0) {
                    if (!TextUtils.isEmpty(tenarrative.getText().toString())) {
                        if (!isvalidNarrative(tenarrative.getText().toString())) {
                            haserror = true;
                            tlnarrative.setError("Please avoid unnecessary characters. ");
                        } else {
                            tlnarrative.setError(null);
                            tlnarrative.setErrorEnabled(false);
                            haserror = false;
                        }
                    }
                } else {
                    tlnarrative.setError(null);
                    tlnarrative.setErrorEnabled(false);
                    haserror = false;
                }


            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        if (TextUtils.isEmpty(tenarrative.getText().toString())) {
            tlnarrative.setError("This field is required");
            haserror = true;
        } else {
            tlnarrative.setError(null);
            tlnarrative.setErrorEnabled(false);
            haserror = false;

        }
        ib_instruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Instructions();
            }
        });


        btnproceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (haserror == false) {
                    new android.app.AlertDialog.Builder(CrimeActivity2b.this)

                            .setTitle("Confirm")

                            .setMessage("Submit this Report?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    String filePath = "/data/data/" + getApplicationContext().getPackageName() + "/files/Input.txt";
                                    File log = new File(filePath);
                                    try {
                                        if (log.exists() == false) {
                                            System.out.println("Create new file.");
                                            log.createNewFile();
                                        }
                                        PrintWriter out = new PrintWriter(new FileWriter(log, true));
                                        out.append("0" + comNumber + "\n");
                                        out.close();

                                        System.out.print("Number saved");
                                    } catch (IOException e) {
                                        System.out.println("Error");
                                    }

                                    SubmitReport();
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


    }


    private String imagetoString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgbytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgbytes, Base64.DEFAULT);
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


    public void UploadSelfie() {
        try {
            String path = "/data/data/"+getPackageName()+"/app_imageDir/";
            Log.d("Files", "Path: " + path);
            File directory = new File(path);
            File[] files = directory.listFiles();
            Log.d("Files", "Count : " + files.length);
            for (int i = 0; i < files.length; i++) {
                Log.d("Files", "FileName:" + files[i].getName());
                String fname = files[i].getName();
                if (fname.startsWith("selfie")) {
                    File loadfile = new File("/data/data/"+getPackageName()+"/app_imageDir/", fname);
                    if (loadfile.exists()) {
                        String selfiepath = loadfile.getPath().toString();
                        Log.d("", "selfiepath" + selfiepath);


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








    public void SubmitReport() {
        progressDialog = progressDialog.show(CrimeActivity2b
                .this, "Please wait", "submitting your report..");


        String url = "http://www.micra.services/IncidentReport/UploadBlotter.php";
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        if (response.equals("success")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(CrimeActivity2b.this);
                            builder.setMessage("Report Submitted!")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {


                                            C2editor = C2.edit();
                                            C3editor = C3.edit();

                                            C2editor.clear().commit();
                                            C3editor.clear().commit();
                                            Intent intent = new Intent(CrimeActivity2b.this, Dashboard.class);
                                            intent.putExtra("Report2", "Done");
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();

                        } else {
                            Toast.makeText(getApplicationContext(), "Something went wrong?" , Toast.LENGTH_LONG).show();
                            Log.d("Response",""+response);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //error.printStackTrace();
                progressDialog.dismiss();
                Log.d("Error in connection", error.toString());
                if (error instanceof NoConnectionError) {

                    Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_LONG).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                if (TextUtils.isEmpty(resOther)) {
                    params.put("crimecategory", resCategory);
                }else {
                    params.put("crimecategory",resOther);
                }
                params.put("respondentfirstname", resFname);
                params.put("respondentlastname", resLname);
                params.put("casenarrative", tenarrative.getText().toString().trim());
                params.put("complainantfirstname", comFname);
                params.put("complainantlastname", comLname);
                params.put("complainantage", comAge);
                params.put("complainantcontact", "0" + comNumber);
                params.put("complainantaddress", comHouseno + " " + comStreet + " " + Local);
                params.put("complainantemail", comEmail);
                params.put("currentlocation", comCurloc);
                params.put("dateofincident", resDate);
                params.put("timeofincident", resTime);
                params.put("locationofincident", resLocation);
                params.put("landmark", resLandmark);
                params.put("evidence", selectedphoto);
                params.put("reporterselfie", selectedselfie);
                params.put("respondentcontact", resContact);
                params.put("respondentaddress", resAddress);
                params.put("isResident", "Resident");
                params.put("report", "1");


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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    speech.startListening(recognizerIntent);
                } else {
                    Toast.makeText(CrimeActivity2b.this, "Permission Denied!", Toast
                            .LENGTH_SHORT).show();
                }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (speech != null) {
            speech.destroy();
            Log.i(LOG_TAG, "destroy");
        }
    }


    @Override
    public void onBeginningOfSpeech() {
        Log.i(LOG_TAG, "onBeginningOfSpeech");
        progressBar.setIndeterminate(false);
        progressBar.setMax(10);
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.i(LOG_TAG, "onBufferReceived: " + buffer);
    }

    @Override
    public void onEndOfSpeech() {
        Log.i(LOG_TAG, "onEndOfSpeech");
        progressBar.setIndeterminate(true);
        toggleButton.setChecked(false);
        tenarrative.requestFocus();
        tenarrative.setSelection(tenarrative.getText().toString().length());
    }

    @Override
    public void onError(int errorCode) {
        String errorMessage = getErrorText(errorCode);
        Log.d(LOG_TAG, "FAILED " + errorMessage);
        Toast toast = Toast.makeText(this, "No match", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
        toast.show();
        tvtalk.setTextColor(Color.parseColor("#757575"));
        tvtalk.setText("Stopped Listening.");
        toggleButton.setChecked(false);
    }

    @Override
    public void onEvent(int arg0, Bundle arg1) {
        Log.i(LOG_TAG, "onEvent");
    }

    @Override
    public void onPartialResults(Bundle arg0) {
        Log.i(LOG_TAG, "onPartialResults");
    }

    @Override
    public void onReadyForSpeech(Bundle arg0) {
        Log.i(LOG_TAG, "onReadyForSpeech");
    }

    @Override
    public void onResults(Bundle results) {
        Log.i(LOG_TAG, "onResults");
        String newLine = "";
        ArrayList<String> matches = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

        tenarrative.setText(tenarrative.getText().toString() + " " + matches.get(0));

    }


    @Override
    public void onRmsChanged(float rmsdB) {
        Log.i(LOG_TAG, "onRmsChanged: " + rmsdB);
        progressBar.setProgress((int) rmsdB);
    }

    public static String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }

    public void Instructions() {

        String[] names = {"1. Speak clearly and conversationally.", "2. Keep background noise to a minimum.", "3. Keep the microphone a few inches away from your face."
        };
        String[] names2 = {"1. Rushing – try to maintain a natural, fluid pace.", "2.  Shouting and Singing.", "3.  Speak obscene language."};
        ArrayAdapter<String> adapter;
        ArrayAdapter<String> adapter2;
        ListView listView, listView2;
        ImageButton ib_close;
        AlertDialog.Builder alertDialog = new
                AlertDialog.Builder(this);
        View rowList = getLayoutInflater().inflate(R.layout.row, null);
        ib_close = rowList.findViewById(R.id.ib_close);
        listView = rowList.findViewById(R.id.listView);
        listView2 = rowList.findViewById(R.id.listView2);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names);
        adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names2);
        listView.setAdapter(adapter);
        listView2.setAdapter(adapter2);
        adapter.notifyDataSetChanged();
        alertDialog.setView(rowList);
        AlertDialog dialog = alertDialog.create();
        dialog.show();

        ib_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public static boolean isvalidNarrative(String Nameformat) {

        String oldregex = "^(?!.*[ ]{2})(.|\\s)*[a-zA-Z]+(.|\\s)*$";
        Matcher matcher = Pattern.compile(oldregex).matcher(Nameformat);
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

            C3editor = C3.edit();
            C3editor.clear();
            C3editor.putString(c3_comNarrative, tenarrative.getText().toString().trim());
            C3editor.commit();
            finish();

        } else {

        }
    }


}