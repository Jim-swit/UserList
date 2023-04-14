package org.project.userlist.ui.view.userDetail

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import org.project.userlist.R
import org.project.userlist.databinding.ActivityUserDetailBinding
import org.project.userlist.ui.view.base.ViewBindingBaseActivity

class UserDetailActivity : ViewBindingBaseActivity<ActivityUserDetailBinding>
    ({ActivityUserDetailBinding.inflate(it)}){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

}