package org.project.userlist.view.userDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.project.userlist.data.network.ApiResult
import org.project.userlist.data.repository.UserRepository
import org.project.userlist.model.User

class UserDetailViewModel(
private val userRepository: UserRepository
): ViewModel() {

    private val _getUser = MutableLiveData<ApiResult<User>>(ApiResult.Loading)
    val getUser: LiveData<ApiResult<User>> get() = _getUser

    suspend fun getUserDetail(url: String) {
        _getUser.postValue(userRepository.getUserDetail(url))
    }
}