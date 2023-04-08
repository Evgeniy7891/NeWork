package ru.stan.nework.presentation.home.users

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.stan.nework.R
import ru.stan.nework.databinding.ItemHomeUsersBinding
import ru.stan.nework.domain.models.ui.user.UserUI

class UsersHomeAdapter(private val listUser: List<UserUI>):
    RecyclerView.Adapter<UsersHomeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemHomeUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = listUser[position]
        holder.bind(user)
    }

    class ViewHolder(
        private val binding: ItemHomeUsersBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserUI) {
            with(binding) {
                tvName.text = user.name
                Glide.with(ivAvatar)
                    .load(user.avatar)
                    .placeholder(R.drawable.ic_avatar)
                    .circleCrop()
                    .timeout(10_000)
                    .into(ivAvatar)
            }
        }
    }
}