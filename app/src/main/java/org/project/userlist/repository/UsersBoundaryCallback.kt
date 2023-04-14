package org.project.userlist.repository

import android.util.Log
import androidx.paging.PagedList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.project.userlist.data.local.UsersDb.Companion.STARTPAGE
import org.project.userlist.model.Users

class UsersBoundaryCallback(
    private val per_page: Int,
    private val getUsersList: (Int, Int) -> Unit
) : PagedList.BoundaryCallback<Users>() {
    private val TAG = "UsersBoundaryCallback"
    
    //  REST API 연결 시도 횟수 제한
    companion object {var networkAccessCount: Int = 5}


    override fun onZeroItemsLoaded() {
        getUsersList(STARTPAGE, per_page)
    }

    override fun onItemAtEndLoaded(itemAtEnd: Users) {
        --networkAccessCount
        getUsersList(itemAtEnd.id, per_page)
    }

    override fun onItemAtFrontLoaded(itemAtFront: Users) {
        super.onItemAtFrontLoaded(itemAtFront)
    }

    fun reTry(users: Users?) {
        if(users == null) {
            onZeroItemsLoaded()
        } else {
            if (networkAccessCount >= 0) {
                onItemAtEndLoaded(users)
            }
        }
    }

    fun setNetworkAccessCount() {
        networkAccessCount = 5
    }
}
