package com.example.sakkahp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sakkahp.R;
import com.example.sakkahp.Property; // Correct import

import java.util.List;

public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.ViewHolder> {

    private Context context;
    private List<Property> propertyList;

    public PropertyAdapter(Context context, List<Property> propertyList) {
        this.context = context;
        this.propertyList = propertyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_property, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Property property = propertyList.get(position);

        holder.propertyTitle.setText(property.getShortDescription()); // Assuming 'shortDescription' is used as title
        holder.propertyDescription.setText(property.getDescription());
        holder.propertyLocation.setText(property.getLocation());
        holder.propertyPrice.setText(property.getPrice());

        // Load image using Glide
        Glide.with(context).load(property.getImageUrl()).into(holder.propertyImage); // Use 'getImageUrl()'
    }

    @Override
    public int getItemCount() {
        return propertyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView propertyImage;
        private TextView propertyTitle;
        private TextView propertyDescription;
        private TextView propertyLocation;
        private TextView propertyPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            propertyImage = itemView.findViewById(R.id.property_image);
            propertyTitle = itemView.findViewById(R.id.property_title);
            propertyDescription = itemView.findViewById(R.id.property_description);
            propertyLocation = itemView.findViewById(R.id.property_location);
            propertyPrice = itemView.findViewById(R.id.property_price);
        }
    }
}
