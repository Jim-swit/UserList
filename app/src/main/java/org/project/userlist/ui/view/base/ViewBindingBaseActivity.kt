package org.project.userlist.ui.view.base

import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class ViewBindingBaseActivity<B: ViewBinding>(
    val bindingFactory: (LayoutInflater) -> B
): AppCompatActivity() {
    private var _binding: B? = null
    val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        _binding = bindingFactory(layoutInflater)
        setContentView(binding.root)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}