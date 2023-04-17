package org.project.userlist.ui.view.userDetail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.project.userlist.data.network.ApiResult
import org.project.userlist.databinding.ActivityUserDetailBinding
import org.project.userlist.ui.view.base.ViewBindingBaseActivity

class UserDetailActivity : ViewBindingBaseActivity<ActivityUserDetailBinding>
    ({ActivityUserDetailBinding.inflate(it)}){

    private val userDetailViewModel: UserDetailViewModel by viewModel<UserDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dialog = ProgressDialog(this@UserDetailActivity)

        intent?.extras?.getString("user_login").let { login ->
            if(!login.isNullOrEmpty()) {
                lifecycleScope.launch(Dispatchers.IO) {
                    val result = userDetailViewModel.getUserDetail(login)

                    when(result) {
                        is ApiResult.ApiSuccess -> {
                            lifecycleScope.launch(Dispatchers.Main) {
                                binding.userLogin.text = result.data.login
                                delay(3000)
                                dialog.dismiss()
                            }
                        }

                        is ApiResult.ApiError -> {

                        }

                        is ApiResult.ApiLoading -> {
                            dialog.show()
                        }
                    }
                }
            }
        }
    }
}