package org.project.userlist.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.project.userlist.databinding.ItemBookMarkViewBinding
import org.project.userlist.model.BookMarkUsers

class BookMarkUsersAdapter (
    private var onItemClicked: ((updatedUsers: BookMarkUsers) -> Unit)
)
    : PagedListAdapter<BookMarkUsers, BookMarkUsersAdapter.UserListViewHolder>(DIFF_CALLBACK) {

    inner class UserListViewHolder(private val binding: ItemBookMarkViewBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(currentItem: BookMarkUsers, position: Int) {
            binding.recyclerTextView.text = currentItem.login
            Glide.with(binding.root)
                .load(currentItem.avatar_url)
                .into(binding.recyclerImageView)
            binding.deleteBtn.setOnClickListener {
                onItemClicked(currentItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListViewHolder {
        val binding = ItemBookMarkViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        val currentItem = getItem(position)
        if(currentItem != null) {
            holder.bind(currentItem, position)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BookMarkUsers>() {
            override fun areItemsTheSame(oldItem: BookMarkUsers, newItem: BookMarkUsers) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: BookMarkUsers, newItem: BookMarkUsers) =
                oldItem == newItem
            //(oldItem == newItem || oldItem == newItem.apply { isChecked = !isChecked })
        }
    }
}