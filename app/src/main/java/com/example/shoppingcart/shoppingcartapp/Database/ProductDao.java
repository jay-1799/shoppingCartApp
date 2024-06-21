package com.example.shoppingcart.shoppingcartapp.Database;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.shoppingcart.shoppingcartapp.Model.Product;

import java.util.List;

@Dao
public interface ProductDao {

    @Query("SELECT * FROM product")
    List<Product> loadAllProducts();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Product> products);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Product product);

    @Query("select * from product where id = :productId")
    Product loadProduct(int productId);

    @Query("select * from product where id = :productId")
    Product loadProductSync(int productId);

    @Query("select * from product where cartId = :cartId")
    List<Product> loadProductByCartId(int cartId);

    @Query("delete from product where id = :productId ")
    int deleteProduct(int productId);

    @Query("delete from product where id = :cartId ")
    int deleteProductsForCartId(int cartId);

}
