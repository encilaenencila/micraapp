package com.example.micraapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.micraapp.databinding.ActivityCallmoduleBinding;
import com.example.micraapp.databinding.ActivityCategoryBinding;

import java.io.File;
import java.util.ArrayList;

public class callmodule extends drawerbase {


    ActivityCallmoduleBinding activityCallmoduleBinding;
    private ArrayList<calllistemergency> contact;
    private RecyclerView recyclerView;
    private int imageicon[] = {R.drawable.policestation,
            R.drawable.firestation,
            R.drawable.hospitastation,
            R.drawable.barangaystation};
    BroadcastReceiver broadcastReceiver;




    SharedPreferences C1, C2, C3;
    SharedPreferences.Editor C1editor;
    SharedPreferences.Editor C2editor;
    SharedPreferences.Editor C3editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCallmoduleBinding = activityCallmoduleBinding.inflate(getLayoutInflater());
        setContentView(activityCallmoduleBinding.getRoot());
        recyclerView = findViewById(R.id.contactlist);
        contact = new ArrayList<>();
        setcontact();
        setAdapter();

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

    private void setAdapter() {

        CallAdapter callAdapter = new CallAdapter(contact);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(callAdapter);

    }

    private void setcontact() {

        contact.add(new calllistemergency("FIRE STATION", "0912212121", "#fd6967", imageicon[1]));
        contact.add(new calllistemergency("BARANGAY STATION", "0959845210", "#FFFDD1", imageicon[3]));
        contact.add(new calllistemergency("HOSPITAL STATION", "0978522482", "#AFAFAF", imageicon[2]));
        contact.add(new calllistemergency("POLICE STATION", "095487445850", "#1AA7EC", imageicon[0]));

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