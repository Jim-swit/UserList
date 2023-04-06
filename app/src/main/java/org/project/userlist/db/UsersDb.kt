package org.project.userlist.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.project.userlist.model.Users

@Database(entities = [Users::class], version = 1, exportSchema = false)
abstract class UsersDb : RoomDatabase() {
    companion object {
        const val STARTPAGE:Int = 0
        fun create(context: Context): UsersDb {
            val databaseBuilder = if(false) {
                Room.inMemoryDatabaseBuilder(context, UsersDb::class.java)
            } else {
                Room.databaseBuilder(context, UsersDb::class.java, "users.db")
            }
            return databaseBuilder
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    abstract fun usersDao() : UsersDao
}