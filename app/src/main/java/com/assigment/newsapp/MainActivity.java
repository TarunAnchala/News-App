package com.assigment.newsapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.assigment.newsapp.adapter.NewsVerticalAdapter;
import com.assigment.newsapp.db.NewsEntity;
import com.assigment.newsapp.detailScreen.DetailScreen;
import com.assigment.newsapp.utils.InjectManager;
import com.assigment.newsapp.utils.Utils;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements InjectManager.InjectedEventNotifier {
    private NewsViewModel newsViewModel;
    private static final String TAG = "MainActivity";
    private RecyclerView newsRecyclerView;
    private NewsVerticalAdapter newsVerticalAdapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newsViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);
        newsRecyclerView = findViewById(R.id.newsList);
        progressBar = findViewById(R.id.progressBar);
        newsRecyclerView.setHasFixedSize(true);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        newsVerticalAdapter = new NewsVerticalAdapter(this);
        newsRecyclerView.setAdapter(newsVerticalAdapter);
        fetchNewsAndSetData();
    }

    private void fetchNewsAndSetData() {
        newsViewModel.getNewsData().observe(this, listOfNewsEntities -> {
            if (listOfNewsEntities != null) {
                progressBar.setVisibility(View.GONE);
                newsVerticalAdapter.setData(listOfNewsEntities);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        InjectManager.getInstance().addListener(InjectManager.LAUNCH_DETAIL_SCREEN, this);
    }

    @Override
    public void onReceiveEvent(int eventType, Object object) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        int position = (int) object;
        Bundle bundle = new Bundle();
        bundle.putInt(Utils.POSITION, position);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DetailScreen detailScreen = new DetailScreen();
        detailScreen.setArguments(bundle);
        fragmentTransaction.replace(R.id.container, detailScreen, DetailScreen.TAG);
        fragmentTransaction.addToBackStack(DetailScreen.TAG);
        fragmentTransaction.commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        InjectManager.getInstance().removeListener(InjectManager.LAUNCH_DETAIL_SCREEN, this);
    }
}