package org.project.userlist

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module
import org.project.userlist.data.local.UsersDb
import org.project.userlist.repository.UsersRepository
import org.project.userlist.ui.view.userList.UsersViewModel
import org.project.userlist.data.remote.Retrofit
import org.project.userlist.ui.view.userList.BookMarkUsersViewModel

class App :Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(appModule)
        }
    }
    private val appModule = module {
        single { UsersDb.create(context = androidContext()) }
        single { Retrofit.instance }
        single { UsersRepository(retrofitApi = get(), db = get()) }

        factory { UsersViewModel(usersRepository = get()) }
        factory { BookMarkUsersViewModel(usersRepository = get()) }
    }
}