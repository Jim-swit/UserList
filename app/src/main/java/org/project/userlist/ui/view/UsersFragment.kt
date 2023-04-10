package org.project.userlist.ui.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.project.userlist.databinding.FragmentUserListBinding
import org.project.userlist.ui.adapter.UsersAdapter

class UsersFragment : ViewBindingFragment<FragmentUserListBinding>() {

    private lateinit var adapter: UsersAdapter

    private val userListViewModel: UsersViewModel by sharedViewModel()
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentUserListBinding {
        return FragmentUserListBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        binding.apply {
            buttonFirst.setOnClickListener {
                userListViewModel.reTryListner()
            }

            buttonInsert.setOnClickListener {
                userListViewModel.reFreshListner()
            }
        }

        initAdapter()

        lifecycleScope.launch {
            userListViewModel.usersList.observe(this@UsersFragment.viewLifecycleOwner, Observer {
                adapter.submitList(it)
            })
        }
    }


    private fun initAdapter() {
        val recyclerView = binding.recyclerView

        adapter = UsersAdapter() {users ->
            CoroutineScope(Dispatchers.IO).launch {
                if (users.bookMarked) {
                    userListViewModel.insertBookMarkUsers(users)
                } else {
                    userListViewModel.deleteBookMarkUsers(users)
                }
            }
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
    }
}



