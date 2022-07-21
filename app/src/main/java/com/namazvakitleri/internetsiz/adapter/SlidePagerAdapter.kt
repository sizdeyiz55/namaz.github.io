package com.namazvakitleri.internetsiz.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class SlidePagerAdapter(fm: FragmentManager, fragmentList: List<Fragment>): FragmentStatePagerAdapter(fm) {

    private var fragmentList = fragmentList

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }
}