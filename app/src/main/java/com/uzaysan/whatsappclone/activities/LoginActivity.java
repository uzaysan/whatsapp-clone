package com.uzaysan.whatsappclone.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.uzaysan.whatsappclone.R;
import com.uzaysan.whatsappclone.models.user.User;
import com.uzaysan.whatsappclone.models.user.UserRepository;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, SignUpCallback, LogInCallback {

    ImageView logo;
    TextView email, password;
    Button login, register;
    ParseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        logo = findViewById(R.id.logo);
        Glide.with(this).load(R.drawable.whatsapp).into(logo);

        email = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);

        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.login) {
            if (email.getText().toString().length() > 0 && password.getText().toString().length() > 0) {

                ParseUser.logInInBackground(email.getText().toString().trim()
                        , password.getText().toString().trim()
                        , this);

                /*FirebaseAuth.getInstance()
                        .signInWithEmailAndPassword(email.getText().toString().trim()
                                , password.getText().toString().trim())
                        .addOnCompleteListener(this);*/
            }
        }
        else {
            if (email.getText().toString().length() > 0 && password.getText().toString().length() > 0) {
                user = new ParseUser();
                user.setEmail(email.getText().toString().trim());
                user.setPassword(password.getText().toString().trim());
                user.setUsername(email.getText().toString().trim());
                user.signUpInBackground(this);
                /*FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(email.getText().toString().trim()
                        ,password.getText().toString().trim())
                        .addOnCompleteListener(this);*/
            }
        }

    }

    @Override
    public void done(ParseException e) {
        if (e == null) {
            UserRepository userRepository = new UserRepository(getApplicationContext());
            userRepository.insertUser(new User(user));
            if(!isDestroyed() && !isFinishing()) {
                startActivity(new Intent(this,MainActivity.class));
            }
        }
        else{
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void done(ParseUser user, ParseException e) {
        if (e == null) {
            UserRepository userRepository = new UserRepository(getApplicationContext());
            userRepository.insertUser(new User(user));
            if(!isDestroyed() && !isFinishing()) {
                startActivity(new Intent(this,MainActivity.class));
            }
        }
        else{
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}