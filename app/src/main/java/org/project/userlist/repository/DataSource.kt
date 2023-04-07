package org.project.userlist.repository

import androidx.paging.ItemKeyedDataSource
import kotlinx.coroutines.*
import org.project.userlist.data.remote.RetrofitGITAPI
import org.project.userlist.model.Users
import kotlin.coroutines.CoroutineContext

class DataSource(
    val apiService: RetrofitGITAPI
) : ItemKeyedDataSource<Int, Users>(), CoroutineScope {
 // getUserListPaging

    private val items = mutableListOf<Users>()

    override val coroutineContext: CoroutineContext get() = Dispatchers.IO

    override fun getKey(item: Users): Int = item.id.toInt()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Users>
    ) {
        CoroutineScope(coroutineContext).launch {
            apiService.getUserListPaging(
                since = 0,
                per_page = params.requestedLoadSize
            ).enqueue(object : retrofit2.Callback<List<Users>> {
                override fun onFailure(call: retrofit2.Call<List<Users>>, t: Throwable) {
                    // TODO : ErrorAction
                }

                override fun onResponse(
                    call: retrofit2.Call<List<Users>>,
                    response: retrofit2.Response<List<Users>>
                ) {
                    response.body().let {
                        if (it.isNullOrEmpty()) {
                            // TODO : EmptyAction
                        } else {
                            this@DataSource.items.clear()
                            this@DataSource.items.addAll(it)

                            callback.onResult(it)
                        }
                    }
                }
            })
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Users>) {
        CoroutineScope(coroutineContext).launch {
            val index = params.key + 1

            apiService.getUserListPaging(
                since = index,
                per_page = params.requestedLoadSize
            ).enqueue(object : retrofit2.Callback<List<Users>> {
                override fun onFailure(call: retrofit2.Call<List<Users>>, t: Throwable) {
                    // TODO : ErrorAction
                }

                override fun onResponse(
                    call: retrofit2.Call<List<Users>>,
                    response: retrofit2.Response<List<Users>>
                ) {
                    response.body().let {
                        if (it.isNullOrEmpty()) {
                            // TODO : EmptyAction
                        } else {
                            this@DataSource.items.clear()
                            this@DataSource.items.addAll(it)

                            callback.onResult(it)
                        }
                    }
                }
            })
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Users>) {
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