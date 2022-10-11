package com.example.micraapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.micraapp.databinding.ActivityAnnouncementBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Announcement extends drawerbase {
    List<AnnounceItem> announceItemList;
    RecyclerView recyclerView;
    ActivityAnnouncementBinding activityAnnouncementBinding;
    AnnouncementAdapter adapter;
    ProgressDialog progressDialog;
    BroadcastReceiver broadcastReceiver;
    private String LOADINFO_URL = "http://www.micra.services/Micra/resources/files/announcement/FetchAnnouncement.php";




    SharedPreferences C1, C2, C3;
    SharedPreferences.Editor C1editor;
    SharedPreferences.Editor C2editor;
    SharedPreferences.Editor C3editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        activityAnnouncementBinding = activityAnnouncementBinding.inflate(getLayoutInflater());
        setContentView(activityAnnouncementBinding.getRoot());
        allocateActivitytitle("Announcement");
        broadcastReceiver = new NetworkListener();
        restorecon();


        recyclerView = findViewById(R.id.recview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));



        announceItemList = new ArrayList<>();
        LoadAnnouncement();


        //SHARED REF
        C1 = getSharedPreferences("SPC1", Context.MODE_PRIVATE);
        C2 = getSharedPreferences("SPC2", Context.MODE_PRIVATE);
        C3 = getSharedPreferences("SPC3", Context.MODE_PRIVATE);


        C2editor = C2.edit();
        C3editor = C3.edit();

        C2editor.clear().commit();
        C3editor.clear().commit();

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


    private void LoadAnnouncement() {
    /*    progressDialog = progressDialog.show(this
                , "Please wait", "loading announcement..");*/

        StringRequest stringRequest = new StringRequest(Request.Method.GET, LOADINFO_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {


                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {

                                JSONObject report = array.getJSONObject(i);

                                announceItemList.add(new AnnounceItem(


                                        report.getString("id"),
                                        report.getString("title"),
                                        report.getString("date"),
                                        report.getString("image"),
                                        report.getString("description"),
                                        report.getString("postedBy")


                                ));
                            }

                            boolean isEmpty = announceItemList.isEmpty();
                            if (isEmpty == true){
                                //    progressDialog.dismiss();
                                Toast.makeText(Announcement.this, "No Announcement", Toast.LENGTH_SHORT).show();
                            }else{
                                adapter = new AnnouncementAdapter(Announcement.this, announceItemList);
                                recyclerView.setAdapter(adapter);
                                //  progressDialog.dismiss();
                            }




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Announcement.this, "Disconnected", Toast.LENGTH_SHORT).show();
                    }
                });

        Volley.newRequestQueue(this).add(stringRequest);
    }

}