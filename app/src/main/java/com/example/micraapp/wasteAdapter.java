package com.example.micraapp;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class wasteAdapter extends RecyclerView.Adapter<wasteAdapter.ViewHolder> implements Filterable {
    Context cont;
    List<wastelistreport> reports;
    List<wastelistreport> Allreports;
    private String IMG_URL = "http://www.micra.services/Wastereport/";
    private String IMG_URL_PROOF = "http://www.micra.services/Micra/resources/files/waste/";

    public wasteAdapter(Context cont, List<wastelistreport> reports) {

        this.reports = reports;
        this.cont = cont;
        this.Allreports = new ArrayList<>(reports);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(cont);
        View view = inflater.inflate(R.layout.custom_listreports, parent, false);
        return new wasteAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        wastelistreport res = reports.get(position);


        if (res.getPhoto().isEmpty()) {
            Glide.with(cont)
                    .load(cont.getResources().getDrawable(R.drawable.no_image_avail_2))
                    .fitCenter()
                    .into(holder.Evidence);

        } else {

         Glide.with(cont)
                    .load(IMG_URL + res.getPhoto())
                    .fitCenter()
                    .into(holder.Evidence);

        }


        holder.category.setText(res.getCategory());
        holder.location.setText(res.getLocation());
        holder.status.setText(res.getStatus());
        holder.date.setText(res.getDateReported());
        holder.crd.setBackgroundResource(R.drawable.lastreportlsi);
        holder.crd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, userfinish.class);
                intent.putExtra("status", String.valueOf(res.getStatus()));
                intent.putExtra("category", String.valueOf(res.getCategory()));
                intent.putExtra("categorydescription", res.getCategoryDescription());
                if (res.getPhoto().isEmpty()) {
                    intent.putExtra("imageurl", "");
                } else {
                    intent.putExtra("imageurl", IMG_URL + String.valueOf(res.getPhoto()));
                }
                intent.putExtra("location", String.valueOf(res.getLocation()));
                intent.putExtra("description", String.valueOf(res.getDescription()));
                intent.putExtra("respondername", String.valueOf(res.getRespondername()));
                intent.putExtra("responddate", String.valueOf(res.getRepondate()));
                intent.putExtra("respondtime", String.valueOf(res.getRespondtime()));
                intent.putExtra("respondmessage", String.valueOf(res.getRespondmessag()));
                intent.putExtra("landmark", String.valueOf(res.getLandmark()));
                intent.putExtra("resolvefeedback", String.valueOf(res.getResolvefeedback()));
                intent.putExtra("residentfeedback", String.valueOf(res.getResidentfeedback()));
                if (res.getFinishproof().equals("null")) {
                    Log.d("fp", res.getFinishproof());
                    intent.putExtra("finishproof", "");
                } else {
                    intent.putExtra("finishproof", IMG_URL_PROOF + String.valueOf(res.getFinishproof().trim()));
                }
                intent.putExtra("reportnumber", String.valueOf(res.getReportnumer()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    @Override



    public Filter getFilter() {
        return filter;
    }
    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

           List<wastelistreport> filterlist = new ArrayList<>();
           if(constraint == null || constraint.length() == 0){
               filterlist.addAll(Allreports);
           }else{
             String filterpattern = constraint.toString().toLowerCase().trim();

            for(wastelistreport item : Allreports){
                if(item.getDateReported().toLowerCase().contains(filterpattern)){
                    filterlist.add(item);
                }
               }
           }

            FilterResults results = new FilterResults();
           results.values = filterlist;
           return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
                reports.clear();
                reports.addAll((List)results.values);
                notifyDataSetChanged();
        }
    };
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView status, date, category, location;
        ImageView Evidence;
        CardView crd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            crd = itemView.findViewById(R.id.cardviewlist);
            date = itemView.findViewById(R.id.txtdatereported);
            status = itemView.findViewById(R.id.liststatus);
            category = itemView.findViewById(R.id.listcategory);
            location = itemView.findViewById(R.id.listlocation);
            Evidence = itemView.findViewById(R.id.listimgreport);
        }

    }
}
