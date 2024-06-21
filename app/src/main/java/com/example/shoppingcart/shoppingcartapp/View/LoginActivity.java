package com.example.shoppingcart.shoppingcartapp.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonLogin;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignup;
    private ProgressDialog progressDialog;
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        appDatabase = AppDatabase.getInstance(this);
        progressDialog = new ProgressDialog(this);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewSignup = findViewById(R.id.textViewSignUp);

        buttonLogin.setOnClickListener(this);
        textViewSignup.setOnClickListener(this);
    }

    private void userLogin(){
        String email= editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

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
        //if validations are ok
        //show progress bar

        progressDialog.setMessage(Constants.PROGRESS_DLG_MSG);
        progressDialog.show();

        User user =  appDatabase.userDao().findByEmail(email);

        progressDialog.dismiss();
        if (user == null || (!user.password.equals(password))) {
            Toast.makeText(getApplicationContext(), "Login Failed...Try Again", Toast.LENGTH_SHORT).show();
        } else {
            finish();

            ShoppingCartApplication.sharedInstance.setCurrentLoggedInUserId(user.uid);
            //start profile activity
            startActivity(new Intent(getApplicationContext(),HomeActivity.class)); //cant do new intent in OncompleteListner so getappli..context used
        }
    }

    @Override
    public void onClick(View v) {
        if(v == buttonLogin){
            userLogin();
        }
        if(v == textViewSignup){
            finish();
            startActivity(new Intent(this, SignUpActivity.class));
        }
    }
}
