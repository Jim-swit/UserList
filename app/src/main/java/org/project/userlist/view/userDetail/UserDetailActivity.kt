package org.project.userlist.view.userDetail

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.project.userlist.data.network.ApiResult
import org.project.userlist.databinding.ActivityUserDetailBinding
import org.project.userlist.model.User
import org.project.userlist.ui.view.base.ViewBindingBaseActivity

class UserDetailActivity : ViewBindingBaseActivity<ActivityUserDetailBinding>
    ({ActivityUserDetailBinding.inflate(it)}){

    private val userDetailViewModel: UserDetailViewModel by viewModel<UserDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dialog = ProgressDialog(this@UserDetailActivity)

        lifecycleScope.launch {
            userDetailViewModel.getUser.observe(this@UserDetailActivity, Observer { result ->
                when (result) {
                    is ApiResult.Success -> {
                        if(result.data is User) {
                            binding.userLogin.text = result.data.login
                            dialog.dismiss()
                            showView()
                        }
                    }

                    is ApiResult.Loading -> {
                        hideView()
                        dialog.show()
                    }

                    is ApiResult.Fail.Error -> {
                        dialog.dismiss()
                    }

                    is ApiResult.Fail.Exception -> {
                        dialog.dismiss()
                    }
                }
            })
        }

        intent?.extras?.getString("user_login").let { login ->
            if(!login.isNullOrEmpty()) {
                lifecycleScope.launch(Dispatchers.IO) {
                    userDetailViewModel.getUserDetail(login)
                }
            }
        }
    }
    private fun hideView() {
        binding.userLogin.visibility = View.GONE
    }

    private fun showView() {
        binding.userLogin.visibility = View.VISIBLE
    }
}