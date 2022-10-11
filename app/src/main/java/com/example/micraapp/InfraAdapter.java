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

public class InfraAdapter extends RecyclerView.Adapter<InfraAdapter.ViewHolder> implements Filterable {
    Context cont;
    List<Infralistreports> infralistreports;
    List<Infralistreports> Allinfralistreports;
    private String IMG_URL = "http://www.micra.services/Infrastracture/";
    private String IMG_URL_PROOF = "http://www.micra.services/Micra/resources/files/infrastructure/";

    public InfraAdapter(Context cont, List<Infralistreports> infralistreports) {

        this.infralistreports = infralistreports;
        this.cont = cont;
        this.Allinfralistreports = new ArrayList<>(infralistreports);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(cont);
        View view = inflater.inflate(R.layout.custom_listreports, parent, false);
        return new InfraAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Infralistreports res = infralistreports.get(position);


        if (res.getReportimg().isEmpty()) {
            Glide.with(cont)
                    .load(cont.getResources().getDrawable(R.drawable.no_image_avail_2))
                    .fitCenter()
                    .into(holder.Evidence);
        } else {

            Glide.with(cont)
                    .load(IMG_URL + res.getReportimg())
                    .fitCenter()
                    .into(holder.Evidence);
        }


        holder.category.setText(res.getReportcategory());
        holder.location.setText(res.getReportlocation());
        holder.date.setText(res.getReportdate());
        holder.status.setText(res.getResolvestatus());
        holder.crd.setBackgroundResource(R.drawable.lastreportlsi);
        holder.crd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, userfinish.class);
                intent.putExtra("status", String.valueOf(res.getResolvestatus()));
                intent.putExtra("category", String.valueOf(res.getReportcategory()));
                intent.putExtra("categorydescription", res.getCategorydescription());
                if (res.getReportimg().isEmpty()) {
                    intent.putExtra("imageurl", "");
                } else {
                    intent.putExtra("imageurl", IMG_URL + String.valueOf(res.getReportimg()));
                }
                intent.putExtra("location", String.valueOf(res.getReportlocation()));
                intent.putExtra("description", String.valueOf(res.getReportdescription()));
                intent.putExtra("respondername", String.valueOf(res.getRespondername()));
                intent.putExtra("responddate", String.valueOf(res.getRepondate()));
                intent.putExtra("respondtime", String.valueOf(res.getRespondtime()));
                intent.putExtra("respondmessage", String.valueOf(res.getRespondmessage()));
                intent.putExtra("landmark", String.valueOf(res.getLandmark()));
                intent.putExtra("resolvefeedback", String.valueOf(res.getResolvefeedback()));
                intent.putExtra("residentfeedback", String.valueOf(res.getResidentfeedback()));
                if (res.getFinishproof().equals("null")) {
                    Log.d("fp",res.getFinishproof());
                    intent.putExtra("finishproof", "");
                } else {
                    intent.putExtra("finishproof", IMG_URL_PROOF + String.valueOf(res.getFinishproof().trim()));
                }
                intent.putExtra("reportnumber", String.valueOf(res.getReportnumber()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return infralistreports.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }
    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<Infralistreports> filterlist = new ArrayList<>();
            if(constraint == null || constraint.length() == 0){
                filterlist.addAll(Allinfralistreports);
            }else{
                String filterpattern = constraint.toString().toLowerCase().trim();

                for(Infralistreports item : Allinfralistreports){
                    if(item.getReportdate().toLowerCase().contains(filterpattern)){
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
            infralistreports.clear();
            infralistreports.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView status,date, category, location;
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
