package com.example.micraapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.micraapp.databinding.ActivityCrime2Binding;
import com.example.micraapp.databinding.ActivityCrimeBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrimeActivity2 extends drawerbase {
    ActivityCrime2Binding activityCrime2Binding;
    TextInputLayout tlfirstname, tllastname, tlcontact, tlstreet, tlage, tlothers, tlincident, tldateofincident, tltimeofincident, tlreshouseno, tlresstreet, tlrescontact;
    TextInputEditText tefirstname, telastname, tedateofincident, tetimeofincident, teothers, tereshouseno, terescontact;

    EditText telandmark;
    AutoCompleteTextView testreet, teincident, teresstreet;
    Button btnproceed;
    Spinner spinners;

    ArrayAdapter<String> arrayAdapter;
    TextView tvlandmark;


    FrameLayout framelayout;

    String newcases[] = { "Bullying/harassment", "Illegal peeing", "Illegal parking", "Vandalism", "Assault (Pag-atake)", "Illegal electricity connection (Ilegal na koneksyon sa kuryente)", "Not paying Debt (Hindi pagbabayad ng utang)", "Not paying Rent (Hindi pagbabayad ng renta)", "Selling Drugs (pagbebenta ng Droga)", "Alarms and Scandals (Page-eskandalo)", "Theft (Pagnanakaw)", "Physical injuries inflicted in a tumultuous affray (Pisikal na pinsala na natamo sa isang away)",
            "Less Serious Physical Injuries", "Slight Physical Injuries And Maltreatment", "Abandoning A Minor (A Child Under Seven [7] Years Old) (Pag-abandona sa Isang Menor de edad (Batang Wala Pang Pitong [7] Taon)", "Trespassing", "Intriguing Against Honor (Paninira ng reputasyon)" ,"Other"};
    String[] street = {"Amaloi", "Amuslan", "Bahawan", "Bakil", "Cagna", "Cadang", "Corumi", "Eskinita", "Gabo", "Gasan", "Ilihan", "Inaman", "Lamila", "Mabituan", "Malac", "Malasimbo", "Masola", "Monong", "Payte", "Posooy", "Toctokan", "Wayan"};

    String[] street2 = {"", "Amaloi", "Amuslan", "Bahawan", "Bakil", "Cagna", "Cadang", "Corumi", "Eskinita", "Gabo", "Gasan", "Ilihan", "Inaman", "Lamila", "Mabituan", "Malac", "Malasimbo", "Masola", "Monong", "Payte", "Posooy", "Toctokan", "Wayan"};

    String[] cases = {"Vandalism", "Assault", "Street fight", "Robbery", "Physical injury", "Child abuse", "Crime against Person", "Selling Drugs", "Other"};
    String[] landmark = {"-Select-", "Near", "Along", "Behind", "Inside", "Outside", "Between", "In front of"};

    boolean haserror = false;
    boolean haserror2 = false;
    boolean haserror3 = false;
    boolean haserror4 = false;
    boolean haserror5 = false;
    boolean haserror6 = false;
    boolean haserror7 = false;
    boolean haserror8 = false;
    boolean haserror9 = false;
    boolean haserror10 = false;
    boolean haserror11 = false;
    boolean haserror12 = false;


    boolean isVisible = false;
    private DatePickerDialog.OnDateSetListener dateSetListener;

    private int CalendarHour, CalendarMinute;
    String format;
    Calendar calendar;
    TimePickerDialog timepickerdialog;


    BroadcastReceiver broadcastReceiver;


    SharedPreferences C2;
    SharedPreferences.Editor C2editor;

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

    Bundle b;
    byte[] bytrarray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime2);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        activityCrime2Binding = activityCrime2Binding.inflate(getLayoutInflater());
        allocateActivitytitle("CrimeActivity2");

        setContentView(activityCrime2Binding.getRoot());


        telandmark = findViewById(R.id.telandmark);
        tvlandmark = findViewById(R.id.tvlandmark);


        btnproceed = findViewById(R.id.btnproceed);
        testreet = findViewById(R.id.testreet);


        framelayout = findViewById(R.id.framelayout);

        telandmark = findViewById(R.id.telandmark);
        teothers = findViewById(R.id.teothers);
        tedateofincident = findViewById(R.id.tedateofincident);
        tefirstname = findViewById(R.id.tefirstname);
        telastname = findViewById(R.id.telastname);
        teincident = findViewById(R.id.teincident);
        tetimeofincident = findViewById(R.id.tetimeofincident);
        //respondent
        tereshouseno = findViewById(R.id.tereshouseno);
        teresstreet = findViewById(R.id.teresstreet);
        terescontact = findViewById(R.id.terescontact);

        tldateofincident = findViewById(R.id.tldateofincident);
        tltimeofincident = findViewById(R.id.tltimeofincident);

        tlincident = findViewById(R.id.tlincident);
        tlothers = findViewById(R.id.tlothers);
        tlage = findViewById(R.id.tlage);
        tlfirstname = findViewById(R.id.tlfirstname);
        tllastname = findViewById(R.id.tllastname);
        tlcontact = findViewById(R.id.tlcontact);
        tlstreet = findViewById(R.id.tlstreet);
        tlresstreet = findViewById(R.id.tlresstreet);
        tlreshouseno =findViewById(R.id.tlreshouseno);
        tlrescontact =findViewById(R.id.tlrescontact);
        spinners = findViewById(R.id.spinners);

        arrayAdapter = new ArrayAdapter<String>(this, R.layout.listitems, street);
        testreet.setAdapter(arrayAdapter);

        arrayAdapter = new ArrayAdapter<String>(this, R.layout.listitems, street2);
        teresstreet.setAdapter(arrayAdapter);

        arrayAdapter = new ArrayAdapter<String>(this, R.layout.listitems2, newcases);
        teincident.setAdapter(arrayAdapter);

        arrayAdapter = new ArrayAdapter<String>(this, R.layout.listitems, landmark);
        spinners.setAdapter(arrayAdapter);


        broadcastReceiver = new NetworkListener();
        restorecon();

        try {

            b = getIntent().getBundleExtra("bundle");

            if (b != null) {
                bytrarray = b.getByteArray("photo");

            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        C2 = getSharedPreferences("SPC2", Context.MODE_PRIVATE);


        if (!C2.contains(c2_resFname)) {
            tefirstname.setFocusable(true);
        } else {
            tefirstname.setText(C2.getString(c2_resFname, ""));

        }
        if (!C2.contains(c2_resLame)) {
            telastname.setFocusable(true);
        } else {
            telastname.setText(C2.getString(c2_resLame, ""));
        }
        if (!C2.contains(c2_resCategory)) {
            teincident.setFocusable(true);
        } else {
            teincident.setText(C2.getString(c2_resCategory, ""));
            arrayAdapter = new ArrayAdapter<String>(this, R.layout.listitems2, newcases);
            teincident.setAdapter(arrayAdapter);
        }

        if (!C2.contains(c2_resOther)) {
            teothers.setFocusable(true);
        } else {
            teincident.setText("Other");
            teothers.setText(C2.getString(c2_resOther, ""));
            framelayout.setVisibility(View.VISIBLE);
            arrayAdapter = new ArrayAdapter<String>(this, R.layout.listitems2, newcases);
            teincident.setAdapter(arrayAdapter);
        }

        if (!C2.contains(c2_resDate)) {
            tedateofincident.setFocusable(true);
        } else {
            tedateofincident.setText(C2.getString(c2_resDate, ""));
        }
        if (!C2.contains(c2_resTime)) {
            tetimeofincident.setFocusable(true);
        } else {
            tetimeofincident.setText(C2.getString(c2_resTime, ""));
        }

        if (!C2.contains(c2_resLocation)) {
            testreet.setFocusable(true);
        } else {
            testreet.setText(C2.getString(c2_resLocation, ""));
            arrayAdapter = new ArrayAdapter<String>(this, R.layout.listitems, street);
            testreet.setAdapter(arrayAdapter);
        }



        if (!C2.contains(c2_resLandmark)) {
            telandmark.setFocusable(true);
        } else {
            telandmark.setText(C2.getString(c2_resLandmark, ""));

            arrayAdapter = new ArrayAdapter<String>(this, R.layout.listitems, landmark);
            spinners.setAdapter(arrayAdapter);

        }

        if (!C2.contains(c2_resHouseno)) {
            tereshouseno.setFocusable(true);
        } else {
            tereshouseno.setText(C2.getString(c2_resHouseno, ""));
        }
        if (!C2.contains(c2_resStreet)) {
            teresstreet.setFocusable(true);
        } else {
            teresstreet.setText(C2.getString(c2_resStreet, ""));
            arrayAdapter = new ArrayAdapter<String>(this, R.layout.listitems, street2);
            teresstreet.setAdapter(arrayAdapter);
        }
        if (!C2.contains(c2_resContact)) {
            terescontact.setFocusable(true);
        } else {
            terescontact.setText(C2.getString(c2_resContact, ""));
        }


        tefirstname.setFilters(new InputFilter[]{filter2, new InputFilter.LengthFilter(16)});
        telastname.setFilters(new InputFilter[]{filter2, new InputFilter.LengthFilter(16)});
        teothers.setFilters(new InputFilter[]{filter2, new InputFilter.LengthFilter(50)});
        telandmark.setFilters(new InputFilter[]{filter2, new InputFilter.LengthFilter(100)});
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
                            haserror3 = true;
                            tlincident.setError("Alphabetical characters only [a-Z]");
                        } else {
                            tlincident.setError(null);
                            tlincident.setErrorEnabled(false);
                            haserror3 = false;
                        }*/


                    }
                } else {
                    tlincident.setError(null);
                    tlincident.setErrorEnabled(false);
                    haserror3 = false;
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
                            haserror4 = true;
                            tlothers.setError("Alphabetical characters only [a-Z]");
                        } else {
                            tlothers.setError(null);
                            tlothers.setErrorEnabled(false);
                            haserror4 = false;
                        }
                    }
                } else {
                    tlothers.setError(null);
                    tlothers.setErrorEnabled(false);
                    haserror4 = false;
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


        tedateofincident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();

                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        CrimeActivity2.this, R.style.MyTheme, dateSetListener, year, month, day
                );
                cal.add(Calendar.YEAR, -2);
                cal.set(Calendar.MONTH, Calendar.JANUARY);

                dialog.getDatePicker().setMinDate(cal.getTimeInMillis());
                dialog.getDatePicker().setMaxDate(new Date().getTime());

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff9c9c")));
                dialog.show();


            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {

                month = month + 1;
                String date = month + "/" + day + "/" + year;
                tedateofincident.setText(date);
            }
        };


        tetimeofincident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calendar = Calendar.getInstance();
                CalendarHour = calendar.get(Calendar.HOUR_OF_DAY);
                CalendarMinute = calendar.get(Calendar.MINUTE);


                timepickerdialog = new TimePickerDialog(CrimeActivity2.this, R.style.MyTheme,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                if (hourOfDay == 0) {
                                    hourOfDay += 12;

                                    format = "AM";
                                } else if (hourOfDay == 12) {
                                    format = "PM";
                                } else if (hourOfDay > 12) {
                                    hourOfDay -= 12;
                                    format = "PM";
                                } else {
                                    format = "AM";
                                }


                                String time = String.format("%02d:%02d", hourOfDay, minute);

                                tetimeofincident.setText(time + format);
                            }
                        }, CalendarHour, CalendarMinute, false);
                timepickerdialog.show();

            }


        });

        tedateofincident.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    tldateofincident.setError(null);
                    tldateofincident.setErrorEnabled(false);
                    haserror7 = false;
                } else {
                    tldateofincident.setError("Please select date");
                    haserror7 = true;
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tetimeofincident.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    tltimeofincident.setError(null);
                    tltimeofincident.setErrorEnabled(false);
                    haserror8 = false;
                } else {
                    tltimeofincident.setError("Please select time");
                    haserror8 = true;
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
                            haserror9 = true;
                            telandmark.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_error_24, 0);
                            tvlandmark.setVisibility(View.VISIBLE);

                        } else {
                            telandmark.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                            tvlandmark.setVisibility(View.GONE);
                            haserror9 = false;
                        }
                    }
                } else {
                    telandmark.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    tvlandmark.setVisibility(View.GONE);
                    haserror9 = false;
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ////////////////////////////////
        terescontact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    if (!TextUtils.isEmpty(terescontact.getText().toString())) {
                        if (!isValidPhoneNumber(tlrescontact.getPrefixText().toString() + terescontact.getText().toString())) {
                            haserror10 = true;
                            tlrescontact.setError("Number must be (+639*********)");
                        } else {
                            tlrescontact.setError(null);
                            tlrescontact.setErrorEnabled(false);
                            haserror10 = false;
                        }
                    }
                } else {
                    tlrescontact.setError(null);
                    tlrescontact.setErrorEnabled(false);
                    haserror10 = false;
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        tereshouseno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    if (!TextUtils.isEmpty(tereshouseno.getText().toString())) {
                        if (!isValidHouseNo(tereshouseno.getText().toString())) {
                            haserror11 = true;
                            tlreshouseno.setError("Invalid House No.");
                        } else {
                            tlreshouseno.setError(null);
                            tlreshouseno.setErrorEnabled(false);
                            haserror11 = false;
                        }
                    }
                } else {
                    tlreshouseno.setError(null);
                    tlreshouseno.setErrorEnabled(false);
                    haserror11 = false;
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        btnproceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /*    if (TextUtils.isEmpty(tefirstname.getText().toString())) {

                    haserror = true;
                    tlfirstname.setError("Alphabetical characters only [a-Z]");
                } else {
                    tlfirstname.setError(null);
                    tlfirstname.setErrorEnabled(false);
                    haserror = false;
                }


                if (TextUtils.isEmpty(telastname.getText().toString())) {
                    if (!isValidNameFormat(telastname.getText().toString())) {
                        haserror2 = true;
                        tllastname.setError("Alphabetical characters only [a-Z]");
                    } else {
                        tllastname.setError(null);
                        tllastname.setErrorEnabled(false);
                        haserror2 = false;
                    }
                }*/
                if (TextUtils.isEmpty(teincident.getText().toString())) {

                    haserror3 = true;
                    tlincident.setError("Please select incident");
                } else {
                    tlincident.setError(null);
                    tlincident.setErrorEnabled(false);
                    haserror3 = false;


                }

                if (framelayout.getVisibility() == View.VISIBLE) {

                    if (TextUtils.isEmpty(teothers.getText().toString())) {
                        haserror4 = true;
                        tlothers.setError("Alphabetical characters only [a-Z]");
                    } else {
                        tlothers.setError(null);
                        tlothers.setErrorEnabled(false);
                        haserror4 = false;

                    }
                }

                if (TextUtils.isEmpty(testreet.getText().toString())) {

                    tlstreet.setError("Please select street");
                    haserror6 = true;
                } else {
                    tlstreet.setError(null);
                    tlstreet.setErrorEnabled(false);
                    haserror6 = false;

                }


                if (TextUtils.isEmpty(tedateofincident.getText().toString())) {

                    tldateofincident.setError("Please select date");
                    haserror7 = true;
                } else {
                    tldateofincident.setError(null);
                    tldateofincident.setErrorEnabled(false);
                    haserror7 = false;

                }
                if (TextUtils.isEmpty(tetimeofincident.getText().toString())) {
                    tltimeofincident.setError("Please select time");
                    haserror8 = true;
                } else {
                    tltimeofincident.setError(null);
                    tltimeofincident.setErrorEnabled(false);
                    haserror8 = false;

                }


                if (TextUtils.isEmpty(telandmark.getText().toString())) {


                    telandmark.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_error_24, 0);
                    tvlandmark.setVisibility(View.VISIBLE);
                    haserror9 = true;
                } else {
                    telandmark.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    tvlandmark.setVisibility(View.GONE);
                    haserror9 = false;
                }

                if (TextUtils.isEmpty(tereshouseno.getText().toString()) && !TextUtils.isEmpty(teresstreet.getText().toString())) {

                    tlresstreet.setError("House No. is required");
                    haserror10 = true;
                } else {
                    tlresstreet.setError(null);
                    tlresstreet.setErrorEnabled(false);
                    haserror10 = false;

                }
                if (!TextUtils.isEmpty(tereshouseno.getText().toString()) && TextUtils.isEmpty(teresstreet.getText().toString())) {

                    tlreshouseno.setError("Street is required");
                    haserror11 = true;
                } else {
                    tlreshouseno.setError(null);
                    tlreshouseno.setErrorEnabled(false);
                    haserror11 = false;

                }

                if (haserror == false && haserror2 == false && haserror3 == false && haserror4 == false && haserror5 == false && haserror6 == false && haserror7 == false && haserror8 == false && haserror9 == false  && haserror10 == false  && haserror11 == false) {


                    C2editor = C2.edit();
                    C2editor.clear();


                    if (teothers.getText().toString().length() <= 0) {
                        C2editor.putString(c2_resCategory, teincident.getText().toString().trim());
                    } else {
                        C2editor.putString(c2_resOther, teothers.getText().toString().trim());
                    }

                    C2editor.putString(c2_resFname, tefirstname.getText().toString().trim());
                    C2editor.putString(c2_resLame, telastname.getText().toString().trim());
                    C2editor.putString(c2_resDate, tedateofincident.getText().toString().trim());
                    C2editor.putString(c2_resTime, tetimeofincident.getText().toString().trim());
                    C2editor.putString(c2_resLocation, testreet.getText().toString().trim());
                    C2editor.putString(c2_resLandmark, telandmark.getText().toString().trim());
                    C2editor.putString(c2_resContact, terescontact.getText().toString().trim());
                    C2editor.putString(c2_resHouseno, tereshouseno.getText().toString().trim());
                    C2editor.putString(c2_resStreet, teresstreet.getText().toString().trim());

                    C2editor.commit();

                    Intent intent = new Intent(CrimeActivity2.this, CrimeActivity2b.class);

                    if (b != null) {
                        b.putByteArray("photo", bytrarray);
                        intent.putExtra("bundle", b);
                    }

                    startActivity(intent);


                }
            }
        });

    }


    InputFilter filter1 = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source.toString().matches("(^(?!.*[ ]{2})[abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ ]+$)|(^[N]+[\\/]+[A])+$")) {
                return source;
            }

            return "";
        }
    };

    InputFilter filter2 = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source.toString().matches("^(?!.*[ ]{2})[abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ ]+")) {
                return source;
            }

            return "";
        }
    };

    public void TextOnly2(TextInputEditText textInputEditText) {


        textInputEditText.setFilters(new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence cs, int start,
                                               int end, Spanned spanned, int dStart, int dEnd) {
                        // TODO Auto-generated method stub
                        if (cs.equals("")) { // for backspace
                            return cs;
                        }
                        if (cs.toString().matches("^(?!.*[ ]{2})[abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ ]+")) {
                            return cs;
                        }
                        return "";
                    }
                }
        });
    }


 /*   public static boolean isValidNameFormat(String Nameformat) {
        String newregex = "(^(?!.*[ ]{2})[abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ ]+)|(^[N]+[\\/]+[A])+$";
        Matcher matcher = Pattern.compile(newregex).matcher(Nameformat);
        return matcher.matches();

    }
*/

    public static boolean isValidNameFormat(String Nameformat) {
        String newregex = "^(?!.*[ ]{2})[abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ ]+";
        Matcher matcher = Pattern.compile(newregex).matcher(Nameformat);
        return matcher.matches();

    }

    public static boolean isvalidOthers(String Nameformat) {

        String oldregex = "^(?!.*[ ]{2})[abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ ]+";
        Matcher matcher = Pattern.compile(oldregex).matcher(Nameformat);
        return matcher.matches();

    }

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

    @Override
    public void onBackPressed() {
        if (true) {

            C2editor = C2.edit();
            C2editor.clear();


            if (teothers.getText().toString().length() <= 0) {
                C2editor.putString(c2_resCategory, teincident.getText().toString().trim());
            } else {
                C2editor.putString(c2_resOther, teothers.getText().toString().trim());
            }


            C2editor.putString(c2_resFname, tefirstname.getText().toString().trim());
            C2editor.putString(c2_resLame, telastname.getText().toString().trim());
            C2editor.putString(c2_resDate, tedateofincident.getText().toString().trim());
            C2editor.putString(c2_resTime, tetimeofincident.getText().toString().trim());
            C2editor.putString(c2_resLocation, testreet.getText().toString().trim());
            C2editor.putString(c2_resLandmark, telandmark.getText().toString().trim());
            C2editor.putString(c2_resContact, terescontact.getText().toString().trim());
            C2editor.putString(c2_resHouseno, tereshouseno.getText().toString().trim());
            C2editor.putString(c2_resStreet, teresstreet.getText().toString().trim());

            C2editor.commit();
            finish();

        } else {

        }
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


}