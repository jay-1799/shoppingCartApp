package com.example.shoppingcart.shoppingcartapp.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.shoppingcart.shoppingcartapp.Model.Cart;

import java.util.List;

@Dao
public interface CartDao {
    @Query("SELECT * FROM cart")
    List<Cart> loadAllCartEntries();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Cart> cartList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Cart cart);

    @Query("select * from cart where cartId = :cartId")
    Cart loadCart(int cartId);

    @Query("select * from cart where paymentStatus = 0") //0 = false,1= true
    Cart loadCartByPaymentStatus();

    @Query("delete from cart where cartId = :cartId ")
    int deleteCart(int cartId);
}

