package com.example.quixote_login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.CursorWindow;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.quixote_login.Data.UserDatabaseHelper;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {

    private Button loginButton;
    private TextView register;
    private EditText editTextPassword;
    private EditText editTextEmail;

    private UserDatabaseHelper userDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = findViewById(R.id.button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateInputs()) {
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
            }
        });

        register = findViewById(R.id.register_tv);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        editTextEmail = findViewById(R.id.editTextTextEmailAddress);
        editTextPassword = findViewById(R.id.editTextTextPassword);

        userDatabaseHelper = new UserDatabaseHelper(this);
    }

    private boolean validateInputs(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        if(email.isEmpty() || email == "" || email == null){
            editTextEmail.setError("Field cannot be empty");
            return false;
        }

        if(email.isEmpty() || password == "" || password == null){
            editTextPassword.setError("Field cannot be empty");
            return false;
        }

        if(!userDatabaseHelper.userRegistered(email, password)){
            editTextEmail.setError("User does not exist");
            return false;
        }
        return true;
    }

}