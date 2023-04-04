package org.project.userlist.ui

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.project.userlist.api.Retrofit
import org.project.userlist.db.ItemSourceFactory
import org.project.userlist.db.UsersDb
import org.project.userlist.model.User
import org.project.userlist.model.Users

class UsersViewModel(
    private val db: UsersDb
): ViewModel() {

    private val retrofitApi = Retrofit.instance

    private lateinit var pagedListbuilder : LivePagedListBuilder<Int, Users>
    private lateinit var temp : LiveData<PagedList<Users>>

    private val config = ItemSourceFactory.providePagingConfig()

    private val _test = MutableLiveData<User>()
    val test:LiveData<User> = _test

//    val usersList : LiveData<PagedList<Users>> = usersDao.getAll()

    init {
        //initViewModel()
        Log.d("test", "init0")
        //viewModelScope.launch { postData() }

        Log.d("test", "init1")
    }


    fun initViewModel() {
        val listUserDataSourceFactory = ItemSourceFactory(retrofitApi)
        pagedListbuilder = LivePagedListBuilder<Int, Users>(listUserDataSourceFactory, config)
    }



    fun getUser(name:String) {
        viewModelScope.launch {
           val response =  withContext(Dispatchers.IO) {
                retrofitApi.getUser(name)
            }
            if (response.isSuccessful) {
                //_test.postValue(response.body())
                _test.value = response.body()
            } else {
                Log.d("test", "response Fail")
            }
        }
    }




    // 특정 키로 이동
    fun load(key: Int) : LiveData<PagedList<Users>> {
        return pagedListbuilder.setInitialLoadKey(key).build()
    }
    class UsersViewModelFactory(val db: UsersDb):ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return UsersViewModel(db) as T
        }
    }
    /////////////
    fun insertUsersDb(users: Users) {
        users.let { posts ->
            db.runInTransaction {
                db.usersDao().insertUsers(
                    Users(
                        id = posts.id,
                        login = posts.login,
                        node_id = posts.node_id,
                        url = posts.url,
                        avatar_url = posts.avatar_url)
                )
            }
        }
    }

    suspend fun postData() : LiveData<PagedList<Users>> {
        val data: DataSource.Factory<Int, Users> = db.usersDao().getAll()
        //pagedListbuilder = LivePagedListBuilder(data, config).build()

        return LivePagedListBuilder(data, config).build()
    }
}