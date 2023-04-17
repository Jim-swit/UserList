package org.project.userlist.ui.view.userList

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import org.project.userlist.model.BookMarkUsers
import org.project.userlist.data.repository.UsersRepository

class BookMarkUsersViewModel(
    private val usersRepository: UsersRepository
):ViewModel() {

    private val _bookMarkUsersList by lazy { usersRepository.loadBookMarkUsers() }
    val boockMarkUsersList: LiveData<PagedList<BookMarkUsers>> get() = _bookMarkUsersList

    suspend fun deleteBookMarkUsers(bookMarkUsers:BookMarkUsers) {
        usersRepository.deleteBookMarkUsersFromBookMarkUsers(bookMarkUsers)
    }
}