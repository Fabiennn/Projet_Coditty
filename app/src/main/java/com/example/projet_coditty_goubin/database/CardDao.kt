package com.example.projet_coditty_goubin.database

import androidx.room.*
import com.example.projet_coditty_goubin.model.Card

@Dao
interface CardDao {

    @Insert
    fun insert(card: Card): Long

    @Delete
    fun delete(card: Card)

    @Update
    fun update(card: Card)

    @Query("SELECT * from Card WHERE id = :key")
    fun get(key: Long): Card?

    @Query("SELECT * FROM CARD WHERE id NOT IN (:list) ORDER BY RANDOM ( ) LIMIT 1")
    fun getRandomNotIn(list: List<Long>): Card

    @Query("DELETE FROM card")
    fun deleteData()
}