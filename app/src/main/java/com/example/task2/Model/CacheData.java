package com.example.task2.Model;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CacheData {
    public CacheData(Context context,ArrayList<RepoApiModel> list) {
        SharedPreferences sharedPreferences= context.getSharedPreferences("cach.txt",context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        if(sharedPreferences.getBoolean("isFirstTime",true)==true)
        {
            // gson for store object into sharedPreferences
            Gson gson = new Gson();
            String json = gson.toJson(list);
            editor.putString("MyList", json);
            editor.putBoolean("isFirstTime",false);
            editor.commit();
        }else
        {
            Gson gson = new Gson();
            String json = sharedPreferences.getString("MyList","");
            Type type = new TypeToken<List<RepoApiModel>>(){}.getType();
            ArrayList<RepoApiModel> models = gson.fromJson(json, type);
        }

    }
}
