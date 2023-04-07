package org.project.userlist.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import org.project.userlist.model.BookMarkUsers
import org.project.userlist.model.Users
import org.project.userlist.repository.UsersRepository

class BookMarkUsersViewModel(
    private val usersRepository: UsersRepository
):ViewModel() {

    private val _bookMarkUsersList by lazy { usersRepository.loadBookMarkUsers() }
    val boockMarkUsersList: LiveData<PagedList<BookMarkUsers>> get() = _bookMarkUsersList

    fun deleteBookMarkUsers(bookMarkUsers:BookMarkUsers) {
        usersRepository.deleteBookMarkUsersFromBookMarkUsers(bookMarkUsers)
    }
}