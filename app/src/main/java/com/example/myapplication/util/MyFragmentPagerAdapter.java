package com.example.myapplication.util;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;

    private String[] titles;

    public MyFragmentPagerAdapter(FragmentManager paramFragmentManager, List<Fragment> paramList, String[] paramArrayOfString) {
        super(paramFragmentManager);
        this.fragmentList = paramList;
        this.titles = paramArrayOfString;
    }

    public int getCount() { return this.fragmentList.size(); }

    public Fragment getItem(int paramInt) { return this.fragmentList.get(paramInt); }

    public CharSequence getPageTitle(int paramInt) { return this.titles[paramInt]; }
}
