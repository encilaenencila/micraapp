package com.example.micraapp;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class NetworkListener extends BroadcastReceiver {
    Context context;

    public void onReceive(Context context, Intent intent) {
        if (isConnected(context)) {

            Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View layout = LayoutInflater.from(context).inflate(R.layout.connection_dialog, null);
            builder.setView(layout);


            Button btnRecon = layout.findViewById(R.id.btnRecon);
            AlertDialog dialog = builder.create();
            dialog.show();
            //dialog.getWindow().setAttributes(lp);
            dialog.getWindow().setGravity(Gravity.CENTER);
            dialog.setCancelable(false);
            btnRecon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    onReceive(context, intent);
                }
            });
        }
    }

    public boolean isConnected(Context context) {

        try {

            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return (networkInfo != null && networkInfo.isConnected());


        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }
}