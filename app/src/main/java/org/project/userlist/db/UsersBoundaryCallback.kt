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
import javax.security.auth.callback.Callback

class UsersBoundaryCallback(
    private val webService: RetrofitGITAPI,
    private val db: UsersDb,
    private val per_page: Int
) : PagedList.BoundaryCallback<Users>() {
    override fun onZeroItemsLoaded() {
        ItemSourceFactory(webService)
        CoroutineScope(Dispatchers.IO).launch {

            db.runInTransaction { db.usersDao().deleteAll() }

            val usersListData = async { webService.getUserListPaging(STARTPAGE, per_page) }

            usersListData.await().body()?.let {
                db.runInTransaction {
                    db.usersDao().insertUsers(*it.toTypedArray())
                }
            }
        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: Users) {
        CoroutineScope(Dispatchers.IO).launch {

            val usersListData = async {  webService.getUserListPaging(itemAtEnd.id.toInt() + 1, 5) }

            usersListData.await().body()?.let {
                db.runInTransaction {
                    db.usersDao().insertUsers(*it.toTypedArray())
                }
            }
        }
    }

    // 최상단 이상의 데이터가 없으므로 필요성을 느끼지 못함.
    override fun onItemAtFrontLoaded(itemAtFront: Users) {
        super.onItemAtFrontLoaded(itemAtFront)
    }
}