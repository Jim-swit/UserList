package org.project.userlist.ui.view.userDetail

import android.os.Bundle
import org.project.userlist.databinding.ActivityUserDetailBinding
import org.project.userlist.ui.view.base.ViewBindingBaseActivity

class UserDetailActivity : ViewBindingBaseActivity<ActivityUserDetailBinding>
    ({ActivityUserDetailBinding.inflate(it)}){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent?.extras?.getString("user_url").let {
            // TODO: Retrofit으로 user 데이터 가져오기
        }
    }
}