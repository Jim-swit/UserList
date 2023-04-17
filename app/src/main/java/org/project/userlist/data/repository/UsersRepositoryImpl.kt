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
import org.project.userlist.data.local.UsersDb
import org.project.userlist.data.network.APICall
import org.project.userlist.data.network.ApiResult
import org.project.userlist.data.network.api.RetrofitGITAPI
import org.project.userlist.data.network.providePagingConfig
import org.project.userlist.model.BookMarkUsers
import org.project.userlist.model.User
import org.project.userlist.model.Users

class userRepositoryImpl (
    private val db: UsersDb,
    private val retrofitApi: RetrofitGITAPI
): UserRepository {
    private lateinit var pagedListbuilder : LivePagedListBuilder<Int, Users>
    private lateinit var bookMarkPagedListbuilder : LivePagedListBuilder<Int, BookMarkUsers>
    private lateinit var boundaryCallback: UsersBoundaryCallback
    private val config = providePagingConfig().set8px()
    private val TAG = "userRepository"

    private val _networkState = MutableLiveData<ApiResult<List<Users>>>()
    override val networkState: LiveData<ApiResult<List<Users>>> get() = _networkState

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
            _networkState.postValue(result)
        }
    }


    // UsersFragment 초기화 시 호출
    override fun loadUsers(): LiveData<PagedList<Users>> {
        return pagedListbuilder.build()
    }

    // BookmarkUsersListFragment 초기화 시 호출
    override fun loadBookMarkUsers(): LiveData<PagedList<BookMarkUsers>> {
        return bookMarkPagedListbuilder.build()
    }


    // 북마크 체크/해제 시 Users 데이터 업데이트
    private suspend fun updateUsers(updatedUsers: Users) {
        db.usersDao().updateUsers(updatedUsers)
    }

    override suspend fun reTry(connected:Boolean) {
        if(connected)
            boundaryCallback.setNetworkAccessCount()

        val getData = db.usersDao().getUsersLast()

        // REST API 연결 재시도 속도 조절을 위한 delay
        delay(1000)
        boundaryCallback.reTry(getData)
    }


    // 북마크 체크 해제 시 BookmarkUsers 데이터 삭제
    override suspend fun deleteBookMarkUsers(users: Users) {
        BookMarkUsers(
            id = users.id, login = users.login,
            node_id = users.node_id, url = users.url,
            avatar_url = users.avatar_url
        ).let {
            db.bookMarkUsersDao().deleteBookMarkUsers(it)
            updateUsers(users)
        }
    }
    override suspend fun deleteBookMarkUsers(bookMarkUsers: BookMarkUsers) {
        bookMarkUsers.let {
            db.bookMarkUsersDao().deleteBookMarkUsers(it)
            updateUsers(
                Users(
                    id = it.id, login = it.login,
                    node_id = it.node_id, url = it.url,
                    avatar_url = it.avatar_url, bookMarked = false
                )
            )
        }
    }

    override suspend fun insertBookMarkUsers(users: Users) {
        BookMarkUsers(
            id = users.id, login = users.login,
            node_id = users.node_id, url = users.url,
            avatar_url = users.avatar_url
        ).let {
            db.bookMarkUsersDao().insertBookMarkUsers(it)
            updateUsers(users)
        }
    }

    override suspend fun insertBookMarkUsers(bookMarkUsers: BookMarkUsers) {
        bookMarkUsers.let {
            db.bookMarkUsersDao().insertBookMarkUsers(it)
            updateUsers(
                Users(
                    id = it.id, login = it.login,
                    node_id = it.node_id, url = it.url,
                    avatar_url = it.avatar_url, bookMarked = true
                )
            )
        }
    }



    // REST API Result Room에 저장
    private suspend fun insertUsersDao(vararg usersList: Users) {
        db.usersDao().insertUsers(*usersList)
        boundaryCallback.setNetworkAccessCount()
    }

    override suspend fun insertUsers(vararg users: Users) {
        insertUsersDao(*users)
    }

    /*
    // 특정 키로 이동
    fun postKeyData(key: Int) : LiveData<PagedList<Users>> {
        return pagedListbuilder.setInitialLoadKey(key).build()
    }

     */

    override suspend fun getUserDetail(login:String) : ApiResult<User> {
        return APICall { retrofitApi.getUserDetail(login) }
    }
}

