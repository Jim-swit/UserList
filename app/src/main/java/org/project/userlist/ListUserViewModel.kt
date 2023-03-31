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

    private lateinit var pagedListbuilder: LivePagedListBuilder<Int, ListUser>

    private val config = ItemSourceFactory.providePagingConfig()

    init {
        viewmodel()
    }

    fun viewmodel() {
        viewModelScope.launch {
            val temp = async {  getData() }
            val listUserDataSourceFactory = async { ItemSourceFactory(temp.await()) }
            pagedListbuilder = LivePagedListBuilder<Int, ListUser>(listUserDataSourceFactory.await(), config)
        }
    }

    // 특정 키로 이동
    fun load(key: Int) : LiveData<PagedList<ListUser>> {
        return pagedListbuilder.setInitialLoadKey(key).build()
    }

    fun getload() : LiveData<PagedList<ListUser>> {
        return pagedListbuilder.build()
    }

    private suspend fun getData(): Response<List<ListUser>> {
        return withContext(Dispatchers.IO) {
            val temp = retrofitApi.getSearchResult()
            temp
        }

    }
}