package org.project.userlist.data.local.dto

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.paging.DataSource
import androidx.room.OnConflictStrategy
import androidx.room.Update
import org.project.userlist.model.Users

@Dao
interface UsersDao {

    @Query("SELECT * FROM Users")
    fun getUsersAll(): DataSource.Factory<Int, Users>

    @Query("SELECT * FROM Users ORDER BY id DESC LIMIT 1")
    suspend fun getUsersLast(): Users

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUsers(vararg users: Users)

    @Delete
    suspend fun deleteUsers(users: Users)

    @Query("DELETE FROM Users")
    suspend fun deleteAll()

    @Update
    suspend fun updateUsers(vararg users:Users)
}