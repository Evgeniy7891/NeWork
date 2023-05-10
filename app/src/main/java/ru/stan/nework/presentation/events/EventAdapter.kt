package ru.stan.nework.presentation.events

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.stan.nework.R
import ru.stan.nework.data.room.entity.EventEntity
import ru.stan.nework.databinding.ItemEventBinding
import ru.stan.nework.domain.models.ui.event.Event
import ru.stan.nework.domain.models.ui.post.Post

interface OnListener {
    fun getUsers(listId: List<Int>) {}
    fun onEdit(event: Event) {}
    fun onRemove(event: Event) {}
}

var users: ((List<Int>) -> Unit)? = null

class EventAdapter(private val onListener: OnListener) :
    PagingDataAdapter<Event, ViewHolder>(EventDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = getItem(position)
        if (event != null) {
            holder.bind(event)
        }
    }

    companion object {
        const val MAX_POOL_SIZE = 50
        const val VIEW_TYPE = 0
    }
}

class ViewHolder(
    private val binding: ItemEventBinding,
    private val onClickListener: OnListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(event: Event) {
        with(binding) {
            tvAuthor.text = event.author
            tvJob.text = event.authorJob
            tvTime.text = event.published
            tvContent.text = event.content
            tvLinkContent.text= event.link
            tvDatetimeContent.text = event.datetime
            tvCountLiked.text = event.likeOwnerIds.size.toString()
            tvCountUsers.text = event.users.size.toString()

            tvGo.setOnClickListener {
                users?.invoke(event.likeOwnerIds)
            }
            tvSpeakersContent.setOnClickListener {
                users?.invoke(event.speakerIds)
            }

            Glide.with(ivAvatar)
                .load(event.authorAvatar)
                .placeholder(R.drawable.ic_avatar)
                .circleCrop()
                .timeout(10_000)
                .into(ivAvatar)

            ibMenu.isVisible = event.ownedByMe
            ibMenu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_menu)
                    menu.setGroupVisible(R.id.menu_owned, event.ownedByMe)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onClickListener.onRemove(event)
                                true
                            }
                            R.id.edit -> {
                                onClickListener.onEdit(event)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }
        }
    }
}


class EventDiffCallback : DiffUtil.ItemCallback<Event>() {
    override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem == newItem
    }
}