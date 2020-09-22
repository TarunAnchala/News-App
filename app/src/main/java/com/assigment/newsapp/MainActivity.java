package com.assigment.newsapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.assigment.newsapp.adapter.NewsVerticalAdapter;
import com.assigment.newsapp.detailScreen.DetailScreen;
import com.assigment.newsapp.utils.InjectManager;
import com.assigment.newsapp.utils.Utils;


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
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        newsViewModel = new ViewModelProvider(this).get(NewsViewModel.class);
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
            progressBar.setVisibility(View.GONE);
            if (listOfNewsEntities != null && listOfNewsEntities.size() > 0) {
                newsVerticalAdapter.setData(listOfNewsEntities);
            } else {
                Toast.makeText(this, R.string.data_not_available, Toast.LENGTH_SHORT).show();
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
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
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