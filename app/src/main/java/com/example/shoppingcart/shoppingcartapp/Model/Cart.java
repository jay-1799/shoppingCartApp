package com.example.shoppingcart.shoppingcartapp.Model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Cart {

    @PrimaryKey
    private int cartId;

    private float pprice;
    private int pquantity;
    private boolean paymentStatus;
    private int paymentMethod;

    public Cart() {

    }

    @Ignore
    public Cart(int cartId,float pprice, int pquantity,boolean isPaymentDone,int paymentMethod) {
        this.cartId = cartId;
        this.pprice = pprice;
        this.pquantity=pquantity;
        this.paymentStatus = isPaymentDone;
        this.paymentMethod = paymentMethod;
    }

    public Cart(Cart cart) {
        this.cartId = cart.getCartId();
        this.pprice = cart.getPprice();
        this.pquantity = cart.getPquantity();
        this.paymentStatus = cart.getPaymentStatus();
        this.paymentMethod = cart.getPaymentMethod();
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public float getPprice() {
        return pprice;
    }

    public void setPprice(float pprice) {
        this.pprice = pprice;
    }

    public int getPquantity() {
        return pquantity;
    }

    public void setPquantity(int pquantity) {
        this.pquantity = pquantity;
    }

    public boolean getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public int getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(int paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}

