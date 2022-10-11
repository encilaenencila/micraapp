package com.example.micraapp;

import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

public class CrimeAdapter extends RecyclerView.Adapter<CrimeAdapter.ViewHolder> implements Filterable {
    Context cont;
    List<incidentlistreports> incidentlistreportsList;
    List<incidentlistreports> AllincidentlistreportsList;
    private String IMG_URL = "http://www.micra.services/IncidentReport/";
    private String IMG_URL_PROOF = "http://www.micra.services/Micra/resources/files/incident/";

    public CrimeAdapter(Context cont, List<incidentlistreports> incidentlistreportsList) {

        this.incidentlistreportsList = incidentlistreportsList;
        this.cont = cont;
        this.AllincidentlistreportsList = new ArrayList<>(incidentlistreportsList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(cont);
        View view = inflater.inflate(R.layout.custom_listreports, parent, false);
        return new CrimeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        incidentlistreports res = incidentlistreportsList.get(position);


        if (res.getEvidence().isEmpty()) {
            Glide.with(cont)
                    .load(cont.getResources().getDrawable(R.drawable.no_image_avail_2))
                    .fitCenter()
                    .into(holder.Evidence);
        } else {

            Glide.with(cont)
                    .load(IMG_URL + res.getEvidence())
                    .fitCenter()
                    .into(holder.Evidence);
        }


        holder.category.setText(res.getCategory());
        holder.location.setText(res.getLocationofincident());
        holder.status.setText(res.getResolveStatus());
        holder.date.setText(res.getReportDate());
        holder.crd.setBackgroundResource(R.drawable.lastreportlsi);
        holder.crd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, userfinish.class);
                intent.putExtra("status", String.valueOf(res.getResolveStatus()));
                intent.putExtra("category", String.valueOf(res.getCategory()));
                intent.putExtra("categorydescription", res.getCrimecategory());
                if (res.getEvidence().isEmpty()) {
                    intent.putExtra("imageurl", "");

                } else {
                    intent.putExtra("imageurl", IMG_URL + String.valueOf(res.getEvidence()));
                }
                intent.putExtra("location", String.valueOf(res.getLocationofincident()));
                intent.putExtra("description", String.valueOf(res.getCasenarrative()));
                intent.putExtra("respondername", String.valueOf(res.getRespondedBy()));
                intent.putExtra("responddate", String.valueOf(res.getRespondDate()));
                intent.putExtra("respondtime", String.valueOf(res.getRespondTime()));
                intent.putExtra("respondmessage", String.valueOf(res.getRespondMessage()));
                intent.putExtra("landmark", String.valueOf(res.getLandmark()));
                intent.putExtra("resolvefeedback", String.valueOf(res.getResolvefeedback()));
                intent.putExtra("residentfeedback", String.valueOf(res.getResidentfeedback()));
                Log.d("residentfeedback",String.valueOf(res.getResidentfeedback()));
                if (res.getFinishproof().equals("null")) {
                    intent.putExtra("finishproof", "");
                } else {
                    intent.putExtra("finishproof", IMG_URL_PROOF + String.valueOf(res.getFinishproof().trim()));
                }
                intent.putExtra("reportnumber", String.valueOf(res.getReportid()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return incidentlistreportsList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }
    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<incidentlistreports> filterlist = new ArrayList<>();
            if(constraint == null || constraint.length() == 0){
                filterlist.addAll(AllincidentlistreportsList);
            }else{
                String filterpattern = constraint.toString().toLowerCase().trim();

                for(incidentlistreports item : AllincidentlistreportsList){
                    if(item.getReportDate().toLowerCase().contains(filterpattern)){
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
            incidentlistreportsList.clear();
            incidentlistreportsList.addAll((List)results.values);
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
            status = itemView.findViewById(R.id.liststatus);
            date = itemView.findViewById(R.id.txtdatereported);
            category = itemView.findViewById(R.id.listcategory);
            location = itemView.findViewById(R.id.listlocation);
            Evidence = itemView.findViewById(R.id.listimgreport);
        }

    }
}
