package com.example.quixote_login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quixote_login.Data.UserDatabaseHelper;
import com.example.quixote_login.Model.User;
import com.example.quixote_login.Utilities.Validator;

public class RegisterActivity extends AppCompatActivity {

    private Button register;
    private EditText editTextName;
    private EditText editTextPhone;
    private EditText editTextEmail;
    private EditText editTextPassword;

    private UserDatabaseHelper userDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register = findViewById(R.id.button2);
        editTextEmail = findViewById(R.id.editTextTextEmailAddress2);
        editTextName = findViewById(R.id.editTextTextPersonName);
        editTextPassword = findViewById(R.id.editTextTextPassword2);
        editTextPhone = findViewById(R.id.editTextPhone);

        userDatabaseHelper = new UserDatabaseHelper(this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateInputs()) {
                    writeUserToDB();
                    Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void writeUserToDB(){
        String name = editTextName.getText().toString().trim();
        String mail = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();

        User user = new User(name, mail, phone, password);
        userDatabaseHelper.addUser(user);
    }

    private boolean validateInputs(){
        editTextPhone.setError(null);
        editTextPassword.setError(null);
        editTextEmail.setError(null);

        String name = editTextName.getText().toString().trim();
        if(name.isEmpty()){
            editTextName.setError("Field Cannot be empty");
            return false;
        }

//        Email validation
        String email = editTextEmail.getText().toString().trim();
        if(!Validator.validateEmail(email)){
            editTextEmail.setError(getString(R.string.wrong_email_format));
            return false;
        }

//        Password
        String password = editTextPassword.getText().toString().trim();
        if(containsName(password, name)){
            editTextPassword.setError("Password cannot contain your name");
            return false;
        }

        if(!Validator.validatePassword(password)){
            editTextPassword.setError(getString(R.string.wrong_password_format));
            return false;
        }

//        Phone
        if(!Validator.validatePhone(editTextPhone.getText().toString().trim())){
            editTextPhone.setError("Phone number is not from India");
            return false;
        }

//        If user already exists
        if(userDatabaseHelper.userExists(email)){
            editTextEmail.setError("User already exists");
            return false;
        }
        return true;
    }

    private boolean containsName(String password, String name){
        name = name.toLowerCase().trim();
        password = password.toLowerCase().trim();
        String[] parts = name.split(" ");
        if(password.contains(name)){
            return false;
        }
        for(String part: parts){
            Log.e("Names", part);
            Log.e("Names", password);
            if(password.contains(part)){
                return true;
            }
        }
        return false;
    }
}