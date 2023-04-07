package org.project.userlist.db

import androidx.paging.ItemKeyedDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class BookMarkUsersDataSource(
    val db: UsersDb
) : ItemKeyedDataSource<Int, BookMarkUsers>(), CoroutineScope {
    // getUserListPaging

    private val items = mutableListOf<BookMarkUsers>()

    override val coroutineContext: CoroutineContext get() = Dispatchers.IO

    override fun getKey(item: BookMarkUsers): Int = item.id.toInt()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<BookMarkUsers>
    ) {
        CoroutineScope(coroutineContext).launch {

        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<BookMarkUsers>) {
        CoroutineScope(coroutineContext).launch {
            val index = params.key + 1
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<BookMarkUsers>) {
        /*
        CoroutineScope(coroutineContext).launch {
            val index = params.key - params.requestedLoadSize

            val items = async {
                apiService.getUserListPaging(
                    since = index,
                    per_page = params.requestedLoadSize
                )
            }
            items.await()?.body().let {
                if(it.isNullOrEmpty()) {
                    // TODO : EmptyAction
                } else {
                    Log.d("test","Before Size : ${it.size}")
                    it.forEachIndexed {index,  v->
                        Log.d("test", "$index  :  ${v.id}")
                    }
                    this@DataSource.items.clear()
                    this@DataSource.items.addAll(it)

                    callback.onResult(it)
                }
            }
        }

         */
    }
}