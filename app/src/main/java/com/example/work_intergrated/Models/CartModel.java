package com.example.work_intergrated.Models;

import java.io.Serializable;

public class CartModel implements Serializable {
    String documentId;
    String ProductName;
    String ProductImage;
    String ProductPrice;
    String CurrentDate;
    String TotalQuantity;
    String CurrentTime;
    int TotalPrice;

    public CartModel() {
    }

    public CartModel(String documentId, String productName, String productImage, String productPrice, String currentDate, String totalQuantity, String currentTime, int totalPrice) {
        ProductName = productName;
        this.documentId = documentId;
        ProductImage = productImage;
        ProductPrice = productPrice;
        CurrentDate = currentDate;
        TotalQuantity = totalQuantity;
        CurrentTime = currentTime;
        TotalPrice = totalPrice;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductImage() {
        return ProductImage;
    }
    public String getDocumentId() {
        return documentId;
    }
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public void setProductImage(String productImage) {
        ProductImage = productImage;
    }

    public String getProductPrice() {
        return ProductPrice;
    }

    public void setProductPrice(String productPrice) {
        ProductPrice = productPrice;
    }

    public String getCurrentDate() {
        return CurrentDate;
    }

    public void setCurrentDate(String currentDate) {
        CurrentDate = currentDate;
    }

    public String getTotalQuantity() {
        return TotalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        TotalQuantity = totalQuantity;
    }

    public String getCurrentTime() {
        return CurrentTime;
    }

    public void setCurrentTime(String currentTime) {
        CurrentTime = currentTime;
    }

    public int getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        TotalPrice = totalPrice;
    }
}
