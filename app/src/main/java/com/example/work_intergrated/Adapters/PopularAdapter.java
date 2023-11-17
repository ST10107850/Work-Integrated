package com.example.work_intergrated.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.work_intergrated.Activities.ViewAllActivity;
import com.example.work_intergrated.Models.PopularModels;
import com.example.work_intergrated.R;

import java.util.List;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.ViewHolder> {

    Context context;
    List<PopularModels> popular_list;

    public PopularAdapter(Context context, List<PopularModels> popular_list) {
        this.context = context;
        this.popular_list = popular_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_item, parent
        ,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context).load(popular_list.get(position).getImage_url()).into(holder.popImage);
        holder.name.setText(popular_list.get(position).getName());
        holder.description.setText(popular_list.get(position).getDescription());
        holder.discount.setText(popular_list.get(position).getDiscount());
        holder.rating.setText(popular_list.get(position).getRating());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewAllActivity.class);
                intent.putExtra("type", popular_list.get(position).getType());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return popular_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView popImage;
        TextView name, description, rating, discount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            popImage = itemView.findViewById(R.id.pop_img);
            name =itemView.findViewById(R.id.item_name);
            description = itemView.findViewById(R.id.pop_desc);
            rating = itemView.findViewById(R.id.pop_rating);
            discount = itemView.findViewById(R.id.pop_discount);
        }
    }
}
