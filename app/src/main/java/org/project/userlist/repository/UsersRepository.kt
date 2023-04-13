package org.project.userlist.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.project.userlist.data.remote.RetrofitGITAPI
import org.project.userlist.data.local.UsersDb
import org.project.userlist.data.remote.APICall
import org.project.userlist.data.remote.ApiResult
import org.project.userlist.model.BookMarkUsers
import org.project.userlist.model.Users

class UsersRepository(
    val db: UsersDb,
    private val retrofitApi: RetrofitGITAPI
) {
    private lateinit var pagedListbuilder : LivePagedListBuilder<Int, Users>
    private lateinit var bookMarkPagedListbuilder : LivePagedListBuilder<Int, BookMarkUsers>
    private lateinit var boundaryCallback: UsersBoundaryCallback
    private val config = providePagingConfig()
    private val TAG = "UsersRepository"

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

    private suspend fun getUsersList(startPage:Int, perPage:Int) {
        return withContext(Dispatchers.IO) {
            when(val result = APICall { retrofitApi.getUserListPaging(startPage, perPage) }) {
                is ApiResult.ApiSuccess -> {
                    result.data?.let { usersList ->
                        insertUsers(usersList.toTypedArray())
                    }
                }
                is ApiResult.ApiError -> {
                    Log.d(TAG, "onFailur: ${result.exception.message}")
                }

                else -> {}
            }
        }
    }

    private suspend fun insertUsers(usersList:Array<Users>) {
        db.usersDao().insertUsers(*usersList)
    }

    fun loadUsers(): LiveData<PagedList<Users>> {
        return pagedListbuilder.build()
    }

    fun loadBookMarkUsers(): LiveData<PagedList<BookMarkUsers>> {
        return bookMarkPagedListbuilder.build()
    }

    fun reTryListener() {
        CoroutineScope(Dispatchers.IO).launch {
            db.usersDao().getUsersLast()?.let {
                boundaryCallback.reTry(users = it)
            }
        }
    }

    fun reFreshListener() {
        boundaryCallback.reFreshListener()
    }

    private suspend fun updateUsers(updatedUsers:Users) {
            db.usersDao().updateUsers(updatedUsers)
    }

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

    suspend fun insertBookMarkUsersFromUsers(users:Users) {
        insertBookMarkUsers(users)
    }

    suspend fun deleteBookMarkUsersFromUsers(users:Users) {
        deleteBookMarkUsers(users)
    }

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


    /*
    // 특정 키로 이동
    fun postKeyData(key: Int) : LiveData<PagedList<Users>> {
        return pagedListbuilder.setInitialLoadKey(key).build()
    }

     */
    companion object {
        fun providePagingConfig() : PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(8) // 최초 로드 사이즈
            .setPageSize(8) // 각 페이지의 크기
            .setPrefetchDistance(5) // 미리 로드할 거리(개수) 정의
            .build()
    }
}

