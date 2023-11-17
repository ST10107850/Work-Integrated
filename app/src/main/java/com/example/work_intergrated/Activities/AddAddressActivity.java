package com.example.work_intergrated.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.work_intergrated.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class AddAddressActivity extends AppCompatActivity {

    EditText name, addressLine, city, postalCode,  phoneNumber;
    Button saveBtn;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        name = findViewById(R.id.ad_name);
        addressLine = findViewById(R.id.ad_address);
        city = findViewById(R.id.ad_city);
        postalCode = findViewById(R.id.ad_code);
        phoneNumber = findViewById(R.id.ad_phone);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Set up Toolbar
        toolbar = findViewById(R.id.add_address_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        saveBtn = findViewById(R.id.ad_add_address);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = name.getText().toString();
                String streetLine = addressLine.getText().toString();
                String cityLine = city.getText().toString();
                String cellNo = phoneNumber.getText().toString();
                String code = postalCode.getText().toString();

                String final_address ="";

                if(!username.isEmpty()){
                    final_address += username;
                }
                if(!streetLine.isEmpty()){
                    final_address += streetLine;
                }
                if(!cityLine.isEmpty()){
                    final_address += cityLine;
                }
                if(!cellNo.isEmpty()){
                    final_address += cellNo;
                }
                if(!code.isEmpty()){
                    final_address += code;
                }
                if(!username.isEmpty() && !streetLine.isEmpty() && !cityLine.isEmpty() && !cellNo.isEmpty() && !code.isEmpty()){
                    final HashMap<String, String> map = new HashMap<>();

                    map.put("Address", final_address);

                    firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                            .collection("Address").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    Toast.makeText(AddAddressActivity.this, "Address has been added successfully!!!!", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                }else{
                    Toast.makeText(AddAddressActivity.this, "Kindly Fill All Fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}