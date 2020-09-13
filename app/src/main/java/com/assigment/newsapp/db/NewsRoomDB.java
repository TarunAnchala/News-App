package com.assigment.newsapp.db;


import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.assigment.newsapp.utils.MyApplication;

@Database(entities = {NewsEntity.class}, version = 1, exportSchema = false)
public abstract class NewsRoomDB extends RoomDatabase {

    private static NewsRoomDB newsRoomDB;

    public static NewsRoomDB getNewsRoomDB() {
        if (newsRoomDB == null) {
            newsRoomDB = Room.databaseBuilder(MyApplication.getAppContext(), NewsRoomDB.class, "news_room_database").build();
        }
        return newsRoomDB;
    }


    public abstract NewsDao newsDao();

}