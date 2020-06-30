package com.example.simpleweatherapp.database_models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TestTable")
data class TestDB(@PrimaryKey val uid: Int,
                  @ColumnInfo(name = "first_name") val firstName: String?,
                  @ColumnInfo(name = "last_name") val lastName: String?)