package ru.stan.nework.presentation.addPost

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.stan.nework.R
import ru.stan.nework.databinding.ItemUsersBinding
import ru.stan.nework.domain.models.network.user.User
import ru.stan.nework.domain.models.ui.user.UserUI

interface CheckedListener {
    fun checked(id: Int) {}
    fun unChecked(id: Int) {}
}

class UsersAdapter(
    private val listUser: List<UserUI>,
    private val checkedListener: CheckedListener
) :
    RecyclerView.Adapter<UsersAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, checkedListener)
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = listUser[position]
        holder.bind(user)
    }

    class ViewHolder(
        private val binding: ItemUsersBinding,
        private val listener: CheckedListener
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserUI) {
            with(binding) {
                tvName.text = user.name
                checkBox.setOnClickListener {
                    if (checkBox.isChecked) listener.checked(user.id) else listener.unChecked(
                        user.id
                    )
                }
                Glide.with(ivAvatar)
                    .load(user.avatar)
                    .placeholder(R.drawable.ic_avatar)
                    .circleCrop()
                    .timeout(10_000)
                    .into(ivAvatar)
            }
        }
    }

    companion object {
        const val MAX_POOL_SIZE = 20
        const val VIEW_TYPE = 0
    }
}