package org.project.userlist.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.project.userlist.R
import org.project.userlist.databinding.FragmentBookmarkUserListBinding
import org.project.userlist.model.Users

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class BookmarkUserListFragment : Fragment() {

    private lateinit var adapter: BookMarkUsersAdapter

    private val userListViewModel: UsersViewModel by sharedViewModel()

    private var _binding: FragmentBookmarkUserListBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentBookmarkUserListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()

        CoroutineScope(Dispatchers.Main).launch {
            userListViewModel.bookMarkUsersList.observe(this@BookmarkUserListFragment.viewLifecycleOwner, Observer {
                adapter.submitList(it)
            })
        }
    }

    private fun initAdapter() {
        val recyclerView = binding.recyclerView

        adapter = BookMarkUsersAdapter() {
            userListViewModel.updateBookmark(it as Users)
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}