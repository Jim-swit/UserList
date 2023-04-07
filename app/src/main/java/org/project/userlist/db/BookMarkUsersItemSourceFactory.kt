package org.project.userlist.db

import androidx.paging.PagedList

class BookMarkUsersItemSourceFactory (
    val db: UsersDb
): androidx.paging.DataSource.Factory<Int, BookMarkUsers>() {
    override fun create(): androidx.paging.DataSource<Int, BookMarkUsers> {
        return BookMarkUsersDataSource(db)
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