package com.example.sakkahp.screens;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sakkahp.R;
import com.example.sakkahp.Property;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddPropertyActivity extends AppCompatActivity {

    private EditText locationEditText, typeEditText, descriptionEditText, shortDescriptionEditText;
    private EditText ownerNameEditText, contactNoEditText, priceEditText, categoryEditText;
    private ImageView uploadedImageView;

    private Uri imageUri;
    private FirebaseFirestore db;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_property);

        // Initialize Firebase components
        db = FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        // Bind views
        locationEditText = findViewById(R.id.property_location);
        typeEditText = findViewById(R.id.property_type);
        descriptionEditText = findViewById(R.id.property_description);
        shortDescriptionEditText = findViewById(R.id.property_shortdescription);
        ownerNameEditText = findViewById(R.id.property_ownername);
        contactNoEditText = findViewById(R.id.property_contactno);
        priceEditText = findViewById(R.id.property_price);
        categoryEditText = findViewById(R.id.property_category);
        uploadedImageView = findViewById(R.id.imageViewUploaded);
        Button uploadImageButton = findViewById(R.id.buttonUploadImage);
        Button submitButton = findViewById(R.id.buttonSubmit);

        // Set up upload image button
        uploadImageButton.setOnClickListener(v -> openImagePicker());

        // Set up submit button
        submitButton.setOnClickListener(v -> submitProperty());
    }

    private void openImagePicker() {
        // Launch image picker to select an image
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Image"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            uploadedImageView.setImageURI(imageUri);
        }
    }

    private void submitProperty() {
        // Retrieve input data
        String location = locationEditText.getText().toString().trim();
        String type = typeEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String shortDescription = shortDescriptionEditText.getText().toString().trim();
        String ownerName = ownerNameEditText.getText().toString().trim();
        String contactNo = contactNoEditText.getText().toString().trim();
        String price = priceEditText.getText().toString().trim();
        String category = categoryEditText.getText().toString().trim();

        // Validate inputs
        if (location.isEmpty() || type.isEmpty() || description.isEmpty() || shortDescription.isEmpty() ||
                ownerName.isEmpty() || contactNo.isEmpty() || price.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create Property object
        Property property = new Property(location, type, description, shortDescription, ownerName, contactNo, price, category);

        // Handle image upload if an image is selected
        if (imageUri != null) {
            uploadImageAndSaveProperty(property);
        } else {
            // Save property without image
            savePropertyToFirestore(property);
        }
    }

    private void uploadImageAndSaveProperty(Property property) {
        // Create a reference to the image location in Firebase Storage
        StorageReference fileReference = storageReference.child("property_images/" + System.currentTimeMillis() + ".jpg");

        // Upload the file
        fileReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Get download URL
                    fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        property.setImageUrl(uri.toString());
                        savePropertyToFirestore(property);
                    }).addOnFailureListener(e -> {
                        Toast.makeText(AddPropertyActivity.this, "Failed to get download URL", Toast.LENGTH_SHORT).show();
                        Log.e("AddPropertyActivity", "Error: " + e.getMessage());
                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AddPropertyActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                    Log.e("AddPropertyActivity", "Error: " + e.getMessage());
                });
    }

    private void savePropertyToFirestore(Property property) {
        db.collection("houses")
                .add(property)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(AddPropertyActivity.this, "Property added successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Close activity after successful submission
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AddPropertyActivity.this, "Error adding property", Toast.LENGTH_SHORT).show();
                    Log.e("AddPropertyActivity", "Error: " + e.getMessage());
                });
    }
}
