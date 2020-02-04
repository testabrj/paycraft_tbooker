package com.paycraft.ticketbooker.adapters;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.paycraft.ticketbooker.view.fragment.ConfirmationFragment;
import com.paycraft.ticketbooker.view.fragment.OTPFragment;
import com.paycraft.ticketbooker.view.fragment.RegistrationFragment;
import com.paycraft.ticketbooker.view.fragment.StationsFragment;

public class StepsPagerAdapter extends FragmentPagerAdapter {

    public StepsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Fragment getItem(int position) {
      switch (position) {
          case 0:
          default:
           return RegistrationFragment.newInstance();
          case 1:
              return OTPFragment.newInstance();
          case 2:
              return StationsFragment.newInstance();
          case 3:
              return ConfirmationFragment.newInstance();

      }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }
}