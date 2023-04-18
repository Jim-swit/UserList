package org.project.userlist.view.userList

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.project.userlist.R
import org.project.userlist.databinding.ItemBookMarkViewBinding
import org.project.userlist.model.BookMarkUsers
import org.project.userlist.view.userDetail.UserDetailActivity

class BookMarkUsersAdapter (
    private var onItemClicked: ((updatedUsers: BookMarkUsers, position: Int) -> Unit)
)
    : PagedListAdapter<BookMarkUsers, BookMarkUsersAdapter.UserListViewHolder>(DIFF_CALLBACK) {

    inner class UserListViewHolder(private val binding: ItemBookMarkViewBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(currentItem: BookMarkUsers, position: Int) {
            binding.recyclerTextView.text = currentItem.login

            binding.recyclerImageView.clipToOutline = true

            Glide.with(binding.root)
                .load(currentItem.avatar_url)
                .override(80,80)
                .skipMemoryCache(false)
                .placeholder(R.drawable.user_default)
                .error(R.drawable.user_default)
                .into(binding.recyclerImageView)

            binding.deleteBtn.setOnClickListener {
                onItemClicked(currentItem, position)
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

        holder.itemView.setOnClickListener { v ->
            if(!currentItem?.url.isNullOrEmpty()) {
                val intent = Intent(v.context, UserDetailActivity::class.java)
                intent.putExtra("user_login", currentItem?.login)
                v.context.startActivity(intent)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BookMarkUsers>() {
            override fun areItemsTheSame(oldItem: BookMarkUsers, newItem: BookMarkUsers) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: BookMarkUsers, newItem: BookMarkUsers) =
                oldItem == newItem
        }
    }
}