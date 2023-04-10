package org.project.userlist.ui.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.project.userlist.databinding.FragmentBookmarkUserListBinding
import org.project.userlist.ui.adapter.BookMarkUsersAdapter

class BookmarkUserListFragment : ViewBindingFragment<FragmentBookmarkUserListBinding>() {


    private lateinit var adapter: BookMarkUsersAdapter

    private val bookMarkUserListViewModel: BookMarkUsersViewModel by sharedViewModel()


    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentBookmarkUserListBinding {
        return FragmentBookmarkUserListBinding.inflate(inflater, container, false)
    }


    override fun initView() {
        initAdapter()

        lifecycleScope.launch {
            bookMarkUserListViewModel.boockMarkUsersList.observe(this@BookmarkUserListFragment.viewLifecycleOwner, Observer {
                adapter.submitList(it)
            })
        }
    }

    private fun initAdapter() {
        val recyclerView = binding.bookMarkRecyclerView


        adapter = BookMarkUsersAdapter() { bookMarkUsers ->
            CoroutineScope(Dispatchers.IO).launch {
                bookMarkUserListViewModel.deleteBookMarkUsers(bookMarkUsers)
            }
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
    }
}