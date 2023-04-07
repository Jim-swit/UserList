package org.project.userlist

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module
import org.project.userlist.db.UsersDb
import org.project.userlist.repository.UsersRepository
import org.project.userlist.ui.UsersViewModel
import org.project.userlist.api.Retrofit

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
    }
}