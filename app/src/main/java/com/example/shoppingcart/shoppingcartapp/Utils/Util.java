package com.example.shoppingcart.shoppingcartapp.Utils;
import android.content.Context;
import com.example.shoppingcart.shoppingcartapp.Database.AppDatabase;
import com.example.shoppingcart.shoppingcartapp.Model.Cart;
import com.example.shoppingcart.shoppingcartapp.Model.Product;
import java.util.List;
import java.util.Random;

public class Util {

    public static int generateRandomNumber(){
        Random random = new Random();
        return random.nextInt(1000);
    }

    public static float getPriceTotal(float pprice,int pquantity){
        float total;
        total=pprice*pquantity;
        return total;
    }

    public static void addProductToCart(Context context, Cart cart){
        Cart dbCart = AppDatabase.getInstance(context).cartDao().loadCart(cart.getCartId());

        //if cart is not stored in db, we will store it, else we will update its total and store it again.
        if(dbCart == null){
            AppDatabase.getInstance(context).cartDao().insert(cart);
        }else{
            dbCart.setPprice(dbCart.getPprice()+cart.getPprice()); //add the current pdt's price with old cart price value.
            dbCart.setPquantity(dbCart.getPquantity()+cart.getPquantity());
            AppDatabase.getInstance(context).cartDao().insert(cart);
        }
    }

    public static void updateCartData(Context context,int cartId){
        float price = 0.0f;
        int qty = 1;

        Cart dbCart = AppDatabase.getInstance(context).cartDao().loadCart(cartId);

        List<Product> products = AppDatabase.getInstance(context).productDao().loadProductByCartId(cartId);

        for (Product product :products) {
                price += product.getPrice();
                qty += product.getQty();
        }

        dbCart.setPprice(price);
        dbCart.setPquantity(qty);

        AppDatabase.getInstance(context).cartDao().insert(dbCart);
    }

    public static boolean validateForLastProduct(Context context,int cartId){
        boolean isCartEmpty = true;

        List<Product> products = AppDatabase.getInstance(context).productDao().loadProductByCartId(cartId);

        if(products!=null && products.size()>0){
            isCartEmpty = false;
        }
        return isCartEmpty;
    }
}
