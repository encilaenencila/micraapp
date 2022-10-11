package com.example.micraapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.MediaSync;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.request.transition.Transition;
import com.example.micraapp.databinding.ActivityRecordreportsBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

public class recordreports extends drawerbase {
    ActivityRecordreportsBinding activityRecordreportsBinding;
    RecyclerView recyclerView;
    List<incidentlistreports> incidentlistreportsList;
    CrimeAdapter adaptercrime;
    List<Infralistreports> reportinfra;
    InfraAdapter adapterinfra;
    List<wastelistreport> reportwaste;
    wasteAdapter adapterwaste;
    BroadcastReceiver broadcastReceiver;
    //dropdown
    ArrayAdapter<String> arrayAdapter;
    String category[] = {"CRIME REPORT", "WASTE REPORT", "INFRASTRUCTURE REPORT"};
    AutoCompleteTextView txtsorted;
    SharedPreferences userinfo;

    private String REPORT_URL;
    private String DropdownPos;


    private static final String FILE_NAME2 = "Savenum.txt";
    ArrayAdapter arrayAdapterz;
    AutoCompleteTextView attrackno;
    String[] arr = null;
    List<String> items = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityRecordreportsBinding = activityRecordreportsBinding.inflate(getLayoutInflater());

        setContentView(activityRecordreportsBinding.getRoot());
        allocateActivitytitle("View Report");
        broadcastReceiver = new NetworkListener();
        restorecon();
        txtsorted = findViewById(R.id.drpcategory);
        attrackno = findViewById(R.id.attrackno);
        recyclerView = findViewById(R.id.listofreports);


