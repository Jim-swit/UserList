package org.project.userlist.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import org.project.userlist.R
import org.project.userlist.databinding.ItemViewBinding
import org.project.userlist.model.Users

class UsersAdapter(
    private var onItemClicked: ((updatedUsers: Users) -> Unit)
)
    : PagedListAdapter<Users, UsersAdapter.UserListViewHolder>(DIFF_CALLBACK) {

    inner class UserListViewHolder(private val binding:ItemViewBinding) : ViewHolder(binding.root){
        fun bind(currentItem: Users, position: Int) {
            binding.recyclerTextView.text = currentItem.login
            Glide.with(binding.root)
                .load(currentItem.avatar_url)
                .into(binding.recyclerImageView)

            binding.favoriteButton.setBackgroundColor(ContextCompat.getColor(binding.root.context,
                if(currentItem.bookMarked) R.color.purple_500 else R.color.black
                ))

            binding.favoriteButton.setOnClickListener {
                Log.d("test", "id : ${currentItem.id}")
                currentItem.bookMarked = !currentItem.bookMarked
                onItemClicked(currentItem)
                notifyItemChanged(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListViewHolder {
        val binding = ItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        val currentItem = getItem(position)
        if(currentItem != null) {
            holder.bind(currentItem, position)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Users>() {
            override fun areItemsTheSame(oldItem: Users, newItem: Users) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Users, newItem: Users) =
                oldItem == newItem
                //(oldItem == newItem || oldItem == newItem.apply { isChecked = !isChecked })
        }
    }
}