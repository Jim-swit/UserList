package org.project.userlist.ui.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import org.project.userlist.model.Users
import org.project.userlist.repository.UsersRepository

class UsersViewModel(
    private val usersRepository: UsersRepository
): ViewModel() {

    private val _usersList:LiveData<PagedList<Users>> by lazy { usersRepository.loadUsers() }
    val usersList:LiveData<PagedList<Users>> get() = _usersList

    suspend fun insertBookMarkUsers(users:Users) {
        usersRepository.insertBookMarkUsersFromUsers(users)
    }

    suspend fun deleteBookMarkUsers(users:Users) {
        usersRepository.deleteBookMarkUsersFromUsers(users)
    }

    fun reTryListner() {
        usersRepository.reTryListener()
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