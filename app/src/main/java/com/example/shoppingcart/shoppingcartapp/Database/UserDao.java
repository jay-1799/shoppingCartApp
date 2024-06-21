package com.example.shoppingcart.shoppingcartapp.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.shoppingcart.shoppingcartapp.Model.User;

import java.util.List;

//Dao = Data Access Object
//Used as abstraction between db class and model class.
@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE uid IN (:userId)")
    User loadUserById(int userId);

    @Query("SELECT * FROM user WHERE user_name LIKE :userName AND " +
            "email_id LIKE :emailId LIMIT 1")
    User findByName(String userName, String emailId);

    @Query("SELECT * FROM user WHERE email_id LIKE :emailId LIMIT 1")
    User findByEmail(String emailId);

    @Query("SELECT * FROM user WHERE isLoggedIn = 1 LIMIT 1")
    User loadByLoginStatus();

    @Insert
    void insertAll(User... users);

    @Insert
    void insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);
}
