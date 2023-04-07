package org.project.userlist.repository

import androidx.paging.DataSource
import androidx.paging.PagedList
import org.project.userlist.data.remote.RetrofitGITAPI
import org.project.userlist.model.Users

class ItemSourceFactory(
    private val api: RetrofitGITAPI
):DataSource.Factory<Int, Users>() {
    override fun create(): DataSource<Int, Users> {
        return DataSource(api)
    }

    companion object {
        fun providePagingConfig() : PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(5) // 최초 로드 사이즈
            .setPageSize(5) // 각 페이지의 크기
            .setPrefetchDistance(5) // 미리 로드할 거리(개수) 정의
            .build()
    }
}