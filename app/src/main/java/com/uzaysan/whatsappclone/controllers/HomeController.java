package com.uzaysan.whatsappclone.controllers;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.RouterTransaction;
import com.uzaysan.whatsappclone.App;
import com.uzaysan.whatsappclone.MainActivity;
import com.uzaysan.whatsappclone.R;
import com.uzaysan.whatsappclone.adapters.ChatsAdapter;
import com.uzaysan.whatsappclone.controllers.base.BaseController;
import com.uzaysan.whatsappclone.models.Chat;
import com.uzaysan.whatsappclone.viewmodels.ChatViewModel;

import java.util.List;

public class HomeController extends Controller
        implements ChatsAdapter.ChatAdapterClickListener
        , Observer<List<Chat>> {

    ChatViewModel chatViewModel;
    ChatsAdapter adapter;

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @Nullable Bundle savedViewState) {
        View view = inflater.inflate(R.layout.home_controller, container, false);

        adapter = new ChatsAdapter();
        adapter.setOnClickListener(this);

        RecyclerView recyclerView = view.findViewById(R.id.homeRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);


        chatViewModel = new ViewModelProvider((MainActivity) getActivity()).get(ChatViewModel.class);
        chatViewModel.getChats().observe((MainActivity) getActivity(),this);
        chatViewModel.setUpData(15);


        return view;
    }

    @Override
    public void onClick(int position) {
        Toast.makeText(getActivity(), "On Click Triggered", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLongClick(int position) {
        Toast.makeText(getActivity(), "On Long Click Triggered", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onChanged(List<Chat> chats) {
        Toast.makeText(getActivity(), "Update Recieved", Toast.LENGTH_SHORT).show();
        adapter.setData(chats);
        adapter.notifyDataSetChanged();

    }
}
