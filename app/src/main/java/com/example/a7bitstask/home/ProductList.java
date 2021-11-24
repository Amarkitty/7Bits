package com.example.a7bitstask.home;

public class ProductList {
    private String image = "";
    private int price;
    private String name = "";


    public ProductList() {
    }

    public ProductList(String image, int price, String name) {
        this.image = image;
        this.price = price;
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
