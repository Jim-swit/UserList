package org.project.userlist

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.project.userlist.data.local.UsersDb
import org.project.userlist.view.userList.UsersViewModel
import org.project.userlist.data.network.Retrofit
import org.project.userlist.data.repository.UsersRepository
import org.project.userlist.data.repository.UsersRepositoryImpl
import org.project.userlist.view.userList.BookMarkUsersViewModel

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
        single<UsersRepository> { UsersRepositoryImpl(retrofitApi = get(), db = get()) }

        viewModel { UsersViewModel(usersRepository = get()) }
        viewModel { BookMarkUsersViewModel(usersRepository = get()) }
    }
}