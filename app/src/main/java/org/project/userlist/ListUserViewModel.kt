package org.project.userlist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class ListUserViewModel: ViewModel() {

    private val retrofitApi = Retrofit.instance

    private lateinit var pagedListbuilder : LivePagedListBuilder<Int, ListUser>

    private val config = ItemSourceFactory.providePagingConfig()

    init {
        viewmodel()
    }

    fun viewmodel() {
        val listUserDataSourceFactory = ItemSourceFactory(retrofitApi)
        pagedListbuilder = LivePagedListBuilder<Int, ListUser>(listUserDataSourceFactory, config)
    }

    // 특정 키로 이동
    fun load(key: Int) : LiveData<PagedList<ListUser>> {
        return pagedListbuilder.setInitialLoadKey(key).build()
    }
}