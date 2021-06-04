package com.uzaysan.whatsappclone.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.quarkworks.roundedframelayout.RoundedFrameLayout;
import com.uzaysan.whatsappclone.App;
import com.uzaysan.whatsappclone.R;
import com.uzaysan.whatsappclone.adapters.SelectedUserAdapter;
import com.uzaysan.whatsappclone.adapters.UserAdapter;
import com.uzaysan.whatsappclone.models.user.User;
import com.uzaysan.whatsappclone.viewmodels.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class NewChatSearchUserActivity extends AppCompatActivity implements UserAdapter.OnUserClick, SelectedUserAdapter.OnUserRemove, Observer<List<User>> {

    UserViewModel userViewModel;
    List<User> users = new ArrayList<>();
    List<User> selectedUsers = new ArrayList<>();
    ArrayList<String> ids = new ArrayList<>();
    UserAdapter adapter;
    SelectedUserAdapter selectedUserAdapter;
    RoundedFrameLayout nextButtonWrapper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_chat);

        SearchView searchView = findViewById(R.id.search);

        RecyclerView selectedUsersRecyclerView = findViewById(R.id.selectedUsersRecyclerview);
        RecyclerView usersRecyclerView = findViewById(R.id.usersRecyclerview);

        adapter = new UserAdapter(Glide.with(this));
        adapter.setData(users);
        adapter.setOnClickListener(this);

        usersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        usersRecyclerView.setAdapter(adapter);

        selectedUserAdapter = new SelectedUserAdapter(Glide.with(this));
        selectedUserAdapter.setData(selectedUsers);
        selectedUserAdapter.setOnRemoveListener(this);

        LinearLayoutManager horizontalManager = new LinearLayoutManager(this);
        horizontalManager.setOrientation(RecyclerView.HORIZONTAL);
        selectedUsersRecyclerView.setLayoutManager(horizontalManager);
        selectedUsersRecyclerView.setAdapter(selectedUserAdapter);




        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getUserLiveData().observe(this,this);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.length() == 0) return false;
                userViewModel.getUsersByQuery(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        nextButtonWrapper = findViewById(R.id.nextButtonWrapper);
        RelativeLayout next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ids.size() > 0) {
                    startActivity(new Intent(NewChatSearchUserActivity.this, CreateNewChatActivity.class)
                            .putStringArrayListExtra("users", ids));
                    App.searchUserActivity = NewChatSearchUserActivity.this;
                }
            }
        });

        FrameLayout back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onUserClick(int position) {
        User user = users.get(position);
        if (!selectedUsers.contains(user)) {
            selectedUsers.add(0, user);
            ids.add(user.getId());
        }
        nextButtonWrapper.setVisibility(View.VISIBLE);
        selectedUserAdapter.notifyDataSetChanged();
    }

    @Override
    public void onChanged(List<User> users) {
        this.users = users;
        adapter.setData(this.users);
    }

    @Override
    public void onUserRemove(int position) {
        ids.remove(selectedUsers.get(position).getId());
        selectedUsers.remove(position);
        if(selectedUsers.size() <= 0) {
            nextButtonWrapper.setVisibility(View.INVISIBLE);
        }
        selectedUserAdapter.notifyDataSetChanged();
    }


    @Override
    protected void onDestroy() {
        if(App.searchUserActivity != null) {
            App.searchUserActivity = null;
        }
        super.onDestroy();
    }
}