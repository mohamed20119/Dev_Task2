package com.example.task2.Model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RepoModel {
    private ArrayList<RepoApiModel> list = new ArrayList<>();

    // using to return ArrayList of data
    public ArrayList<RepoApiModel> getRepo()
    {

        return this.list;
    }

    // using to call with API and fetch Data
    public void getRepoInfoFromApi()
    {
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl("https://api.github.com/users/").build();
        retrofit.create(RepoApi.class).getRepo().enqueue(new Callback<List<RepoApiModel>>() {
            @Override
            public void onResponse(Call<List<RepoApiModel>> call, Response<List<RepoApiModel>> response) {
                if(response.isSuccessful())
                {
                    Log.e("response","Success");

                    for(int x=0;x<response.body().size();x++)
                    {
                        list.add(response.body().get(x));
                    }
                }else
                {
                    Log.e("response","Disconnect");
                }
            }

            @Override
            public void onFailure(Call<List<RepoApiModel>> call, Throwable t) {

            }
        });
    }
}
