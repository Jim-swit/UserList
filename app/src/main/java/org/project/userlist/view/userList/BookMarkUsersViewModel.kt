package org.project.userlist.view.userList

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import org.project.userlist.data.repository.UserRepository
import org.project.userlist.model.BookMarkUsers

class BookMarkUsersViewModel(
    private val userRepository: UserRepository
):ViewModel() {

    private val _bookMarkUsersList by lazy { userRepository.loadBookMarkUsers() }
    val boockMarkUsersList: LiveData<PagedList<BookMarkUsers>> get() = _bookMarkUsersList

    suspend fun deleteBookMarkUsers(bookMarkUsers:BookMarkUsers) {
        userRepository.deleteBookMarkUsers(bookMarkUsers)
    }
}