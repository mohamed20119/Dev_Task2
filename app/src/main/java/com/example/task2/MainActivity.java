package com.example.task2;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import com.example.task2.Model.CacheData;
import com.example.task2.Model.RepoApiModel;
import com.example.task2.Model.RepoModel;
import com.example.task2.Presenter.RepoPresenter;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private RepoPresenter presenter ;
    private RecyclerView recyclerView ;
    private ArrayList<RepoApiModel> list ;
    private ProgressBar progressBar ;
    private CustomRecycleViewAdaptor adaptor;
    private SwipeRefreshLayout swipeRefreshLayout ;
    private LinearLayoutManager linearLayoutManager;

    // Handler using to update data
    private Handler handler= new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeContainer);
        this.list = new ArrayList<>();
        this.progressBar = (ProgressBar)findViewById(R.id.progressBar);
        this.presenter = new RepoPresenter(new RepoModel(),this);
        this.recyclerView = (RecyclerView)findViewById(R.id.repoList);
        this.linearLayoutManager = new LinearLayoutManager(this);
        this.presenter.getRepoInfo();
        DisplayRecycleView();
        this.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {

                    @Override public void run() {
                        DisplayRecycleView();
                        swipeRefreshLayout.setRefreshing(false);
                    }

                }, 2000);
            }
        });
    }

    public void init_RecycleView()
    {
        adaptor = new CustomRecycleViewAdaptor(list,this);
        this.recyclerView.setLayoutManager(this.linearLayoutManager);
        this.recyclerView.setAdapter(adaptor);
    }

    public void DisplayRecycleView()
    {
        // Threading to update data from Api
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);


                  handler.post(new Runnable() {
                      @Override
                      public void run() {
                          progressBar.setVisibility(View.GONE);
                          list = presenter.showRepoInfo();
                          init_RecycleView();
                          // using to cache data into file
                          new CacheData(MainActivity.this,list);
                      }
                  });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("saveState",this.linearLayoutManager.onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.linearLayoutManager.onRestoreInstanceState(savedInstanceState.getParcelable("saveState"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        final SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setQueryHint("Searching using repository name");
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        final SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
              adaptor.Filter(newText);
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {

                return true;
            }
        };

        searchView.setOnQueryTextListener(queryTextListener);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler = null;
    }
}