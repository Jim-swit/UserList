package org.project.userlist.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.project.userlist.databinding.FragmentUserListBinding
import org.project.userlist.db.UsersDb

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class UsersFragment : Fragment() {

    private lateinit var adapter: UsersAdapter

    private var _binding: FragmentUserListBinding? = null

    //private val userListViewModel: UsersViewModel by viewModels()
    private val userListViewModel: UsersViewModel by viewModels {
        UsersViewModel.UsersViewModelFactory(UsersDb.create(requireActivity().applicationContext))
    }

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = binding.recyclerView
        adapter = UsersAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

        binding.buttonFirst.setOnClickListener {
            /*
            // findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
            Log.d("test", "회사 : ${userListViewModel.test.value?.company}")
            CoroutineScope(Dispatchers.IO).launch {
                userListViewModel.insertUsersDb(
                    Users(
                        "2","2","3","4","https://user-images.githubusercontent.com/32217176/228440522-d84a78d1-23c4-48a4-bb37-bfa059da7a08.png"
                    )
                )
            }


            CoroutineScope(Dispatchers.Main).launch {
                userListViewModel.load(0).observe(this@UsersFragment.viewLifecycleOwner, Observer {
                    adapter.submitList(it)
                })
            }

             */

            CoroutineScope(Dispatchers.Main).launch {
                userListViewModel.load(0).observe(this@UsersFragment.viewLifecycleOwner, Observer {
                    adapter.submitList(it)
                })
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}




