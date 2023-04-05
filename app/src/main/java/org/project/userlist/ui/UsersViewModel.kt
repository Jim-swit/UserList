package org.project.userlist.ui

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.project.userlist.api.Retrofit
import org.project.userlist.db.ItemSourceFactory
import org.project.userlist.db.UsersBoundaryCallback
import org.project.userlist.db.UsersDb
import org.project.userlist.model.User
import org.project.userlist.model.Users
import org.project.userlist.repository.UsersRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsersViewModel(
    private val usersRepository: UsersRepository
): ViewModel() {

    private val _usersList by lazy { usersRepository.loadUsers() }
    val usersList:LiveData<PagedList<Users>> get() = _usersList


    /* TODO: 사용자 등록
    fun insertUsersDb(users: Users) {
        db.runInTransaction {
            db.usersDao().insertUsers(users)
        }
    }

     */
}