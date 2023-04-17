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
import org.project.userlist.data.repository.UserRepository
import org.project.userlist.data.repository.userRepositoryImpl
import org.project.userlist.view.userList.BookMarkUsersViewModel
import org.project.userlist.ui.view.userDetail.UserDetailViewModel

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
        single<UserRepository> { userRepositoryImpl(retrofitApi = get(), db = get()) }

        viewModel { UsersViewModel(userRepository = get()) }
        viewModel { BookMarkUsersViewModel(userRepository = get()) }

        viewModel { UserDetailViewModel(userRepository = get())}
    }
}