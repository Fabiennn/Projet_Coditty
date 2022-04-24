package com.example.projet_coditty_goubin.database

import androidx.room.*
import com.example.projet_coditty_goubin.model.User

@Dao
interface UserDao {

    @Insert
    fun insert(user: User): Long
    @Delete
    fun delete(user: User)
    @Update
    fun update(user: User)
    @Query("SELECT * from user WHERE id = :key")
    fun get(key: Long): User?
    @Query("DELETE FROM user")
    fun deleteData()
    @Query("SELECT * FROM user ORDER BY score DESC")
    fun getUsers() : List<User>
}