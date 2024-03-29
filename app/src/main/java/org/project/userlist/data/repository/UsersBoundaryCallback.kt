package org.project.userlist.data.repository

import androidx.paging.PagedList
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

    // REST API 연결 실패 시 or 온라인(mobile, wifi 연결)으로 전환 시 재시도
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
