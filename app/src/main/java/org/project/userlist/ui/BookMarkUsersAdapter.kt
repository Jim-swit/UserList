package org.project.userlist.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.project.userlist.R
import org.project.userlist.databinding.ItemViewBinding
import org.project.userlist.model.Users

class BookMarkUsersAdapter (
    private var onItemClicked: ((updatedUsers: Users) -> Unit)
)
    : PagedListAdapter<Users, BookMarkUsersAdapter.BookMarkUserListViewHolder>(DIFF_CALLBACK) {

    inner class BookMarkUserListViewHolder(private val binding: ItemViewBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(currentItem: Users) {
            binding.recyclerTextView.text = currentItem.login
            Glide.with(binding.root)
                .load(currentItem.avatar_url)
                .into(binding.recyclerImageView)

            binding.favoriteButton.setBackgroundColor(ContextCompat.getColor(binding.root.context,
                if(currentItem.isChecked) R.color.purple_500 else R.color.black)
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookMarkUserListViewHolder {
        val binding = ItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookMarkUserListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookMarkUserListViewHolder, position: Int) {
        val currentItem = getItem(position)
        if(currentItem != null) {
            holder.bind(currentItem)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Users>() {
            override fun areItemsTheSame(oldItem: Users, newItem: Users) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Users, newItem: Users) =
                oldItem == newItem
        }
    }
}