package org.project.userlist.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.project.userlist.databinding.FragmentUserListBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class UsersFragment : Fragment() {

    private lateinit var adapter: UsersAdapter

    private var _binding: FragmentUserListBinding? = null

    private val userListViewModel: UsersViewModel by sharedViewModel()

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
            //userListViewModel.reTryListner()
            userListViewModel.updateData()
            Log.d("test", "click")

        }

        binding.buttonInsert.setOnClickListener {
            userListViewModel.reFreshListner()
        }
    }
    private fun initAdapter() {
        val recyclerView = binding.recyclerView

        adapter = UsersAdapter() {
            userListViewModel.updateBookmark(it)
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



