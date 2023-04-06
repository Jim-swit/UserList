package org.project.userlist.repository

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
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

    /*
    // 특정 키로 이동
    fun postKeyData(key: Int) : LiveData<PagedList<Users>> {
        return pagedListbuilder.setInitialLoadKey(key).build()
    }

     */
}