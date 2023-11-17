package com.example.work_intergrated.Activities;

import static android.app.PendingIntent.getActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.work_intergrated.Adapters.AddressAdapter;
import com.example.work_intergrated.Models.AddressModel;
import com.example.work_intergrated.Models.CartModel;
import com.example.work_intergrated.Models.ViewAllModel;
import com.example.work_intergrated.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AddressActivity extends AppCompatActivity implements AddressAdapter.SelectedAddress{

    RecyclerView recyclerView;
    Button addAddress, continueAddress,payment_btn;
    private List<AddressModel> addressModelList;
    AddressAdapter adapter;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    Toolbar toolbar;
    ViewAllModel viewAllModel = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        toolbar = findViewById(R.id.address_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get data from detailed activity
        Object obj = getIntent().getSerializableExtra("item");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        firestore =FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        payment_btn = findViewById(R.id.payment_btn);
        payment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                double amount = 0.0;
                if (obj instanceof ViewAllModel) {
                    viewAllModel = (ViewAllModel) obj;
                    amount = viewAllModel.getPrice();
                }
                Intent intent = new Intent(AddressActivity.this,PaymentActivity.class);
                intent.putExtra("amount",amount);
                startActivity(intent);
            }
        });




        addAddress = findViewById(R.id.add_address_btn);
        continueAddress = findViewById(R.id.payment_btn);

        recyclerView = findViewById(R.id.address_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        adapter = new AddressAdapter(getApplicationContext(), addressModelList, this);
        recyclerView.setAdapter(adapter);

        addressModelList = new ArrayList<>(); // Initialize the list

        adapter = new AddressAdapter(getApplicationContext(), addressModelList, this);
        recyclerView.setAdapter(adapter);

        firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("Address")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                AddressModel addressModel = document.toObject(AddressModel.class);
                                addressModelList.add(addressModel);
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            // Toast.makeText(getActivity(), "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });



        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddressActivity.this, AddAddressActivity.class));
            }
        });

        continueAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if any radio button is selected
                boolean isAnyRadioButtonSelected = false;
                for (AddressModel addressModel : addressModelList) {
                    if (addressModel.isSelected()) {
                        isAnyRadioButtonSelected = true;
                        break;
                    }
                }

                if (isAnyRadioButtonSelected) {
                    // At least one radio button is selected, proceed to PaymentActivity
                    startActivity(new Intent(AddressActivity.this, PaymentActivity.class));
                } else {
                    // No radio button is selected, display a message to the user
                    Toast.makeText(AddressActivity.this, "Please select an address", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    @Override
    public void selectedAddress(String addressName) {

    }
}