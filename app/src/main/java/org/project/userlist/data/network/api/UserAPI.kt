package org.project.userlist.data.network.api

import org.project.userlist.model.User
import org.project.userlist.model.Users
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitGITAPI {
    @GET("users")
    suspend fun getUserListPaging(
        @Query("since") since: Int,
        @Query("per_page") per_page: Int
    ): Response<List<Users>>

    @GET("users/{login}")
    suspend fun getUserDetail(
        @Path("login") login: String
    ): Response<User>
}