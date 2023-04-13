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
    private val getUsersList: suspend (Int, Int) -> Unit
) : PagedList.BoundaryCallback<Users>() {
    private val TAG = "UsersBoundaryCallback"
    companion object {var networkAccessCount: Int = 5}


    override fun onZeroItemsLoaded() {
        Log.d(TAG, "Zero")
        CoroutineScope(Dispatchers.IO).launch {
            getUsersList(STARTPAGE, per_page)
        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: Users) {
        Log.d(TAG, "AtEnd ${itemAtEnd.id}}")
        --networkAccessCount
        CoroutineScope(Dispatchers.IO).launch {
            getUsersList(itemAtEnd.id.toInt(), per_page)
        }
    }

    // 최상단 이상의 데이터가 없으므로 필요성을 느끼지 못함.
    override fun onItemAtFrontLoaded(itemAtFront: Users) {
        super.onItemAtFrontLoaded(itemAtFront)
    }

    fun reTry(users: Users) {
        Log.d(TAG, "reTry $networkAccessCount")
        if(networkAccessCount >= 0) {
            Log.d(TAG, "reTry Success")
            onItemAtEndLoaded(users)
        }
    }
    fun reFreshListener() {
        Log.d(TAG, "reFresh")
        //onZeroItemsLoaded()
    }
    fun setNetworkAccessCount() {
        networkAccessCount = 5
    }
}