        delete();
        PassDataList();
        arrayAdapterz = new ArrayAdapter<String>(this, R.layout.listitems, items);
        attrackno.setAdapter(arrayAdapterz);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        //dropdown
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.listitems, category);
        txtsorted.setAdapter(arrayAdapter);
        txtsorted.setFocusable(false);


        txtsorted.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                if (item.equals("CRIME REPORT")) {

                    REPORT_URL = "http://www.micra.services/IncidentReport/BlotterReport.php";
                    incidentlistreportsList = new ArrayList<>();
                    CrimeResult();
                } else if (item.equals("WASTE REPORT")) {

                    REPORT_URL = "http://www.micra.services/Wastereport/Wastereport.php";
                    reportwaste = new ArrayList<>();
                    WasteResult();
                } else if (item.equals("INFRASTRUCTURE REPORT")) {


                    REPORT_URL = "http://www.micra.services/Infrastracture/InfraReport.php";
                    reportinfra = new ArrayList<>();

                    InfraResult();
                }
            }
        });
    }


    private void CrimeResult() {

/*   progressDialog = progressDialog.show(this
                , "Please wait", "loading announcement..");*/


        StringRequest stringRequest = new StringRequest(Request.Method.POST, REPORT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {

                                JSONObject report = array.getJSONObject(i);

                                incidentlistreportsList.add(new incidentlistreports(

                                        report.getString("resolveStatus"),
                                        report.getString("category"),
                                        report.getString("crimecategory"),
                                        report.getString("evidence"),
                                        report.getString("locationofincident"),
                                        report.getString("casenarrative"),
                                        report.getString("respondedBy"),
                                        report.getString("respondDate"),
                                        report.getString("respondTime"),
                                        report.getString("respondMessage"),
                                        report.getString("landmark"),
                                        report.getString("resolvefeedback"),
                                        report.getString("residentfeedback"),
                                        report.getString("reportid"),
                                        report.getString("finishproof"),
                                        report.getString("reportDate")


                                ));
                            }

                            boolean isEmpty = incidentlistreportsList.isEmpty();
                            if (isEmpty == true) {
                                //    progressDialog.dismiss();
                                recyclerView.setAdapter(null);
                                recyclerView.removeAllViewsInLayout();
                                Toast.makeText(recordreports.this, "No Record", Toast.LENGTH_SHORT).show();

                            } else {
                                adaptercrime = new CrimeAdapter(recordreports.this, incidentlistreportsList);
                                recyclerView.setAdapter(adaptercrime);
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
                        Toast.makeText(recordreports.this, "Connection Error", Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("contact", attrackno.getText().toString().trim());
                return param;
            }

        };

        Volley.newRequestQueue(this).add(stringRequest);
    }


    private void WasteResult() {
    /*    progressDialog = progressDialog.show(this
                , "Please wait", "loading announcement..");*/

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REPORT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {

                                JSONObject report = array.getJSONObject(i);
                                reportwaste.add(new wastelistreport(


                                        report.getString("dateReported"),
                                        report.getString("location"),
                                        report.getString("category"),
                                        report.getString("Description"),
                                        report.getString("photo"),
                                        report.getString("resolvefeedback"),
                                        report.getString("status"),
                                        report.getString("categoryDescription"),
                                        report.getString("resolvedate"),
                                        report.getString("resolvetime"),
                                        report.getString("timeReport"),
                                        report.getString("repondate"),
                                        report.getString("respondtime"),
                                        report.getString("respondername"),
                                        report.getString("respondmessage"),
                                        report.getString("Landmark"),
                                        report.getString("finishproof"),
                                        report.getString("residentfeedback"),
                                        report.getString("reportnumber")

                                ));
                            }

                            boolean isEmpty = reportwaste.isEmpty();
                            if (isEmpty == true) {
                                //    progressDialog.dismiss();
                                recyclerView.setAdapter(null);
                                recyclerView.removeAllViewsInLayout();
                                Toast.makeText(recordreports.this, "No Record", Toast.LENGTH_SHORT).show();
                            } else {
                                adapterwaste = new wasteAdapter(recordreports.this, reportwaste);
                                recyclerView.setAdapter(adapterwaste);
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
                        Toast.makeText(recordreports.this, "Connection Error", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("contact", attrackno.getText().toString().trim());
                return param;
            }

        };

        Volley.newRequestQueue(this).add(stringRequest);
    }


    private void InfraResult() {
    /*    progressDialog = progressDialog.show(this
                , "Please wait", "loading announcement..");*/

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REPORT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {

                                JSONObject report = array.getJSONObject(i);

                                reportinfra.add(new Infralistreports(

                                        report.getString("resolvestatus"),
                                        report.getString("reportcategory"),
                                        report.getString("categorydescription"),
                                        report.getString("reportimg"),
                                        report.getString("reportlocation"),
                                        report.getString("reportdescription"),
                                        report.getString("respondername"),
                                        report.getString("repondate"),
                                        report.getString("respondtime"),
                                        report.getString("respondmessage"),
                                        report.getString("Landmark"),
                                        report.getString("resolvefeedback"),
                                        report.getString("residentfeedback"),
                                        report.getString("reportnumber"),
                                        report.getString("finishproof"),
                                        report.getString("reportdate")

                                ));
                            }

                            boolean isEmpty = reportinfra.isEmpty();
                            if (isEmpty == true) {
                                //    progressDialog.dismiss();
                                recyclerView.setAdapter(null);
                                recyclerView.removeAllViewsInLayout();
                                Toast.makeText(recordreports.this, "No Record", Toast.LENGTH_SHORT).show();

                            } else {
                                adapterinfra = new InfraAdapter(recordreports.this, reportinfra);
                                recyclerView.setAdapter(adapterinfra);
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
                        Toast.makeText(recordreports.this, "Connection Error", Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("contact", attrackno.getText().toString().trim());
                return param;
            }

        };

        Volley.newRequestQueue(this).add(stringRequest);
    }

    public void PassDataList() {

        try {
            File file = getApplicationContext().getFileStreamPath("Savenum.txt");
            FileInputStream fstream = new FileInputStream(file);
            DataInputStream data_input = new DataInputStream(fstream);
            BufferedReader buffer = new BufferedReader(new InputStreamReader(data_input));
            String str_line;

            while ((str_line = buffer.readLine()) != null) {
                str_line = str_line.trim();
                if ((str_line.length() != 0)) {
                    items.add(str_line);
                }
            }

            arr = (String[]) items.toArray(new String[items.size()]);
            //  Log.d("Pass", "" + arr);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete() {
        try {


            String filePath = "/data/data/" + getApplicationContext().getPackageName() + "/files/Input.txt";
            String filePaths = "/data/data/" + getApplicationContext().getPackageName() + "/files/Savenum.txt";
            String input = null;
            //Instantiating the Scanner class
            Scanner sc = new Scanner(new File(filePath));
            //Instantiating the FileWriter class
            FileWriter writer = new FileWriter(filePaths);
            //Instantiating the Set class
            Set set = new HashSet();
            while (sc.hasNextLine()) {
                input = sc.nextLine();
                if (set.add(input)) {
                    writer.append(input + "\n");
                }
            }
            writer.flush();
            System.out.println("Duplicate removed............");


        } catch (IOException e) {

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu2, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        EditText textView = (EditText) searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        textView.setTextColor(Color.WHITE);
        textView.setHintTextColor(Color.WHITE);
        textView.setHint("Search Date");


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                switch (txtsorted.getText().toString().trim()) {
                    case "CRIME REPORT":
                        adaptercrime.getFilter().filter(newText);
                        break;
                    case "WASTE REPORT":
                        adapterwaste.getFilter().filter(newText);
                        break;
                    case "INFRASTRUCTURE REPORT":
                        adapterinfra.getFilter().filter(newText);
                        break;


                }


                return false;
            }

        });

        return true;
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