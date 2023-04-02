package org.project.userlist.model

import com.google.gson.annotations.SerializedName

data class UserList(
    @SerializedName("login") val login:String,
    @SerializedName("id") val id:String,
    @SerializedName("node_id") val node_id:String,
    @SerializedName("url") val url:String,
    @SerializedName("avatar_url") val avatar_url:String
)
