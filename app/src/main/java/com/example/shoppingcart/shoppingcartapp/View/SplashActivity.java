package com.example.shoppingcart.shoppingcartapp.View;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.view.Gravity;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.shoppingcart.shoppingcartapp.Controller.ShoppingCartApplication;
import com.example.shoppingcart.shoppingcartapp.Database.AppDatabase;
import com.example.shoppingcart.shoppingcartapp.Model.User;
import com.example.shoppingcart.shoppingcartapp.R;

public class SplashActivity extends AppCompatActivity {

    ProgressBar progressBar2;
    boolean isAllPermissionGranted = false;
    // The request code used in ActivityCompat.requestPermissions()
    // and returned in the Activity's onRequestPermissionsResult()
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progressBar2 = findViewById(R.id.progressBar2);

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
            isAllPermissionGranted = false;
        }else{
            isAllPermissionGranted = true;
        }

        Thread splashThread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (waited < 5000) {
                        sleep(100);
                        waited += 100;
                    }
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    Looper.prepare();
                    if (!isAllPermissionGranted) {
                        SplashActivity.this.runOnUiThread(() -> {
                            Toast toast = Toast.makeText(SplashActivity.this, "You must allow the permissions to continue", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                            toast.show();
                        });
                    }else{
                        User user = AppDatabase.getInstance(SplashActivity.this).userDao().loadByLoginStatus();
                        if(user==null){
                            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        }else{
                            ShoppingCartApplication.sharedInstance.setCurrentLoggedInUserId(user.uid);
                            startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                        }
                    }
                    Looper.loop();
                }
            }
        };
        splashThread.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0) {
            String permissionsDenied = "";
            for (String per : permissions) {
                if(grantResults[0] == PackageManager.PERMISSION_DENIED){
                    permissionsDenied += "\n" + per;
                    isAllPermissionGranted = false;
                }else{
                    isAllPermissionGranted = true;
                }
            }
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}



