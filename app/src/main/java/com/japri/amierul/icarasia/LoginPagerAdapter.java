package com.japri.amierul.icarasia;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class LoginPagerAdapter extends FragmentPagerAdapter {

    private List<String> page;

    public LoginPagerAdapter(FragmentManager fm) {
        super(fm);
        page = new ArrayList<>();
    }

    public void setPage(List<String> page){
        this.page = page;
    }

    @Override
    public Fragment getItem(int position) {
        return getFragment(position);
    }

    private Fragment getFragment(int position) {

        switch (position){
            case 0:
                return LoginDefaultPageFragment.newInstance();

            case 1:
                return SignUpPageFragment.newInstance();

            default:
                return LoginDefaultPageFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return page == null ? 0:page.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return page.get(position);
    }
}
