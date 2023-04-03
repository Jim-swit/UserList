package org.project.userlist.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.paging.DataSource
import org.project.userlist.model.Users

@Dao
interface UsersDao {
    @Query("SELECT * FROM Users")
    fun getAll(): DataSource.Factory<Int, Users>

    @Insert
    fun insertUsers(users: Users)

    @Delete
    fun delete(userList: Users)
}