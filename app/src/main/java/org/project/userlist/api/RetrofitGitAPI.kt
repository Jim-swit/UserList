package org.project.userlist

import org.project.userlist.model.ListUser
import org.project.userlist.model.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitGITAPI {
    @GET("users")
    suspend fun getSearchResult(
    ): Response<List<ListUser>>

    @GET("users/{login}")
    suspend fun getUser(
        @Path("login") login: String
    ): Response<User>

    @GET("users")
    suspend fun getUserListPaging(
        @Query("since") since: Int,
        @Query("per_page") per_page: Int
    ): Response<List<ListUser>>
}