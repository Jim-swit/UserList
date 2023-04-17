package org.project.userlist.view.userList

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.project.userlist.data.network.ApiResult
import org.project.userlist.data.repository.UserRepository
import org.project.userlist.model.Users

class UsersViewModel(
    private val userRepository: UserRepository
): ViewModel() {


    // Room에서 받아오는 UI에 보여질 Users List
    private val _loadUsersList:LiveData<PagedList<Users>> by lazy { userRepository.loadUsers() }
    val loadUsersList:LiveData<PagedList<Users>> get() = _loadUsersList


    // REST API로 부터 받아오는 Result(Success, Error, Loading)
    private val _getUsersList:LiveData<ApiResult<List<Users>>> = userRepository.networkResult
    val getUsersList:LiveData<ApiResult<List<Users>>> get() = _getUsersList



    // REST API Success 시 Paging 된 Users List를 Room에 저장
    suspend fun insertUsersList(users:List<Users>) {
        userRepository.insertUsers(*users.toTypedArray())
    }

    // 북마크 체크 시 BookMarkUsers에 해당 Users 데이터 저장
    suspend fun insertBookMarkUsers(users:Users) {
        userRepository.insertBookMarkUsers(users)
    }

    // 북마크 체크 해제 시 BookMarkUsers에 해당 Users 데이터 삭제
    suspend fun deleteBookMarkUsers(users:Users) {
        userRepository.deleteBookMarkUsers(users)
    }

    // 온라인(mobile, wifi 연결)으로 전환 시 재시도
    fun reConnectNetWork() {
        if(getUsersList.value is ApiResult.Fail.Error || getUsersList.value is ApiResult.Fail.Exception) {
            reTryListener(true)
        }
    }

    // 다음 페이지 로드 재시도
    fun reTryListener(connected:Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.reTry(connected)
        }
    }
}