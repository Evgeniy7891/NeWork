package ru.stan.nework.presentation.usersProfile.pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.stan.nework.presentation.account.pager.settings.SettingsFragment
import ru.stan.nework.presentation.usersProfile.pager.jobs.UserJobsFragment
import ru.stan.nework.presentation.usersProfile.pager.posts.UserPostsFragment

class PagerUsersAdapter(fragmentActivity: FragmentActivity, private val id: Long): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> UserPostsFragment(id)
            1 -> UserJobsFragment(id)
            else -> SettingsFragment()
        }
    }
}