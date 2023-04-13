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
import org.project.userlist.databinding.FragmentBookmarkUserListBinding
import org.project.userlist.ui.adapter.BookMarkUsersAdapter
import org.project.userlist.ui.view.base.ViewBindingBaseFragment

class BookmarkUserListFragment : ViewBindingBaseFragment<FragmentBookmarkUserListBinding>() {


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

        adapter = BookMarkUsersAdapter() { bookMarkUsers, _ ->
            CoroutineScope(Dispatchers.IO).launch {
                bookMarkUserListViewModel.deleteBookMarkUsers(bookMarkUsers)
            }
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
    }
}