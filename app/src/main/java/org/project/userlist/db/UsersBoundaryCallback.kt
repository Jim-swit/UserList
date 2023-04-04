package org.project.userlist.db

import android.util.Log
import androidx.paging.PagedList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.project.userlist.RetrofitGITAPI
import org.project.userlist.model.Users
import javax.security.auth.callback.Callback

class UsersBoundaryCallback(
    private val webService: RetrofitGITAPI,
    private val db: UsersDb
) : PagedList.BoundaryCallback<Users>() {
    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded()
        CoroutineScope(Dispatchers.IO).launch {
            //db.usersDao().deleteAll()
            val temp = webService.getUserListPaging(0,5)

            temp.body()?.forEach {users ->
                users.let { posts ->
                    db.runInTransaction {
                        db.usersDao().insertUsers(
                            Users(
                                id = posts.id,
                                login = posts.login,
                                node_id = posts.node_id,
                                url = posts.url,
                                avatar_url = posts.avatar_url)
                        )
                    }
                }
            }
        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: Users) {
        super.onItemAtEndLoaded(itemAtEnd)

        CoroutineScope(Dispatchers.IO).launch {
            Log.d("test", "EndLoaded : ${itemAtEnd.id}")

            val temp = webService.getUserListPaging(itemAtEnd.id.toInt() + 1, 5)

            temp.body()?.forEach { users ->
                users.let { posts ->
                    db.runInTransaction {
                        db.usersDao().insertUsers(
                            Users(
                                id = posts.id,
                                login = posts.login,
                                node_id = posts.node_id,
                                url = posts.url,
                                avatar_url = posts.avatar_url
                            )
                        )
                    }
                }
            }
        }
    }

    override fun onItemAtFrontLoaded(itemAtFront: Users) {
        super.onItemAtFrontLoaded(itemAtFront)
    }
}