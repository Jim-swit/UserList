package org.project.userlist

import org.project.userlist.model.User
import org.project.userlist.model.Users
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitGITAPI {
    @GET("users")
    fun getUserListPaging(
        @Query("since") since: Int,
        @Query("per_page") per_page: Int
    ): Call<List<Users>>

    @GET("users/{login}")
    fun getUser(
        @Path("login") login: String
    ): Call<User>
}