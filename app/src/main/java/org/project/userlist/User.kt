package org.project.userlist

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("login") val login:String,
    @SerializedName("id") val id:String,
    @SerializedName("name") val name:String
)
