package com.example.projet_coditty_goubin.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.projet_coditty_goubin.model.Card

@Database(entities = [Card::class], version = 7, exportSchema = false)
abstract class DatabaseCard : RoomDatabase() {

    abstract val cardDao: CardDao


    companion object {
        @Volatile
        private var INSTANCE: DatabaseCard? = null
        fun getInstance(context: Context): DatabaseCard {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        DatabaseCard::class.java,
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