package com.example.micraapp;

import androidx.annotation.NonNull;
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
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
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
import com.example.micraapp.databinding.ActivityCategoryBinding;
import com.example.micraapp.databinding.ActivityInsfractractureBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class insfractracture extends drawerbase {
    private final int IMG_REQUEST = 1;
    BroadcastReceiver broadcastReceiver;
    ActivityInsfractractureBinding activityInsfractractureBinding;
    private static final String TAG = "PushNotification";
    private static final String CHANNEL_ID = "101";
    private String devicetoken;
    String[] items = {"Potholes", "Traffic Signal Lights","Street Light post","Drainage","Broken roadways","Electrical lines","Water Pipe","Roadside gutters","Septic tank (physical)"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> arrayAdapter;
    private URI filepath;
    private Bitmap bitmap;
    private Button btndatereported,btntimereported,btnselecphotos,btnsubmit;
    private TextView txtdesciption,textcategory;
    private ImageView imgViewsinfra;
    private String location = "", description = "", category = "";
    public DatePickerDialog datePickerDialog;
    SharedPreferences userinfo ;
    AutoCompleteTextView txtlocation;
    String date;
    private Calendar calendar;
    String[] street= {"Amaloi", "Amuslan","Bahawan","Bakil","Cagna","Corumi","Eskinita","Gabo","Gasan","Ilihan","Inaman","Lamila","Mabituan","Malac","Malasimbo",
            "Masola","Monong","Payte","Posooy","Toctokan","Wayan"};
    ArrayAdapter<String> arrayLocation;



    String upload_URL = "http://www.micra.services/Infrastracture/upload.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        broadcastReceiver = new NetworkListener();
        restorecon();
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        activityInsfractractureBinding = activityInsfractractureBinding.inflate(getLayoutInflater());
        setContentView(activityInsfractractureBinding.getRoot());
        allocateActivitytitle("Category");
        initDatePicker();
        autoCompleteTextView =(AutoCompleteTextView)findViewById(R.id.dropdowninfa);
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.listitems, items);
        autoCompleteTextView.setAdapter(arrayAdapter);

        txtlocation =(AutoCompleteTextView) findViewById(R.id.editextlocation);
        arrayLocation = new ArrayAdapter<String>(this,R.layout.liststreetlocation,street);
        txtlocation.setAdapter(arrayLocation);
        calendar = Calendar.getInstance();
        txtdesciption = findViewById(R.id.editextdescription);
        txtlocation = findViewById(R.id.editextlocation);
        textcategory = findViewById(R.id.dropdowninfa);
        btnselecphotos = findViewById(R.id.btnuploadphoto);
        btnsubmit = findViewById(R.id.btnsubmitinfra);
        btndatereported = findViewById(R.id.btndate);
        imgViewsinfra =(ImageView) findViewById(R.id.imginfra);
        Date d=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("hh:mm a");
        String currentDateTimeString = sdf.format(d);
       btntimereported = findViewById(R.id.btntime);
       btntimereported.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               btntimereported.setText(currentDateTimeString);
           }
       });
        btnselecphotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  getToken();
                if (imgViewsinfra.getDrawable()==null){
                    Toast.makeText(insfractracture.this, "Pls Insert image", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(txtlocation.getText().toString()) || TextUtils.isEmpty(textcategory.getText().toString()) || TextUtils.isEmpty(txtdesciption.getText().toString()) ) {
                    Toast.makeText(insfractracture.this, "pls complete the field", Toast.LENGTH_LONG).show();
                }else {
                    submit();
                }*/
                imagetoString(bitmap);
                Log.d("","this is"+ bitmap);
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

    private String getTodaysDate() {
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
            btndatereported.setText(date);
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

        date = DateFormat.getDateInstance(DateFormat.MEDIUM).format(calendar.getTime());
        btndatereported.setText(date);
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
                bitmap = MediaStore.Images.Media.getBitmap(insfractracture.this.getContentResolver(), path);
                imgViewsinfra.getLayoutParams().height = 500;
                imgViewsinfra.getLayoutParams().width = 500;
                imgViewsinfra.setImageBitmap(bitmap);
                imgViewsinfra.setVisibility(View.VISIBLE);
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
    private void getToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        devicetoken = task.getResult();
                        Log.d(TAG, "onComplete: " + devicetoken);
                    }
                });
    }
    public void submit() {
        userinfo = getSharedPreferences("info", Context.MODE_PRIVATE);
        String residentid = userinfo.getString("infoid","");
        String firstname = userinfo.getString("infofirstname","");
        String lastname = userinfo.getString("infolastname","");


        location = txtlocation.getText().toString().trim();
        category = textcategory.getText().toString().trim();
        description = txtdesciption.getText().toString().trim();


        if (!txtlocation.equals("") && !txtdesciption.equals("") && !textcategory.equals("")) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, upload_URL, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    txtlocation.setText("");
                    txtdesciption.setText("");
                    textcategory.setText("");
                    btndatereported.setText("");
                    btntimereported.setText("");
                    imgViewsinfra.setVisibility(View.INVISIBLE);
                    Toast.makeText(insfractracture.this.getApplicationContext(), "Successfully Report Added", Toast.LENGTH_LONG).show();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(insfractracture.this.getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> param = new HashMap<String, String>();
                    param.put("reportlocation", location);
                    param.put("reportDescription", description);
                    param.put("categorydescription",category);
                    param.put("reporttime",btntimereported.getText().toString().trim());
                    param.put("dateReported",btndatereported.getText().toString().trim());
                    param.put("evidence", imagetoString(bitmap));
                    param.put("reportstatus","PENDING");
                    param.put("devicetoken",  devicetoken);
                    param.put("nameofreporter",lastname.toString()+" "+firstname.toString());
                    param.put("residentid",residentid.toString());
                    param.put("report", "0");
                    return param;
                }
            };
            RequestQueue queue = Volley.newRequestQueue(insfractracture.this.getApplicationContext());
            queue.add(stringRequest);
        } else {
            Toast.makeText(insfractracture.this, "complete all the field", Toast.LENGTH_SHORT).show();
        }
    }
}