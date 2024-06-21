package com.example.shoppingcart.shoppingcartapp.View;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shoppingcart.shoppingcartapp.Controller.ShoppingCartApplication;
import com.example.shoppingcart.shoppingcartapp.Database.AppDatabase;
import com.example.shoppingcart.shoppingcartapp.Model.Cart;
import com.example.shoppingcart.shoppingcartapp.Model.Product;
import com.example.shoppingcart.shoppingcartapp.Model.User;
import com.example.shoppingcart.shoppingcartapp.R;
import com.example.shoppingcart.shoppingcartapp.Utils.Util;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class BillingActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG ="BillingActivity" ;
  //  private TextView textView1, textView2, textView3, textView4, GrandTotal;
    private Button PaymentEmail, shareViaMail,PaymentCard,PaymentNet;
    private TextView tvThanks;
    float GTT1=0;
    public String string="",string1;
    Cart cart;
    List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);

        shareViaMail = findViewById(R.id.shareViaMail);
        PaymentEmail = findViewById(R.id.Email);
        PaymentCard = findViewById(R.id.card);
        PaymentNet = findViewById(R.id.netBanking);

        tvThanks = findViewById(R.id.thanks);
        tvThanks.setVisibility(View.INVISIBLE);

        shareViaMail.setOnClickListener(this);
        PaymentEmail.setOnClickListener(this);
        PaymentCard.setOnClickListener(this);
        PaymentNet.setOnClickListener(this);

        cart = AppDatabase.getInstance(BillingActivity.this).cartDao().loadCart(ShoppingCartApplication.sharedInstance.getCurrentCartId());

        if(cart!=null){
            productList = AppDatabase.getInstance(BillingActivity.this).productDao().loadProductByCartId(cart.getCartId());
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(BillingActivity.this,HomeActivity.class));
        ShoppingCartApplication.sharedInstance.setCurrentCartId(-1);
    }

    @Override
    public void onClick(View v) {
        if(v==PaymentCard){
            AlertDialog.Builder builder = new AlertDialog.Builder(BillingActivity.this);
            builder.setTitle("Payment");

            LinearLayout layout = new LinearLayout(BillingActivity.this);
            layout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
            params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);

            final EditText inputCardNo = new EditText(BillingActivity.this);
            inputCardNo.setInputType(InputType.TYPE_CLASS_NUMBER);
            inputCardNo.setHint("Card Number");
            inputCardNo.setSingleLine();

            final EditText inputCardDate = new EditText(BillingActivity.this);
            inputCardDate.setInputType(InputType.TYPE_CLASS_DATETIME);
            inputCardDate.setHint("Expiry Date");
            inputCardDate.setSingleLine();

            final EditText inputCvv = new EditText(BillingActivity.this);
            inputCvv.setInputType(InputType.TYPE_CLASS_NUMBER);
            inputCvv.setHint("CVV");
            inputCvv.setSingleLine();

            inputCardNo.setLayoutParams(params);
            inputCardDate.setLayoutParams(params);
            inputCvv.setLayoutParams(params);

            layout.addView(inputCardNo);
            layout.addView(inputCardDate);
            layout.addView(inputCvv);

            builder.setView(layout);
            builder.setMessage("Debit/Credit Card")
                    .setCancelable(false)
                    .setPositiveButton("OK", (dialog, id) -> {
                        if(!inputCardDate.getText().toString().equals("")&&
                                !inputCardNo.getText().toString().equals("") && !inputCvv.getText().toString().equals("")){
                            dialog.cancel();
                            tvThanks.setVisibility(View.VISIBLE);
                        }else{
                            Toast.makeText(BillingActivity.this,"Please fill all the details",Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton("Cancel", (dialog, id) -> {
                        dialog.cancel();
                        Intent intent = getIntent();
                        startActivity(intent);
                    });

            AlertDialog alert1 = builder.create();
            alert1.show();
        }

        if(v==PaymentNet){
            AlertDialog.Builder builder = new AlertDialog.Builder(BillingActivity.this);
            builder.setTitle("Payment");

            LinearLayout layout = new LinearLayout(BillingActivity.this);
            layout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
            params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);

            final EditText inputUserName = new EditText(BillingActivity.this);
            inputUserName.setInputType(InputType.TYPE_CLASS_NUMBER);
            inputUserName.setHint("User Name");
            inputUserName.setSingleLine();

            final EditText inputPasswrd = new EditText(BillingActivity.this);
            inputPasswrd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            inputPasswrd.setHint("Password");
            inputPasswrd.setSingleLine();

            inputUserName.setLayoutParams(params);
            inputPasswrd.setLayoutParams(params);

            layout.addView(inputUserName);
            layout.addView(inputPasswrd);

            builder.setView(layout);
            builder.setMessage("Net Banking")
                    .setCancelable(false)
                    .setPositiveButton("OK", (dialog, id) -> {
                        if(!inputUserName.getText().toString().equals("") && !inputPasswrd.getText().toString().equals("")){
                            dialog.cancel();
                            tvThanks.setVisibility(View.VISIBLE);
                        }else{
                            Toast.makeText(BillingActivity.this,"Please fill all the details",Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton("Cancel", (dialog, id) -> {
                        dialog.cancel();//
                    });

            AlertDialog alert1 = builder.create();
            alert1.show();
        }

        if(v== shareViaMail){
            Toast.makeText(getApplicationContext(), "Please Wait for a Moment", Toast.LENGTH_SHORT).show();
            float totalPrice;
            if (productList != null) {
                for (Product product : productList) {
                    totalPrice = Util.getPriceTotal(product.getPrice(), product.getQty());
                    string = string + (product.getName() + " | " + product.getPrice() + " | " + product.getQty() + " | " + totalPrice + "\n");
                    GTT1 = GTT1 + totalPrice;
                    string1 = "\n\nTotal: " + GTT1 + " Rs.";
                }
            }
            User user = AppDatabase.getInstance(BillingActivity.this).userDao().loadByLoginStatus();
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            if(user!=null){
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{user.emailId});
            }
            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_SUBJECT, "CASH MEMO-ShoppingCart");
            intent.putExtra(Intent.EXTRA_TEXT, "\n\nCASH MEMO\n\nProduct | Rate | Quantity | Value\n\n" + string + "\n" + string1);
            try {
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
//                                startActivity(Intent.createChooser(i, "Send mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(BillingActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
        }

        if(v== PaymentEmail) {
            AlertDialog.Builder builder = new AlertDialog.Builder(BillingActivity.this);
            builder.setTitle("Collect from location");

            LinearLayout layout = new LinearLayout(BillingActivity.this);
            layout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
            params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);

            final EditText inputLocation = new EditText(BillingActivity.this);
            inputLocation.setInputType(InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE);
            inputLocation.setHint("Address");

            final EditText inputContactNo = new EditText(BillingActivity.this);
            inputContactNo.setInputType(InputType.TYPE_CLASS_NUMBER);
            inputContactNo.setHint("Contact No");
            inputContactNo.setSingleLine();

            inputLocation.setLayoutParams(params);
            inputContactNo.setLayoutParams(params);

            layout.addView(inputLocation);
            layout.addView(inputContactNo);

            builder.setView(layout);
            builder.setCancelable(false)
                    .setPositiveButton("OK", (dialog, id) -> {
                        if(!inputLocation.getText().toString().equals("") && !inputContactNo.getText().toString().equals("")){
                            tvThanks.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(), "Please Wait for a Moment", Toast.LENGTH_SHORT).show();
                            float totalPrice;
                            if (productList != null) {
                                for (Product product : productList) {
                                    totalPrice = Util.getPriceTotal(product.getPrice(), product.getQty());
                                    string = string + (product.getName() + " | " + product.getPrice() + " | " + product.getQty() + " | " + totalPrice + "\n");
                                    GTT1 = GTT1 + totalPrice;
                                    string1 = "\n\nTotal: " + GTT1 + " Rs.";
                                }
                            }
                            User user = AppDatabase.getInstance(BillingActivity.this).userDao().loadByLoginStatus();
                            Intent intent = new Intent(Intent.ACTION_SENDTO);
                            if(user!=null){
                                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{user.emailId});
                            }
                            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                            intent.putExtra(Intent.EXTRA_SUBJECT, "CASH MEMO-ShoppingCart");
                            //
                            intent.putExtra(Intent.EXTRA_TEXT, "\n\nCASH MEMO\n\nProduct | Rate | Quantity | Value\n\n" + string + "\n" + string1);
                            try {
                                if (intent.resolveActivity(getPackageManager()) != null) {
                                    startActivity(intent);
                                }
//                                startActivity(Intent.createChooser(i, "Send mail..."));
                            } catch (android.content.ActivityNotFoundException ex) {
                                Toast.makeText(BillingActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(BillingActivity.this,"Please fill all the details",Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton("Cancel", (dialog, id) -> {
                        dialog.cancel();
                    });

            AlertDialog alert1 = builder.create();
            alert1.show();
        }
    }
}






