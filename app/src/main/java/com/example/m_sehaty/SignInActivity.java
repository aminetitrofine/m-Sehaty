package com.abdsoft.med_dose;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.abdsoft.med_dose.db.UserDAO;

public class SignInActivity extends AppCompatActivity {
    EditText firstname, lastname, email, phone, password, repassword;
    Button signup , redirectToSignIn;

    UserDAO DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firstname = (EditText) findViewById(R.id.sup_firstname);
        lastname = (EditText) findViewById(R.id.sup_lastname);
        email = (EditText) findViewById(R.id.sup_email);
        phone = (EditText) findViewById(R.id.sup_number);
        password = (EditText) findViewById(R.id.sup_password);
        repassword = (EditText) findViewById(R.id.sup_confirmpassword);

        DB = new com.abdsoft.med_dose.db.UserDAO(getApplicationContext());
        signup = (Button) findViewById(R.id.signupbtn);
        redirectToSignIn = (Button) findViewById(R.id.sup_already);


        signup.setOnClickListener( v -> {
            String first_input = firstname.getText().toString();
            String last_input = lastname.getText().toString();
            String pass_input = password.getText().toString();
            String email_input = email.getText().toString();
            String phone_input = phone.getText().toString();
            String repass_input = repassword.getText().toString();

            if(first_input.equals("") || last_input.equals("") || pass_input.equals("") || email_input.equals("") || phone_input.equals("") || repass_input.equals(""))
                Toast.makeText(SignUpActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
            else if(!pass_input.equals(repass_input)){
                Toast.makeText(SignUpActivity.this, "Password do not match", Toast.LENGTH_LONG).show();
            }else{
                if(DB.checkEmail(email_input)){
                    Toast.makeText(SignUpActivity.this, "Email already registered", Toast.LENGTH_SHORT).show();
                }else{
                    boolean newUser = DB.registerNewUser(first_input, last_input, email_input, pass_input, phone_input);
                    if(newUser){
                        Toast.makeText(SignUpActivity.this, "Sign up successfull", Toast.LENGTH_SHORT).show();
                        Intent intent  = new Intent(getApplicationContext(), com.abdsoft.med_dose.HomeActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(SignUpActivity.this, "Sign up failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        redirectToSignIn.setOnClickListener( v -> {
            Intent intent = new Intent(getApplicationContext(),SignInActivity.class);
            startActivity(intent);
        });
    }
}