package com.example.sakkahp.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.sakkahp.R;
import com.example.sakkahp.screens.AddPropertyActivity;
import com.example.sakkahp.screens.LoginActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener {

    private CircleImageView userProfile;
    private EditText userName, userEmail;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private String userId;

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private StorageReference storageRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        storageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize views
        // Navigation menu
        DrawerLayout drawerLayout = view.findViewById(R.id.drawer_layout);
        NavigationView navigationView = view.findViewById(R.id.nav_view);
        Toolbar toolbar = view.findViewById(R.id.toolbar_1);

        userProfile = view.findViewById(R.id.profile_image);
        userName = view.findViewById(R.id.user_name);
        userEmail = view.findViewById(R.id.user_email);
        AppCompatButton updateButton = view.findViewById(R.id.update_button);
        AppCompatButton signOutButton = view.findViewById(R.id.sign_out);
        Button addProperty = view.findViewById(R.id.add_property);

        // Setup the toolbar as the action bar
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);

            // Initialize the DrawerLayout and ActionBarDrawerToggle
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    activity,
                    drawerLayout,
                    toolbar,
                    R.string.navigation_drawer_open,
                    R.string.navigation_drawer_close
            );
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();
            navigationView.bringToFront();
            navigationView.setNavigationItemSelectedListener(this);
        }

        // Fetch user data from Firestore
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getUid();
            DocumentReference userRef = db.collection("users").document(userId);
            userRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String name = documentSnapshot.getString("name");
                    String email = documentSnapshot.getString("email");
                    String imageUrl = documentSnapshot.getString("imageUrl");

                    userName.setText(name);
                    userEmail.setText(email);
                    if (imageUrl != null) {
                        // Load profile image using Glide
                        Glide.with(requireContext()).load(imageUrl).into(userProfile);
                    }

                } else {
                    // Handle the case where the document doesn't exist
                    Toast.makeText(getActivity(), "User data not found", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> {
                // Handle failure
                Log.e("AccountFragment", "Error fetching user data", e);
                Toast.makeText(getActivity(), "Failed to fetch user data", Toast.LENGTH_SHORT).show();
            });
        }

        // Set click listener for profile image to choose image
        userProfile.setOnClickListener(v -> chooseImage());

        // Update user data on button click
        updateButton.setOnClickListener(v -> updateUserData());

        // Set click listener for sign out button
        signOutButton.setOnClickListener(v -> signOut());

        // Set click listener for add property button
        addProperty.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddPropertyActivity.class);
            startActivity(intent);
        });
    }

    private void signOut() {
        mAuth.signOut();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        // Finish the current activity to prevent the user from navigating back
        getActivity().finish();
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            userProfile.setImageURI(imageUri);
        }
    }

    private void updateUserData() {
        Map<String, Object> newData = new HashMap<>();
        DocumentReference userRef = db.collection("users").document(userId);

        if (imageUri != null) {
            // Upload image to Firebase Storage
            StorageReference profileImageRef = storageRef.child("profile_images/" + userId + ".jpg");
            profileImageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Get the image URL
                        profileImageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            newData.put("imageUrl", uri.toString());
                            // Update Firestore document with new data
                            userRef.set(newData, SetOptions.merge())
                                    .addOnSuccessListener(aVoid -> Toast.makeText(getActivity(), "Profile Picture updated successfully", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e -> {
                                        Log.e("AccountFragment", "Error updating data", e);
                                        Toast.makeText(getActivity(), "Failed to update Profile Picture", Toast.LENGTH_SHORT).show();
                                    });
                        });
                    })
                    .addOnFailureListener(e -> {
                        Log.e("AccountFragment", "Error uploading image", e);
                        Toast.makeText(getActivity(), "Failed to upload image", Toast.LENGTH_SHORT).show();
                    });
        } else {
            // Update Firestore document with new data (without image)
            userRef.set(newData, SetOptions.merge())
                    .addOnSuccessListener(aVoid -> Toast.makeText(getActivity(), "Data updated successfully", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> {
                        Log.e("AccountFragment", "Error updating data", e);
                        Toast.makeText(getActivity(), "Failed to update data", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        // Add your navigation logic here
        return true;
    }
}
