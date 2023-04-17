package org.project.userlist.ui.view.userList

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import org.project.userlist.model.BookMarkUsers
import org.project.userlist.repository.UserRepository

class BookMarkUsersViewModel(
    private val userRepository: UserRepository
):ViewModel() {

    private val _bookMarkUsersList by lazy { userRepository.loadBookMarkUsers() }
    val boockMarkUsersList: LiveData<PagedList<BookMarkUsers>> get() = _bookMarkUsersList

    suspend fun deleteBookMarkUsers(bookMarkUsers:BookMarkUsers) {
        userRepository.deleteBookMarkUsersFromBookMarkUsers(bookMarkUsers)
    }
}