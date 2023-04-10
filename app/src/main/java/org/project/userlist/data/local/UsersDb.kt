package org.project.userlist.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.project.userlist.data.local.dao.BookMarkUsersDao
import org.project.userlist.data.local.dao.UsersDao
import org.project.userlist.model.BookMarkUsers
import org.project.userlist.model.Users

@Database(entities = [Users::class, BookMarkUsers::class], version = 2, exportSchema = false)
abstract class UsersDb : RoomDatabase() {
    companion object {
        const val STARTPAGE:Int = 0
        fun create(context: Context): UsersDb {
            val databaseBuilder =
                Room.databaseBuilder(context, UsersDb::class.java, "user.db")

            return databaseBuilder
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    abstract fun usersDao() : UsersDao
    abstract fun bookMarkUsersDao() : BookMarkUsersDao
}