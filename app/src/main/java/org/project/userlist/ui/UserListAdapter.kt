package org.project.userlist.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import org.project.userlist.databinding.ItemViewBinding
import org.project.userlist.model.ListUser

class UserListAdapter
    : PagedListAdapter<ListUser, UserListAdapter.UserListViewHolder>(DIFF_CALLBACK) {

    class UserListViewHolder(private val binding:ItemViewBinding) : ViewHolder(binding.root){
        fun bind(currentItem: ListUser) {
            binding.recyclerTextView.text = currentItem.login
            Glide.with(binding.root)
                .load(currentItem.avatar_url)
                .into(binding.recyclerImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListViewHolder {
        val binding = ItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        val currentItem = getItem(position)

        if(currentItem != null) {
            holder.bind(currentItem)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListUser>() {
            override fun areItemsTheSame(oldItem: ListUser, newItem: ListUser) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ListUser, newItem: ListUser) =
                oldItem == newItem
        }
    }
}