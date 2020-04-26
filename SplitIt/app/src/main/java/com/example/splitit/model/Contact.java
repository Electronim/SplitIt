package com.example.splitit.model;

import java.io.Serializable;

public class Contact implements Serializable {
    public String id;
    public String name;
    public String phoneNumber;
    public Boolean isSelected;

    public void changeSelectedState() {
        isSelected = !isSelected;
    }

    public Contact(String id, String name, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.isSelected = false;
    }
}
