package com.example.micraapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CallAdapter extends RecyclerView.Adapter<CallAdapter.ViewHolder> {
    private ArrayList<calllistemergency>contacts;
    Context ctx;

    public CallAdapter(ArrayList<calllistemergency> contact) {
        this.contacts= contact;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_calllist,parent,false);
    return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        calllistemergency res = contacts.get(position);
        String namedepartment = contacts.get(position).getDepartment();
        String numberhotline = contacts.get(position).getHotline();
        int icon = contacts.get(position).getIconimage();
        int color = Color.parseColor(String.valueOf(res.backgroundcolor));
        holder.department.setText(String.valueOf(namedepartment));
    holder.hotline.setText(String.valueOf(numberhotline));
    holder.Evidence.setImageResource(icon);
    holder.crd.setCardBackgroundColor(color);
    holder.crd.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Context context = v.getContext();
            Intent intent = new Intent(context,callhotline.class);
            intent.putExtra("station",String.valueOf(res.getDepartment()));
            intent.putExtra("hotline",String.valueOf(res.getHotline()));
            context.startActivity(intent);
        }
    });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView department, hotline;
        ImageView Evidence;
        CardView crd;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            crd = itemView.findViewById(R.id.cardviewlist);
            department = itemView.findViewById(R.id.txtdepartment);
            hotline = itemView.findViewById(R.id.txtnumber);
            Evidence = itemView.findViewById(R.id.listimgreport);
        }
        }
    }
