package org.project.userlist.db

import android.util.Log
import androidx.paging.PagedList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.project.userlist.RetrofitGITAPI
import org.project.userlist.model.Users
import javax.security.auth.callback.Callback

class UsersBoundaryCallback(
    private val webService: RetrofitGITAPI,
    private val db: UsersDb
) : PagedList.BoundaryCallback<Users>() {
    override fun onZeroItemsLoaded() {
        CoroutineScope(Dispatchers.IO).launch {

            db.runInTransaction { db.usersDao().deleteAll() }

            val temp = async { webService.getUserListPaging(0,5) }

            temp.await().body()?.let {
                db.runInTransaction {
                    db.usersDao().insertUsers(*it.toTypedArray())
                }
            }
        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: Users) {
        CoroutineScope(Dispatchers.IO).launch {

            val temp = async {  webService.getUserListPaging(itemAtEnd.id.toInt() + 1, 5) }

            temp.await().body()?.let {
                db.runInTransaction {
                    db.usersDao().insertUsers(*it.toTypedArray())
                }
            }
        }
    }

    override fun onItemAtFrontLoaded(itemAtFront: Users) {
        super.onItemAtFrontLoaded(itemAtFront)
    }
}