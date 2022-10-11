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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.micraapp.databinding.ActivityIncidentBinding;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class incident extends drawerbase {
    ActivityIncidentBinding activityIncidentBinding;
    Button btnuploadphotos, btnsubmit,btndatereport,btnincidentdate,btntimereported;
    ImageView imageViewinci;
    TextView txtdescriptioninci,txtcategory;
    BroadcastReceiver broadcastReceiver;
    private final int IMG_REQUEST = 1;
    String[] street= {"Amaloi", "Amuslan","Bahawan","Bakil","Cagna","Corumi","Eskinita","Gabo","Gasan","Ilihan","Inaman","Lamila","Mabituan","Malac","Malasimbo",
            "Masola","Monong","Payte","Posooy","Toctokan","Wayan"};

    String[] items = {"Vandalism", "Assault","Street fight","Robbery","Physical injury","Child abuse","Crime against Person","Selling Drugs","Other"};
    AutoCompleteTextView autoCompleteTextView;
    AutoCompleteTextView AutoconmpleteLocation;
    ArrayAdapter<String> arrayAdapter;
    ArrayAdapter<String> arrayLocation;
    private URI filepath;
    private Bitmap bitmap;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    private SimpleDateFormat simpleDateFormat;
    public DatePickerDialog datePickerDialog;
    String location="",description="",category="";
    String upload_URL = "http://www.micra.services/IncidentReport/upload.php";
    SharedPreferences userinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityIncidentBinding = activityIncidentBinding.inflate(getLayoutInflater());
        allocateActivitytitle("Crime");
        setContentView(activityIncidentBinding.getRoot());
        broadcastReceiver = new NetworkListener();
        restorecon();

        autoCompleteTextView =(AutoCompleteTextView)findViewById(R.id.dropdownincident);
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.listitems, items);
        autoCompleteTextView.setAdapter(arrayAdapter);


        AutoconmpleteLocation =(AutoCompleteTextView) findViewById(R.id.txtlocatioOfincident);
        arrayLocation = new ArrayAdapter<String>(this,R.layout.liststreetlocation,street);
        AutoconmpleteLocation.setAdapter(arrayLocation);

        btnuploadphotos = findViewById(R.id.btnuploadphotoincident);
        btnsubmit = findViewById(R.id.btnsubmitinci);
        btndatereport = findViewById(R.id.btndateofreport);
        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        calendar = Calendar.getInstance();
        Date d=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("hh:mm a");
        String currentDateTimeString = sdf.format(d);
        btntimereported = findViewById(R.id.btntimereported);
        userinfo =getSharedPreferences("info", Context.MODE_PRIVATE);
        date = DateFormat.getDateInstance(DateFormat.MEDIUM).format(calendar.getTime());
        txtcategory = findViewById(R.id.dropdownincident);
        txtdescriptioninci = findViewById(R.id.txtdesctionIncident);
        btndatereport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btndatereport.setText(date);
            }
        });
        btntimereported.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btntimereported.setText(currentDateTimeString);
            }
        });

        btnincidentdate= findViewById(R.id.btndateofincident);
        imageViewinci = (ImageView)findViewById(R.id.imginincident);
        initDatePickers();
        btnuploadphotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageViewinci.getDrawable()==null){
                    Toast.makeText(incident.this, "Pls Insert image", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(AutoconmpleteLocation.getText().toString()) || TextUtils.isEmpty(txtcategory.getText().toString()) || TextUtils.isEmpty(txtdescriptioninci.getText().toString()) ) {
                    Toast.makeText(incident.this, "Pls complete the field", Toast.LENGTH_LONG).show();
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


    public void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST);
    }
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri path = data.getData();
            try {
                //Assign image into bitmap
                bitmap = MediaStore.Images.Media.getBitmap(incident.this.getContentResolver(), path);
                imageViewinci.getLayoutParams().height = 500;
                imageViewinci.getLayoutParams().width = 500;
                imageViewinci.setImageBitmap(bitmap);
                imageViewinci.setVisibility(View.VISIBLE);
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

       Date dateObject;
        location = AutoconmpleteLocation.getText().toString().trim();
        category = txtcategory.getText().toString().trim();
        description = txtdescriptioninci.getText().toString().trim();
        if (!AutoconmpleteLocation.equals("") && !txtdescriptioninci.equals("") && !txtdescriptioninci.equals("")) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, upload_URL, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    AutoconmpleteLocation.setText("");
                    txtcategory.setText("");
                    txtdescriptioninci.setText("");
                    btndatereport.setText("");
                    btnincidentdate.setText("");
                    btntimereported.setText("");
                    imageViewinci.setVisibility(View.INVISIBLE);
                    Toast.makeText(incident.this.getApplicationContext(), response.toString().trim(), Toast.LENGTH_LONG).show();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(incident.this.getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> param = new HashMap<String, String>();
                    param.put("reportlocation", location);
                    param.put("reportdescription", description);
                    param.put("categorydescription",category);
                    param.put("dateReported",btndatereport.getText().toString().trim());
                    param.put("timereported",btntimereported.getText().toString().trim());
                    param.put("DateofIncident",btnincidentdate.getText().toString().trim());
                    param.put("nameofreporter",lastname.trim()+" "+firstname.trim());
                    param.put("residentid",residentid.trim());
                    param.put("evidence", imagetoString(bitmap));
                     param.put("register", "0");
                    return param;
                }
            };
            RequestQueue queue = Volley.newRequestQueue(incident.this.getApplicationContext());
            queue.add(stringRequest);
        } else {
            Toast.makeText(incident.this, "complete all the field", Toast.LENGTH_SHORT).show();
        }
    }
    private String getMonthFormat(int month) {
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
    private void initDatePickers() {DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
    {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day)
        {
            month = month + 1;
            String dates = makeDateString(day, month, year);
            btnincidentdate.setText(dates);
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
    public void OpendatePickerReport(View view) {
        datePickerDialog.show();
    }
    public void OpendatePicker(View view) {
        datePickerDialog.show();
    }

}