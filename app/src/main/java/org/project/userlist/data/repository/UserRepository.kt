package org.project.userlist.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import org.project.userlist.data.network.ApiResult
import org.project.userlist.model.BookMarkUsers
import org.project.userlist.model.User
import org.project.userlist.model.Users

interface UserRepository {
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

    suspend fun getUserDetail(url: String): ApiResult<User>
}


