package com.assigment.newsapp;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.assigment.newsapp.api.ApiInterface;
import com.assigment.newsapp.api.RetrofitService;
import com.assigment.newsapp.api.data.Articles;
import com.assigment.newsapp.api.data.News;
import com.assigment.newsapp.api.data.Source;
import com.assigment.newsapp.db.NewsDao;
import com.assigment.newsapp.db.NewsEntity;
import com.assigment.newsapp.db.NewsRoomDB;
import com.assigment.newsapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsViewModel extends AndroidViewModel {

    private static final String TAG = "NewsViewModel";
    private ApiInterface apiInterface;
    public static final String API_KEY = "02b91f5b4a744daf87f50b0f31609da9";
    private NewsDao newsDao;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    ArrayList<NewsEntity> listOfNews = new ArrayList<>();
    private MutableLiveData<ArrayList<NewsEntity>> newsLiveData = new MutableLiveData<>();

    public NewsViewModel(@NonNull Application application) {
        super(application);
        init();
    }

    private void init() {
        newsDao = NewsRoomDB.getNewsRoomDB().newsDao();
        if (Utils.isNetworkAvailable()) {
            Log.e(TAG, "init: Network Available.So,fetching news from n/w");
            apiInterface = RetrofitService.getRetrofitInstance().create(ApiInterface.class);
            fetchNewsFromServer();
        } else {
            Log.e(TAG, "init: Network not available.So,fetching news from DB");
            fetchNewsFromDB();
        }
    }

    private void fetchNewsFromDB() {
        executor.execute(() -> {
            List<NewsEntity> newsList = newsDao.getListOfNews();
            if (newsList != null) {
                listOfNews.clear();
                listOfNews.addAll(newsList);
                newsLiveData.postValue((ArrayList<NewsEntity>) newsList);
            }

        });
    }

    private void fetchNewsFromServer() {
        Log.e(TAG, "fetchNews: called");
        Call<News> newsApiCall = apiInterface.getNews("bitcoin", "2020-09-11", "publishedAt", API_KEY);
        newsApiCall.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                executor.execute(() -> {
                    if (response.isSuccessful() && response.body() != null) {
                        ArrayList<Articles> listOfArticles = response.body().getArticles();
                        if (listOfArticles != null && listOfArticles.size() > 0) {
                            newsDao.deleteAllNewsFromDB();
                            listOfNews.clear();
                            for (int i = 0; i < listOfArticles.size(); i++) {
                                Articles article = listOfArticles.get(i);
                                if (article != null) {
                                    Source source = article.getSource();
                                    if (source != null) {
                                        String newsSource = source.getName();
                                        NewsEntity newsEntity = new NewsEntity(0, article.getTitle(), article.getDescription(), newsSource, article.getUrlToImage(), "2019-07-03");
                                        newsDao.addNewsToDb(newsEntity);
                                        listOfNews.add(newsEntity);
                                    }
                                }
                            }
                            newsLiveData.postValue(listOfNews);
                        }
                    }
                });

            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Log.e(TAG, "onFailure: called");
                fetchNewsFromDB();
            }
        });
    }

    public MutableLiveData<ArrayList<NewsEntity>> getNewsData() {
        return newsLiveData;
    }

    public NewsEntity getNewsObj(int position) {
        if (listOfNews.size() > position) {
            return listOfNews.get(position);
        }
        return null;
    }

    public ArrayList<NewsEntity> getListOfNews() {
        return listOfNews;
    }
}
