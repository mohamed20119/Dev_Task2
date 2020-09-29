package com.example.task2.Presenter;

import com.example.task2.MainActivity;
import com.example.task2.Model.RepoApiModel;
import com.example.task2.Model.RepoModel;
import java.util.ArrayList;

public class RepoPresenter {
    private RepoModel repoModel ;
    private MainActivity mainActivity ;

    public RepoPresenter(RepoModel repoModel, MainActivity mainActivity) {
        this.repoModel = repoModel;
        this.mainActivity = mainActivity;
    }

    // using to Call Retrofit to get data from API
    public void  getRepoInfo()
    {
        this.repoModel.getRepoInfoFromApi();
    }

    // using to set Data from API into Array List
    public ArrayList<RepoApiModel> showRepoInfo()
    {
        return this.repoModel.getRepo();
    }


}
