package com.example.micraapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.graphics.drawable.ColorDrawable;
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
import android.text.Html;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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

import com.example.micraapp.databinding.ActivityInfra4Binding;
import com.example.micraapp.databinding.ActivityWaste4Binding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.BufferedReader;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WasteActivity4 extends drawerbase implements  RecognitionListener {
    ActivityWaste4Binding activityWaste4Binding;
    TextInputLayout tlfirstname, tllastname, tlcontact, tlhouseno, tlstreet, tlage, tlothers, tlincident, tlnarrative, tldateofincident, tltimeofincident;
    TextInputEditText tedateofincident, tetimeofincident, teothers, tehouseno, tenarrative;
    EditText telandmark;
    AutoCompleteTextView tefirstname, telastname, testreet, teincident;
    Button proceed;
    Spinner spinners;
    String SpinnerText;
    ArrayAdapter<String> arrayAdapter;
    TextView tvlandmark;

    FrameLayout framelayout;
    String[] street = {"Amaloi", "Amuslan", "Bahawan", "Bakil", "Cagna", "Cadang", "Corumi", "Eskinita", "Gabo", "Gasan", "Ilihan", "Inaman", "Lamila", "Mabituan", "Malac", "Malasimbo", "Masola", "Monong", "Payte", "Posooy", "Toctokan", "Wayan"};

    String[] cases = {"Vandalism", "Assault", "Street fight", "Robbery", "Physical injury", "Child abuse", "Crime against Person", "Selling Drugs", "Other"};
    String[] NA = {"N/A"};
    String[] landmark = {"-Select-", "Near", "Along", "Behind", "Inside", "Outside", "Between", "In front of"};
    String waste2[]={"Illegal Dumping of Garbage / Waste Disposal (Pagtapon ng basura sa hindi tamang lugar) ", "Burning of waste (Pagsusunog ng basura)", "Open-burning dumps (Pagsusunog sa tambakan ng basura)",
            "Improper collection of waste (Hindi wastong pagkolekta ng basura)", "Illegal Waste Disposal (Pagtapon ng mapanganib na  basura) ", "Clogged Drainage (Baradong kanal)", "Chemical Waste (Pagtatapon ng kemikal na basura)", "Other"};

    private String selectedphoto;

    boolean isVisible = false;
    ProgressDialog progressDialog;
    boolean haserror = false;
    boolean haserror2 = false;
    boolean haserror3 = false;
    boolean haserror4 = false;
    boolean haserror5 = false;


    private String comFname, comLname, ComAge, ComContact, ComAddress, ComEmail, CurLoc;
    Bitmap intentBitmap;
    BroadcastReceiver broadcastReceiver;


    private static final int REQUEST_RECORD_PERMISSION = 100;

    private ToggleButton toggleButton;
    private ProgressBar progressBar;
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;


    TextView tvtalk;
    ImageButton ib_instruction, ibread, ibread2;
    private String LOG_TAG = "Speechtotext";
    TextToSpeech textToSpeech;


    SharedPreferences C3;
    SharedPreferences.Editor C3editor;
    private String c3_Category = "KEY_C3CATEGORY";
    private String c3_Other = "KEY_C3OTHER";
    private String c3_Location = "KEY_C3LOCATION";
    private String c3_Landmark = "KEY_C3LANDMARK";
    private String c3_Narrative = "KEY_C3NARRATIVE";


    private static final String FILE_NAME = "Input.txt";


    private  String selectedselfie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        String languageToLoad = "fil"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());


        setContentView(R.layout.activity_infra4);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        activityWaste4Binding = activityWaste4Binding.inflate(getLayoutInflater());
        allocateActivitytitle("WasteActivity4");
        setContentView(activityWaste4Binding.getRoot());


        tvlandmark = findViewById(R.id.tvlandmark);
        proceed = findViewById(R.id.btnproceed);
        ib_instruction = findViewById(R.id.ib_instruction);
        testreet = findViewById(R.id.testreet);


        framelayout = findViewById(R.id.framelayout);

        tenarrative = findViewById(R.id.tenarrative);
        telandmark = findViewById(R.id.telandmark);
        teothers = findViewById(R.id.teothers);
        tedateofincident = findViewById(R.id.tedateofincident);
        tefirstname = findViewById(R.id.tefirstname);
        telastname = findViewById(R.id.telastname);
        teincident = findViewById(R.id.teincident);
        tetimeofincident = findViewById(R.id.tetimeofincident);


        tldateofincident = findViewById(R.id.tldateofincident);
        tltimeofincident = findViewById(R.id.tltimeofincident);
        tlnarrative = findViewById(R.id.tlnarrative);
        tlincident = findViewById(R.id.tlincident);
        tlothers = findViewById(R.id.tlothers);
        tlage = findViewById(R.id.tlage);
        tlfirstname = findViewById(R.id.tlfirstname);
        tllastname = findViewById(R.id.tllastname);
        tlcontact = findViewById(R.id.tlcontact);
        tlstreet = findViewById(R.id.tlstreet);

        spinners = findViewById(R.id.spinners);
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.listitems, street);
        testreet.setAdapter(arrayAdapter);


        arrayAdapter = new ArrayAdapter<String>(this, R.layout.listitems, waste2);
        teincident.setAdapter(arrayAdapter);

        arrayAdapter = new ArrayAdapter<String>(this, R.layout.listitems, landmark);
        spinners.setAdapter(arrayAdapter);
        SpinnerText = spinners.getSelectedItem().toString();


        broadcastReceiver = new NetworkListener();
        restorecon();
        UploadSelfie();
        C3 = getSharedPreferences("SPC3", Context.MODE_PRIVATE);


        if (!C3.contains(c3_Category)) {
            teincident.setFocusable(true);
        } else {
            teincident.setText(C3.getString(c3_Category, ""));
            arrayAdapter = new ArrayAdapter<String>(this, R.layout.listitems, waste2);
            teincident.setAdapter(arrayAdapter);

        }

        if (!C3.contains(c3_Other)) {
            teothers.setFocusable(true);
        } else {
            teincident.setText("Other");
            teothers.setText(C3.getString(c3_Other, ""));
            framelayout.setVisibility(View.VISIBLE);
            arrayAdapter = new ArrayAdapter<String>(this, R.layout.listitems, cases);
            teincident.setAdapter(arrayAdapter);
        }

        if (!C3.contains(c3_Location)) {
            testreet.setFocusable(true);
        } else {
            testreet.setText(C3.getString(c3_Location, ""));
            arrayAdapter = new ArrayAdapter<String>(this, R.layout.listitems, street);
            testreet.setAdapter(arrayAdapter);
        }


        if (!C3.contains(c3_Landmark)) {
            telandmark.setFocusable(true);
        } else {
            telandmark.setText(C3.getString(c3_Landmark, ""));
            arrayAdapter = new ArrayAdapter<String>(this, R.layout.listitems, landmark);
            spinners.setAdapter(arrayAdapter);

        }


        if (!C3.contains(c3_Narrative)) {
            tenarrative.setFocusable(true);
        } else {
            tenarrative.setText(C3.getString(c3_Narrative, ""));
        }

        telandmark.setFilters(new InputFilter[]{filter2, new InputFilter.LengthFilter(100)});
        teothers.setFilters(new InputFilter[]{filter2, new InputFilter.LengthFilter(50)});
        spinners.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (spinners.getSelectedItem().toString().equals("-Select-")) {

                } else {
                    telandmark.setText(telandmark.getText().toString() + " " + parent.getItemAtPosition(position).toString());
                    telandmark.requestFocus();
                    telandmark.setSelection(telandmark.getText().toString().length());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                telandmark.setFocusable(true);
            }
        });


        progressBar = findViewById(R.id.progressBar1);
        toggleButton = findViewById(R.id.toggleButton1);

        tenarrative = findViewById(R.id.tenarrative);
        tvtalk = findViewById(R.id.tvtalk);

        ib_instruction = findViewById(R.id.ib_instruction);

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
                            (WasteActivity4.this,
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
                    Toast.makeText(WasteActivity4.this, "Field is empty", Toast.LENGTH_SHORT).show();
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

        try {

            Bundle b = getIntent().getBundleExtra("bundle");

            if (b != null) {
                byte[] bytrarray = b.getByteArray("photo");
                intentBitmap = BitmapFactory.decodeByteArray(bytrarray, 0, bytrarray.length);
                selectedphoto = imagetoString(ReduceSize(intentBitmap, 400));
            } else {
                selectedphoto = "";
            }
            comFname = getIntent().getStringExtra("firstname");
            comLname = getIntent().getStringExtra("lastname");
            ComContact = getIntent().getStringExtra("contact");
            ComEmail = getIntent().getStringExtra("email");
            ComAddress = getIntent().getStringExtra("address");
            CurLoc = getIntent().getStringExtra("curloc");
            //Toast.makeText(getApplicationContext(), CurLoc, Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
        }


        teincident.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    if (!TextUtils.isEmpty(teincident.getText().toString())) {
                        if (teincident.getText().toString().equals("Other")) {
                            framelayout.setVisibility(View.VISIBLE);
                            isVisible = true;
                            framelayout.animate().translationYBy(1).setDuration(1000).start();
                        } else {
                            teothers.setText("");
                            framelayout.setVisibility(View.GONE);
                            framelayout.animate().translationYBy(1).setDuration(600).start();
                            isVisible = false;
                        }

                        /*
                        if (!isvalidOthers(teincident.getText().toString())) {
                            haserror = true;
                            tlincident.setError("Alphabetical characters only [a-Z]");
                        } else {
                            tlincident.setError(null);
                            tlincident.setErrorEnabled(false);
                            haserror = false;
                        }*/


                    }
                } else {
                    tlincident.setError(null);
                    tlincident.setErrorEnabled(false);
                    haserror = false;
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        teothers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    if (!TextUtils.isEmpty(teothers.getText().toString())) {
                        if (!isvalidOthers(teothers.getText().toString())) {
                            haserror2 = true;
                            tlothers.setError("Alphabetical characters only [a-Z]");
                        } else {
                            tlothers.setError(null);
                            tlothers.setErrorEnabled(false);
                            haserror2 = false;
                        }
                    }
                } else {
                    tlothers.setError(null);
                    tlothers.setErrorEnabled(false);
                    haserror2 = false;
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        testreet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    tlstreet.setError(null);
                    tlstreet.setErrorEnabled(false);
                    haserror3 = false;
                } else {
                    haserror3 = true;
                    tlstreet.setError("Please select street");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        telandmark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    if (!TextUtils.isEmpty(telandmark.getText().toString())) {
                        if (!isvalidOthers(telandmark.getText().toString())) {
                            haserror4= true;
                            telandmark.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_error_24, 0);
                            tvlandmark.setVisibility(View.VISIBLE);

                        } else {
                            telandmark.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                            tvlandmark.setVisibility(View.GONE);
                            haserror4 = false;
                        }
                    }
                } else {
                    telandmark.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    tvlandmark.setVisibility(View.GONE);
                    haserror4 = false;
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

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
                            haserror5 = true;
                            tlnarrative.setError("Please avoid unnecessary characters. ");
                        } else {
                            tlnarrative.setError(null);
                            tlnarrative.setErrorEnabled(false);
                            haserror5 = false;
                        }
                    }
                } else {
                    tlnarrative.setError(null);
                    tlnarrative.setErrorEnabled(false);
                    haserror5 = false;
                }


            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        ib_instruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Instructions();
            }
        });
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(teincident.getText().toString())) {

                    tlincident.setError("Please select infrastructure");
                    haserror = true;
                } else {
                    tlincident.setError(null);
                    tlincident.setErrorEnabled(false);
                    haserror = false;

                }

                if (framelayout.getVisibility() == View.VISIBLE) {
                    if (TextUtils.isEmpty(teothers.getText().toString())) {

                        tlothers.setError("Alphabetical characters only [a-Z]");
                        haserror2 = true;
                    } else {
                        tlothers.setError(null);
                        tlothers.setErrorEnabled(false);
                        haserror2 = false;

                    }
                }
                if (TextUtils.isEmpty(testreet.getText().toString())) {

                    tlstreet.setError("Please select street");
                    haserror3 = true;
                } else {
                    tlstreet.setError(null);
                    tlstreet.setErrorEnabled(false);
                    haserror3 = false;

                }

                if (TextUtils.isEmpty(telandmark.getText().toString())) {

                    telandmark.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_error_24, 0);
                    tvlandmark.setVisibility(View.VISIBLE);
                    haserror4 = true;
                } else {
                    telandmark.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    tvlandmark.setVisibility(View.GONE);
                    haserror4 = false;
                }

                if (TextUtils.isEmpty(tenarrative.getText().toString())) {
                    tlnarrative.setError("This field is required");
                    haserror5 = true;
                } else {
                    tlnarrative.setError(null);
                    tlnarrative.setErrorEnabled(false);
                    haserror5 = false;

                }


                if (haserror == false && haserror2 == false && haserror3 == false && haserror4 == false && haserror5 == false) {

                    new android.app.AlertDialog.Builder(WasteActivity4.this)

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
                                        out.append(ComContact + "\n");
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
        progressDialog = progressDialog.show(WasteActivity4
                .this, "Please wait", "submitting your report..");


        String url = "http://www.micra.services/Wastereport/UploadWaste.php";
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        if (response.equals("success")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(WasteActivity4.this);
                            builder.setMessage("Report Submitted!")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            C3editor = C3.edit();
                                            C3editor.clear().commit();

                                            Intent intent = new Intent(WasteActivity4.this, Dashboard.class);
                                            intent.putExtra("Report", "Done");
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();

                        } else {
                            Log.d("Response",""+response);
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
                    Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_LONG).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                comFname = comFname.substring(0, 1).toUpperCase() + comFname.substring(1).toLowerCase();
                comLname = comLname.substring(0, 1).toUpperCase() + comLname.substring(1).toLowerCase();


                params.put("photo", selectedphoto);
                params.put("firstname", comFname);
                params.put("lastname", comLname);
                params.put("address", ComAddress);
                params.put("contact", ComContact);
                params.put("currentlocation", CurLoc);
                params.put("emailaddress", ComEmail);
                params.put("location", testreet.getText().toString().trim());
                if(TextUtils.isEmpty(teothers.getText().toString())){
                    params.put("categoryDescription", teincident.getText().toString());
                }else{
                    params.put("categoryDescription", teothers.getText().toString());
                }
                params.put("Description", tenarrative.getText().toString());
                params.put("Landmark", telandmark.getText().toString().trim());
                params.put("reporterselfie",selectedselfie);
                params.put("isResident", "Non Resident");
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
                    Toast.makeText(WasteActivity4.this, "Permission Denied!", Toast
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
            tvtalk.setTextColor(Color.parseColor("#757575"));
            tvtalk.setText("Stopped Listening.");
            tenarrative.requestFocus();
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

      /*  Log.d("MATCHES",""+matches);
        String text = "";
        for (String result : matches)
            text += result + "\n";


            returnedText.setText(text);
      */

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



    InputFilter filter2 = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source.toString().matches("^(?!.*[ ]{2})[abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ ]+")) {
                return source;
            }

            return "";
        }
    };
    public static boolean isvalidOthers(String Nameformat) {

        String oldregex = "^(?!.*[ ]{2})[abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ ]+";
        Matcher matcher = Pattern.compile(oldregex).matcher(Nameformat);
        return matcher.matches();

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


            if (teothers.getText().toString().length() <= 0) {
                C3editor.putString(c3_Category, teincident.getText().toString().trim());
            } else {
                C3editor.putString(c3_Other, teothers.getText().toString().trim());
            }
            C3editor.putString(c3_Location, testreet.getText().toString().trim());
            C3editor.putString(c3_Landmark, telandmark.getText().toString().trim());
            C3editor.putString(c3_Narrative, tenarrative.getText().toString().trim());

            C3editor.commit();
            finish();

        } else {

        }
    }
}