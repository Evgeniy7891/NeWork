package ru.stan.nework.presentation.account.pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.stan.nework.presentation.account.pager.settings.SettingsFragment
import ru.stan.nework.presentation.account.pager.jobs.JobsFragment
import ru.stan.nework.presentation.account.pager.wall.WallFragment

class PagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 3
    }
    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> WallFragment()
            1 -> JobsFragment()
            else -> SettingsFragment()
        }
    }
}