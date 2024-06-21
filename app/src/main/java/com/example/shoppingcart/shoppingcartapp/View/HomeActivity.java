package com.example.shoppingcart.shoppingcartapp.View;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.shoppingcart.shoppingcartapp.Controller.ShoppingCartApplication;
import com.example.shoppingcart.shoppingcartapp.Database.AppDatabase;
import com.example.shoppingcart.shoppingcartapp.Model.User;
import com.example.shoppingcart.shoppingcartapp.R;
import com.google.android.material.navigation.NavigationView;

import static com.example.shoppingcart.shoppingcartapp.Utils.Constants.BUNDL_CART_ID;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener{

    boolean doubleBackToExitPressedOnce = false;
//    private Button buttonShop;
    private TextView tapToScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.nav_view);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        tapToScan = findViewById(R.id.tapToScan);
        tapToScan.setOnClickListener(this);

       navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;}

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce=false, 2000);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(this, HomeActivity.class));
        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(this, SignUpActivity.class));
        }
//        else if (id == R.id.nav_about) {
////            startActivity(new Intent(this, Aboutus.class));
//        }
        else if(id==R.id.nav_list){
            if(ShoppingCartApplication.sharedInstance.getCurrentCartId() == -1){
                Toast.makeText(HomeActivity.this,"Cart is empty!!!",Toast.LENGTH_SHORT).show();
            }else{
                Intent intent = new Intent(this,CartActivity.class);
                intent.putExtra(BUNDL_CART_ID,ShoppingCartApplication.sharedInstance.getCurrentCartId());
                startActivity(intent);
            }
        }
        else if (id == R.id.nav_logout) {
            //just update here, the status of current log in user & navigate to Login Activity.
            User user = AppDatabase.getInstance(HomeActivity.this).userDao().loadByLoginStatus();
            if(user!=null){
                user.isLoggedIn = false;
                AppDatabase.getInstance(HomeActivity.this).userDao().update(user);
            }
            ShoppingCartApplication.sharedInstance.setCurrentLoggedInUserId(-1); //reset the value.
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        if(v == tapToScan){
            startActivity(new Intent(this, ContinuousCaptureActivity.class));
        }
    }
}
