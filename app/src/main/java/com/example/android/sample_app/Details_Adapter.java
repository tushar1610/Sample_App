package com.example.android.sample_app;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Details_Adapter extends RecyclerView.Adapter<Details_Adapter.Details_ViewHolder> {

    ArrayList<Details> details;
    Context mContext;
    public Details_Adapter(ArrayList<Details> detail, Context context){
        this.details = detail;
        this.mContext = context;
    }


    @NonNull
    @Override
    public Details_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_row, parent, false);
        return new Details_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Details_ViewHolder holder, int position) {
        Details detail1 = details.get(position);
        holder.bind(detail1);
    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    class Details_ViewHolder extends RecyclerView.ViewHolder{

        TextView firstName, lastName, emailAddress;
        ImageView avatarImage;

        public Details_ViewHolder(@NonNull View itemView) {
            super(itemView);
            avatarImage = itemView.findViewById(R.id.image_avatar);
            firstName = itemView.findViewById(R.id.first_name_hp);
            lastName = itemView.findViewById(R.id.last_name_hp);
            emailAddress = itemView.findViewById(R.id.email_hp);
        }
        public void bind(final Details details1){
            Glide.with(itemView.getContext()).load(details1.url_to_avatar).into(avatarImage);
            firstName.setText(details1.first_name);
            lastName.setText(details1.last_name);
            emailAddress.setText(details1.email_address);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
        }
    }

}
