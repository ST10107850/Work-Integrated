package com.example.work_intergrated;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.work_intergrated.Activities.AddAddressActivity;
import com.example.work_intergrated.Activities.AddressActivity;
import com.example.work_intergrated.Activities.PlaceOrderActivity;
import com.example.work_intergrated.Adapters.CartAdapter;
import com.example.work_intergrated.Adapters.NavCategoryAdapter;
import com.example.work_intergrated.Models.CartModel;
import com.example.work_intergrated.Models.NavCategoryModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MyCartFragment extends Fragment {

    FirebaseFirestore db;
    RecyclerView recyclerView;
    List<CartModel> cartModelList;
    TextView overTotal;
    CartAdapter cartAdapter;
    FirebaseAuth auth;
    ProgressBar progressBar;
    Button buyNow;

    public MyCartFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_my_cart, container, false);

        recyclerView = root.findViewById(R.id.recyclerview);
        recyclerView.setVisibility(View.GONE);
        overTotal = root.findViewById(R.id.textView4);

        progressBar = root.findViewById(R.id.view_progessBar);
        progressBar.setVisibility(View.VISIBLE);

        buyNow = root.findViewById(R.id.buy_now);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        //Popular Items
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        cartModelList = new ArrayList<>();
        cartAdapter = new CartAdapter(getActivity(), cartModelList);
        recyclerView.setAdapter(cartAdapter);

        LocalBroadcastManager.getInstance(getActivity())
                        .registerReceiver(eMassageReceiver,new IntentFilter("MyTotalPrice"));

        db.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("AddToCart")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                CartModel cartModel = document.toObject(CartModel.class);
                                cartModelList.add(cartModel);
                                cartAdapter.notifyDataSetChanged();
                                progressBar.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);

                            }
                        } else {
                            Toast.makeText(getActivity(), "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddressActivity.class);
                intent.putExtra("itemList", (Serializable) cartModelList);
                startActivity(intent);
            }
        });
        return root;
    }
public BroadcastReceiver eMassageReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
        int totalMoney = intent.getIntExtra("totalAmount", 0);
        overTotal.setText("Total Bill:    R " + totalMoney);
    }
};
}