package org.project.userlist.ui.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.project.userlist.databinding.FragmentBookmarkUserListBinding
import org.project.userlist.ui.adapter.BookMarkUsersAdapter

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class BookmarkUserListFragment : Fragment() {


    private lateinit var adapter: BookMarkUsersAdapter

    private val bookMarkUserListViewModel: BookMarkUsersViewModel by sharedViewModel()

    private var _binding: FragmentBookmarkUserListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
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
            bookMarkUserListViewModel.boockMarkUsersList.observe(this@BookmarkUserListFragment.viewLifecycleOwner, Observer {
                Log.d("test", "book Data : ${it.size}")
                adapter.submitList(it)
            })
        }

        binding.testButton.setOnClickListener {
            bookMarkUserListViewModel.boockMarkUsersList
        }
    }

    private fun initAdapter() {
        val recyclerView = binding.bookMarkRecyclerView


        adapter = BookMarkUsersAdapter() { bookMarkUsers ->
            CoroutineScope(Dispatchers.IO).launch {
                bookMarkUserListViewModel.deleteBookMarkUsers(bookMarkUsers)
            }
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}