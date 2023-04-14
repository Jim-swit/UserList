package org.project.userlist.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "BookMarkUsers")
data class BookMarkUsers(
    @PrimaryKey
    @SerializedName("id") val id: Int,
    @SerializedName("login") val login:String,
    @SerializedName("node_id") val node_id:String,
    @SerializedName("url") val url:String,
    @SerializedName("avatar_url") val avatar_url:String
)