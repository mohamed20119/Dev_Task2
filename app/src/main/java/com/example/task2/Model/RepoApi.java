package com.example.task2.Model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RepoApi {

    @GET("square/repos")
    public Call<List<RepoApiModel>> getRepo();
}
