package com.example.micraapp;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.micraapp.databinding.ActivityCategoryBinding;

public class category extends drawerbase implements View.OnClickListener {
    public CardView Cminor, Creport, Cinfra;
    ActivityCategoryBinding activityCategoryBinding;
    BroadcastReceiver broadcastReceiver;

    SharedPreferences C1, C2, C3;
    SharedPreferences.Editor C1editor;
    SharedPreferences.Editor C2editor;
    SharedPreferences.Editor C3editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        activityCategoryBinding = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(activityCategoryBinding.getRoot());
        allocateActivitytitle("Category");
        Creport = (CardView) findViewById(R.id.report);
        Cinfra = (CardView) findViewById(R.id.reportinfras);
        Cminor = (CardView) findViewById(R.id.reportincident);

        Creport.setOnClickListener(this);
        Cinfra.setOnClickListener(this);
        Cminor.setOnClickListener(this);
        broadcastReceiver = new NetworkListener();
        restorecon();

        //SHARED REF
        C1 = getSharedPreferences("SPC1", Context.MODE_PRIVATE);
        C2 = getSharedPreferences("SPC2", Context.MODE_PRIVATE);
        C3 = getSharedPreferences("SPC3", Context.MODE_PRIVATE);


        C2editor = C2.edit();
        C3editor = C3.edit();

        C2editor.clear().commit();
        C3editor.clear().commit();

    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        Intent intent;
        intent = new Intent(this, FaceRecognition.class);
        switch (v.getId()) {

            case R.id.report:
                builder.setMessage("Are you a resident of Barangay Masambong?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog


                        intent.putExtra("type", "waste");
                        startActivity(intent);

                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        intent.putExtra("type", "waste2");
                        startActivity(intent);
                    }
                });

                AlertDialog alert3= builder.create();
                alert3.show();

                break;
            case R.id.reportinfras:
                builder.setMessage("Are you a resident of Barangay Masambong?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog


                        intent.putExtra("type", "infra1");
                        startActivity(intent);
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        intent.putExtra("type", "infra2");
                        startActivity(intent);
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();


                break;
            case R.id.reportincident:


                builder.setMessage("Are you a resident of Barangay Masambong?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog


                        intent.putExtra("type", "crime1");
                        startActivity(intent);

                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        intent.putExtra("type", "crime2");
                        startActivity(intent);
                    }
                });

                AlertDialog alert2 = builder.create();
                alert2.show();

                break;

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