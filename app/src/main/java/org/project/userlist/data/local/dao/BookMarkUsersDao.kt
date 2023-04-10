package org.project.userlist.data.local.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.project.userlist.model.BookMarkUsers

@Dao
interface BookMarkUsersDao {

    @Query("SELECT * FROM BookMarkUsers")
    fun getBookMarkUsersAll(): DataSource.Factory<Int, BookMarkUsers>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBookMarkUsers(vararg bookMarkUsers: BookMarkUsers)

    @Delete
    suspend fun deleteBookMarkUsers(bookMarkUsers: BookMarkUsers)
}