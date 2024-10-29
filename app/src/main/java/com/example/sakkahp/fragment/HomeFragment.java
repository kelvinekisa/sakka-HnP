package com.example.sakkahp.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sakkahp.Property;
import com.example.sakkahp.R;
import com.example.sakkahp.adapters.PropertyAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private PropertyAdapter propertyAdapter;
    private FirebaseFirestore db;
    private List<Property> propertyList;
    private TextView userName;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        userName = view.findViewById(R.id.user_name);
        EditText search = view.findViewById(R.id.search);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);

        db = FirebaseFirestore.getInstance();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        propertyList = new ArrayList<>();
        propertyAdapter = new PropertyAdapter(getContext(), propertyList);
        recyclerView.setAdapter(propertyAdapter);

        fetchUserDetails();
        fetchProperties();

        swipeRefreshLayout.setOnRefreshListener(this::fetchProperties);

        search.setOnClickListener(v -> {
            // Implement search functionality
        });

        return view;
    }

    private void fetchUserDetails() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            db.collection("users").document(currentUser.getUid()).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String name = document.getString("name");
                        userName.setText("Welcome back, " + name);

                        String imageUrl = document.getString("imageUrl");
                        if (imageUrl != null) {
                            Picasso.get().load(imageUrl).into((ImageView) getView().findViewById(R.id.profile_image));
                        }
                    }
                }
            });
        }
    }

    private void fetchProperties() {
        swipeRefreshLayout.setRefreshing(true);

        db.collection("properties").get().addOnCompleteListener(task -> {
            swipeRefreshLayout.setRefreshing(false);

            if (task.isSuccessful()) {
                propertyList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Property property = document.toObject(Property.class);
                    propertyList.add(property);
                }
                propertyAdapter.notifyDataSetChanged();
            } else {
                Log.w("HomeFragment", "Error getting documents.", task.getException());
            }
        });
    }
}
