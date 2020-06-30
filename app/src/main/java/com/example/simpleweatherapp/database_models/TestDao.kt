package com.example.simpleweatherapp.database_models

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TestDao {
    @Query("SELECT * FROM TestTable")
    fun getAll(): List<TestDB>

   /* @Query("SELECT * FROM TestTable WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<TestDB>

    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    fun findByName(first: String, last: String): TestDB
*/
    @Insert
    fun insertAll(vararg users: TestDB)

    @Delete
    fun delete(user: TestDB)
}