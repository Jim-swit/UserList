package org.project.userlist.repository

import org.project.userlist.RetrofitGITAPI
import org.project.userlist.db.UsersDb

class UsersRepository(
    val db: UsersDb,
    private val retrofitApi: RetrofitGITAPI,
    private val networkPageSize: Int) {
}