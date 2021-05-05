package com.uzaysan.whatsappclone.controllers.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bluelinelabs.conductor.Controller;
import com.uzaysan.whatsappclone.MainActivity;

public class BaseController extends Controller {
    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @Nullable Bundle savedViewState) {

        return null;
    }


    public MainActivity getMainActivity() {
        return (MainActivity) getActivity();
    }
}
