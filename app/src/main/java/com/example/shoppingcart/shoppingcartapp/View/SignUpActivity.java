package com.example.shoppingcart.shoppingcartapp.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shoppingcart.shoppingcartapp.Controller.ShoppingCartApplication;
import com.example.shoppingcart.shoppingcartapp.Database.AppDatabase;
import com.example.shoppingcart.shoppingcartapp.Model.User;
import com.example.shoppingcart.shoppingcartapp.R;
import com.example.shoppingcart.shoppingcartapp.Utils.Constants;
import com.example.shoppingcart.shoppingcartapp.Utils.Util;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonSignUp;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextUsername;
    private TextView textViewLogIn;

    private ProgressDialog progressDialog;   //progress loading
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        appDatabase = AppDatabase.getInstance(this);

        progressDialog = new ProgressDialog(this);

        buttonSignUp = findViewById(R.id.buttonSignUp);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextUsername = findViewById(R.id.editTextUsername);
        textViewLogIn = findViewById(R.id.textViewLogIn);

        buttonSignUp.setOnClickListener(this);
        textViewLogIn.setOnClickListener(this);

        if(ShoppingCartApplication.sharedInstance.getCurrentLoggedInUserId()!=-1){
            User user = appDatabase.userDao().loadUserById(ShoppingCartApplication.sharedInstance.getCurrentLoggedInUserId());

            //If user is not null, means user has logged in & we need to display Profile page, so he can update profile
            if(user!=null){
                editTextEmail.setText(user.emailId);
                editTextPassword.setText(user.password);
                editTextUsername.setText(user.userName);

                buttonSignUp.setText("Update");
                textViewLogIn.setVisibility(View.INVISIBLE); //and change the login text's visibility
            }
        }
    }


    private void registerUser(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String userName = editTextUsername.getText().toString().trim();

        int length = editTextPassword.getText().length();

        if(TextUtils.isEmpty(email)){
            //email is empty
            Toast.makeText(this, Constants.EMAIL_ID_VALIDATION, Toast.LENGTH_SHORT).show();
            //stop the function
            return;
        }

        if(TextUtils.isEmpty(password)){
            //password is empty
            Toast.makeText(this,Constants.PASSWORD_VALIDATION, Toast.LENGTH_SHORT).show();
            //stop the function
            return;
        }
        if(TextUtils.isEmpty(userName)){
            //password is empty
            Toast.makeText(this,"Please Enter UserName",Toast.LENGTH_SHORT).show();
            //stop the function
            return;
        }

        if(length<6){
            Toast.makeText(this,Constants.INVALID_PASSWORD_VALIDATION, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!validEmail(email)) {
            Toast.makeText(SignUpActivity.this,"Enter valid e-mail!",Toast.LENGTH_LONG).show();
            return;
        }
        //if validations are ok
        //show progress bar

        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        User user = appDatabase.userDao().findByEmail(email);
        if(user == null)
        {
            user = new User();
            user.uid = Util.generateRandomNumber();
            user.emailId = email;
            user.password = password;
            user.userName = userName;
            user.isLoggedIn = true;

            appDatabase.userDao().insert(user);
            progressDialog.hide();
            Toast.makeText(this,"Registered Successfully",Toast.LENGTH_SHORT).show();
            ShoppingCartApplication.sharedInstance.setCurrentLoggedInUserId(user.uid);
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
        }else{
            progressDialog.hide();
            if(ShoppingCartApplication.sharedInstance.getCurrentLoggedInUserId()!=-1){
                user = appDatabase.userDao().loadUserById(ShoppingCartApplication.sharedInstance.getCurrentLoggedInUserId());

                //If user is not null, means user has logged in & we need to display Profile page, so he can update profile
                if(user!=null){
                    user.emailId = email;
                    user.password = password;
                    user.userName = userName;
                    user.isLoggedIn = true;

                    appDatabase.userDao().update(user);

                    Toast.makeText(this,"Updated Successfully",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                }
            }else
                Toast.makeText(this,"Could'nt Register, Email already exist",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        if(v == buttonSignUp){
            registerUser();
        }

        if(v == textViewLogIn){
            //open signin activity
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    private boolean validEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
}





