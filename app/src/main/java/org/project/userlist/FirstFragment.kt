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
import retrofit2.http.Headers

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
                val userData = withContext(Dispatchers.IO) { retrofitApi.getSearchResult() }

                userData?.body()?.forEach {
                    Log.d("Test", "test : ${it.name}")

                    Glide.with(this@FirstFragment)
                        .load(it.image)
                        .into(binding.imageviewFirst)
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
    private const val BASE_URL = "https://my-json-server.typicode.com/Jim-swit/userJson/"
    private var retrofit: Retrofit? = null

    val instance: RetrofitAPI
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
            return retrofit!!.create(RetrofitAPI::class.java)
        }
}

interface RetrofitAPI {
    @Headers("Content-Type: application/json")
    @GET("posts")
    suspend fun getSearchResult():
            Response<List<UserProfile>>
}

data class UserProfile(
    @SerializedName("name") val name:String,
    @SerializedName("image") val image:String,
    @SerializedName("position") val position:String,
    @SerializedName("team") val team:String,
    @SerializedName("phone") val phone:String,
    @SerializedName("email") val email:String,
)
