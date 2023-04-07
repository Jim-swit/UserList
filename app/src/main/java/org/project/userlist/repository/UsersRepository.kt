package org.project.userlist.repository

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.project.userlist.data.remote.RetrofitGITAPI
import org.project.userlist.data.local.UsersDb
import org.project.userlist.model.BookMarkUsers
import org.project.userlist.model.Users

class UsersRepository(
    val db: UsersDb,
    private val retrofitApi: RetrofitGITAPI
) {
    private lateinit var pagedListbuilder : LivePagedListBuilder<Int, Users>
    private lateinit var bookMarkPagedListbuilder : LivePagedListBuilder<Int, BookMarkUsers>
    private lateinit var boundaryCallback: UsersBoundaryCallback
    private val config = ItemSourceFactory.providePagingConfig()

    init {
        initBuilder()
    }


    private fun initBuilder() {
        boundaryCallback = UsersBoundaryCallback(retrofitApi, db, config.pageSize)
        val data: DataSource.Factory<Int, Users> = db.usersDao().getUsersAll()
        val bookMarkData: DataSource.Factory<Int, BookMarkUsers> = db.bookMarkUsersDao().getBookMarkUsersAll()

        pagedListbuilder = LivePagedListBuilder(data, config)
            .setBoundaryCallback(boundaryCallback)

        bookMarkPagedListbuilder = LivePagedListBuilder(bookMarkData, config)
    }

    fun loadUsers(): LiveData<PagedList<Users>> {
        return pagedListbuilder.build()
    }

    fun loadBookMarkUsers(): LiveData<PagedList<BookMarkUsers>> {
        return bookMarkPagedListbuilder.build()
    }

    fun reTryListener() {
        boundaryCallback.reTryListener()
    }

    fun reFreshListener() {
        boundaryCallback.reFreshListener()
    }

    private fun updateUsers(updatedUsers:Users) {
        CoroutineScope(Dispatchers.IO).launch {
            db.usersDao().updateUsers(updatedUsers)
        }
    }

    private fun insertBookMarkUsers(users:Users) {
        val bookMarkUsers = BookMarkUsers(
            id = users.id,
            login = users.login,
            node_id = users.node_id,
            url = users.url,
            avatar_url = users.avatar_url
        )
        CoroutineScope(Dispatchers.IO).launch {
            db.bookMarkUsersDao().insertBookMarkUsers(bookMarkUsers)
        }
        updateUsers(users)
    }

    private fun deleteBookMarkUsers(users:Users) {
        val bookMarkUsers = BookMarkUsers(
            id = users.id,
            login = users.login,
            node_id = users.node_id,
            url = users.url,
            avatar_url = users.avatar_url
        )
        CoroutineScope(Dispatchers.IO).launch {
            db.bookMarkUsersDao().deleteBookMarkUsers(bookMarkUsers)
        }
        updateUsers(users)
    }

    fun insertBookMarkUsersFromUsers(users:Users) {
        insertBookMarkUsers(users)
    }

    fun deleteBookMarkUsersFromUsers(users:Users) {
        deleteBookMarkUsers(users)
    }

    fun deleteBookMarkUsersFromBookMarkUsers(bookMarkUsers: BookMarkUsers) {
        deleteBookMarkUsers(
            bookMarkUsers.let {
                Users(
                    id = it.id,
                    login = it.login,
                    node_id = it.node_id,
                    url = it.url,
                    avatar_url = it.avatar_url,
                    bookMarked = false
                )
            }
        )
    }


    /*
    // 특정 키로 이동
    fun postKeyData(key: Int) : LiveData<PagedList<Users>> {
        return pagedListbuilder.setInitialLoadKey(key).build()
    }

     */
}