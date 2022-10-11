package com.example.micraapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.micraapp.databinding.ActivityInsfractractureBinding;
import com.example.micraapp.databinding.ActivityWastereportBinding;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class wastereport extends drawerbase {
    Button btnsubmit;
    ActivityWastereportBinding activityWastereportBinding;
    String[] itemsa = {"Illegal waste", "Uncollected waste","Illegal Dumping", "Burning of waste","Clogged drainage","Chemical Waste","Open burning dumps"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> arrayAdapter;
    BroadcastReceiver broadcastReceiver;
    AutoCompleteTextView AutoconmpleteLocation;
    String[] street= {"Amaloi", "Amuslan","Bahawan","Bakil","Cagna","Corumi","Eskinita","Gabo","Gasan","Ilihan","Inaman","Lamila","Mabituan","Malac","Malasimbo",
            "Masola","Monong","Payte","Posooy","Toctokan","Wayan"};
    ArrayAdapter<String> arrayLocation;

    private URI filepath;
    private Bitmap bitmap;
    private final int IMG_REQUEST = 1;
    public ImageView imgview;

    public EditText txtlocation, txttime, txtdescription, txtcategory ;
    private String location = "", description = "", category = "", date, time;
    String upload_URL = "http://www.micra.services/Wastereport/upload.php";
    public Button selectphoto,txtdate;
    SharedPreferences userinfo;
    public DatePickerDialog datePickerDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityWastereportBinding = activityWastereportBinding.inflate(getLayoutInflater());
        setContentView(activityWastereportBinding.getRoot());
        allocateActivitytitle("Waste Report");
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        broadcastReceiver = new NetworkListener();
        restorecon();


        imgview= (ImageView) findViewById(R.id.imageWASTE);
        autoCompleteTextView =(AutoCompleteTextView)findViewById(R.id.dropdownwaste);
        arrayAdapter = new ArrayAdapter<String>(wastereport.this, R.layout.listitems, itemsa);
        autoCompleteTextView.setAdapter(arrayAdapter);

        AutoconmpleteLocation =(AutoCompleteTextView) findViewById(R.id.txtlocationsinfra);
        arrayLocation = new ArrayAdapter<String>(this,R.layout.liststreetlocation,street);
        AutoconmpleteLocation.setAdapter(arrayLocation);


        txtcategory=findViewById(R.id.dropdownwaste);
        txtdescription=findViewById(R.id.editTextTextDescrriptionwaste);
        btnsubmit=(Button)findViewById(R.id.buttonUpload);
        initDatePicker();
        txtdate = findViewById(R.id.editTextDate);
        selectphoto=findViewById(R.id.btnchoosephoto);
        selectphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (imgview.getDrawable()==null){
                    Toast.makeText(wastereport.this, "Please Insert image", Toast.LENGTH_SHORT).show();
                }
                if ( TextUtils.isEmpty(txtcategory.getText().toString()) || TextUtils.isEmpty(txtdescription.getText().toString()) ) {
                    Toast.makeText(wastereport.this, "Please complete the field", Toast.LENGTH_LONG).show();
                }else {
                    submit();

                }
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

    private String getTodaysDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month)
    {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";
        return "JAN";
    }
    private void initDatePicker() {DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
    {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day)
        {
            month = month + 1;
            String date = makeDateString(day, month, year);
            txtdate.setText(date);
        }

        private String makeDateString(int day, int month, int year) {
            return getMonthFormat(month) + " " + day + " " + year;
        }
    };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }
    public void OpendatePicker(View view) {
        datePickerDialog.hide();
        txtdate.setText(getTodaysDate());
    }
    public void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri path = data.getData();
            try {
                //Assign image into bitmap
                bitmap = MediaStore.Images.Media.getBitmap(wastereport.this.getContentResolver(), path);
                imgview.getLayoutParams().height = 500;
                imgview.getLayoutParams().width = 500;
                imgview.setImageBitmap(bitmap);
                imgview.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private String imagetoString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgbytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgbytes, Base64.DEFAULT);
    }
    public void submit() {
        userinfo = getSharedPreferences("info", Context.MODE_PRIVATE);
        String residentid = userinfo.getString("infoid","");
        String firstname = userinfo.getString("infofirstname","");
        String lastname = userinfo.getString("infolastname","");


        location = AutoconmpleteLocation.getText().toString().trim();
        category = txtcategory.getText().toString().trim();
        description = txtdescription.getText().toString().trim();


        if (!AutoconmpleteLocation.equals("") && !txtdescription.equals("") && !txtcategory.equals("")) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, upload_URL, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    AutoconmpleteLocation.setText("");
                    txtdescription.setText("");
                    txtcategory.setText("");
                    txtdate.setText("");
                    imgview.setVisibility(View.INVISIBLE);
                    Toast.makeText(wastereport.this.getApplicationContext(), response.toString().trim(), Toast.LENGTH_LONG).show();
                    System.out.println(response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(wastereport.this.getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> param = new HashMap<String, String>();
                    param.put("location", location);
                    param.put("description", description);
                    param.put("category", "WASTE");
                    param.put("categoryDescription",category);
                    param.put("dateReported",txtdate.getText().toString().trim());
                    param.put("evidence", imagetoString(bitmap));
                    param.put("nameofreporterfirst",firstname.trim());
                    param.put("nameofreporterlast",lastname.trim());
                    param.put("residentid",residentid.trim());
                    param.put("register", "0");
                    return param;

                }
            };
            RequestQueue queue = Volley.newRequestQueue(wastereport.this.getApplicationContext());
            queue.add(stringRequest);
            btnsubmit.postDelayed(new Runnable() {
                public void run() {
                    btnsubmit.setEnabled(true);
                }
            }, 0*11*1000);
        } else {


        }
    }
}