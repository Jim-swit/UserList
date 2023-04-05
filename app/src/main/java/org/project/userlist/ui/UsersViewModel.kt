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
import org.project.userlist.db.UsersBoundaryCallback
import org.project.userlist.db.UsersDb
import org.project.userlist.model.User
import org.project.userlist.model.Users

class UsersViewModel(
    private val db: UsersDb
): ViewModel() {

    private val retrofitApi = Retrofit.instance

    private lateinit var pagedListbuilder : LivePagedListBuilder<Int, Users>

    private val config = ItemSourceFactory.providePagingConfig()

    //private val _usersList = MutableLiveData<PagedList<Users>>()
    private val _usersList by lazy { pagedListbuilder.build() }
    val usersList:LiveData<PagedList<Users>> get() = _usersList

    private val _test = MutableLiveData<User>()
    val test:LiveData<User> = _test

//    val usersList : LiveData<PagedList<Users>> = usersDao.getAll()

    init {
        initViewModel()
        //viewModelScope.launch { postData() }
    }


    fun initViewModel() {
        /*
        // ItemSourceFactory와 DataSource를 기반하여 Paging 처리를 했을 때 사용
        val listUserDataSourceFactory = ItemSourceFactory(retrofitApi)
        pagedListbuilder = LivePagedListBuilder<Int, Users>(listUserDataSourceFactory, config)

         */
        val boundaryCallback = UsersBoundaryCallback(retrofitApi, db, config.pageSize)
        val data: DataSource.Factory<Int, Users> = db.usersDao().getAll()

        pagedListbuilder = LivePagedListBuilder(data, config)
            .setBoundaryCallback(boundaryCallback)
    }



    fun getUser(name:String) {
        viewModelScope.launch {
           val response =  withContext(Dispatchers.IO) {
                retrofitApi.getUser(name)
            }
            if (response.isSuccessful) {
                _test.value = response.body()
            } else {
                Log.d("test", "response Fail")
            }
        }
    }


    // 특정 키로 이동
    fun postKeyData(key: Int) : LiveData<PagedList<Users>> {
        return pagedListbuilder.setInitialLoadKey(key).build()
    }

    // Users 데이터 초기화
    fun getData():LiveData<PagedList<Users>> {
        return pagedListbuilder.build()
    }

    // ViewModel에 넘겨줄 매개변수가 있기에 Factory 구현
    // Koin 적용 시 제거 가능
    class UsersViewModelFactory(val db: UsersDb):ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return UsersViewModel(db) as T
        }
    }
    /* TODO: 사용자 등록
    fun insertUsersDb(users: Users) {
        db.runInTransaction {
            db.usersDao().insertUsers(users)
        }
    }

     */
}