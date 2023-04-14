package org.project.userlist.ui.view.userList

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.project.userlist.data.remote.ApiResult
import org.project.userlist.model.Users
import org.project.userlist.repository.UsersRepository

class UsersViewModel(
    private val usersRepository: UsersRepository
): ViewModel() {

    init {
        initNetworkState()
    }
    private val _usersList:LiveData<PagedList<Users>> by lazy { usersRepository.loadUsers() }
    val usersList:LiveData<PagedList<Users>> get() = _usersList


    private val _networkState:MutableLiveData<ApiResult<List<Users>>> = MutableLiveData<ApiResult<List<Users>>>()
    val networkState:LiveData<ApiResult<List<Users>>> get() = _networkState

    private fun initNetworkState() {
        viewModelScope.launch {
            usersRepository.networkState.observeForever {
                _networkState.value = it
            }
        }
    }

    suspend fun insertUsersList(users:List<Users>) {
        usersRepository.insertUsers(*users.toTypedArray())
    }

    suspend fun insertBookMarkUsers(users:Users) {
        usersRepository.insertBookMarkUsersFromUsers(users)
    }

    suspend fun deleteBookMarkUsers(users:Users) {
        usersRepository.deleteBookMarkUsersFromUsers(users)
    }

    fun reConnectNetWork() {
        if(networkState.value is ApiResult.ApiError) {
            reTryListener(true)
        }
    }

    fun reTryListener(connected:Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            usersRepository.reTryListener(connected)
        }
    }
}