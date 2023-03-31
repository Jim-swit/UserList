package org.project.userlist.api

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.project.userlist.BuildConfig
import org.project.userlist.RetrofitGITAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException

object Retrofit {
    private const val BASE_URL = "https://api.github.com/"
    private const val GITHEADERAUTH = "Authorization"

    private var retrofit: Retrofit? = null

    val instance: RetrofitGITAPI
        get() {
            val gson = GsonBuilder()
                .setLenient()
                .create()
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(provideOkHttpClient(AppInterceptor()))
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
            }
            return retrofit!!.create(RetrofitGITAPI::class.java)
        }
    private fun provideOkHttpClient(interceptor: AppInterceptor): OkHttpClient
            = OkHttpClient.Builder().run {
        addInterceptor(interceptor)
        build()
    }
    class AppInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain) : okhttp3.Response = with(chain) {
            val newRequest = request().newBuilder()
                .addHeader(GITHEADERAUTH, BuildConfig.GITAUTHORIZATIONTOKEN)
                .build()
            proceed(newRequest)
        }
    }
}
