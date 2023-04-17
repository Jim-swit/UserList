package org.project.userlist.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.project.userlist.data.network.api.RetrofitGITAPI
import org.project.userlist.data.local.UsersDb
import org.project.userlist.data.network.APICall
import org.project.userlist.data.network.ApiResult
import org.project.userlist.data.network.providePagingConfig
import org.project.userlist.model.BookMarkUsers
import org.project.userlist.model.Users

interface UsersRepository {
    val networkState: LiveData<ApiResult<List<Users>>>
    fun loadUsers(): LiveData<PagedList<Users>>
    fun loadBookMarkUsers(): LiveData<PagedList<BookMarkUsers>>
    suspend fun reTry(connected:Boolean)
    
    suspend fun insertUsers(vararg users: Users)


    // BookMarkUsers 이벤트 처리
    suspend fun deleteBookMarkUsers(users: Users)
    suspend fun deleteBookMarkUsers(bookMarkUsers: BookMarkUsers)

    suspend fun insertBookMarkUsers(users: Users)
    suspend fun insertBookMarkUsers(bookMarkUsers: BookMarkUsers)
}


