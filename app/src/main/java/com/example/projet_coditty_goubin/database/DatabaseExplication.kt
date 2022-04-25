package com.example.projet_coditty_goubin.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.projet_coditty_goubin.model.Explication

@Database(entities = [Explication::class], version = 2, exportSchema = false)
abstract class DatabaseExplication : RoomDatabase() {

    abstract val explicationDao: ExplicationDao


    companion object {
        @Volatile
        private var INSTANCE: DatabaseExplication? = null
        fun getInstance(context: Context): DatabaseExplication {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        DatabaseExplication::class.java,
                        "database"
                    )
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}