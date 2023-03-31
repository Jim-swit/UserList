package org.project.userlist

import android.util.Log
import androidx.paging.DataSource
import androidx.paging.ItemKeyedDataSource
import kotlinx.coroutines.*
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

class DataSource(
    val apiService:RetrofitGITAPI
) : ItemKeyedDataSource<Int, ListUser>(), CoroutineScope {
 // getUserListPaging

    private val items = mutableListOf<ListUser>()

    override val coroutineContext: CoroutineContext get() = Dispatchers.IO

    override fun getKey(item: ListUser): Int = item.id.toInt()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<ListUser>
    ) {
        CoroutineScope(coroutineContext).launch {
            val items = async {
                apiService.getUserListPaging(
                    since = 0,
                    per_page = params.requestedLoadSize
                )
            }
            items.await()?.body().let {
                if(it.isNullOrEmpty()) {
                    // TODO : EmptyAction
                } else {
                    this@DataSource.items.clear()
                    this@DataSource.items.addAll(it)

                    callback.onResult(it)
                }
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<ListUser>) {
        CoroutineScope(coroutineContext).launch {
            val index = params.key + 1

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
                    this@DataSource.items.clear()
                    this@DataSource.items.addAll(it)

                    callback.onResult(it)
                }
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<ListUser>) {
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