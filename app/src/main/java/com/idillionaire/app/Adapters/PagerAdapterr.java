package com.idillionaire.app.Adapters;

import android.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.idillionaire.app.Fragments.FiveFragment;
import com.idillionaire.app.Fragments.FourFragment;
import com.idillionaire.app.Fragments.OneFragment;
import com.idillionaire.app.Fragments.ThreeFragment;
import com.idillionaire.app.Fragments.TwoFragment;

public class PagerAdapterr extends FragmentPagerAdapter {

    int myNumofTabs;

    public PagerAdapterr(FragmentManager fm, int NumofTabs) {
        super(fm);
        this.myNumofTabs = NumofTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                OneFragment tab1 = new OneFragment();
                return tab1;
            case 1:
                TwoFragment tab2 = new TwoFragment();
                return tab2;
            case 2:
                ThreeFragment tab3 = new ThreeFragment();
                return tab3;
            case 3:
                FourFragment tab4 = new FourFragment();
                return tab4;
            case 4:
                FiveFragment tab5 = new FiveFragment();
                return tab5;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return myNumofTabs;
    }
}
