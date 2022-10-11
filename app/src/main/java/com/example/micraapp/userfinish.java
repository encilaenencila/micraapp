package com.example.micraapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.bumptech.glide.Glide;
import com.example.micraapp.databinding.ActivityUserfinishBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class userfinish extends drawerbase {
    ActivityUserfinishBinding activityUserfinishBinding;
    ImageView imageview, imageview2;
    TextView getstatus, getcategory, getcategorydescription, getlocation, getlandmark, getdescription, getrespondedby, getresponddate, getrespondtime, getrespondmessage, tvfeedback;
    ToggleButton tb_show_report, tb_show_report2, tb_show_report3;
    CardView cardView, cardView2, cardView3;
    Button btnfeedback;
    ProgressDialog progressDialog;
    private String STATUS, CATEGORY, CATEGORYDESCRIPTION, IMAGEURL, IMAGEURL2, LOCATION, DESCRIPTION, RESPONDEDBY, RESPONDDATE, RESPONDTIME, RESPONDMESSAGE, LANDMARK, RESOLVEFEEDBACK, RESIDENTFEEDBACK, REPORTNUMBER;
    private String FEEDBACK_URL;
    boolean haserror = false;
    boolean haserror2 = false;
    TextInputEditText tefeedback;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        activityUserfinishBinding = ActivityUserfinishBinding.inflate(getLayoutInflater());
        setContentView(activityUserfinishBinding.getRoot());
        allocateActivitytitle("Status Report");


        STATUS = getIntent().getStringExtra("status");
        CATEGORY = getIntent().getStringExtra("category");
        CATEGORYDESCRIPTION = getIntent().getStringExtra("categorydescription");
        IMAGEURL = getIntent().getStringExtra("imageurl");
        IMAGEURL2 = getIntent().getStringExtra("finishproof");
        LOCATION = getIntent().getStringExtra("location");
        DESCRIPTION = getIntent().getStringExtra("description");
        RESPONDEDBY = getIntent().getStringExtra("respondername");
        RESPONDDATE = getIntent().getStringExtra("responddate");
        RESPONDTIME = getIntent().getStringExtra("respondtime");
        RESPONDMESSAGE = getIntent().getStringExtra("respondmessage");
        LANDMARK = getIntent().getStringExtra("landmark");
        RESOLVEFEEDBACK = getIntent().getStringExtra("resolvefeedback");
        RESIDENTFEEDBACK = getIntent().getStringExtra("residentfeedback");
        REPORTNUMBER = getIntent().getStringExtra("reportnumber");


        imageview = findViewById(R.id.imgviewinfra);
        imageview2 = findViewById(R.id.imgviewinfra2);
        getstatus = findViewById(R.id.tvstatus);
        getcategory = findViewById(R.id.tvcategory);
        getcategorydescription = findViewById(R.id.tvcategorydescription);
        getlocation = findViewById(R.id.tvlocation);
        getdescription = findViewById(R.id.infradescriptiondata);
        getrespondedby = findViewById(R.id.tvrespondername);
        getresponddate = findViewById(R.id.tvresponddate);
        getrespondtime = findViewById(R.id.tvrespondtimes);
        getrespondmessage = findViewById(R.id.tvrespondmessage);
        getlandmark = findViewById(R.id.tvlandmark);
        tb_show_report = findViewById(R.id.tb_show_report);
        tb_show_report2 = findViewById(R.id.tb_show_report2);
        tb_show_report3 = findViewById(R.id.tb_show_report3);
        cardView = findViewById(R.id.carding);
        cardView2 = findViewById(R.id.carding2);
        cardView3 = findViewById(R.id.carding3);

        btnfeedback = findViewById(R.id.btnfeedback);
        tvfeedback = findViewById(R.id.tvfeedback);




        Log.d("CATEGORY", CATEGORY);
        Log.d("REPORTNUMBER", REPORTNUMBER);



        if (!IMAGEURL.isEmpty()) {
            Glide.with(this)
                    .load(IMAGEURL)
                    .fitCenter()
                    .into(imageview);
        } else {
            imageview.setBackgroundResource(R.drawable.no_image_avail_2);
        }
        if (!IMAGEURL2.isEmpty()) {
            Glide.with(this)
                    .load(IMAGEURL2)
                    .fitCenter()
                    .into(imageview2);
        } else {
            imageview2.setBackgroundResource(R.drawable.no_image_avail_2);
        }
        if (RESPONDEDBY.equals("null")) {
            getrespondedby.setText("No Response Yet");
        } else {
            getrespondedby.setText(getIntent().getStringExtra("respondername"));
        }

        if (RESPONDDATE.equals("null")) {
            getresponddate.setText("No Response yet");
        } else {
            getresponddate.setText(getIntent().getStringExtra("responddate"));
        }
        if (RESPONDTIME.equals("null")) {
            getrespondtime.setText("No Response yet");
        } else {
            getrespondtime.setText(getIntent().getStringExtra("respondtime"));
        }
        if (RESPONDMESSAGE.equals("null")) {
            getrespondmessage.setText("No Response yet");
        } else {
            getrespondmessage.setText(getIntent().getStringExtra("respondmessage"));
        }

        getstatus.setText(STATUS);
        getcategory.setText(CATEGORY);
        getcategorydescription.setText(CATEGORYDESCRIPTION);
        getlocation.setText(LOCATION);
        getdescription.setText(DESCRIPTION);
        getlandmark.setText(LANDMARK);


        tb_show_report.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_down_24, 0);
        tb_show_report2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_down_24, 0);
        tb_show_report3.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_down_24, 0);

        tb_show_report.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked) {
                if (isChecked) {
                    tb_show_report.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_up_24, 0);
                    cardView.setVisibility(View.VISIBLE);
                    cardView.setAlpha(0.0f);
                    cardView.animate()
                            .translationY(1)
                            .alpha(1.0f)
                            .setListener(null);
                } else {
                    tb_show_report.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_down_24, 0);

                    cardView.setAlpha(0);
                    cardView.animate()
                            .translationY(0)
                            .alpha(0)
                            .setListener(null);
                    cardView.setVisibility(View.GONE);
                }
            }
        });
        tb_show_report2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked) {
                if (isChecked) {
                    tb_show_report2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_up_24, 0);
                    cardView2.setVisibility(View.VISIBLE);
                    cardView2.setAlpha(0.0f);
                    cardView2.animate()
                            .translationY(1)
                            .alpha(1.0f)
                            .setListener(null);
                } else {
                    tb_show_report2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_down_24, 0);

                    cardView2.setAlpha(0);
                    cardView2.animate()
                            .translationY(0)
                            .alpha(0)
                            .setListener(null);
                    cardView2.setVisibility(View.GONE);
                }
            }
        });
        tb_show_report3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked) {
                if (isChecked) {
                    tb_show_report3.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_up_24, 0);
                    cardView3.setVisibility(View.VISIBLE);
                    cardView3.setAlpha(0.0f);
                    cardView3.animate()
                            .translationY(1)
                            .alpha(1.0f)
                            .setListener(null);
                } else {
                    tb_show_report3.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_down_24, 0);
                    cardView3.setAlpha(0);
                    cardView3.animate()
                            .translationY(0)
                            .alpha(0)
                            .setListener(null);
                    cardView3.setVisibility(View.GONE);
                }
            }
        });




        if (STATUS.equals("Finished") && RESIDENTFEEDBACK.equals("null")) {
            tvfeedback.setVisibility(View.VISIBLE);
            btnfeedback.setVisibility(View.VISIBLE);

        } else {
            btnfeedback.setVisibility(View.GONE);
            tvfeedback.setVisibility(View.VISIBLE);
            tvfeedback.setText("Feedback currently unavailable!");
        }
        if (CATEGORY.equals("Waste")) {
            FEEDBACK_URL = "http://www.micra.services/Wastereport/InsertFeedbackWaste.php";

        } else if (CATEGORY.equals("Infrastructure")) {
            FEEDBACK_URL = "http://www.micra.services/Infrastracture/InsertFeedbackInfra.php";

        } else if (CATEGORY.equals("Crime")) {
            FEEDBACK_URL = "http://www.micra.services/IncidentReport/InsertFeedbackCrime.php";


        }
        btnfeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SendFeedback();


            }
        });


    }


    public void SendFeedback() {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.send_feedback);
        dialog.show();
        dialog.setCancelable(false);
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.99);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.35);

        dialog.getWindow().setLayout(width, height);
        Button btnok = (Button) dialog.findViewById(R.id.btnok);
        Button btncancel = (Button) dialog.findViewById(R.id.btncancel);
        TextInputLayout tlfeedback = (TextInputLayout) dialog.findViewById(R.id.tlfeedback);
        tefeedback = (TextInputEditText) dialog.findViewById(R.id.tefeedback);

        TextOnly(tefeedback);
        tlfeedback.setError("Alphabetical characters only!");
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(tefeedback.getText().toString())) {
                    tlfeedback.setError("This field is required!");
                    haserror2 = true;
                } else {
                    haserror2 = false;
                }
                if (haserror == false && haserror2 == false) {

                    InsertFeedback();
                    dialog.dismiss();

                }

            }
        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tefeedback.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() <= 0) {
                    tlfeedback.setError("Alphabetical characters only!");
                    haserror = true;
                } else {
                    tlfeedback.setError(null);
                    tlfeedback.setErrorEnabled(false);
                    haserror = false;
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void TextOnly(TextInputEditText textInputEditText) {
        textInputEditText.setFilters(new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence cs, int start,
                                               int end, Spanned spanned, int dStart, int dEnd) {

                        if (cs.equals("")) {
                            return cs;
                        }
                        if (cs.toString().matches("[a-zA-Z ]+")) {
                            return cs;
                        }
                        return "";
                    }
                }
        });
    }

    public static boolean isValidNameFormat(String Format) {
        Matcher matcher = Pattern.compile("/^\\w+$/").matcher(Format);
        return matcher.matches();

    }

    public void InsertFeedback() {
        progressDialog = progressDialog.show(userfinish
                .this, "Please wait", "sending feedback..");


        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FEEDBACK_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        if (response.equals("success")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(userfinish.this);
                            builder.setMessage(" Thank you. Feedback sent!")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            tvfeedback.setVisibility(View.VISIBLE);
                                            btnfeedback.setVisibility(View.GONE);
                                            tvfeedback.setText("Feedback currently unavailable!");
                                            dialog.dismiss();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();

                        } else {
                            Toast.makeText(getApplicationContext(), "Something went wrong?", Toast.LENGTH_LONG).show();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("Error in connection", error.toString());
                if (error instanceof NoConnectionError) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_LONG).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("reportnumber", REPORTNUMBER);
                params.put("residentfeedback", tefeedback.getText().toString());
                params.put("feedback", "1");
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
    public void onBackPressed() {
        if (true) {
            Intent intent = new Intent(userfinish.this,recordreports.class);
            startActivity(intent);

        } else {

        }
    }


}