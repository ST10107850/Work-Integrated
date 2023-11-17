package com.example.work_intergrated.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.TextView;

import com.example.work_intergrated.R;

public class PaymentActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView SubTotal,discount,shipping,total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        //Toolbar
       toolbar = findViewById(R.id.payment_toolbar);
       setSupportActionBar(toolbar);
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);

       double amount = 0.0;
       amount = getIntent().getDoubleExtra("amount",0.0);

        SubTotal = findViewById(R.id.sub_total);
        discount = findViewById(R.id.textView17);
        shipping = findViewById(R.id.textView18);
        total = findViewById(R.id.total_amt);

        SubTotal.setText(amount+"R");

    }
}