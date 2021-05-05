package com.uzaysan.whatsappclone.controllers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.RouterTransaction;
import com.uzaysan.whatsappclone.R;
import com.uzaysan.whatsappclone.controllers.base.BaseController;

public class ProfileController extends Controller {


    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @Nullable Bundle savedViewState) {
        View view = inflater.inflate(R.layout.profile_controller, container, false);
        //getRouter().pushController(RouterTransaction.with());
        return view;
    }
}
