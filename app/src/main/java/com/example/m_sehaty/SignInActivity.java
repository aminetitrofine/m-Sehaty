package com.abdsoft.med_dose;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.abdsoft.med_dose.db.UserDAO;

public class SignInActivity extends AppCompatActivity {
    EditText email, password;
    Button signin , redirectToSignUp;

    UserDAO DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        email = (EditText) findViewById(R.id.sin_email);
        password = (EditText) findViewById(R.id.sin_password);
        DB = new UserDAO(getApplicationContext());
        signin = (Button) findViewById(R.id.signinbtn);
        redirectToSignUp = (Button) findViewById(R.id.sin_dont_have);


        signin.setOnClickListener( v -> {
            String email_input = email.getText().toString();
            String pass_input = password.getText().toString();

            if(email_input.equals("") || pass_input.equals(""))
                Toast.makeText(SignInActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
            else{
                boolean checkuserpass = DB.checkEmailAndPassword(email_input, pass_input);
                if(checkuserpass){
                    Toast.makeText(SignInActivity.this, "Sign in successfull", Toast.LENGTH_SHORT).show();
                    Intent intent  = new Intent(getApplicationContext(), com.abdsoft.med_dose.HomeActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(SignInActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

        redirectToSignUp.setOnClickListener( v -> {
            Intent intent = new Intent(getApplicationContext(),SignUpActivity.class);
            startActivity(intent);
        });
    }
}