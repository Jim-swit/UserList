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

    // Room에서 받아오는 UI에 보여질 Users List
    private val _usersList:LiveData<PagedList<Users>> by lazy { usersRepository.loadUsers() }
    val usersList:LiveData<PagedList<Users>> get() = _usersList


    // REST API로 부터 받아오는 Result(Success, Error, Loading)
    private val _networkState:MutableLiveData<ApiResult<List<Users>>> = MutableLiveData<ApiResult<List<Users>>>()
    val networkState:LiveData<ApiResult<List<Users>>> get() = _networkState

    private fun initNetworkState() {
        viewModelScope.launch {
            usersRepository.networkState.observeForever {
                _networkState.value = it
            }
        }
    }

    // REST API Success 시 Paging 된 Users List를 Room에 저장
    suspend fun insertUsersList(users:List<Users>) {
        usersRepository.insertUsers(*users.toTypedArray())
    }

    // 북마크 체크 시 BookMarkUsers에 해당 Users 데이터 저장
    suspend fun insertBookMarkUsers(users:Users) {
        usersRepository.insertBookMarkUsersFromUsers(users)
    }

    // 북마크 체크 해제 시 BookMarkUsers에 해당 Users 데이터 삭제
    suspend fun deleteBookMarkUsers(users:Users) {
        usersRepository.deleteBookMarkUsersFromUsers(users)
    }

    // 온라인(mobile, wifi 연결)으로 전환 시 재시도
    fun reConnectNetWork() {
        if(networkState.value is ApiResult.ApiError) {
            reTryListener(true)
        }
    }

    // 다음 페이지 로드 재시도
    fun reTryListener(connected:Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            usersRepository.reTryListener(connected)
        }
    }
}