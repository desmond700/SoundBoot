package com.example.soundboot;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.soundboot.login.DatabaseHelper;
import com.example.soundboot.login.InputValidation;
import com.example.soundboot.login.User;

public class LoginActivity extends AppCompatActivity {

    private ConstraintLayout constraintLayout;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        initObjects();
    }

    public void loadHome(View view){

        if(!inputValidation.isInputEditTextFilled(editTextEmail, getString(R.string.input_field_error_message))){
            return;
        }
        if(!inputValidation.isInputEditTextEmail(editTextEmail, getString(R.string.input_field_error_message))) {
            return;
        }
        if(!inputValidation.isInputEditTextFilled(editTextPassword, getString(R.string.input_field_error_message))){
            return;
        }
        if(databaseHelper.checkUser(editTextEmail.getText().toString().trim(), editTextPassword.getText().toString().trim())){
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }else {
            Snackbar.make(constraintLayout, getString(R.string.input_field_error_message), Snackbar.LENGTH_LONG).show();
        }

    }

    private void initViews(){
        editTextEmail =  findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        constraintLayout = findViewById(R.id.login_layout);
    }

    private void initObjects(){
        databaseHelper = new DatabaseHelper(LoginActivity.this);
        inputValidation = new InputValidation(LoginActivity.this);
    }
}
