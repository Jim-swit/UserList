package org.project.userlist.view.userDetail

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.project.userlist.R
import org.project.userlist.data.network.ApiResult
import org.project.userlist.databinding.ActivityUserDetailBinding
import org.project.userlist.model.User
import org.project.userlist.ui.view.base.ViewBindingBaseActivity

class UserDetailActivity : ViewBindingBaseActivity<ActivityUserDetailBinding>
    ({ActivityUserDetailBinding.inflate(it)}){

    private val userDetailViewModel: UserDetailViewModel by viewModel<UserDetailViewModel>()
    private val progressDialog by lazy { ProgressDialog(this@UserDetailActivity) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        lifecycleScope.launch {
            userDetailViewModel.getUser.observe(this@UserDetailActivity, Observer { result ->
                when (result) {
                    is ApiResult.Success -> {
                        if(result.data is User) {
                            setContent(result.data)
                            progressDialog.dismiss()
                            showView()
                        }
                    }

                    is ApiResult.Loading -> {
                        hideView()
                        progressDialog.show()
                    }

                    is ApiResult.Fail.Error -> {
                        progressDialog.dismiss()
                    }

                    is ApiResult.Fail.Exception -> {
                        progressDialog.dismiss()
                    }
                }
            })
        }

        getUser()
    }
    private fun getUser() {
        intent?.extras?.getString("user_login").let { login ->
            if(!login.isNullOrEmpty()) {
                lifecycleScope.launch(Dispatchers.IO) {
                    userDetailViewModel.getUserDetail(login)
                }
            }
        }
    }

    private fun setContent(data: User) {
        binding.userLogin.text = data.login

        binding.userAvatar.clipToOutline = true

        Glide.with(this@UserDetailActivity)
            .load(data.avatar_url)
            .placeholder(R.drawable.user_default)
            .error(R.drawable.user_default)
            .into(binding.userAvatar)
    }

    private fun hideView() {
        binding.userLogin.visibility = View.GONE
        binding.userAvatar.visibility = View.GONE
    }

    private fun showView() {
        binding.userLogin.visibility = View.VISIBLE
        binding.userAvatar.visibility = View.VISIBLE
    }
}