package com.example.work_intergrated.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.work_intergrated.Adapters.PopularAdapter;
import com.example.work_intergrated.Adapters.ViewAllAdapter;
import com.example.work_intergrated.Models.PopularModels;
import com.example.work_intergrated.Models.ViewAllModel;
import com.example.work_intergrated.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewAllActivity extends AppCompatActivity {
    FirebaseFirestore db;
    RecyclerView viewRec;
    //Popular items
    List<ViewAllModel> viewAllModelList;
    ViewAllAdapter viewAllAdapter;
    Toolbar toolbar;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

      //  getSupportActionBar().hide();
        progressBar = findViewById(R.id.view_progessBar);
        progressBar.setVisibility(View.VISIBLE);

        String type = getIntent().getStringExtra("type");
        db = FirebaseFirestore.getInstance();
        viewRec = findViewById(R.id.view_all_rec);
        viewRec.setVisibility(View.GONE);
        // Hide the default ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

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

        viewAllModelList = new ArrayList<>();
        viewAllAdapter = new ViewAllAdapter(this, viewAllModelList);
        viewRec.setAdapter(viewAllAdapter);
        viewRec.setLayoutManager(new LinearLayoutManager(this));

        if(type != null && type.equalsIgnoreCase(type)){
            db.collection("Products").whereEqualTo("type", type).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                            ViewAllModel viewAllModel = documentSnapshot.toObject(ViewAllModel.class);
                            if (viewAllModel != null) {
                                viewAllModelList.add(viewAllModel);
                                progressBar.setVisibility(View.GONE);
                                viewRec.setVisibility(View.VISIBLE);
                            } else {
                                Log.e("ViewAllActivity", "viewAllModel is null for document ID: " + documentSnapshot.getId());
                            }
                        }
                        viewAllAdapter.notifyDataSetChanged();
                    } else {
                        Log.e("ViewAllActivity", "Firestore query failed: " + task.getException());
                    }
                }

            });
        }
    }
}