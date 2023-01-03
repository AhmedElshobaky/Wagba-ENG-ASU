package com.example.wagba.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {
    @Insert
    fun addUser(user: User)

    @Query("SELECT * FROM user_table ORDER BY mail ASC")
    fun readAllData(): LiveData<List<User>>

    @Query("SELECT * FROM user_table WHERE uid = :uid LIMIT 1")
    fun getUser(uid: String): LiveData<User>

    @Update
    fun updateUser(user: User)

    @Delete
    fun deleteUser(user: User)
    @Query("DELETE FROM user_table")
    fun deleteAllUsers()

}