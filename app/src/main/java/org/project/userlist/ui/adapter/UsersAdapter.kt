package org.project.userlist.ui.adapter

import android.content.Intent
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
import org.project.userlist.ui.view.userDetail.UserDetailActivity

class UsersAdapter(
    private var onItemClicked: ((updatedUsers: Users, position: Int) -> Unit)
)
    : PagedListAdapter<Users, UsersAdapter.UserListViewHolder>(DIFF_CALLBACK) {

    inner class UserListViewHolder(private val binding:ItemViewBinding) : ViewHolder(binding.root){
        fun bind(currentItem: Users, position: Int) {
            binding.recyclerTextView.text = currentItem.login

            Glide.with(binding.root)
                .load(currentItem.avatar_url)
                .override(80,80)
                .skipMemoryCache(false)
                .placeholder(R.drawable.user_default)
                .error(R.drawable.user_default)
                .into(binding.recyclerImageView)


            binding.favoriteButton.setBackgroundColor(ContextCompat.getColor(binding.root.context,
                if(currentItem.bookMarked) R.color.purple_500 else R.color.black
                ))

            binding.favoriteButton.setOnClickListener {
                currentItem.bookMarked = !currentItem.bookMarked
                onItemClicked(currentItem, position)
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

        holder.itemView.setOnClickListener { v ->
            if(!currentItem?.url.isNullOrEmpty()) {
                val intent = Intent(v.context, UserDetailActivity::class.java)
                intent.putExtra("user_login", currentItem?.login)
                v.context.startActivity(intent)
            }
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