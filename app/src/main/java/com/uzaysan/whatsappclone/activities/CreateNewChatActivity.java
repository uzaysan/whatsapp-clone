package com.uzaysan.whatsappclone.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;
import com.theartofdev.edmodo.cropper.CropImage;
import com.uzaysan.whatsappclone.App;
import com.uzaysan.whatsappclone.R;
import com.uzaysan.whatsappclone.models.chat.Chat;
import com.uzaysan.whatsappclone.models.chat.ChatRepository;
import com.uzaysan.whatsappclone.parseclasses.ParseChat;
import com.uzaysan.whatsappclone.viewmodels.ChatViewModel;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class CreateNewChatActivity extends AppCompatActivity implements View.OnClickListener, SaveCallback {

    RelativeLayout chatIconLayout;
    ImageView chatIcon;
    ParseFile icon;
    Button create;
    EditText chatName;

    int imageRequestCode = 1234;
    ProgressDialog progressDialog;
    ParseChat parseChat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_chat);

        chatIconLayout = findViewById(R.id.group_icon_layout);
        chatIcon = findViewById(R.id.group_icon);
        create = findViewById(R.id.create_chat);
        create.setOnClickListener(this);
        chatIconLayout.setOnClickListener(this);
        chatName = findViewById(R.id.chat_name);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.create));
        progressDialog.setCancelable(false);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.group_icon_layout) {
            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            Permissions.check(this,permissions,null, null, new PermissionHandler() {
                @Override
                public void onGranted() {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent,"Photo"), imageRequestCode);
                }
                @Override
                public void onDenied(Context context, ArrayList<String> deniedPermissions){

                }
            });
        } else if (v.getId() == R.id.create_chat) {
            if(icon == null) return;
            if(chatName.getText().toString().trim().length() <= 0) return;
            progressDialog.show();
            saveIcon(icon);

        }
    }

    private void saveIcon(ParseFile file) {
        file.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null) {
                    Log.e("File error:", e.getMessage());
                    progressDialog.dismiss();
                    return;
                }
                saveChat(file);
            }
        });
    }

    private void saveChat(ParseFile icon) {
        parseChat = new ParseChat();
        parseChat.addMember(getIntent().getStringArrayListExtra("users"));
        parseChat.setChatName(chatName.getText().toString().trim());
        parseChat.setChatIconURL(icon);
        parseChat.setIsGroupChat(true);
        parseChat.setLastMessage(".");
        parseChat.setLastMessageDate(new Date());
        parseChat.saveInBackground(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data != null) {
            if(requestCode == imageRequestCode) {
                Uri uri = data.getData();
                CropImage.activity(uri).start(this);
            }
            else if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Uri uri=result.getUri();
                icon = new ParseFile(new File(uri.getPath()));
                Glide.with(this).load(uri).into(chatIcon);
            }
        }
    }


    @Override
    public void done(ParseException e) {
        if(e != null) {
            Log.e("Save Error: ", e.getMessage());
            progressDialog.dismiss();
            return;
        }
        ChatRepository chatRepository = new ChatRepository(this);
        chatRepository.insert(new Chat(parseChat));
        if(App.searchUserActivity != null) {
            App.searchUserActivity.finish();
        }
        progressDialog.dismiss();
        finish();
    }
}