package org.project.userlist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.project.userlist.databinding.FragmentFirstBinding
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path
import java.io.IOException

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    private val retrofitApi = org.project.userlist.Retrofit.instance

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            // findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)

            CoroutineScope(Dispatchers.Main).launch {
                val listUser = withContext(Dispatchers.IO) {
                    retrofitApi.getSearchResult()
                }

                Log.d("test", "listUser : ${listUser?.body()}")
                listUser?.body()?.forEach {
                    Glide.with(this@FirstFragment)
                        .load(it.avatar_url)
                        .into(binding.imageviewFirst)
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    suspend fun getUser(login: String):Response<User> {
        return withContext(Dispatchers.IO) { retrofitApi.getUser(login) }
    }
}


object Retrofit {
    private const val BASE_URL = "https://api.github.com/"
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
                .addHeader("Authorization", "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")
                .build()
            proceed(newRequest)
        }
    }
}

interface RetrofitGITAPI {
    @GET("users")
    suspend fun getSearchResult(
    ): Response<List<ListUser>>

    @GET("users/{login}")
    suspend fun getUser(
        @Path("login") login: String
    ): Response<User>

}

data class ListUser(
    @SerializedName("login") val login:String,
    @SerializedName("id") val id:String,
    @SerializedName("node_id") val node_id:String,
    @SerializedName("url") val url:String,
    @SerializedName("avatar_url") val avatar_url:String

)
data class User(
    @SerializedName("login") val login:String,
    @SerializedName("id") val id:String,
    @SerializedName("name") val name:String
)


