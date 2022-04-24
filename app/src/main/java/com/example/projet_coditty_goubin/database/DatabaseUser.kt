package com.example.projet_coditty_goubin.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.projet_coditty_goubin.model.User

@Database(entities = [User::class], version = 8, exportSchema = false)
abstract class DatabaseUser : RoomDatabase() {

    abstract val userDao: UserDao


    companion object {
        @Volatile
        private var INSTANCE: DatabaseUser? = null
        fun getInstance(context: Context): DatabaseUser {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        DatabaseUser::class.java,
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