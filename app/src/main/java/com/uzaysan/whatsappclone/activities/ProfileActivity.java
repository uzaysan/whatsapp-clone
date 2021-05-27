package com.uzaysan.whatsappclone.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseUser;
import com.uzaysan.whatsappclone.R;
import com.uzaysan.whatsappclone.models.user.User;
import com.uzaysan.whatsappclone.viewmodels.UserViewModel;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements Observer<User> {

    CircleImageView profilePhoto;
    TextView name, status;
    UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = findViewById(R.id.name);
        status = findViewById(R.id.status);
        profilePhoto = findViewById(R.id.profile_photo);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        LiveData<User> userLiveData = userViewModel.getUserLiveData(ParseUser.getCurrentUser().getObjectId());
        userLiveData.observe(this,this);
        userViewModel.startListen();

    }

    private void update(User user) {
        System.out.println("User name:" + user.getName());
        System.out.println("User status:" + user.getStatus());
        System.out.println("User name:" + user.getProfile_photo());
        name.setText(user.getName());
        status.setText(user.getStatus());
        Glide.with(this).load(user.getProfile_photo()).into(profilePhoto);
    }

    @Override
    public void onChanged(User user) {
        update(user);
    }
}