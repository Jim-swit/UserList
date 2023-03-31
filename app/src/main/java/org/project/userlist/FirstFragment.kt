package org.project.userlist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    private lateinit var adapter: UserListAdapter

    private var _binding: FragmentFirstBinding? = null


    //private lateinit var viewmodel: ListUserViewModel
    private lateinit var viewmodel:ListUserViewModel


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

        val recyclerView = binding.recyclerView
        adapter = UserListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

        viewmodel = ViewModelProvider(this).get(ListUserViewModel::class.java)


        binding.buttonFirst.setOnClickListener {
            // findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
            CoroutineScope(Dispatchers.Main).launch {
                viewmodel.load(10).observe(this@FirstFragment.viewLifecycleOwner, Observer {
                    adapter.submitList(it)
                })
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    /*
    suspend fun getUser(login: String):Response<User> {
        return withContext(Dispatchers.IO) { retrofitApi.getUser(login) }
    }

     */
}









