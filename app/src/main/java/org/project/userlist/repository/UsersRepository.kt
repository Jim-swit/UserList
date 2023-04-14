package org.project.userlist.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.room.withTransaction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.project.userlist.data.remote.RetrofitGITAPI
import org.project.userlist.data.local.UsersDb
import org.project.userlist.data.remote.APICall
import org.project.userlist.data.remote.ApiResult
import org.project.userlist.data.remote.providePagingConfig
import org.project.userlist.model.BookMarkUsers
import org.project.userlist.model.Users

class UsersRepository(
    private val db: UsersDb,
    private val retrofitApi: RetrofitGITAPI
) {
    private lateinit var pagedListbuilder : LivePagedListBuilder<Int, Users>
    private lateinit var bookMarkPagedListbuilder : LivePagedListBuilder<Int, BookMarkUsers>
    private lateinit var boundaryCallback: UsersBoundaryCallback
    private val config = providePagingConfig().set8px()
    private val TAG = "UsersRepository"

    var networkState: MutableLiveData<ApiResult<List<Users>>> = MutableLiveData<ApiResult<List<Users>>>()

    init {
        initBuilder()
    }

    private fun initBuilder() {
        boundaryCallback = UsersBoundaryCallback(config.pageSize, ::getUsersList)
        val data: DataSource.Factory<Int, Users> = db.usersDao().getUsersAll()
        val bookMarkData: DataSource.Factory<Int, BookMarkUsers> = db.bookMarkUsersDao().getBookMarkUsersAll()

        pagedListbuilder = LivePagedListBuilder(data, config)
            .setBoundaryCallback(boundaryCallback)

        bookMarkPagedListbuilder = LivePagedListBuilder(bookMarkData, config)
    }

    private fun getUsersList(startPage:Int, perPage:Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = APICall { retrofitApi.getUserListPaging(startPage, perPage) }
            networkState.postValue(result)
        }
    }


    // UsersFragment 초기화 시 호출
    fun loadUsers(): LiveData<PagedList<Users>> {
        return pagedListbuilder.build()
    }

    // BookmarkUsersListFragment 초기화 시 호출
    fun loadBookMarkUsers(): LiveData<PagedList<BookMarkUsers>> {
        return bookMarkPagedListbuilder.build()
    }

    suspend fun reTryListener(connected:Boolean) {
        if(connected)
            boundaryCallback.setNetworkAccessCount()

        val getData = db.usersDao().getUsersLast()

        // REST API 연결 재시도 속도 조절을 위한 delay
        delay(1000)
        boundaryCallback.reTry(getData)
    }


    // 북마크 체크/해제 시 Users 데이터 업데이트
    private suspend fun updateUsers(updatedUsers:Users) {
        db.usersDao().updateUsers(updatedUsers)
    }


    // 북마크 체크 해제 시 BookmarkUsers 데이터 삭제
    private suspend fun deleteBookMarkUsers(users:Users) {
        BookMarkUsers(
            id = users.id, login = users.login,
            node_id = users.node_id, url = users.url,
            avatar_url = users.avatar_url
        ).let {
            db.bookMarkUsersDao().deleteBookMarkUsers(it)
        }
        updateUsers(users)
    }

    // UsersFragment에서 북마크 체크 시 호출
    suspend fun deleteBookMarkUsersFromUsers(users:Users) {
        deleteBookMarkUsers(users)
    }

    // BookMarkFragment에서 북마크 체크 해제 시 호출
    suspend fun deleteBookMarkUsersFromBookMarkUsers(bookMarkUsers: BookMarkUsers) {
        deleteBookMarkUsers(
            bookMarkUsers.let {
                Users(
                    id = it.id, login = it.login,
                    node_id = it.node_id, url = it.url,
                    avatar_url = it.avatar_url, bookMarked = false
                )
            }
        )
    }

    
    // 북마크 체크 시 BookmarkUsers 데이터 저장
    private suspend fun insertBookMarkUsers(users:Users) {
        BookMarkUsers(
            id = users.id, login = users.login,
            node_id = users.node_id, url = users.url,
            avatar_url = users.avatar_url
        ).let {
            db.bookMarkUsersDao().insertBookMarkUsers(it)
        }

        updateUsers(users)
    }
    
    suspend fun insertBookMarkUsersFromUsers(users:Users) {
        insertBookMarkUsers(users)
    }



    // REST API Result Room에 저장
    private suspend fun insertUsersDao(vararg usersList: Users) {
        db.usersDao().insertUsers(*usersList)
        boundaryCallback.setNetworkAccessCount()
    }

    suspend fun insertUsers(vararg users: Users) {
        insertUsersDao(*users)
    }

    /*
    // 특정 키로 이동
    fun postKeyData(key: Int) : LiveData<PagedList<Users>> {
        return pagedListbuilder.setInitialLoadKey(key).build()
    }

     */
}

