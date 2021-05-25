package com.uzaysan.whatsappclone.helper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

public class TypeConverter {
    @androidx.room.TypeConverter
    public static ArrayList<String> fromString(String value) {
        Type listType = new TypeToken<ArrayList<String>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @androidx.room.TypeConverter
    public static String fromArrayList(ArrayList<String> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }


}
