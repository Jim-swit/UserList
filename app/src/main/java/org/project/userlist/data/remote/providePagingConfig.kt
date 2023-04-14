package org.project.userlist.data.remote

import androidx.paging.PagedList

class providePagingConfig {
    fun set8px() : PagedList.Config = PagedList.Config.Builder()
        .setEnablePlaceholders(true)
        .setInitialLoadSizeHint(8) // 최초 로드 사이즈
        .setPageSize(8) // 각 페이지의 크기
        .setPrefetchDistance(8) // 미리 로드할 거리(개수) 정의
        .build()
}