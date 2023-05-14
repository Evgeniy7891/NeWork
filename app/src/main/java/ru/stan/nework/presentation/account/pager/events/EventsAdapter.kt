package ru.stan.nework.presentation.account.pager.events

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.stan.nework.databinding.ItemEventsBinding
import ru.stan.nework.domain.models.ui.event.Event
import ru.stan.nework.presentation.events.EventDiffCallback

interface OnListenerEvent {
    fun getUsers(listId: List<Int>) {}
    fun onEdit(event: Event) {}
    fun onRemove(event: Event) {}
}

class EventsAdapter(private val onListenerEvent: OnListenerEvent) :
    PagingDataAdapter<Event, ViewHolder>(EventDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEventsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onListenerEvent)
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
    private val binding: ItemEventsBinding,
    private val onClickListener: OnListenerEvent
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(event: Event) {
        with(binding) {
            tvContent.text = event.content
            tvLinkContent.text = event.link
            tvDatetimeContent.text = event.datetime
        }
    }
}
