package com.example.shoppingcart.shoppingcartapp.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {

    @PrimaryKey
    public int uid;

    @ColumnInfo(name = "user_name")
    public String userName;

    @ColumnInfo(name = "email_id")
    public String emailId;

    @ColumnInfo(name = "password")
    public String password;

    @ColumnInfo(name = "isLoggedIn")
    public boolean isLoggedIn;
}
