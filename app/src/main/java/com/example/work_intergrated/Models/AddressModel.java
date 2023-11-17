package com.example.work_intergrated.Models;

public class AddressModel {
    String Address;
    boolean isSelected;

    public AddressModel() {
    }

    public AddressModel(String address) {
        Address = address;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
