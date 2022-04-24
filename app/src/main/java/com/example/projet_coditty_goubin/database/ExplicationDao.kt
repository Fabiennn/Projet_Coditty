package com.example.projet_coditty_goubin.database

import androidx.room.*
import com.example.projet_coditty_goubin.model.Explication


@Dao
interface ExplicationDao {

    @Insert
    fun insert(explication: Explication): Long
    @Delete
    fun delete(explication: Explication)
    @Update
    fun update(explication: Explication)
    @Query("SELECT * from explication WHERE id = :key")
    fun get(key: Long): Explication?
    @Query("DELETE FROM explication")
    fun deleteData()
}