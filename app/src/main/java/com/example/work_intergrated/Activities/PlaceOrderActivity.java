package com.example.work_intergrated.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.work_intergrated.Models.CartModel;
import com.example.work_intergrated.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlaceOrderActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        List<CartModel> cartModels = (List<CartModel>) getIntent().getSerializableExtra("itemList");

        if (cartModels != null && cartModels.size() > 0) {
            for(CartModel model: cartModels){
               // String documentId = UUID.randomUUID().toString();
                final HashMap<String, Object> cartMap = new HashMap<>();
                cartMap.put("ProductName", model.getProductName());
                cartMap.put("ProductImage", model.getProductImage());
                cartMap.put("ProductPrice", model.getProductPrice());
                cartMap.put("CurrentDate", model.getCurrentDate());
                cartMap.put("CurrentTime", model.getCurrentTime());
                cartMap.put("TotalQuantity", model.getTotalQuantity());
                cartMap.put("TotalPrice", model.getTotalPrice());

                firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                        .collection("MyOrder").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                Toast.makeText(PlaceOrderActivity.this, "Added to cart", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
            }
        }
    }
}