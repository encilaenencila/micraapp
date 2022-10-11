package com.example.micraapp;


import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class AnnouncementAdapter  extends RecyclerView.Adapter<AnnouncementAdapter.AnnouncementViewHolder> {
    private Context context;
    private List<AnnounceItem> announceItemList;
    private String IMAGE_URL = "http://www.micra.services/Micra/resources/files/announcement/";

    public AnnouncementAdapter(Context context, List<AnnounceItem> announceItemList) {
        this.context = context;
        this.announceItemList = announceItemList;
    }
    @Override
    public AnnouncementViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.announcement_list, null, false);
        return new AnnouncementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AnnouncementViewHolder holder, int position) {
        AnnounceItem announceItem = announceItemList.get(position);

            Glide.with(context).load( IMAGE_URL + announceItem.getImage()).into(holder.ivannouncementimg);

        holder.tvdateposted.setText(announceItem.getDate());
        holder.tvtitle.setText(announceItem.getTitle());

        holder.ivannouncementimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context); // Context, this, etc.
                dialog.setContentView(R.layout.detailsdialog);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                dialog.show();
                dialog.getWindow().setAttributes(lp);

                dialog.setCancelable(false);

                // Button btnOkay = (Button) dialog.findViewById(R.id.btnok);
                TextView tvdetails = (TextView) dialog.findViewById(R.id.tvdetails);
                TextInputEditText tetitle = (TextInputEditText) dialog.findViewById(R.id.tetitle);
                TextInputEditText tegetannounce = (TextInputEditText) dialog.findViewById(R.id.tegetannounce);
                TextInputEditText tepostedby = (TextInputEditText) dialog.findViewById(R.id.tepostedby);
                TextInputEditText tedateposted = (TextInputEditText) dialog.findViewById(R.id.tedateposted);


                tetitle.setText(announceItem.getTitle());
                tegetannounce.setText(announceItem.getDescription());
                tepostedby.setText(announceItem.getPostedby());
                tedateposted.setText(announceItem.getDate());

                tvdetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return announceItemList.size();
    }

    class AnnouncementViewHolder   extends RecyclerView.ViewHolder {

        TextView tvdateposted,tvtitle;
        ImageView ivannouncementimg;
        public AnnouncementViewHolder(View itemView) {
            super(itemView);
            tvdateposted = itemView.findViewById(R.id.tvdateposted);
            tvtitle = itemView.findViewById(R.id.tvtitle);
            ivannouncementimg = itemView.findViewById(R.id.ivannouncementimg);


        }
    }

    public void OpenDialog(View View){

    }
}
