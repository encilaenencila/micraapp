package com.example.micraapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.micraapp.databinding.ActivityCallhotlineBinding;
import com.example.micraapp.databinding.ActivityCategoryBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class callhotline extends drawerbase {
    ActivityCallhotlineBinding activityCallhotlineBinding;
    EditText phoneNo;
    FloatingActionButton callbtn;
    TextView station;

    static int PERMISSION_CODE= 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        activityCallhotlineBinding =activityCallhotlineBinding.inflate(getLayoutInflater());
        setContentView(activityCallhotlineBinding.getRoot());
        allocateActivitytitle("Hotline");

        phoneNo = findViewById(R.id.editTextPhone);
        phoneNo.setEnabled(false);
        callbtn = findViewById(R.id.callbtn);
        station = findViewById(R.id.lbldepartment);
        station.setText( getIntent().getStringExtra("station"));
        phoneNo.setText( getIntent().getStringExtra("hotline"));

        if (ContextCompat.checkSelfPermission(callhotline.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(callhotline.this,new String[]{Manifest.permission.CALL_PHONE},PERMISSION_CODE);

        }

        callbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phoneno = phoneNo.getText().toString();
                Intent i = new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel:"+phoneno));
                startActivity(i);

            }
        });
    }
}