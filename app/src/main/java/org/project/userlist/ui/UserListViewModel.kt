package org.project.userlist.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import org.project.userlist.api.Retrofit
import org.project.userlist.db.ItemSourceFactory
import org.project.userlist.model.UserList

class UserListViewModel: ViewModel() {

    private val retrofitApi = Retrofit.instance

    private lateinit var pagedListbuilder : LivePagedListBuilder<Int, UserList>

    private val config = ItemSourceFactory.providePagingConfig()

    init {
        viewmodel()
    }

    fun viewmodel() {
        val listUserDataSourceFactory = ItemSourceFactory(retrofitApi)
        pagedListbuilder = LivePagedListBuilder<Int, UserList>(listUserDataSourceFactory, config)
    }

    // 특정 키로 이동
    fun load(key: Int) : LiveData<PagedList<UserList>> {
        return pagedListbuilder.setInitialLoadKey(key).build()
    }
}