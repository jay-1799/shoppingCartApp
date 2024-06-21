package com.example.shoppingcart.shoppingcartapp.Controller;
import android.app.Application;

public class ShoppingCartApplication extends Application {

    int currentCartId = -1;
    int currentLoggedInUserId = -1;
    public static ShoppingCartApplication sharedInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sharedInstance = this;
    }

    public int getCurrentCartId() {
        return currentCartId;
    }

    public void setCurrentCartId(int currentCartId) {
        this.currentCartId = currentCartId;
    }

    public int getCurrentLoggedInUserId() {
        return currentLoggedInUserId;
    }

    public void setCurrentLoggedInUserId(int currentLoggedInUserId) {
        this.currentLoggedInUserId = currentLoggedInUserId;
    }
}
