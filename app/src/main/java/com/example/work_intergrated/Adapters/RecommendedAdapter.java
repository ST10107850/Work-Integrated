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
import com.example.work_intergrated.Activities.ProductDetailsActivity;
import com.example.work_intergrated.Models.PopularModels;
import com.example.work_intergrated.Models.RecommendedModel;
import com.example.work_intergrated.R;

import java.io.Serializable;
import java.util.List;

public class RecommendedAdapter extends RecyclerView.Adapter<RecommendedAdapter.ViewHolder> implements Serializable {
    Context context;
    List<RecommendedModel> recommendedModelList;

    public RecommendedAdapter(Context context, List<RecommendedModel> recommendedModelList) {
        this.context = context;
        this.recommendedModelList = recommendedModelList;
    }

    @NonNull
    @Override
    public RecommendedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recommended_view, parent
                ,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendedAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context).load(recommendedModelList.get(position).getImage_url()).into(holder.rec_image);
        holder.rec_name.setText(recommendedModelList.get(position).getName());
        holder.rec_description.setText(recommendedModelList.get(position).getDescription());
        holder.rec_rating.setText(recommendedModelList.get(position).getRating());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the selected RecommendedModel
                RecommendedModel selectedModel = recommendedModelList.get(position);

                // Open ProductDetailsActivity with the selected model
                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.putExtra("details", selectedModel);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recommendedModelList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView rec_image;
        TextView rec_name, rec_description, rec_rating;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rec_image =itemView.findViewById(R.id.rec_image);
            rec_name = itemView.findViewById(R.id.rec_name);
            rec_description = itemView.findViewById(R.id.rec_description);
            rec_rating = itemView.findViewById(R.id.rec_rating);
        }
    }
}
