package com.example.sakkahp;

public class Property {
    private String location;
    private String type;
    private String description;
    private String shortDescription;
    private String ownerName;
    private String contactNo;
    private String price;
    private String category;
    private String imageUrl;


    // Default constructor required for Firestore serialization
    public Property() { }

    // Constructor with all parameters
    public Property(String location, String type, String description, String shortDescription, String ownerName, String contactNo, String price, String category) {
        this.location = location;
        this.type = type;
        this.description = description;
        this.shortDescription = shortDescription;
        this.ownerName = ownerName;
        this.contactNo = contactNo;
        this.price = price;
        this.category = category;
        this.imageUrl = null; // or provide a default value if needed

    }

    // Getters and setters
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getShortDescription() { return shortDescription; }
    public void setShortDescription(String shortDescription) { this.shortDescription = shortDescription; }

    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    public String getContactNo() { return contactNo; }
    public void setContactNo(String contactNo) { this.contactNo = contactNo; }

    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }




}
