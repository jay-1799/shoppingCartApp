package com.example.shoppingcart.shoppingcartapp.Model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Product{

    @PrimaryKey
    private int id;
    private int cartId; //cartId will be used to keep reference of pdts in cart.

    private String name;
    private String description;
    private float price;
    private int qty;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getQty(){ return qty; }

    public void setQty(int qty){
        this.qty = qty;
    }

    public int getCartId(){ return cartId; }

    public void setCartId(int cartId){
        this.cartId = cartId;
    }

    public Product() {
    }

    @Ignore
    public Product(int id, String name, String description, float price,int cartId,int qty) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.cartId = cartId;
        this.qty = qty;
    }

    public Product(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.cartId = product.getCartId();
        this.qty = product.getQty();
    }
}
