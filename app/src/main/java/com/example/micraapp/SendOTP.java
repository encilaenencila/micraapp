package com.example.micraapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
public class SendOTP extends AppCompatActivity {
    Button btn,btnsubmit,btnback,btnsendotp;
    TextView tvresend;
    TextInputLayout tlemail,tlotp;
    TextInputEditText teemail,teotp;
    private String email,password;
    boolean haserror = false;
    Session session;
    int code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_otp);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        btnsubmit  = findViewById(R.id.btnsubmit);
        btnsendotp  =  findViewById(R.id.btnsendotp);
        btnback  =  findViewById(R.id.btnback);

        tlemail  = findViewById(R.id.tlemail);
        tlotp  = findViewById(R.id.tlotp);
        tvresend = findViewById(R.id.tvresend);

        teemail  = findViewById(R.id.teemail);
        teotp  = findViewById(R.id.teotp);

        email = "microworkertest01@gmail.com";
        password ="encilus213";

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });


        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CompareOTP();

            }
        });

        btnsendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendOTP();
            }
        });

        teemail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    tlemail.setError(null);
                    teemail.setFocusable(true);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        teotp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    tlotp.setError(null);
                    teotp.setFocusable(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


    public void SendOTP(){
        if (!isValidEmail(teemail.getText().toString()) ) {
            haserror = true;
            tlemail.setError("Please enter valid Email designated to your account!");
        }else{


            CheckEmailWorker checkEmailWorker = new CheckEmailWorker(this);
            String type = "checkemail";
            checkEmailWorker.execute(type,teemail.getText().toString().trim(),teemail.getText().toString().trim());

        }

      if(haserror == false ){

          new CountDownTimer(15000, 1000) {

              public void onTick(long millisUntilFinished) {
                  tvresend.setText("Resend OTP: " + millisUntilFinished / 1000);
                  btnsendotp.setClickable(false);
                  btnsendotp.setBackgroundColor(Color.GRAY);
                  //here you can have your logic to set text to edittext
              }

              public void onFinish() {
                  tvresend.setText("");
                  btnsendotp.setClickable(true);
                  btnsendotp.setBackgroundColor(getResources().getColor(R.color.purple_500));
              }

          }.start();
            Properties properties = new Properties();
            properties.put("mail.smtp.auth","true");
            properties.put("mail.smtp.starttls.enable","true");
            properties.put("mail.smtp.host","smtp.gmail.com");
            properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
            properties.put("mail.smtp.port","587");
            session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(email,password);
                }
            });

            try {

                Random random = new Random();
                code = random.nextInt(8999)+1000;
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(email));
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(teemail.getText().toString()));
                message.setSubject("Reset your MICRA APP password");

                message.setText("Hi,"
                        + "\n\nWe're sending you this email because you requested a password reset" + "\n\nYour OTP is:" + code +"\n\n\n\n\n\nIf you didnt request a password reset"
                        +"\nplease ignore this email. Your password will not be changed");

                new SendMail().execute(message);
            }
            catch (MessagingException e)
            {
                e.printStackTrace();
                throw new RuntimeException(e);
            }


        }

    }
    private class SendMail extends AsyncTask<Message,String,String> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            progressDialog = progressDialog.show(SendOTP
                    .this,"Please wait","Sending OTP to your Email..");
        }

        @Override
        protected String doInBackground(Message... messages) {
            try{

                Transport.send(messages[0]);
                return "success";
            }catch (MessagingException e){
                e.printStackTrace();
                return "error" + e;
            }

        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            progressDialog.dismiss();
            if(s.equals("success")){
                AlertDialog.Builder builder = new AlertDialog.Builder(SendOTP.this);
                builder.setCancelable(false);
                builder.setTitle(Html.fromHtml("<font-color='#509324'>Success</font>"));
                builder.setMessage("OTP sent Successfully. Please check your Email.");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }else{
                Toast.makeText(SendOTP.this,"Something went wrong ?",Toast.LENGTH_SHORT).show();
            }
        }
    }
                public void CompareOTP() {
                    String strOTP = String.valueOf(code);
                    if (!teotp.getText().toString().trim().equals(strOTP)) {
                        tlotp.setError("Invalid OTP, please check your email for the OTP.");

                    } else {
                        Intent intent = new Intent(SendOTP.this, ChangePassword.class);
                        intent.putExtra("email", teemail.getText().toString().trim());
                        startActivity(intent);
                    }

                }





    private class CheckEmailWorker  extends AsyncTask<String,Void,String> {
        Context context;
        android.app.AlertDialog alertDialog;
        ProgressDialog progressDialog;

        CheckEmailWorker(Context ctx) {
            context = ctx;
        }

        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
       // String login_url = "http://192.168.1.196/dashboard/Checkemail.php";
          String check_email ="https://unviewed-lapse.000webhostapp.com/Checkemail.php";
            if (type.equals("checkemail")) {
                try {
                    String email = params[1];
                    String email2 = params[2];
                    URL url = new URL(check_email);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8")+"&"
                            +URLEncoder.encode("email2","UTF-8")+"="+URLEncoder.encode(email2,"UTF-8");
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

            progressDialog = progressDialog.show(SendOTP
                    .this,"Please wait","Checking your Email..");
        }

        @Override
        protected void onPostExecute(String result) {
            String getresult = result;
            progressDialog.dismiss();
            if (getresult.equals("success")) {
                haserror = false;
            } else {
                progressDialog.dismiss();
                haserror = true;
               tlemail.setError("Please enter valid Email designated to your account!");
            }

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
        Matcher matcher = Pattern.compile(EMAIL_PATTERN).matcher(email);
        return matcher.matches();
    }
}