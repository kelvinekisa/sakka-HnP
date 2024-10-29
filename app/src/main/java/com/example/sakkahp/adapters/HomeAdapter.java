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
import com.example.sakkahp.Property;
import com.example.sakkahp.R;
import com.example.sakkahp.listeners.ItemListener;
import com.example.sakkahp.model.Item; // Assuming you have an Item class

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_PROPERTY = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    private Context context;
    private List<Object> items;
    private ItemListener itemListener;

    public HomeAdapter(Context context, List<Object> items, ItemListener itemListener) {
        this.context = context;
        this.items = items;
        this.itemListener = itemListener;
    }

    @Override
    public int getItemViewType(int position) {
        Object item = items.get(position);
        if (item instanceof Property) {
            return VIEW_TYPE_PROPERTY;
        } else if (item instanceof Item) {
            return VIEW_TYPE_ITEM;
        } else {
            throw new IllegalArgumentException("Invalid item type");
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_PROPERTY) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_property, parent, false);
            return new PropertyViewHolder(view);
        } else if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.top_deals, parent, false);
            return new ItemViewHolder(view);
        } else {
            throw new IllegalArgumentException("Invalid view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object item = items.get(position);
        if (holder instanceof PropertyViewHolder) {
            Property property = (Property) item;
            PropertyViewHolder propertyHolder = (PropertyViewHolder) holder;
            // Bind property data here
            // Example: propertyHolder.propertyTitle.setText(property.getTitle());
        } else if (holder instanceof ItemViewHolder) {
            Item itemData = (Item) item;
            ItemViewHolder itemHolder = (ItemViewHolder) holder;
            itemHolder.price.setText(itemData.getPrice());
            itemHolder.location.setText(itemData.getLocation());
            itemHolder.shortDescription.setText(itemData.getShortDescription());

            // Load background image using Glide
            Glide.with(context).load(itemData.getImageUri()).into(itemHolder.backgroundImageView);

            itemHolder.itemView.setOnClickListener(v -> itemListener.OnItemPosition(position));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class PropertyViewHolder extends RecyclerView.ViewHolder {
        // Define views for the property item layout
        public PropertyViewHolder(View itemView) {
            super(itemView);
            // Initialize views here
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView price;
        private TextView location;
        private TextView shortDescription;
        private ImageView backgroundImageView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            price = itemView.findViewById(R.id.price);
            location = itemView.findViewById(R.id.location);
            shortDescription = itemView.findViewById(R.id.short_description);
            backgroundImageView = itemView.findViewById(R.id.bg);
        }
    }
}
