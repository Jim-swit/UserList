package org.project.userlist.db

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
    fun getAllUsers(): DataSource.Factory<Int, Users>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUsers(vararg users: Users)

    @Update
    fun updateUsers(vararg users:Users)


    @Query("SELECT * FROM Users WHERE isChecked = 1")
    fun getAllBookMarkUsers(): DataSource.Factory<Int, Users>


    @Delete
    fun delete(users: Users)

    @Query("DELETE FROM Users")
    fun deleteAll()
}