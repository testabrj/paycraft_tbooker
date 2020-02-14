package com.paycraft.ticketbooker.adapters


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.paycraft.ticketbooker.view.fragment.ConfirmationFragment

import com.paycraft.ticketbooker.view.fragment.OTPFragment
import com.paycraft.ticketbooker.view.fragment.RegistrationFragment
import com.paycraft.ticketbooker.view.fragment.StationFragment

class StepsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm,FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int {
        return 4
    }

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return RegistrationFragment.newInstance()
            1 -> return OTPFragment.newInstance()
            2 -> return StationFragment.newInstance()
            3 -> return ConfirmationFragment.newInstance()
            else -> return RegistrationFragment.newInstance()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return "Page $position"
    }
}