package org.project.userlist.ui.view.userList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.project.userlist.data.remote.ApiResult
import org.project.userlist.utils.NetworkConnect
import org.project.userlist.utils.NetworkResult
import org.project.userlist.databinding.FragmentUsersBinding
import org.project.userlist.ui.adapter.UsersAdapter
import org.project.userlist.ui.view.base.ViewBindingBaseFragment
import org.project.userlist.utils.makeToast

class UsersFragment: ViewBindingBaseFragment<FragmentUsersBinding>() {

    private lateinit var adapter: UsersAdapter

    private val userListViewModel: UsersViewModel by sharedViewModel()

    private lateinit var networkConnect: NetworkConnect


    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentUsersBinding {
        return FragmentUsersBinding.inflate(inflater, container, false)
    }

    override fun initView() {

        networkConnect = NetworkConnect(context = requireContext())

        binding.apply {
            buttonFirst.setOnClickListener {
                lifecycleScope.launch(Dispatchers.IO) {
                    userListViewModel.reTryListener(true)
                }
            }

            buttonInsert.setOnClickListener {
                adapter.notifyDataSetChanged()
            }
        }

        initAdapter()

        lifecycleScope.launch {
            userListViewModel.usersList.observe(this@UsersFragment.viewLifecycleOwner, Observer {
                adapter.submitList(it)
            })

            userListViewModel.networkState.observe(this@UsersFragment.viewLifecycleOwner, Observer { result ->
                when(result) {
                    is ApiResult.ApiSuccess -> {
                        binding.buttonLinearLayout.visibility = android.view.View.GONE

                        runBlocking {
                            result.data.let {
                                userListViewModel.insertUsersList(it)
                            }
                        }
                    }

                    is ApiResult.ApiError -> {
                        binding.buttonLinearLayout.visibility = android.view.View.VISIBLE
                        userListViewModel.reTryListener(false)
                    }

                    is ApiResult.ApiLoading -> {
                        binding.buttonLinearLayout.visibility = android.view.View.VISIBLE
                        // TODO: Loading ProgressBar / Skeleton
                    }
                }
            })

            networkConnect.observe(this@UsersFragment.viewLifecycleOwner, Observer { NETWORK_TYPE ->
                when(NETWORK_TYPE) {
                    is NetworkResult.MOBILE  -> { userListViewModel.reConnectNetWork() }

                    is NetworkResult.WIFI -> { userListViewModel.reConnectNetWork() }

                    is NetworkResult.NOT_CONNECTED -> {
                        // TODO : Network Not Connected : Dialog or Toast
                        makeToast("Network Not Connected")
                    }
                }
            })
        }
    }


    private fun initAdapter() {
        val recyclerView = binding.recyclerView

        adapter = UsersAdapter() {users, position ->
            lifecycleScope.launch(Dispatchers.IO) {
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



