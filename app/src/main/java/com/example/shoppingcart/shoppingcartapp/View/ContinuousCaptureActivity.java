package com.example.shoppingcart.shoppingcartapp.View;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shoppingcart.shoppingcartapp.Controller.ShoppingCartApplication;
import com.example.shoppingcart.shoppingcartapp.Database.AppDatabase;
import com.example.shoppingcart.shoppingcartapp.Model.Cart;
import com.example.shoppingcart.shoppingcartapp.Model.Product;
import com.example.shoppingcart.shoppingcartapp.R;
import com.example.shoppingcart.shoppingcartapp.Utils.Constants;
import com.example.shoppingcart.shoppingcartapp.Utils.Util;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.example.shoppingcart.shoppingcartapp.Utils.Constants.BUNDL_CART_ID;

/**
 * This sample performs continuous scanning, displaying the barcode and source image whenever
 * a barcode is scanned.
 */
public class ContinuousCaptureActivity extends Activity {
    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    private String lastText;
    int cartId;

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if(result.getText() == null || result.getText().equals(lastText)) {
                // Prevent duplicate scans
                return;
            }
            lastText = result.getText();
            barcodeView.setStatusText(result.getText());
            beepManager.playBeepSoundAndVibrate();

            //we will pause the barcodeView,once scan is completed, as we will display the dialog.
            barcodeView.pause();
            //Added preview of scanned barcode
            ImageView imageView = findViewById(R.id.barcodePreview);
            imageView.setImageBitmap(result.getBitmapWithResultPoints(Color.YELLOW));

            //add here the product to the cart.
            validateAndProcessQR(lastText);
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.continuous_scan);

        barcodeView = findViewById(R.id.barcode_scanner);
        Collection<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39);
        barcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats));
        barcodeView.initializeFromIntent(getIntent());
        barcodeView.decodeContinuous(callback);

        beepManager = new BeepManager(this);

        cartId = Util.generateRandomNumber();
    }

    @Override
    protected void onResume() {
        super.onResume();
        barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        barcodeView.pause();
    }

    public void pause(View view) {
        barcodeView.pause();
    }

    public void resume(View view) {
        barcodeView.resume();
    }

    public void triggerScan(View view) {
        barcodeView.decodeSingle(callback);
    }

    public void proceedToCart(View view) {
        try {
            if(ShoppingCartApplication.sharedInstance.getCurrentCartId()==-1){
                Toast.makeText(ContinuousCaptureActivity.this,"Cart is empty!!!",Toast.LENGTH_SHORT).show();
            }else{
                Intent intent = new Intent(this,CartActivity.class);
                intent.putExtra(BUNDL_CART_ID,cartId);
                startActivity(intent);
            }
        }catch (Exception e){
            Log.d(ContinuousCaptureActivity.class.getSimpleName(),e.getMessage());
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    /*============== QR Code Processing methods =================*/

    //Correct format of QR is : pdtName@Price
    private void validateAndProcessQR(String resultText){
        String pdtName;
        String pdtPrice;
        if(resultText.contains("@")){
            pdtName = resultText.split("@")[0];
            pdtPrice = resultText.split("@")[1];

            proceedForCorrectQR(pdtName,pdtPrice);
        }else{
            proceedForInvalidQR();
        }
    }

    void proceedForInvalidQR(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ContinuousCaptureActivity.this);
        builder.setCancelable(false);
        builder.setMessage("Invalid QR Code")
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.cancel();
                    barcodeView.resume(); //resume scan on cancel.
                });
        builder.create();
        builder.show();
    }

    void proceedForCorrectQR(String pdtName,String pdtPrice){
        // show the scanner result into dialog box.
        AlertDialog.Builder builder = new AlertDialog.Builder(ContinuousCaptureActivity.this);
        builder.setTitle("Product Description");

        final EditText input = new EditText(ContinuousCaptureActivity.this);
        input.setText("1");
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setSingleLine();

        FrameLayout container = new FrameLayout(ContinuousCaptureActivity.this);
        FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        input.setLayoutParams(params);
        container.addView(input);
        builder.setView(container);
        builder.setMessage(pdtName + "   " + pdtPrice + " Rs.\n\nQuantity (in digits) ")
                .setCancelable(false)
                .setPositiveButton("Add to Cart", (dialog, id) -> {
                    int pqty = -1;
                    if (!input.getText().toString().trim().equals("")) {
                        pqty =Integer.parseInt(input.getText().toString().trim());
                    }else{
                        pqty = 1;
                    }

                    float price = Util.getPriceTotal(Float.parseFloat(pdtPrice),pqty);

                    //Prepare product model to store in db.
                    Product product = new Product();
                    product.setId(Util.generateRandomNumber());
                    product.setName(pdtName);
                    product.setQty(pqty);
                    product.setPrice(Float.parseFloat(pdtPrice));
                    product.setCartId(cartId);
                    AppDatabase.getInstance(ContinuousCaptureActivity.this).productDao().insert(product);

                    Toast.makeText(ContinuousCaptureActivity.this, pdtName + " Added to Cart", Toast.LENGTH_SHORT).show();

                    Cart cart = new Cart(cartId,price,pqty,false,Constants.PAYMENT_METHOD_CASH ); //insert object in cart.
                    Util.addProductToCart(ContinuousCaptureActivity.this,cart);
                    ShoppingCartApplication.sharedInstance.setCurrentCartId(cartId);

                    dialog.cancel();
                    barcodeView.resume();
                })
                .setNegativeButton("Not Interested", (dialog, id) -> {
                    dialog.cancel();
                    barcodeView.resume();
                });

        AlertDialog alert1 = builder.create();
        alert1.show();
    }
}
