package org.project.userlist

import org.project.userlist.model.UserList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitGITAPI {
    @GET("users")
    suspend fun getUserListPaging(
        @Query("since") since: Int,
        @Query("per_page") per_page: Int
    ): Response<List<UserList>>
}