package com.example.micraapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Properties;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
public class ChangePassword extends AppCompatActivity {
    Button btnreset,btnback;
    TextInputLayout tlnewpassword,tlconfirmpassword;
    TextInputEditText tenewpassword,teconfirmpassword;
    boolean haserror,haserror2 = false;
    private String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        btnreset  = findViewById(R.id.btnreset);
        btnback  =  findViewById(R.id.btnback);
        tlnewpassword  = findViewById(R.id.tlnewpassword);
        tlconfirmpassword  = findViewById(R.id.tlconfirmpassword);
        tenewpassword  = findViewById(R.id.tenewpassword);
        teconfirmpassword  = findViewById(R.id.teconfirmpassword);


        String result = getIntent().getStringExtra("email");
        if(result != null){
            email = result;
        }
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });


        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isValidPassword(tenewpassword.getText().toString())) {
                    haserror = true;
                    tlnewpassword.setError("Password must be Upper & Lowercase Letters. Numbers. Special characters. Length (8-30)");
                }else{
                    haserror = false;
                }if(!teconfirmpassword.getText().toString().equals(tenewpassword.getText().toString())){
                    haserror2 = true;
                    tlconfirmpassword.setError("Password does not match!");
                }else{
                    haserror2 = false;
                }if(haserror == false && haserror2 == false){
                    ChangepassWorker changepassWorker = new ChangepassWorker(ChangePassword.this);
                    changepassWorker.execute("changepass",email,teconfirmpassword.getText().toString().trim());
                }
            }
        });



        tenewpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    tlnewpassword.setError(null);
                    tenewpassword.setFocusable(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        teconfirmpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    tlconfirmpassword.setError(null);
                    teconfirmpassword.setFocusable(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
}



    private class ChangepassWorker  extends AsyncTask<String,Void,String> {
        Context context;
        android.app.AlertDialog alertDialog;
        ProgressDialog progressDialog;

        ChangepassWorker(Context ctx) {
            context = ctx;
        }

        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
          //  String login_url = "http://192.168.1.196/dashboard/Changepass.php";
            String login_url = "http://www.micra.services/Changepass.php";
            if (type.equals("changepass")) {
                try {
                    email = params[1];
                    String password = params[2];
                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8")+"&"
                            +URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                    String result = "";
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        result += line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return result;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {

            progressDialog = progressDialog.show(ChangePassword
                    .this,"Please wait","Applying changes..");
        }

        @Override
        protected void onPostExecute(String result) {
            String getresult = result;
            if (getresult.equals("success")) {
                progressDialog.dismiss();
                haserror = false;
                Intent Login = new Intent (context, MainActivity.class);
                Login.putExtra("changepass","changepass");
                context.startActivity(Login);

            } else {
                progressDialog.dismiss();
                haserror = true;

                new AlertDialog.Builder(context)

                        .setTitle("Reset Password")
                        .setMessage("Error occurred please try again!")
                        .setCancelable(false)
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }

                        }) .create().show();


            }

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }


    public static boolean isValidPassword(String password) {
        Matcher matcher = Pattern.compile("((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,30})").matcher(password);
        return matcher.matches();
    }
}