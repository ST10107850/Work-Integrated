package com.example.work_intergrated.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.work_intergrated.Models.CartModel;
import com.example.work_intergrated.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    Context context;
    List<CartModel> cartModelList;


    public CartAdapter(Context context, List<CartModel> cartModelList) {
        this.context = context;
        this.cartModelList = cartModelList;

    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_detail_cartegory, parent
                ,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        CartModel cartModel = cartModelList.get(position);

        if (cartModel != null) {
            Glide.with(context).load(cartModel.getProductImage()).into(holder.cartImage);
            holder.date.setText(cartModel.getCurrentDate());
            holder.time.setText(cartModel.getCurrentTime());
            holder.prod_price.setText(String.valueOf(cartModel.getProductPrice()));
            holder.prod_name.setText(cartModel.getProductName());
            holder.total.setText(String.valueOf(cartModel.getTotalPrice()));
            holder.quantity.setText(String.valueOf(cartModel.getTotalQuantity()));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                        showConfirmationDialog(position);
                }
            });
        } else {
            Log.e("CartAdapter", "Null CartModel at position " + position);
        }

    }
    private void showConfirmationDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Confirmation");
        builder.setMessage("Do you want to delete this item?");

        builder.setPositiveButton("Yes", (dialog, which) -> {
            // User clicked Yes, perform the deletion
            deleteItem(position);
        });

        builder.setNegativeButton("No", (dialog, which) -> {
            dialog.dismiss();
        });

        builder.show();
    }
    private void deleteItem(int position) {
        // Get the document ID (or any unique identifier) of the item
        String itemId = cartModelList.get(position).getDocumentId();

        // Remove the item from Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("AddToCart"  )
                .document(itemId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    // DocumentSnapshot successfully deleted
                    // Perform any additional tasks if needed
                    cartModelList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, cartModelList.size());
                    Toast.makeText(context, "Item deleted successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Handle errors
                    Toast.makeText(context, "Failed to delete item: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
    @Override
    public int getItemCount() {
        return cartModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cartImage;
        TextView prod_name, prod_price, quantity, date, time, total;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cartImage = itemView.findViewById(R.id.nav_cat_img);
            prod_name = itemView.findViewById(R.id.product_name);
            prod_price = itemView.findViewById(R.id.product_price);
            quantity = itemView.findViewById(R.id.quantity);
            date = itemView.findViewById(R.id.current_date);
            time = itemView.findViewById(R.id.current_time);
            total = itemView.findViewById(R.id.total_price);

        }
    }
}
