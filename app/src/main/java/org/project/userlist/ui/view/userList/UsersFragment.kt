package org.project.userlist.ui.view.userList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.project.userlist.databinding.FragmentUserListBinding
import org.project.userlist.ui.adapter.UsersAdapter
import org.project.userlist.ui.view.base.ViewBindingBaseFragment

class UsersFragment : ViewBindingBaseFragment<FragmentUserListBinding>() {

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

        adapter = UsersAdapter() {users, position ->
            CoroutineScope(Dispatchers.IO).launch {
                if (users.bookMarked) {
                    userListViewModel.insertBookMarkUsers(users)
                    withContext(Dispatchers.Main) {
                        binding.recyclerView.adapter?.notifyItemChanged(position,1 )
                    }
                } else {
                    userListViewModel.deleteBookMarkUsers(users)
                    withContext(Dispatchers.Main) {
                        binding.recyclerView.adapter?.notifyItemChanged(position, 1)
                    }
                }
            }
        }

        recyclerView.itemAnimator = null
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
    }
}



