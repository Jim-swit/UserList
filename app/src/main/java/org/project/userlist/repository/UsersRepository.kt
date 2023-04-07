package org.project.userlist.repository

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.project.userlist.RetrofitGITAPI
import org.project.userlist.db.ItemSourceFactory
import org.project.userlist.db.UsersBoundaryCallback
import org.project.userlist.db.UsersDb
import org.project.userlist.model.Users

class UsersRepository(
    val db: UsersDb,
    private val retrofitApi: RetrofitGITAPI
) {
    private lateinit var pagedListbuilder : LivePagedListBuilder<Int, Users>
    private lateinit var boundaryCallback: UsersBoundaryCallback
    private val config = ItemSourceFactory.providePagingConfig()

    init {
        initBuilder()
    }


    private fun initBuilder() {
        boundaryCallback = UsersBoundaryCallback(retrofitApi, db, config.pageSize)
        val data: DataSource.Factory<Int, Users> = db.usersDao().getAll()

        pagedListbuilder = LivePagedListBuilder(data, config)
            .setBoundaryCallback(boundaryCallback)

    }

    fun loadUsers(): LiveData<PagedList<Users>> {
        return pagedListbuilder.build()
    }

    fun reTryListener() {
        boundaryCallback.reTryListener()
    }

    fun reFreshListener() {
        boundaryCallback.reFreshListener()
    }

    fun updateUsers(updatedUsers:Users) {
        CoroutineScope(Dispatchers.IO).launch {
            db.usersDao().updateUsers(updatedUsers)
        }
    }

    fun updateTest() {
        CoroutineScope(Dispatchers.IO).launch {
            db.usersDao().updateUsers(
                Users(
                    login = "testName",
                    id = "1",
                    node_id = "MDQ6VXNlcjE=",
                    avatar_url = "https://avatars.githubusercontent.com/u/1?v=4",
                    url = "https://api.github.com/users/mojombo"
                )
            )
        }
    }

    /*
    // 특정 키로 이동
    fun postKeyData(key: Int) : LiveData<PagedList<Users>> {
        return pagedListbuilder.setInitialLoadKey(key).build()
    }

     */
}