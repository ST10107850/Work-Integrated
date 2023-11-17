package com.example.work_intergrated.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.work_intergrated.Models.RecommendedModel;
import com.example.work_intergrated.Models.ViewAllModel;
import com.example.work_intergrated.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class ProductDetailsActivity extends AppCompatActivity {

    ImageView detail_img, minus_img,detail_add_cycle;
    TextView detail_name, detail_description, detail_price, detail_rating, quantity;
    Button addToCart, buy_now;
    Toolbar toolbar;
    ViewAllModel viewAllModel = null;
    int totalQuantity = 1;
    int totalPrice = 0;

    FirebaseAuth auth;
    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();


       // Set up Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        detail_name = findViewById(R.id.detail_name);
        detail_description = findViewById(R.id.detail_description);
        detail_price = findViewById(R.id.detail_price);
        detail_rating = findViewById(R.id.detail_rating);
        quantity = findViewById(R.id.quantity);

        detail_img = findViewById(R.id.detail_img);
        minus_img = findViewById(R.id.detail_minus_cycle);
        detail_add_cycle = findViewById(R.id.detail_add_cycle);

        Object object = getIntent().getSerializableExtra("details");

        if (object instanceof ViewAllModel) {
            viewAllModel = (ViewAllModel) object;
            setProductDetails(viewAllModel);
        } else if (object instanceof RecommendedModel) {
            RecommendedModel recommendedModel = (RecommendedModel) object;
            setProductDetails(recommendedModel);
        }

        addToCart = findViewById(R.id.add_to_cart);
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addedToCart();
            }
        });
        detail_add_cycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(totalQuantity < 10){
                    totalQuantity++;
                    quantity.setText(String.valueOf(totalQuantity));
                    totalPrice = viewAllModel.getPrice() * totalQuantity;
                }else{
                    Toast.makeText(ProductDetailsActivity.this, "You have reach the number of Items you can buy", Toast.LENGTH_SHORT).show();
                }
            }
        });
        minus_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(totalQuantity > 1){
                    totalQuantity--;
                    quantity.setText(String.valueOf(totalQuantity));
                    totalPrice = viewAllModel.getPrice() * totalQuantity;
                }else{
                    Toast.makeText(ProductDetailsActivity.this, "You can't have items selected", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buy_now = findViewById(R.id.buy_now);
        buy_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductDetailsActivity.this,AddressActivity.class);

                if(viewAllModel != null){
                    intent.putExtra("item",viewAllModel);
                }
                startActivity(intent);
            }
        });
    }
    private void setProductDetails(ViewAllModel model) {
        Glide.with(getApplicationContext()).load(model.getImage_url()).into(detail_img);
        detail_name.setText(model.getName());
        detail_description.setText(model.getDescription());
        detail_price.setText(String.valueOf("R" + model.getPrice()));
        detail_rating.setText(model.getRating());

        totalPrice = model.getPrice() * totalQuantity;
    }

    private void setProductDetails(RecommendedModel model) {
        Glide.with(getApplicationContext()).load(model.getImage_url()).into(detail_img);
        detail_name.setText(model.getName());
        detail_description.setText(model.getDescription());
        detail_price.setText(String.valueOf("R" + model.getPrice()));
        detail_rating.setText(model.getRating());

        totalPrice = model.getPrice() * totalQuantity;
    }
    private void addedToCart() {
        if (viewAllModel == null) {
            // Handle the case where viewAllModel is null
            Toast.makeText(this, "Error: Product details not available", Toast.LENGTH_SHORT).show();
            return;
        }

        String saveCurrentDate, currentTime;
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/DD/YYY");
        saveCurrentDate = simpleDateFormat.format(calendar.getTime());

        SimpleDateFormat timeFormay = new SimpleDateFormat("HH:MM:SS a");
        currentTime = timeFormay.format(calendar.getTime());

        String documentId = UUID.randomUUID().toString();
        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("documentId", documentId);
        cartMap.put("ProductName", detail_name.getText().toString());
        cartMap.put("ProductImage", viewAllModel.getImage_url()); // Use viewAllModel or recommendedModel accordingly
        cartMap.put("ProductPrice", detail_price.getText().toString());
        cartMap.put("CurrentDate", saveCurrentDate);
        cartMap.put("CurrentTime", currentTime);
        cartMap.put("TotalQuantity", quantity.getText().toString());
        cartMap.put("TotalPrice", totalPrice);

        firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("AddToCart").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(ProductDetailsActivity.this, "Added to cart", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }
}