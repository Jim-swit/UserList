package org.project.userlist.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.project.userlist.databinding.FragmentUserListBinding
import org.project.userlist.db.UsersDb
import org.project.userlist.model.Users

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class UsersFragment : Fragment() {

    private lateinit var adapter: UsersAdapter

    private var _binding: FragmentUserListBinding? = null

    
    // ViewModel에 넘겨줄 매개변수를 Koin으로 주입해준다면 아래 코드로 대체 가능
    //private val userListViewModel: UsersViewModel by viewModels()
    private val userListViewModel: UsersViewModel by viewModels {
        UsersViewModel.UsersViewModelFactory(UsersDb.create(requireActivity().applicationContext))
    }

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserListBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()

        CoroutineScope(Dispatchers.Main).launch {
            userListViewModel.usersList.observe(this@UsersFragment.viewLifecycleOwner, Observer {
                adapter.submitList(it)
            })
        }

        binding.buttonFirst.setOnClickListener {

            Log.d("test", "click")
        }
    }
    private fun initAdapter() {
        val recyclerView = binding.recyclerView

        adapter = UsersAdapter()

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}




