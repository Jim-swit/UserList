package org.project.userlist.ui.view.userDetail

import androidx.lifecycle.ViewModel
import org.project.userlist.repository.UserRepository

class UserDetailViewModel(
private val userRepository: UserRepository
): ViewModel() {


    suspend fun getUserDetail(url: String) = userRepository.getUserDetail(url)


}