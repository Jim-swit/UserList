package org.project.userlist

import androidx.paging.DataSource
import androidx.paging.PagedList
import kotlinx.coroutines.Deferred
import retrofit2.Response

class ItemSourceFactory(
    private val api: RetrofitGITAPI
):DataSource.Factory<Int, ListUser>() {
    override fun create(): DataSource<Int, ListUser> {
        return DataSource(api)
    }

    companion object {
        fun providePagingConfig() : PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(10) // 최초 로드 사이즈
            .setPageSize(5) // 각 페이지의 크기
            .setPrefetchDistance(5) // 미리 로드할 거리(개수) 정의
            .build()
    }
}