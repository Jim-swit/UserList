package org.project.userlist.repository

import android.util.Log
import androidx.paging.PagedList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.project.userlist.data.remote.RetrofitGITAPI
import org.project.userlist.data.local.UsersDb
import org.project.userlist.data.local.UsersDb.Companion.STARTPAGE
import org.project.userlist.data.remote.APICall
import org.project.userlist.data.remote.ApiResult
import org.project.userlist.model.Users

class UsersBoundaryCallback(
    private val webService: RetrofitGITAPI,
    private val db: UsersDb,
    private val per_page: Int
) : PagedList.BoundaryCallback<Users>() {
    private val TAG = "UsersBoundaryCallback"

    init {
        initKeepData()
    }

    private lateinit var keepData:Users

    override fun onZeroItemsLoaded() {
        Log.d(TAG, "Zero")
        CoroutineScope(Dispatchers.IO).launch {
            when(val result = APICall { webService.getUserListPaging(STARTPAGE, per_page) }) {
                is ApiResult.ApiSuccess -> {
                    Log.d(TAG, "ApiSuccess")
                    result.data?.let { usersList ->
                        Log.d(TAG, "ApiSuccess & Data Not Null")
                        db.usersDao().insertUsers(*usersList.toTypedArray())
                    }
                }
                is ApiResult.ApiError -> {
                    Log.d(TAG, "onFailure: ${result.exception.message}")
                }

                else -> {}
            }
        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: Users) {
        Log.d(TAG, "AtEnd")
        CoroutineScope(Dispatchers.IO).launch {
            when(val result = APICall { webService.getUserListPaging(itemAtEnd.id.toInt(), per_page) }) {
                is ApiResult.ApiSuccess -> {
                    result.data?.let { usersList ->
                        keepData = usersList.last()
                        db.usersDao().insertUsers(*usersList.toTypedArray())
                    }
                }
                is ApiResult.ApiError -> {
                    Log.d(TAG, "onFailure: ${result.exception.message}")
                }
                else -> { }
            }
        }
    }

    // 최상단 이상의 데이터가 없으므로 필요성을 느끼지 못함.
    override fun onItemAtFrontLoaded(itemAtFront: Users) {
        super.onItemAtFrontLoaded(itemAtFront)
    }

    fun initKeepData() {
        Log.d(TAG, "initKeepData")
        CoroutineScope(Dispatchers.IO).launch {
            db.usersDao().getUsersLast()?.let {
                keepData = it
            }
        }
    }

    fun reTryListener() {
        // TODO: keepData
        onItemAtEndLoaded(keepData)
    }
    fun reFreshListener() {
        Log.d(TAG, "reFresh")
        //onZeroItemsLoaded()
    }
}
