package org.project.userlist.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "User")
data class User(
    @PrimaryKey
    @SerializedName("id") val id: String,                   // PrimaryKey로 중복 X
    @SerializedName("login") val login:String,              // 유저 닉네임
    @SerializedName("node_id") val node_id:String,          // GraphQL 작업에 필요한 전역 id : 사용자, 이슈, 끌어오기 요청 등에 엑세스 가능
    @SerializedName("avatar_url") val avatar_url:String,    // 유저 프로필 사진
    @SerializedName("html_url") val html_url:String,        // 사용자 깃허브 주소
    @SerializedName("email") val email:String,              // 사용자 이메일
    // Optional
    @SerializedName("location") val location:String,        // 사용자 소속 국가
    @SerializedName("blog") val blog:String,                // 사용자 블로그
    @SerializedName("company") val company:String,          // 사용자 회사
    @SerializedName("created_at") val created_at:String     // 깃허브 계정 생성 시기
)
