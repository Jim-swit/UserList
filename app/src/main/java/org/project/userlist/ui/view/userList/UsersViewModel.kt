package org.project.userlist.ui.view.userList

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.paging.PagedList
import org.project.userlist.data.remote.ApiResult
import org.project.userlist.model.Users
import org.project.userlist.repository.UsersRepository

class UsersViewModel(
    private val usersRepository: UsersRepository
): ViewModel() {

    private val _usersList:LiveData<PagedList<Users>> by lazy { usersRepository.loadUsers() }
    val usersList:LiveData<PagedList<Users>> get() = _usersList


    private val _networkState:LiveData<ApiResult<List<Users>>> =  usersRepository.networkState
    val networkState:LiveData<ApiResult<List<Users>>> get() = _networkState

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
        Log.d("netStateTest", "reConnectNetWork: ${networkState.value}")
        if(networkState.value is ApiResult.ApiError) {
            usersRepository.reTryListener(true)
        }
        usersRepository.reTryListener(true)
        // usersRepository.unConnectNetWork()
    }


    fun reTryListner() {
        usersRepository.reTryListener(false)
    }

    fun reFreshListner() {
        usersRepository.reFreshListener()
    }
    /* TODO: 사용자 등록
    fun insertUsersDb(users: Users) {
        db.runInTransaction {
            db.usersDao().insertUsers(users)
        }
    }

     */
}