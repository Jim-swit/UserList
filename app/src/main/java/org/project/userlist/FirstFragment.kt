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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
                val listUser = withContext(Dispatchers.IO) { retrofitApi.getSearchResult() }

                listUser?.body()?.forEach {
                    /*
                    Glide.with(this@FirstFragment)
                        .load(it.image)
                        .into(binding.imageviewFirst)

                     */

                    val userData = withContext(Dispatchers.IO) { retrofitApi.getUser(it.login) }
                    Log.d("Test", "login : ${it.login}   /   name  :  ${userData?.body()?.name}")

                }

            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
            }
            return retrofit!!.create(RetrofitGITAPI::class.java)
        }
}

interface RetrofitGITAPI {
    @GET("users")
    suspend fun getSearchResult(): Response<List<ListUser>>

    @GET("users/{user_name}")
    suspend fun getUser(
        @Path("user_name") user_name: String
    ): Response<User>

}

data class ListUser(
    @SerializedName("login") val login:String,
    @SerializedName("id") val id:String,
    @SerializedName("node_id") val node_id:String,
    @SerializedName("url") val url:String
)
data class User(
    @SerializedName("login") val login:String,
    @SerializedName("id") val id:String,
    @SerializedName("name") val name:String
)


