package com.example.sakkahp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sakkahp.Property;
import com.example.sakkahp.R;
import com.example.sakkahp.listeners.ItemListener;

import java.util.List;

public class PropertyHomeAdapter extends RecyclerView.Adapter<PropertyHomeAdapter.ViewHolder> {

    private Context context;
    private List<Property> propertyList;
    private ItemListener itemListener;

    public PropertyHomeAdapter(Context context, List<Property> propertyList, ItemListener itemListener) {
        this.context = context;
        this.propertyList = propertyList;
        this.itemListener = itemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_property, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Property property = propertyList.get(position);

        // Load image using Glide
        Glide.with(context).load(property.getImageUrl()).into(holder.propertyImage);

        // Set onClickListener for property image
        holder.propertyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category = propertyList.get(position).getCategory();
                // Create an intent to navigate to the ListingsActivity
                Intent intent = new Intent(context, com.example.sakkahp.screens.ListingsActivity.class);
                intent.putExtra("category", category); // Pass the category to the ListingsActivity
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return propertyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView propertyImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            propertyImage = itemView.findViewById(R.id.property_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemListener.OnItemPosition(getAdapterPosition());
                }
            });
        }
    }
}
