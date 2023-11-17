package com.example.work_intergrated.Adapters;

import static android.content.Intent.getIntent;

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
import com.example.work_intergrated.Activities.ProductDetailsActivity;
import com.example.work_intergrated.Activities.ViewAllActivity;
import com.example.work_intergrated.Models.PopularModels;
import com.example.work_intergrated.Models.ViewAllModel;
import com.example.work_intergrated.R;

import java.util.List;

public class ViewAllAdapter extends RecyclerView.Adapter<ViewAllAdapter.ViewHolder>{
    Context context;
    List<ViewAllModel> viewAllModelList;

    public ViewAllAdapter(Context context, List<ViewAllModel> viewAllModelList) {
        this.context = context;
        this.viewAllModelList = viewAllModelList;
    }
    @NonNull
    @Override
    public ViewAllAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewAllAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_all_product, parent
                ,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewAllAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // Get the current ViewAllModel from the list based on the position
        ViewAllModel viewAllModel = viewAllModelList.get(position);

        // Now you can use viewAllModel to set the details in your ViewHolder
        Glide.with(context).load(viewAllModel.getImage_url()).into(holder.viewImage);
        holder.name.setText(viewAllModel.getName());
        holder.description.setText(viewAllModel.getDescription());
        holder.price.setText(String.valueOf(viewAllModel.getPrice()));
        holder.rating.setText(viewAllModel.getRating());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.putExtra("details", viewAllModel);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return viewAllModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView viewImage;
        TextView name, description, rating, price;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            viewImage = itemView.findViewById(R.id.view_img);
            name =itemView.findViewById(R.id.view_name);
            description = itemView.findViewById(R.id.view_description);
            rating = itemView.findViewById(R.id.view_rating);
            price = itemView.findViewById(R.id.view_price);
        }
    }
}
