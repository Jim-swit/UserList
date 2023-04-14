package org.project.userlist.ui.view.userList

import android.util.Log
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
import org.project.userlist.utils.NetworkConnect
import org.project.userlist.utils.NetworkResult
import org.project.userlist.databinding.FragmentBookmarkUsersBinding
import org.project.userlist.ui.adapter.BookMarkUsersAdapter
import org.project.userlist.ui.view.base.ViewBindingBaseFragment
import org.project.userlist.utils.makeToast

class BookmarkUserFragment : ViewBindingBaseFragment<FragmentBookmarkUsersBinding>() {


    private lateinit var adapter: BookMarkUsersAdapter

    private val bookMarkUserListViewModel: BookMarkUsersViewModel by sharedViewModel()

    private lateinit var networkConnect: NetworkConnect


    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentBookmarkUsersBinding {
        return FragmentBookmarkUsersBinding.inflate(inflater, container, false)
    }


    override fun initView() {

        networkConnect = NetworkConnect(context = requireContext())

        initAdapter()


        lifecycleScope.launch {
            bookMarkUserListViewModel.boockMarkUsersList.observe(this@BookmarkUserFragment.viewLifecycleOwner, Observer {
                adapter.submitList(it)
            })

            networkConnect.observe(this@BookmarkUserFragment.viewLifecycleOwner, Observer { NETWORK_TYPE ->
                when(NETWORK_TYPE) {

                    is NetworkResult.MOBILE  -> {
                        Log.d("NetworkResult", "MOBILE")
                    }

                    is NetworkResult.WIFI -> {
                        Log.d("NetworkResult", "WIFI")
                    }

                    is NetworkResult.NOT_CONNECTED -> {
                        // TODO : Network Not Connected : Dialog or Toast
                        makeToast("Network Not Connected")
                    }

                }
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