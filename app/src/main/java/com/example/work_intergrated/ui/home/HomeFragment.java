package com.example.work_intergrated.ui.home;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.work_intergrated.Adapters.CategoryAdapter;
import com.example.work_intergrated.Adapters.PopularAdapter;
import com.example.work_intergrated.Adapters.RecommendedAdapter;
import com.example.work_intergrated.Adapters.ViewAllAdapter;
import com.example.work_intergrated.Models.CategoriesModel;
import com.example.work_intergrated.Models.PopularModels;
import com.example.work_intergrated.Models.RecommendedModel;
import com.example.work_intergrated.Models.ViewAllModel;
import com.example.work_intergrated.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    ScrollView scrollView;
    ProgressBar progressBar;
    //Firebase database
    FirebaseFirestore db;
    //Popular Recycle view
    RecyclerView popRec, cateRec, recomm_recycleview;
    //Popular items
    List<PopularModels> popularModelsList;
    PopularAdapter popularAdapter;

    //Category Item
    List<CategoriesModel> categoriesModelList;
    CategoryAdapter categoryAdapter;

    //Recommended items
    List<RecommendedModel> recommendedModelList;
    RecommendedAdapter recommendedAdapter;

   //////////////// //Search bar
    EditText search;
    private RecyclerView search_rec;
    private List<ViewAllModel> listModel;
    private ViewAllAdapter viewAllAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home,container, false);

        db = FirebaseFirestore.getInstance();

        //Initialize
        popRec = root.findViewById(R.id.pop_recycleview);
        cateRec = root.findViewById(R.id.explore_recycleview);
        recomm_recycleview = root.findViewById(R.id.recomm_recycleview);
        scrollView = root.findViewById(R.id.scroll_view);
        progressBar = root.findViewById(R.id.progressBar);

        progressBar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);

        //Popular Items
        popRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        popularModelsList = new ArrayList<>();
        popularAdapter =new PopularAdapter(getActivity(), popularModelsList);
        popRec.setAdapter(popularAdapter);

        db.collection("PopularProducts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                PopularModels popularModels = document.toObject(PopularModels.class);
                                popularModelsList.add(popularModels);
                                popularAdapter.notifyDataSetChanged();

                                progressBar.setVisibility(View.GONE);
                                scrollView.setVisibility(View.VISIBLE);

                            }
                        } else {
                            Toast.makeText(getActivity(), "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        //Categories Items
        cateRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        categoriesModelList = new ArrayList<>();
        categoryAdapter =new CategoryAdapter(getActivity(), categoriesModelList);
        cateRec.setAdapter(categoryAdapter);

        db.collection("Categories")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                CategoriesModel categoriesModel = document.toObject(CategoriesModel.class);
                                categoriesModelList.add(categoriesModel);
                                categoryAdapter.notifyDataSetChanged();

                            }
                        } else {
                            Toast.makeText(getActivity(), "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        //Recommended Items
        recomm_recycleview.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        recommendedModelList = new ArrayList<>();
        recommendedAdapter =new RecommendedAdapter(getActivity(), recommendedModelList);
        recomm_recycleview.setAdapter(recommendedAdapter);

        db.collection("RecommendedProduct")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                RecommendedModel recommendedModel = document.toObject(RecommendedModel.class);
                                recommendedModelList.add(recommendedModel);
                                recommendedAdapter.notifyDataSetChanged();

                            }
                        } else {
                            Toast.makeText(getActivity(), "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        //////////////// //Search bar
        search = root.findViewById(R.id.search_box);
        search_rec = root.findViewById(R.id.search_rec);
        listModel = new ArrayList<>();
        viewAllAdapter = new ViewAllAdapter(getContext(), listModel);
        search_rec.setLayoutManager(new LinearLayoutManager(getContext()));
        search_rec.setAdapter(viewAllAdapter);
        search_rec.setHasFixedSize(true);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().isEmpty()){
                    listModel.clear();
                    viewAllAdapter.notifyDataSetChanged();
                }else{
                    searchProduct(editable.toString());
                }
            }
        });

        return root;
    }

    private void searchProduct(String letter) {
        if (!letter.isEmpty()) {
            String searchLetter = letter.toLowerCase(); // Convert search letter to lowercase

            db.collection("Products")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                listModel.clear();
                                viewAllAdapter.notifyDataSetChanged();
                                for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                                    ViewAllModel viewAllModel = documentSnapshot.toObject(ViewAllModel.class);
                                    if (viewAllModel.getName() != null && viewAllModel.getName().toLowerCase().contains(searchLetter)) {
                                        listModel.add(viewAllModel);
                                        viewAllAdapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        }
                    });
        } else {
            Toast.makeText(getContext(), "Enter a letter to search for items.", Toast.LENGTH_SHORT).show();
        }
    }



}