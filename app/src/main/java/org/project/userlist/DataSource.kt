package org.project.userlist

import android.util.Log
import androidx.paging.DataSource
import androidx.paging.ItemKeyedDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

class DataSource(

    val asyncItems: Response<List<ListUser>>
) : ItemKeyedDataSource<Int, ListUser>(), CoroutineScope {


    private val items = mutableListOf<ListUser>()

    override val coroutineContext: CoroutineContext get() = Dispatchers.IO

    override fun getKey(item: ListUser): Int = item.id.toInt()
    // items.indexOfFirst { it.id == item.id }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<ListUser>
    ) {
        val items = asyncItems?.body()

        if(items.isNullOrEmpty()) {
            // TODO : EmptyAction
        } else {
            this@DataSource.items.clear()
            this@DataSource.items.addAll(items)

            val subList = getSubList(items, params.requestedInitialKey ?: 0, params.requestedLoadSize)
            callback.onResult(subList)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<ListUser>) {
        val index = params.key + 1
        Log.d("test", "after : ${index}")
        val subList = getSubList(items, index, params.requestedLoadSize)
        callback.onResult(subList)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<ListUser>) {
        val index = params.key - 1
        Log.d("test", "before : ${index}")
        val subList = getSubList(items, index, params.requestedLoadSize, true)
        callback.onResult(subList)
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