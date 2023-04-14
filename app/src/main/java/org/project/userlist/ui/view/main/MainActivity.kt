package org.project.userlist.ui.view.main

import android.os.Bundle
import android.view.Menu
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import org.project.userlist.R
import org.project.userlist.databinding.ActivityMainBinding
import org.project.userlist.ui.view.base.ViewBindingBaseActivity

class MainActivity : ViewBindingBaseActivity<ActivityMainBinding>
    ({ActivityMainBinding.inflate(it)}) {

    private val navController: NavController by lazy {
        requireNotNull(supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main)).findNavController()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        binding.bottomNavigationView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
}
