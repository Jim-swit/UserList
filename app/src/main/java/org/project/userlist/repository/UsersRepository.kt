
package org.project.userlist.repository

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.project.userlist.RetrofitGITAPI
import org.project.userlist.db.BookMarkUsersDataSource
import org.project.userlist.db.BookMarkUsersItemSourceFactory
import org.project.userlist.db.ItemSourceFactory
import org.project.userlist.db.UsersBoundaryCallback
import org.project.userlist.db.UsersDb
import org.project.userlist.model.Users

class UsersRepository(
    val db: UsersDb,
    private val retrofitApi: RetrofitGITAPI
) {
    private lateinit var usersPagedListbuilder : LivePagedListBuilder<Int, Users>
    private lateinit var bookMarkUsersPagedListbuilder : LivePagedListBuilder<Int, Users>

    private lateinit var boundaryCallback: UsersBoundaryCallback

    private val config = ItemSourceFactory.providePagingConfig()

    init {
        initBuilder()
    }


    private fun initBuilder() {
        boundaryCallback = UsersBoundaryCallback(retrofitApi, db, config.pageSize)
        val usersData: DataSource.Factory<Int, Users> = db.usersDao().getAllUsers()

        usersPagedListbuilder = LivePagedListBuilder(usersData, config)
            .setBoundaryCallback(boundaryCallback)

        val bookMarkUsersData = db.usersDao().getAllBookMarkUsers()
        bookMarkUsersPagedListbuilder = LivePagedListBuilder(bookMarkUsersData, config)
    }

    fun loadUsers(): LiveData<PagedList<Users>> {
        return usersPagedListbuilder.build()
    }

    fun updateUsers(updatedUsers:Users) {
        CoroutineScope(Dispatchers.IO).launch {
            db.usersDao().updateUsers(updatedUsers)
        }
    }

    fun loadBookMarkUsers(): LiveData<PagedList<Users>> {
        return bookMarkUsersPagedListbuilder.build()
    }


    fun reTryListener() {
        boundaryCallback.reTryListener()
    }

    fun reFreshListener() {
        boundaryCallback.reFreshListener()
    }


    /*
    // 특정 키로 이동
    fun postKeyData(key: Int) : LiveData<PagedList<Users>> {
        return pagedListbuilder.setInitialLoadKey(key).build()
    }

     */
}