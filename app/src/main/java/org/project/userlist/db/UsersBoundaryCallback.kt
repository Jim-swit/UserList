package org.project.userlist.db

import android.util.Log
import androidx.paging.PagedList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.project.userlist.RetrofitGITAPI
import org.project.userlist.db.UsersDb.Companion.STARTPAGE
import org.project.userlist.model.Users
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsersBoundaryCallback(
    private val webService: RetrofitGITAPI,
    private val db: UsersDb,
    private val per_page: Int
) : PagedList.BoundaryCallback<Users>() {
    override fun onZeroItemsLoaded() {
        ItemSourceFactory(webService)
        CoroutineScope(Dispatchers.IO).launch {

            db.runInTransaction { db.usersDao().deleteAll() }

            webService.getUserListPaging(STARTPAGE, per_page).enqueue(createWebServiceCallback())
        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: Users) {
        CoroutineScope(Dispatchers.IO).launch {
            webService.getUserListPaging(itemAtEnd.id.toInt() + 1, 5)
                .enqueue(createWebServiceCallback())
        }
    }

    private fun createWebServiceCallback(): Callback<List<Users>> {
        return object : Callback<List<Users>> {
            override fun onFailure(call: Call<List<Users>>, t: Throwable) {
                Log.d("TAG", "onFailure: ${t.message}")
            }

            override fun onResponse(call: Call<List<Users>>, response: Response<List<Users>>) {
                Log.d("TAG", "onResponse: ${response.body()}")
                response.body()?.let {
                    CoroutineScope(Dispatchers.IO).launch {
                        db.runInTransaction {
                            db.usersDao().insertUsers(*it.toTypedArray())
                        }
                    }
                }
            }
        }
    }

    // 최상단 이상의 데이터가 없으므로 필요성을 느끼지 못함.
    override fun onItemAtFrontLoaded(itemAtFront: Users) {
        super.onItemAtFrontLoaded(itemAtFront)
    }
}