package ru.stan.nework.presentation.account.pager.jobs

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.stan.nework.R
import ru.stan.nework.databinding.ItemJobBinding
import ru.stan.nework.domain.models.network.job.Job
import ru.stan.nework.utils.DateHelper

class JobAdapter () : ListAdapter<Job, JobViewHolder>(JobDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val binding = ItemJobBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return JobViewHolder(parent.context, binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }
}

class JobViewHolder(
    private val context: Context,
    private val binding: ItemJobBinding,
) : RecyclerView.ViewHolder(binding.root) {

    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(job: Job) {

        binding.apply {
            tvCompanyName.text = job.name
            tvPositionName.text = job.position
            tvStartName.text = DateHelper.convertDate(job.start).substring(0, 10)
            tvEndName.text =
                if (job.finish == null) context.getString(R.string.job_now)
                else DateHelper.convertDate(job.finish).substring(0, 10)
            tvLinkName.text = job.link
        }
    }
}

class JobDiffCallback : DiffUtil.ItemCallback<Job>() {
    override fun areItemsTheSame(oldItem: Job, newItem: Job): Boolean {
        if (oldItem::class != newItem::class) {
            return false
        }
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Job, newItem: Job): Boolean {
        return oldItem == newItem
    }
}