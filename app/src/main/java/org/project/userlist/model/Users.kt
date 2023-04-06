package org.project.userlist.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Users")
data class Users(
    @PrimaryKey
    @SerializedName("id") val id: String,
    @SerializedName("login") val login:String,
    @SerializedName("node_id") val node_id:String,
    @SerializedName("url") val url:String,
    @SerializedName("avatar_url") val avatar_url:String,
    @SerializedName("favorite") var isChecked: Boolean = false
)
