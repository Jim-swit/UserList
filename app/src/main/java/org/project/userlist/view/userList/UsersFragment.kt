package org.project.userlist.view.userList

import android.view.LayoutInflater
import android.view.View
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
import org.project.userlist.data.network.ApiResult
import org.project.userlist.utils.NetworkConnect
import org.project.userlist.utils.NetworkResult
import org.project.userlist.databinding.FragmentUsersBinding
import org.project.userlist.view.base.ViewBindingBaseFragment
import org.project.userlist.utils.makeToast

class UsersFragment: ViewBindingBaseFragment<FragmentUsersBinding>() {

    private lateinit var adapter: UsersAdapter

    private val userListViewModel: UsersViewModel by sharedViewModel()

    // 네트워크 상태 변경을 확인하기 위한 객체
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
            retryButton.setOnClickListener {
                lifecycleScope.launch(Dispatchers.IO) {
                    reTryPaging(true)
                }
            }

            refreshButton.setOnClickListener {
                adapter.notifyDataSetChanged()
            }
        }

        initAdapter()

        lifecycleScope.launch {
            userListViewModel.loadUsersList.observe(this@UsersFragment.viewLifecycleOwner, Observer {
                hideRetryAndRefreshButton()
                adapter.submitList(it)
            })

            // REST API 결과에 따른 분기 처리 및 에러 핸들링
            userListViewModel.getUsersList.observe(this@UsersFragment.viewLifecycleOwner, Observer { result ->
                when(result) {
                    is ApiResult.Success -> {
                        hideRetryAndRefreshButton()

                        runBlocking(Dispatchers.IO) {
                            result.data.let {
                                userListViewModel.insertUsersList(it)
                            }
                        }
                    }

                    // Error 발생 시 reTry & reFresh 버튼 활성화
                    is ApiResult.Fail.Error -> {
                        showRetryAndRefreshButton()
                        reTryPaging(false)
                    }

                    // Loading 상태에 reTry & reFresh 버튼 활성화
                    is ApiResult.Loading -> {
                        // TODO: Loading ProgressBar / Skeleton
                        // 최하단 아이템 밑에 스피너를 띄우는 방식으로 고민중입니다.
                    }

                    is ApiResult.Fail.Exception -> {
                        showRetryAndRefreshButton()
                        reTryPaging(false)
                    }
                }
            })

            // 네트워크 상태 변경 시 Callback
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
                    // 북마크 체크
                    userListViewModel.insertBookMarkUsers(users)

                    withContext(Dispatchers.Main) {
                        binding.recyclerView.adapter?.notifyItemChanged(position,1 )
                    }
                } else {
                    // 북마크 체크 해제
                    userListViewModel.deleteBookMarkUsers(users)

                    withContext(Dispatchers.Main) {
                        binding.recyclerView.adapter?.notifyItemChanged(position, 1)
                    }
                }
            }
        }

        // Blinking Animation 제거
        recyclerView.itemAnimator = null
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
    }


    private fun reTryPaging(autoCheck:Boolean) {
        userListViewModel.reTryListener(autoCheck)
    }
    private fun hideRetryAndRefreshButton() {
        binding.buttonLinearLayout.visibility = View.GONE
    }

    private fun showRetryAndRefreshButton() {
        binding.buttonLinearLayout.visibility = View.VISIBLE
    }
}



