package com.assigment.newsapp.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NewsDao {

    @Insert
    void addNewsToDb(NewsEntity news);

    @Query("SELECT * FROM news_table")
    List<NewsEntity> getListOfNews();

    @Query("DELETE FROM news_table")
    void deleteAllNewsFromDB();
}
