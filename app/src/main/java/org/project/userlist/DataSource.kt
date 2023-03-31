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
    // items.indexOfFirst { it.id == item.id }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<ListUser>
    ) {
        CoroutineScope(coroutineContext).launch {
            val items = async {
                apiService.getUserListPaging(
                    since = 1,
                    per_page = 5
                )
            }
            items.await()?.body().let {
                if(it.isNullOrEmpty()) {
                    // TODO : EmptyAction
                } else {
                    this@DataSource.items.clear()
                    this@DataSource.items.addAll(it)
                    Log.d("test", "Load : ${it.get(0).id}")
                    //val subList = getSubList(it, params.requestedInitialKey ?: 0, params.requestedLoadSize)
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
                    per_page = 5
                )
            }
            items.await()?.body().let {
                if(it.isNullOrEmpty()) {
                    // TODO : EmptyAction
                } else {
                    this@DataSource.items.clear()
                    this@DataSource.items.addAll(it)
                    Log.d("test", "After : ${it.get(0).id}")
                    //val subList = getSubList(it, index, params.requestedLoadSize)
                    callback.onResult(it)
                }
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<ListUser>) {
        CoroutineScope(coroutineContext).launch {
            val index = params.key - 1

            val items = async {
                apiService.getUserListPaging(
                    since = index,
                    per_page = 5
                )
            }
            items.await()?.body().let {
                if(it.isNullOrEmpty()) {
                    // TODO : EmptyAction
                } else {
                    this@DataSource.items.clear()
                    this@DataSource.items.addAll(it)
                    Log.d("test", "Before : ${it.get(0).id}")
                    //val subList = getSubList(it, index, params.requestedLoadSize, true)
                    callback.onResult(it)
                }
            }
        }
    }

    private fun getSubList(
        items: List<ListUser>,
        index: Int,
        requestedLoadSize: Int,
        before: Boolean = false
    ): List<ListUser> {

        val fromIndex = inRange(index, 0, items.size)
        val toIndex = when {
            // scroll up - calculate the items before the actual index
            // by subtracting the requestedLoadSize from it
            before -> inRange(fromIndex - requestedLoadSize, 0, items.size)
            // scroll down - calculate the items after the actual index
            // by adding the requestedLoadSize to it
            else -> inRange(fromIndex + requestedLoadSize, 0, items.size)
        }

        return if (fromIndex == toIndex) { // Can only mean list is empty
            emptyList()
        } else {
            items.subList(fromIndex, toIndex)
        }
    }
    // Ensures the calculated start or end value are within the list bounds -
// When scrolling to the end of the list or to its beginning
    private fun inRange(position: Int, start: Int, end: Int): Int {
        if (position < start) return start
        if (position > end) return end
        return position
    }
}