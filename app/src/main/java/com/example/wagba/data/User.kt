package com.example.wagba.data
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User (
    @PrimaryKey val uid: String,
    @ColumnInfo(name = "mail") val mail: String,
    @ColumnInfo(name = "username") val username: String?,
    @ColumnInfo(name = "phone_number") val phone_number: String?
)

