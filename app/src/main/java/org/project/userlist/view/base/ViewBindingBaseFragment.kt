package org.project.userlist.view.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class ViewBindingBaseFragment<T: ViewBinding> : Fragment() {
    private var _binding: T? = null
    val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getFragmentBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        super.onViewCreated(view, savedInstanceState)
    }

    abstract fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?) : T

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()

    }

    abstract fun initView()
}