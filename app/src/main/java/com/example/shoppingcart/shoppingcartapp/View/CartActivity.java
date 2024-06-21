package com.example.shoppingcart.shoppingcartapp.View;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shoppingcart.shoppingcartapp.Database.AppDatabase;
import com.example.shoppingcart.shoppingcartapp.Model.Cart;
import com.example.shoppingcart.shoppingcartapp.Model.Product;
import com.example.shoppingcart.shoppingcartapp.R;
import com.example.shoppingcart.shoppingcartapp.Utils.Util;

import java.util.List;

import static com.example.shoppingcart.shoppingcartapp.Utils.Constants.BUNDL_CART_ID;

public class CartActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "CartActivity";
    private TextView GrandTotal;
    private Button btnCheckout,btnClearCart;
    private android.widget.TableLayout TableLayout;
    float GTT = 0;
    String string1;
    private int cartId = -1;
    private Cart cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carting);
        btnCheckout = findViewById(R.id.Button02);
        btnClearCart = findViewById(R.id.btnClearCart);

        GrandTotal = findViewById(R.id.GrandTotal);
        TableLayout = findViewById(R.id.myTableLayout);

        btnCheckout.setOnClickListener(this);
        btnClearCart.setOnClickListener(this);

        Bundle b = getIntent().getExtras();
        if(b!=null){
            cartId = (int) b.get((BUNDL_CART_ID));
        }
        if(cartId!=-1){
            //if cartId is retrieved, fetch the cart value with it.
            cart = AppDatabase.getInstance(CartActivity.this).cartDao().loadCart(cartId);
        }

        float finalPrice = 0.0f;
        List<Product> products = AppDatabase.getInstance(CartActivity.this).productDao().loadProductByCartId(cartId);

        for (int i =0;i<products.size();i++){
            final Product product = products.get(i);

            final TableRow tableRow1 = new TableRow(CartActivity.this);
            final TextView textView1 = new TextView(CartActivity.this);
            TextView textView2 = new TextView(CartActivity.this);
            TextView textView3 = new TextView(CartActivity.this);
            TextView textView4 = new TextView(CartActivity.this);

            textView1.setText(product.getName());
            textView3.setText(product.getPrice() + "");
            textView2.setText(product.getQty() + "");
            finalPrice = Util.getPriceTotal(product.getPrice(),product.getQty());
            textView4.setText(finalPrice+"");

            textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            textView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            textView3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            textView4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

            tableRow1.addView(textView1);
            tableRow1.addView(textView3);
            tableRow1.addView(textView2);
            tableRow1.addView(textView4);

            tableRow1.setPadding(0,10,0,10);
            GTT = GTT + finalPrice;
            GrandTotal.setText("Total: " + GTT + " Rs.");
            TableLayout.addView(tableRow1, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            tableRow1.setOnClickListener(v -> {
//                final float price = product.getPrice();
                final String pname = product.getName();
                AlertDialog dialog = new AlertDialog.Builder(CartActivity.this)
                        .setCancelable(true)
                        .setTitle(pname)
                        .setMessage(string1)
                        .setPositiveButton("Delete Item", (dialog12, which) -> {
                            Toast.makeText(CartActivity.this, pname + " Deleted from Cart", Toast.LENGTH_SHORT).show();
                            //delete product from the cart.
                            AppDatabase.getInstance(CartActivity.this).productDao().deleteProduct(product.getId());
                            //if it was the last product in cart, than no need to update cart data
                            if(Util.validateForLastProduct(CartActivity.this,cartId)){
                                //finish this activity in this case. will need to delete the cart data in this case.
                                AppDatabase.getInstance(CartActivity.this).cartDao().deleteCart(cartId);
                                finish();
                            }
                            else{
                                //update cart data here.
                                finish();
                                Util.updateCartData(CartActivity.this,cartId);
                                Intent intent = getIntent();
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Modify Quantity", (dialog13, id) -> {
                            AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                            builder.setTitle("Enter New Quantity");

                            final EditText input = new EditText(CartActivity.this);
                            input.setInputType(InputType.TYPE_CLASS_NUMBER);
                            input.setSingleLine();

                            FrameLayout container = new FrameLayout(CartActivity.this);
                            FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                            params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);

                            input.setLayoutParams(params);
                            container.addView(input);
                            builder.setView(container);
                            builder.setNeutralButton("Submit", (dialog131, which) -> {
                                String pquantity1 = input.getText().toString().trim();
                                int pquantity = Integer.parseInt(pquantity1);

                                product.setQty(pquantity);
                                AppDatabase.getInstance(CartActivity.this).productDao().insert(product);
                                Util.updateCartData(CartActivity.this,cartId);
                                finish();
                                Intent intent = getIntent();
                                startActivity(intent);
                            });
                            builder.create();
                            builder.show();
                        }).setNeutralButton("Cancel", (dialog1, id) -> {

                        })
                        .create();
                dialog.show();
            });
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnCheckout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
            builder.setTitle("Confirmation to Proceed");
            builder.setMessage("You Won't be able to do changes again in Cart")
                    .setCancelable(false)
                    .setPositiveButton("OK", (dialog, id) -> startActivity(new Intent(CartActivity.this, BillingActivity.class)))
                    .setNegativeButton("Cancel", (dialog, id) -> {
                        dialog.cancel();
                        Intent intent = getIntent();
                        startActivity(intent);
                    });

            AlertDialog alert1 = builder.create();
            alert1.show();
        }else if(v==btnClearCart)
        {
            AlertDialog dialog = new AlertDialog.Builder(CartActivity.this)
                    .setCancelable(true)
                    .setTitle("Confirmation")
                    .setMessage("Please Confirm to Clear Your Cart")
                    .setPositiveButton("Proceed", (dialog12, id12) -> {
                        //delete all products as well with the same cartId
                        AppDatabase.getInstance(CartActivity.this).productDao().deleteProductsForCartId(cartId);
                        //finish this activity in this case. will need to delete the cart data in this case.
                        AppDatabase.getInstance(CartActivity.this).cartDao().deleteCart(cartId);
                        //finish activity
                        finish();
                    })
                    .setNegativeButton("Cancel", (dialog1, id1) -> {
                        finish();
                        startActivity(new Intent(CartActivity.this, CartActivity.class));
                    })
                    .create();
            dialog.show();

        }
    }
}
