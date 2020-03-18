package com.fadybassem.rafiqtask.data.models;

import com.google.gson.Gson;

import java.util.ArrayList;

public class LocationRequestModel {

    private String address;
    private String reference;
    private String referenceType;
    private String latitude;
    private String longitude;
    private ArrayList delivery;

    public LocationRequestModel(String address, String reference, String referenceType, String latitude, String longitude, ArrayList delivery) {
        this.address = address;
        this.reference = reference;
        this.referenceType = referenceType;
        this.latitude = latitude;
        this.longitude = longitude;
        this.delivery = delivery;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(String referenceType) {
        this.referenceType = referenceType;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String toString(LocationRequestModel model) {
        Gson gson = new Gson();
        String json = gson.toJson(model);
        return json;
    }

    public ArrayList getDelivery() {
        return delivery;
    }

    public void setDelivery(ArrayList delivery) {
        this.delivery = delivery;
    }
}
